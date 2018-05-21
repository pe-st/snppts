package ch.schlau.pesche.snppts.metrics;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Meter;

@ExtendWith(MockitoExtension.class)
class SlidingTimeWindowMeterTest {

    private final Clock clock = mock(Clock.class);

    // use a Meter as variable to make sure a SlidingTimeWindowMeter can replace a Meter
    private final Meter meter = new SlidingTimeWindowMeter(clock);

    private final SlidingTimeWindowMeter stwm = new SlidingTimeWindowMeter(clock);

    @Test
    void normalizeIndex() {
        simulateTick(0);

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
        simulateTick(0);

        assertThat(stwm.calculateIndexOfTick(0L), is(0));
        assertThat(stwm.calculateIndexOfTick(TimeUnit.SECONDS.toNanos(1)), is(1));
    }

    @Test
    public void getOneMinuteRate_10_values() {

        // given
        simulateTicks(LongStream
                .range(0L, 50L).map(TimeUnit.SECONDS::toNanos)
                .boxed().collect(Collectors.toList()));

        // when
        for (int i = 0; i < 10; i++) {
            meter.mark();
        }

        // then
        assertThat(meter.getCount(), is(10L));
        assertThat(meter.getOneMinuteRate(), is(10.0));
    }

    private void simulateTick(long tick) {
        when(clock.getTick()).thenReturn(tick);
    }

    private void simulateTicks(List<Long> ticks) {
        //        System.out.printf("Ticks list %s\n", ticks);
        when(clock.getTick()).thenReturn(ticks.get(0), ticks.stream().skip(1L).toArray(Long[]::new));
    }
}