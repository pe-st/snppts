package ch.schlau.pesche.snppts.csv.garmin.opencsv;

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

import org.junit.jupiter.api.Test;

import com.opencsv.exceptions.CsvException;

class GarminExportWriterTest {

    @Test
    public void write_stdout() throws IOException, CsvException {

        List<Fit> beans = createBeans();
        GarminExportWriter.write(beans, new OutputStreamWriter(System.out));
    }

    @Test
    public void write_file() throws IOException, CsvException {
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
            assertThat(contents, is("\"datum\",\"elevationGain\",\"km\",\"shoes\"\n"
                    + "\"2017-06-11\",\"\",\"0.0\",\"\""));
        }
    }

    private List<Fit> createBeans() {
        Fit fit = new Fit();
        fit.setDatum(LocalDate.parse("2017-06-11"));
        List<Fit> beans = new ArrayList<>();
        beans.add(fit);
        return beans;
    }
}