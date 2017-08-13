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

            List<Fit> fitBeans = new ArrayList<>();
            for (Activity a : activities) {
                fitBeans.add(FitMapper.INSTANCE.activityToFit(a));
//                System.out.printf("%s %s\n", a.getBeginTimestamp(), a.getName());
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
}
