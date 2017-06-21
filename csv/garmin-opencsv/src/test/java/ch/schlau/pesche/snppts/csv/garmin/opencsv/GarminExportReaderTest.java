package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

class GarminExportReaderTest {

    @Test
    void parse_activities() throws IOException {

        String csvfile = getClass().getClassLoader().getResource("activities.csv").getFile();
        List<String[]> activities = GarminExportReader.parse(csvfile);

        assertThat("First String array contains the column names", activities.get(0)[1], is("Activity Name"));
        assertThat(activities.get(1)[1], is("Oberwald-Münster (Gommer Höhenweg)"));
    }

}