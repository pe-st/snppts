package ch.schlau.pesche.snppts.csv.garmin.supercsv;

import static org.kohsuke.args4j.OptionHandlerFilter.ALL;

import java.io.IOException;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class GarminExportMain {

    @Option(name = "-f", usage = "activities CSV input file")
    String filename;

    public static void main(String[] args) throws IOException {
        new GarminExportMain().doMain(args);
    }

    public void doMain(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java SampleMain [options...] arguments...");

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
            for (Activity a : activities) {
                System.out.printf("%s %s\n", a.getBeginTimestamp(), a.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
