package ch.schlau.pesche.snppts.csv.garmin.commons_csv;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

class GarminExportReaderTest {

    @Test
    void parse_activities() throws IOException {

        String csvfile = getClass().getClassLoader().getResource("activities.csv").getFile();
        List<CSVRecord> activities = GarminExportReader.parse(csvfile);

        assertThat("First String array contains the column names", activities.get(0).get(1), is("Activity Name"));
        assertThat(activities.get(1).get(1), is("Oberwald-Münster (Gommer Höhenweg)"));
    }
}