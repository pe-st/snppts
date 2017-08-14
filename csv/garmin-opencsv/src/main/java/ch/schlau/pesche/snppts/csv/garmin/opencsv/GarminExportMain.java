package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.opencsv.exceptions.CsvException;

public class GarminExportMain {

    @Parameter(names = { "--file", "-f" }, required = true)
    String filename;

    @Parameter(names = { "--outputfile", "-o" })
    String outputFile;

    @Parameter(names = { "--filter", "-t" })
    String activityType;

    @Parameter(names = { "--verbose", "-v" })
    boolean verbose;

    @Parameter(names = { "--stats", "-s" })
    boolean statistics;

    public static void main(String... argv) {

        GarminExportMain main = new GarminExportMain();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.validateParameters();
        main.run();
    }

    public void run() {

        try {
            List<Activity> activities = activityType == null
                    ? GarminExportReader.parse(filename)
                    : GarminExportReader.parse(filename, activityType);

            List<Fit> fitBeans = new ArrayList<>();
            for (Activity a : activities) {
                fitBeans.add(FitMapper.INSTANCE.activityToFit(a));
                if (verbose) {
                    System.out.printf("%s %s %s\n", a.getBeginTimestamp(), a.getActivityType(), a.getName());
                }
            }
            if (statistics) {
                System.out.printf("\nFound %d entries.\n", activities.size());
            }

            try (Writer writer = createWriter()) {
                GarminExportWriter.write(fitBeans, writer);
            } catch (CsvException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Writer createWriter() throws FileNotFoundException {
        if (outputFile == null) {
            return new OutputStreamWriter(System.out);
        } else {
            return new OutputStreamWriter(new FileOutputStream(outputFile));
        }
    }

    private void validateParameters() {
        if (outputFile == null) {
            if (verbose) {
                System.err.printf("--verbose not allowed without --outputFile, ignoring it");
                verbose = false;
            }
            if (statistics) {
                System.err.printf("--stats not allowed without --outputFile, ignoring it");
                statistics = false;
            }
        }
    }
}
