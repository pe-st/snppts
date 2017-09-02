package ch.schlau.pesche.snppts.csv.garmin.supercsv;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class GarminExportWriterTest {

    @Test
    @Disabled("'write_stdout' disabled because it crashes")
    public void write_stdout() throws IOException {

        List<Fit> beans = createBeans();
        // this crashes somewhere. Surefire plugin reports:
        // "Corrupted stdin stream in forked JVM 1"
        // and later
        // "The forked VM terminated without properly saying goodbye. VM crash or System.exit called?"
        // The same tests works with opencsv...
        GarminExportWriter.write(beans, new OutputStreamWriter(System.out));
    }

    @Test
    public void write_file() throws IOException {
        // given
        List<Fit> beans = createBeans();
        File tempFile = File.createTempFile("opencsv", ".csv");
        // System.out.printf("using temp file %s", tempFile.getCanonicalPath());

        // when
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(tempFile))) {
            GarminExportWriter.write(beans, writer);
        }

        // then
        try (Scanner s = new Scanner(tempFile).useDelimiter("\\Z")) {
            String contents = s.next();
            assertThat(contents, is("\"Date\",\"Km\",\"Shoes\",\"ElevationGain\",\"MmSs\",\"Minutes\","
                    + "\"Pace\",\"GradeAdjustedPace\",\"Name\",\"HeartRate\",\"Calories\",\"CaloriesPercent\",\"Notes\"\n"
                    + "\"2017-06-11\",\"0.0\",,,\"0.0\","
                    + ",,,\"Parcours\",,,,"));
        }
    }

    private List<Fit> createBeans() {
        Fit fit = new Fit();
        fit.setDate(LocalDate.parse("2017-06-11"));
        fit.setName("Parcours");
        List<Fit> beans = new ArrayList<>();
        beans.add(fit);
        return beans;
    }
}