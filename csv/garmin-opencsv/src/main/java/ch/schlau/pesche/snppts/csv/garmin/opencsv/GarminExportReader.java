package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.bean.CsvToBeanBuilder;

public class GarminExportReader {

    public static List<Activity> parse(String filename) throws IOException {
        CsvToBeanBuilder reader = new CsvToBeanBuilder(new FileReader(filename));
        return reader.withType(Activity.class).build().parse();
    }
}
