package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class GarminExportWriter {
    
    public static void write(List<Fit> beans, Writer writer) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
        beanToCsv.write(beans);
    }
}
