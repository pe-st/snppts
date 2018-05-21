package ch.schlau.pesche.snppts.metrics;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Meter;

public class SlidingTimeWindowMeter extends Meter {

    private static final long TICK_INTERVAL = TimeUnit.SECONDS.toNanos(1);
    private static final long TIME_WINDOW_DURATION = TimeUnit.MINUTES.toNanos(15);
    private static final int NUMBER_OF_BUCKETS = (int) (TIME_WINDOW_DURATION / TICK_INTERVAL);

    private final LongAdder count = new LongAdder();
    private final long startTime;

    private final AtomicLong lastTick;
    private final Clock clock;

    private ArrayList<LongAdder> buckets;
    private long bucketBaseTime;
    private long oldestBucketTime;
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
        bucketBaseTime = startTime;
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
                currentBucketIndex = calculateIndexOfTick(newLastTick);
                //                System.out.printf("newTick %d currentBucketIndex %d\n", newLastTick, currentBucketIndex);
                cleanOldBuckets(newLastTick);
            }
        }
    }

    int calculateIndexOfTick(long tickTime) {
        return (int) ((tickTime - bucketBaseTime) / TICK_INTERVAL);
    }

    int normalizeIndex(int index) {
        int mod = index % NUMBER_OF_BUCKETS;
        return mod >= 0 ? mod : mod + NUMBER_OF_BUCKETS;
    }

    private void cleanOldBuckets(long currentTick) {
        int newestToClean;
        if (currentTick > oldestBucketTime + 2 * TIME_WINDOW_DURATION) {
            // clean all...
            newestToClean = normalizeIndex(oldestBucketIndex - 1);
            oldestBucketTime = currentTick;
        } else if (currentTick > oldestBucketTime + TIME_WINDOW_DURATION) {
            newestToClean = normalizeIndex(calculateIndexOfTick(currentTick - TIME_WINDOW_DURATION));
            oldestBucketTime = currentTick - TIME_WINDOW_DURATION + TICK_INTERVAL;
        } else {
            return;
        }

        //        System.out.printf("oldestBucketIndex %d newestToClean %d\n", oldestBucketIndex, newestToClean);
        if (oldestBucketIndex < newestToClean) {
            for (int i = oldestBucketIndex; i <= newestToClean; i++) {
                buckets.get(i).reset();
            }
        } else {
            for (int i = oldestBucketIndex; i < NUMBER_OF_BUCKETS; i++) {
                buckets.get(i).reset();
            }
            for (int i = 0; i <= newestToClean; i++) {
                buckets.get(i).reset();
            }
        }
        oldestBucketIndex = normalizeIndex(newestToClean + 1);
    }

    private long sumBuckets(long fromTime, long toTime) {
        LongAdder adder = new LongAdder();
        int fromIndex = calculateIndexOfTick(Math.max(oldestBucketTime, fromTime));
        int toIndex = calculateIndexOfTick(toTime);
        //        System.out.printf("sumBuckets %d..%d, %d..%d\n", fromTime, toTime, fromIndex, toIndex);
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
        long now = lastTick.get();
        return sumBuckets(now - TimeUnit.MINUTES.toNanos(15), now);
    }

    @Override
    public double getFiveMinuteRate() {
        updateLastTickIfNecessary();
        long now = lastTick.get();
        return sumBuckets(now - TimeUnit.MINUTES.toNanos(5), now);
    }

    @Override
    public double getOneMinuteRate() {
        updateLastTickIfNecessary();
        long now = lastTick.get();
        return sumBuckets(now - TimeUnit.MINUTES.toNanos(1), now);
    }
}
