package ch.schlau.pesche.snppts.csv.garmin.supercsv;

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

        assertThat(activities.get(0).getName(), is("Oberwald-Münster (Gommer Höhenweg)"));
        assertThat(activities.get(0).getBeginTimestamp(), is(LocalDateTime.parse("2017-06-11T11:02")));
    }
}