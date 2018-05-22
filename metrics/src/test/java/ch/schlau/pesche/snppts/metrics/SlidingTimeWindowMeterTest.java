package ch.schlau.pesche.snppts.metrics;

import static ch.schlau.pesche.snppts.metrics.SlidingTimeWindowMeter.NUMBER_OF_BUCKETS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Meter;

class SlidingTimeWindowMeterTest {

    class MockingClock extends Clock {

        long nextTick = 0L;

        public void setNextTick(long nextTick) {
            this.nextTick = nextTick;
        }

        @Override
        public long getTick() {
            return nextTick;
        }
    }

    private final MockingClock mockingClock = new MockingClock();

    // use a Meter as variable to make sure a SlidingTimeWindowMeter can replace a Meter
    private final Meter meter = new SlidingTimeWindowMeter(mockingClock);

    private final SlidingTimeWindowMeter stwm = new SlidingTimeWindowMeter(mockingClock);

    @Test
    void normalizeIndex() {

        assertThat(stwm.normalizeIndex(0), is(0));
        assertThat(stwm.normalizeIndex(900), is(0));
        assertThat(stwm.normalizeIndex(9000), is(0));
        assertThat(stwm.normalizeIndex(-900), is(0));

        assertThat(stwm.normalizeIndex(1), is(1));

        assertThat(stwm.normalizeIndex(899), is(899));
        assertThat(stwm.normalizeIndex(-1), is(899));
        assertThat(stwm.normalizeIndex(-901), is(899));
    }

    @Test
    public void calculateIndexOfTick() {

        assertThat(stwm.calculateIndexOfTick(0L), is(0));
        assertThat(stwm.calculateIndexOfTick(TimeUnit.SECONDS.toNanos(1)), is(1));
    }

    @Test
    public void mark_max_without_cleanup() {

        int markCount = NUMBER_OF_BUCKETS;

        for (int i = 0; i < markCount; i++) {
            mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(i));
            meter.mark();
        }

        // then
        assertThat(meter.getOneMinuteRate(), is(60.0));
        assertThat(meter.getFiveMinuteRate(), is(300.0));
        assertThat(meter.getFifteenMinuteRate(), is(900.0));
    }

    @Test
    public void cleanOldBuckets_first_cleanup() {

        // when
        for (int i = 0; i < NUMBER_OF_BUCKETS; i++) {
            mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(i));
            meter.mark();
        }

        // then
        assertThat(meter.getOneMinuteRate(), is(60.0));
        assertThat(meter.getFiveMinuteRate(), is(300.0)); // 299
        assertThat(meter.getFifteenMinuteRate(), is(900.0)); // 898
    }

    @Test
    public void counts_10_values() {

        for (int i = 0; i < 10; i++) {
            mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(i));
            meter.mark();
        }

        assertThat(meter.getCount(), is(10L));
        assertThat(meter.getOneMinuteRate(), is(10.0));
        assertThat(meter.getFiveMinuteRate(), is(10.0));
        assertThat(meter.getFifteenMinuteRate(), is(10.0));
    }

    @Test
    @Disabled("doesn't work yet")
    public void counts_1000_values() {

        for (int i = 0; i < 1000; i++) {
            mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(i));
            meter.mark();
        }

        // only 60/300/900 of the 1000 events took place in the last 1/5/15 minute(s)
        assertThat(meter.getOneMinuteRate(), is(60.0));
        assertThat(meter.getFiveMinuteRate(), is(300.0));
        assertThat(meter.getFifteenMinuteRate(), is(900.0));
    }
}