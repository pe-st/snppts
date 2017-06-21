package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;

public class GarminExportReader {

    public static List<String[]> parse(String filename) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filename));
        return reader.readAll();
    }
}
