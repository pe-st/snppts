package ch.schlau.pesche.snppts.metrics;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Meter;

public class SlidingTimeWindowMeter extends Meter {

    static final long TIME_WINDOW_DURATION_MINUTES = 15;
    private static final long TICK_INTERVAL = TimeUnit.SECONDS.toNanos(1);
    private static final long TIME_WINDOW_DURATION_TICKS = TimeUnit.MINUTES.toNanos(TIME_WINDOW_DURATION_MINUTES);
    private static final Duration TIME_WINDOW_DURATION = Duration.ofMinutes(TIME_WINDOW_DURATION_MINUTES);
    static final int NUMBER_OF_BUCKETS = (int) (TIME_WINDOW_DURATION_TICKS / TICK_INTERVAL);

    private final LongAdder count = new LongAdder();
    private final long startTime;

    private final AtomicLong lastTick;
    private final Clock clock;

    private ArrayList<LongAdder> buckets;
    private Instant bucketBaseTime;
    private Instant oldestBucketTime;
    private int oldestBucketIndex;
    private int currentBucketIndex;

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
        this.startTime = this.clock.getTick();
        this.lastTick = new AtomicLong(startTime);

        this.buckets = new ArrayList<>(NUMBER_OF_BUCKETS);
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
        //        System.out.printf("mark %d currentBucketIndex %d, count %d\n", n, currentBucketIndex, buckets.get(currentBucketIndex).longValue());
    }

    private void updateLastTickIfNecessary() {
        final long oldTick = lastTick.get();
        final long newTick = clock.getTick();
        //        System.out.printf("oldTick %d newTick %d\n", oldTick, newTick);
        final long age = newTick - oldTick;
        if (age >= TICK_INTERVAL) {
            final long newLastTick = newTick - age % TICK_INTERVAL;
            if (lastTick.compareAndSet(oldTick, newLastTick)) {
                Instant currentInstant = Instant.ofEpochSecond(0L, newLastTick);
                currentBucketIndex = normalizeIndex(calculateIndexOfTick(currentInstant));
                //                System.out.printf("newTick %d currentBucketIndex %d\n", newLastTick, currentBucketIndex);
                cleanOldBuckets(currentInstant);
            }
        }
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
        if (oldestStillNeededTime.isAfter(youngestNotInWindow)) {
            // clean all...
            newOldestIndex = oldestBucketIndex;
            //            newOldestIndex = normalizeIndex(oldestBucketIndex - 1);
            oldestBucketTime = currentTick;
        } else if (oldestStillNeededTime.isAfter(oldestBucketTime)) {
            newOldestIndex = normalizeIndex(calculateIndexOfTick(oldestStillNeededTime));
            oldestBucketTime = oldestStillNeededTime;
            //            oldestBucketTime = oldestStillNeededTime.plusNanos(TICK_INTERVAL);
        } else {
            return;
        }

        //        System.out.printf("oldestBucketIndex %d newOldestIndex %d\n", oldestBucketIndex, newOldestIndex);
        if (oldestBucketIndex < newOldestIndex) {
            for (int i = oldestBucketIndex; i < newOldestIndex; i++) {
                buckets.get(i).reset();
            }
        } else {
            for (int i = oldestBucketIndex; i < NUMBER_OF_BUCKETS; i++) {
                buckets.get(i).reset();
            }
            for (int i = 0; i < newOldestIndex; i++) {
                buckets.get(i).reset();
            }
        }
        oldestBucketIndex = newOldestIndex;
    }

    private long sumBuckets(Instant toTime, int numberOfBuckets) {
        LongAdder adder = new LongAdder();
        int toIndex = calculateIndexOfTick(toTime);
        // increment toIndex to include the current bucket into the sum
        toIndex = normalizeIndex(toIndex + 1);
        int fromIndex = normalizeIndex(toIndex - numberOfBuckets);
        if (fromIndex < toIndex) {
            buckets.stream().skip(fromIndex).limit(toIndex - fromIndex).mapToLong(LongAdder::longValue).forEach(adder::add);
        } else {
            buckets.stream().limit(toIndex).mapToLong(LongAdder::longValue).forEach(adder::add);
            buckets.stream().skip(fromIndex).mapToLong(LongAdder::longValue).forEach(adder::add);
        }
        return adder.longValue();
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

    @Override
    public double getFifteenMinuteRate() {
        updateLastTickIfNecessary();
        Instant now = Instant.ofEpochSecond(0L, lastTick.get());
        return sumBuckets(now, (int) (TimeUnit.MINUTES.toNanos(15) / TICK_INTERVAL));
    }

    @Override
    public double getFiveMinuteRate() {
        updateLastTickIfNecessary();
        Instant now = Instant.ofEpochSecond(0L, lastTick.get());
        return sumBuckets(now, (int) (TimeUnit.MINUTES.toNanos(5) / TICK_INTERVAL));
    }

    @Override
    public double getOneMinuteRate() {
        updateLastTickIfNecessary();
        Instant now = Instant.ofEpochSecond(0L, lastTick.get());
        return sumBuckets(now, (int) (TimeUnit.MINUTES.toNanos(1) / TICK_INTERVAL));
    }
}