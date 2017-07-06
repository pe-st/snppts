package ch.schlau.pesche.snppts.csv.garmin.commons_csv;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class GarminExportReader {

    public static List<CSVRecord> parse(String filename) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(new FileReader(filename));
        List<CSVRecord> retval = new ArrayList<>();
        records.forEach(retval::add);
        return retval;
    }
}
