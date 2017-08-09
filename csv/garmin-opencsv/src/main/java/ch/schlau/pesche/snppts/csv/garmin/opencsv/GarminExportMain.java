package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.io.IOException;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class GarminExportMain {

    @Parameter(names = { "--file", "-f" })
    String filename;

    public static void main(String ... argv) {

        GarminExportMain main = new GarminExportMain();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    public void run() {

        try {
            List<Activity> activities = GarminExportReader.parse(filename);
            for (Activity a : activities) {
                System.out.printf("%s %s\n", a.getBeginTimestamp(), a.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
