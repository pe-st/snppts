package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;

public class GarminExportWriter {

    private GarminExportWriter() {
        // Utility classes should not have public constructors
    }

    public static void write(List<Fit> beans, Writer writer) throws IOException, CsvException {
        StatefulBeanToCsv<Fit> beanToCsv = new StatefulBeanToCsvBuilder<Fit>(writer)
                .withMappingStrategy(new ColumnPositionWithHeaderStrategy<Fit>(Fit.class))
                .build();
        beanToCsv.write(beans);
    }
}
