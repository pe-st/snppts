package ch.schlau.pesche.snppts.csv.garmin.supercsv;

import static org.kohsuke.args4j.OptionHandlerFilter.ALL;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class GarminExportMain {

    @Option(name = "-f", required = true, usage = "activities CSV input file", metaVar = "INPUT")
    String filename;

    @Option(name = "-o", usage = "name of the output file", metaVar = "OUTPUT")
    String outputFile;

    @Option(name = "-t", usage = "filter on the given activity type", metaVar = "ACTIVITY_TYPE")
    String activityType;

    @Option(name = "-v", depends = { "-o" }, usage = "verbose output")
    boolean verbose;

    @Option(name = "-s", depends = { "-o" }, usage = "additional statistics output")
    boolean statistics;

    public static void main(String[] args) throws IOException {
        new GarminExportMain().doMain(args);
    }

    public void doMain(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java GarminExportMain -f INPUT [options...]");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("  Example: java GarminExportMain" + parser.printExample(ALL));

            return;
        }

        run();
    }

    public void run() {

        try {
            List<Activity> activities = GarminExportReader.parse(filename);

            List<Fit> fitBeans = new ArrayList<>();
            for (Activity a : activities) {
                fitBeans.add(FitMapper.INSTANCE.activityToFit(a));
                if (verbose) {
                    System.out.printf("%s %s %s%n", a.getBeginTimestamp(), a.getActivityType(), a.getName());
                }
            }
            if (statistics) {
                System.out.printf("%nFound %d entries.%n", activities.size());
            }

            try (Writer writer = createWriter()) {
                GarminExportWriter.write(fitBeans, writer);
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
