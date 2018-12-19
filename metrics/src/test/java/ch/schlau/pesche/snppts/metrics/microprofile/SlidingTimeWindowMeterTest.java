package ch.schlau.pesche.snppts.metrics.microprofile;

import static ch.schlau.pesche.snppts.metrics.microprofile.SlidingTimeWindowMeter.NUMBER_OF_BUCKETS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.metrics.Meter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.smallrye.metrics.app.Clock;

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
    private Meter meter;

    @BeforeEach
    void init() {
        meter = new SlidingTimeWindowMeter(mockingClock);
    }

    @Test
    void normalizeIndex() {

        SlidingTimeWindowMeter stwm = new SlidingTimeWindowMeter();

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

        SlidingTimeWindowMeter stwm = new SlidingTimeWindowMeter(new MockingClock());

        assertThat(stwm.calculateIndexOfTick(Instant.ofEpochSecond(0L)), is(0));
        assertThat(stwm.calculateIndexOfTick(Instant.ofEpochSecond(1L)), is(1));
    }

    @Test
    public void mark_max_without_cleanup() {

        int markCount = NUMBER_OF_BUCKETS;

        for (int i = 0; i < markCount; i++) {
            mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(i));
            meter.mark();
        }

        // verify that no cleanup happened yet
        assertThat(((SlidingTimeWindowMeter)meter).oldestBucketTime, is(Instant.ofEpochSecond(0L)));

        assertThat(meter.getOneMinuteRate(), is(60.0));
        assertThat(meter.getFiveMinuteRate(), is(300.0));
        assertThat(meter.getFifteenMinuteRate(), is(900.0));
    }

    @Test
    public void mark_first_cleanup() {

        int markCount = NUMBER_OF_BUCKETS + 1;

        for (int i = 0; i < markCount; i++) {
            mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(i));
            meter.mark();
        }

        // verify that at least one cleanup happened
        assertThat(((SlidingTimeWindowMeter)meter).oldestBucketTime, not(is(Instant.ofEpochSecond(0L))));

        assertThat(meter.getOneMinuteRate(), is(60.0));
        assertThat(meter.getFiveMinuteRate(), is(300.0));
        assertThat(meter.getFifteenMinuteRate(), is(900.0));
    }

    @Test
    public void mark_10_values() {

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
    public void mark_1000_values() {

        for (int i = 0; i < 1000; i++) {
            mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(i));
            meter.mark();
        }

        // only 60/300/900 of the 1000 events took place in the last 1/5/15 minute(s)
        assertThat(meter.getOneMinuteRate(), is(60.0));
        assertThat(meter.getFiveMinuteRate(), is(300.0));
        assertThat(meter.getFifteenMinuteRate(), is(900.0));
    }

    @Test
    public void cleanup_pause_shorter_than_window() {

        meter.mark(10);

        // no mark for three minutes
        mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(180));
        assertThat(meter.getOneMinuteRate(), is(0.0));
        assertThat(meter.getFiveMinuteRate(), is(10.0));
        assertThat(meter.getFifteenMinuteRate(), is(10.0));
    }

    @Test
    public void cleanup_window_wrap_around() {

        // mark at 14:40 minutes of the 15 minute window...
        mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(880));
        meter.mark(10);

        // and query at 15:30 minutes (the bucket index must have wrapped around)
        mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(930));
        assertThat(meter.getOneMinuteRate(), is(10.0));
        assertThat(meter.getFiveMinuteRate(), is(10.0));
        assertThat(meter.getFifteenMinuteRate(), is(10.0));

        // and query at 30:10 minutes (the bucket index must have wrapped around for the second time)
        mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(1810));
        assertThat(meter.getOneMinuteRate(), is(0.0));
        assertThat(meter.getFiveMinuteRate(), is(0.0));
        assertThat(meter.getFifteenMinuteRate(), is(0.0));
    }

    @Test
    public void cleanup_pause_longer_than_two_windows() {

        meter.mark(10);

        // after forty minutes all rates should be zero
        mockingClock.setNextTick(TimeUnit.SECONDS.toNanos(2400));
        assertThat(meter.getOneMinuteRate(), is(0.0));
        assertThat(meter.getFiveMinuteRate(), is(0.0));
        assertThat(meter.getFifteenMinuteRate(), is(0.0));
    }
}