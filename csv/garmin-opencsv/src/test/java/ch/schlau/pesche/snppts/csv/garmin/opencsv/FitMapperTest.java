package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class FitMapperTest {

    @Test
    void activityToFit() {
        // given
        Activity activity = new Activity();
        activity.setBeginTimestamp(LocalDateTime.parse("2017-06-11T11:02"));
        activity.setDistance(42.195);
        activity.setElevationGain(365.38);

        // when
        Fit fit = FitMapper.INSTANCE.activityToFit(activity);

        // then
        assertThat(fit, not(is(nullValue())));
        assertThat(fit.getDatum(), is(LocalDate.parse("2017-06-11")));
        assertThat(fit.getKm(), is(42.195));
        assertThat(fit.getElevationGain(), is(365));
    }

    @Test
    void activityToFit_emptyValues() {
        // given
        Activity activity = new Activity();

        // when
        Fit fit = FitMapper.INSTANCE.activityToFit(activity);

        // then
        assertThat(fit, not(is(nullValue())));
        assertThat(fit.getDatum(), is(nullValue()));
        assertThat(fit.getKm(), is(0.0));
        assertThat(fit.getElevationGain(), is(nullValue()));
    }
}