package ch.schlau.pesche.snppts.csv.garmin.supercsv;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

class GarminExportReaderTest {

    @Test
    void parse_activities() throws IOException {

        String csvfile = getClass().getClassLoader().getResource("activities.csv").getFile();
        List<Activity> activities = GarminExportReader.parse(csvfile);

        assertThat(activities.size(), is(7));
        assertThat(activities.get(0).getName(), is("Oberwald-Münster (Gommer Höhenweg)"));
        assertThat(activities.get(0).getBeginTimestamp(), is(LocalDateTime.parse("2017-06-11T11:02")));
        assertThat(activities.get(0).getActivityType(), is("Hiking"));
        assertThat(activities.get(0).getHeartRate(), is(nullValue()));
        assertThat(activities.get(0).getDuration(), is(11601.952));
        assertThat(activities.get(0).getDistance(), is(11.94647));
        assertThat(activities.get(0).getElevationGain(), is(663.25));

        assertThat(activities.get(3).getHeartRate(), is(162));
    }
}