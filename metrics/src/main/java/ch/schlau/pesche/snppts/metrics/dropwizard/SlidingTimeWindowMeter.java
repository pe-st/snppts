package ch.schlau.pesche.snppts.metrics.dropwizard;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Meter;

/**
 * A meter metric which measures mean throughput and one-, five-, and fifteen-minute
 * simple (arithmetic) moving average throughputs.
 * <p>
 * The moving averages are unweighted, i.e. they include strictly only the events in the
 * sliding time window, every event having the same weight. Unlike the more widely used
 * {@link Meter} with an exponentially-weighted moving average ({@link com.codahale.metrics.EWMA}),
 * with this class the moving average rate drops immediately to zero if the last
 * marked event is older than the time window.
 * <p>
 * A {@link SlidingTimeWindowMeter} works similarly to
 * a {@link com.codahale.metrics.Histogram} with an
 * {@link com.codahale.metrics.SlidingTimeWindowArrayReservoir}, but as a Meter
 * needs to keep track only of the count of events (not the events itself), the memory
 * overhead is much smaller. SlidingTimeWindowMeter uses buckets with just one
 * counter to accumulate the number of events (one bucket per seconds, giving 900 buckets
 * for the 15 minutes time window).
 */
public class SlidingTimeWindowMeter extends Meter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlidingTimeWindowMeter.class);

    private static final long TIME_WINDOW_DURATION_MINUTES = 15;
    private static final long TICK_INTERVAL = TimeUnit.SECONDS.toNanos(1);
    private static final Duration TIME_WINDOW_DURATION = Duration.ofMinutes(TIME_WINDOW_DURATION_MINUTES);

    // package private for the benefit of the unit test
    static final int NUMBER_OF_BUCKETS = (int) (TIME_WINDOW_DURATION.toNanos() / TICK_INTERVAL);

    private final LongAdder count = new LongAdder();
    private final long startTime;

    private final AtomicLong lastTick;
    private final Clock clock;

    /**
     * One counter per time bucket/slot (i.e. per second, see TICK_INTERVAL) for the entire
     * time window (i.e. 15 minutes, see TIME_WINDOW_DURATION_MINUTES)
     */
    private ArrayList<LongAdder> buckets;

    /**
     * Index into buckets, pointing at the bucket containing the oldest counts
     */
    private int oldestBucketIndex;

    /**
     * Index into buckets, pointing at the bucket with the count for the current time (tick)
     */
    private int currentBucketIndex;

    /**
     * Instant at creation time of the time window. Used to calculate the currentBucketIndex
     * for the instant of a given tick (instant modulo time window duration)
     */
    private final Instant bucketBaseTime;

    /**
     * Instant of the bucket with index oldestBucketIndex
     */
    Instant oldestBucketTime;

    /**
     * Creates a new {@link SlidingTimeWindowMeter}.
     */
    public SlidingTimeWindowMeter() {
        this(Clock.defaultClock());
    }

    /**
     * Creates a new {@link SlidingTimeWindowMeter}.
     *
     * @param clock the clock to use for the meter ticks
     */
    public SlidingTimeWindowMeter(Clock clock) {
        this.clock = clock;
        startTime = clock.getTick();
        lastTick = new AtomicLong(startTime);

        buckets = new ArrayList<>(NUMBER_OF_BUCKETS);
        for (int i = 0; i < NUMBER_OF_BUCKETS; i++) {
            buckets.add(new LongAdder());
        }
        bucketBaseTime = Instant.ofEpochSecond(0L, startTime);
        oldestBucketTime = bucketBaseTime;
        oldestBucketIndex = 0;
        currentBucketIndex = 0;
    }

    /**
     * Mark the occurrence of an event.
     */
    @Override
    public void mark() {
        mark(1);
    }

    /**
     * Mark the occurrence of a given number of events.
     *
     * @param n the number of events
     */
    @Override
    public void mark(long n) {
        updateLastTickIfNecessary();
        count.add(n);
        buckets.get(currentBucketIndex).add(n);
        LOGGER.trace("mark({}) : current bucket (idx/cnt): ({}/{})",
                n, currentBucketIndex, buckets.get(currentBucketIndex).longValue());
    }

    private void updateLastTickIfNecessary() {
        final long oldTick = lastTick.get();
        final long newTick = clock.getTick();
        // System.out.printf("oldTick %d newTick %d\n", oldTick, newTick);
        final long age = newTick - oldTick;
        if (age >= TICK_INTERVAL) {
            // - the newTick doesn't fall into the same slot as the oldTick anymore
            // - newLastTick is the lower border time of the new currentBucketIndex slot
            final long newLastTick = newTick - age % TICK_INTERVAL;
            if (lastTick.compareAndSet(oldTick, newLastTick)) {
                Instant currentInstant = Instant.ofEpochSecond(0L, newLastTick);
                currentBucketIndex = normalizeIndex(calculateIndexOfTick(currentInstant));
                LOGGER.debug("new 'current bucket' {} for instant {}", currentBucketIndex, currentInstant);
                cleanOldBuckets(currentInstant);
            }
        }
    }

    @Override
    public double getFifteenMinuteRate() {
        updateLastTickIfNecessary();
        return getMinuteRate(15);
    }

    @Override
    public double getFiveMinuteRate() {
        updateLastTickIfNecessary();
        return getMinuteRate(5);
    }

    @Override
    public double getOneMinuteRate() {
        updateLastTickIfNecessary();
        return getMinuteRate(1);
    }

    private double getMinuteRate(int minutes) {
        Instant now = Instant.ofEpochSecond(0L, lastTick.get());
        return sumBuckets(now, (int) (TimeUnit.MINUTES.toNanos(minutes) / TICK_INTERVAL));
    }

    int calculateIndexOfTick(Instant tickTime) {
        return (int) (Duration.between(bucketBaseTime, tickTime).toNanos() / TICK_INTERVAL);
    }

    int normalizeIndex(int index) {
        int mod = index % NUMBER_OF_BUCKETS;
        return mod >= 0 ? mod : mod + NUMBER_OF_BUCKETS;
    }

    private void cleanOldBuckets(Instant currentTick) {
        int newOldestIndex;
        Instant oldestStillNeededTime = currentTick.minus(TIME_WINDOW_DURATION).plusNanos(TICK_INTERVAL);
        Instant youngestNotInWindow = oldestBucketTime.plus(TIME_WINDOW_DURATION);
        LOGGER.trace("current/oldest/stillNeeded/youngestNot {}/{}/{}/{}",
                currentTick, oldestBucketTime, oldestStillNeededTime, youngestNotInWindow);
        if (oldestStillNeededTime.isAfter(youngestNotInWindow)) {
            // there was no update() call for more than two whole TIME_WINDOW_DURATION
            newOldestIndex = oldestBucketIndex;
            LOGGER.debug("cleanup: throw away all values, old/new 'oldest': {}/{}", oldestBucketTime, currentTick);
            oldestBucketTime = currentTick;
        } else if (oldestStillNeededTime.isAfter(oldestBucketTime)) {
            newOldestIndex = normalizeIndex(calculateIndexOfTick(oldestStillNeededTime));
            LOGGER.debug("cleanup: old/new index {}/{}, old/new 'oldest': {}/{}",
                    oldestBucketIndex, newOldestIndex, oldestBucketTime, currentTick);
            oldestBucketTime = oldestStillNeededTime;
        } else {
            return;
        }

        cleanBucketRange(oldestBucketIndex, newOldestIndex);
        oldestBucketIndex = newOldestIndex;
    }

    private void cleanBucketRange(int fromIndex, int toIndex) {
        if (fromIndex < toIndex) {
            for (int i = fromIndex; i < toIndex; i++) {
                buckets.get(i).reset();
            }
        } else {
            for (int i = fromIndex; i < NUMBER_OF_BUCKETS; i++) {
                buckets.get(i).reset();
            }
            for (int i = 0; i < toIndex; i++) {
                buckets.get(i).reset();
            }
        }
    }

    private long sumBuckets(Instant toTime, int numberOfBuckets) {

        // increment toIndex to include the current bucket into the sum
        int toIndex = normalizeIndex(calculateIndexOfTick(toTime) + 1);
        int fromIndex = normalizeIndex(toIndex - numberOfBuckets);
        LongAdder adder = new LongAdder();

        if (fromIndex < toIndex) {
            buckets.stream()
                    .skip(fromIndex)
                    .limit(toIndex - fromIndex)
                    .mapToLong(LongAdder::longValue)
                    .forEach(adder::add);
        } else {
            buckets.stream().limit(toIndex).mapToLong(LongAdder::longValue).forEach(adder::add);
            buckets.stream().skip(fromIndex).mapToLong(LongAdder::longValue).forEach(adder::add);
        }
        long retval = adder.longValue();
        LOGGER.info("sum at {} over {} buckets = {}, current/from/to index {}/{}/{}, oldest index/time {}/{}",
                toTime, numberOfBuckets, retval, currentBucketIndex, fromIndex, toIndex, oldestBucketIndex, oldestBucketTime);
        return retval;
    }

    @Override
    public long getCount() {
        return count.sum();
    }

    @Override
    public double getMeanRate() {
        if (getCount() == 0) {
            return 0.0;
        } else {
            final double elapsed = clock.getTick() - startTime;
            return getCount() / elapsed * TimeUnit.SECONDS.toNanos(1);
        }
    }
}
