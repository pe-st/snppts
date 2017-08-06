package ch.schlau.pesche.snppts.csv.garmin.supercsv;

import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.time.ParseLocalDateTime;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class GarminExportReader {

    public static List<Activity> parse(String filename) throws IOException {
        ICsvBeanReader reader = null;
        try {
            reader = new CsvBeanReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE);

            // skip past the header (we're defining our own)
            reader.getHeader(true);

            final String[] header = getHeaderMapping();
            final CellProcessor[] processors = getProcessors();

            List<Activity> retval = new ArrayList<>();
            Activity activity;
            while ((activity = reader.read(Activity.class, header, processors)) != null) {
                retval.add(activity);
            }

            return retval;

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private static String[] getHeaderMapping() {

        // there are 41 columns in total
        // only map some columns - setting header elements to null means those columns are ignored
        // use the field names of the bean, not the real header names here!
        return new String[] {
                "Id", // "Activity ID"
                "Name", // "Activity Name"
                "Description",
                "BeginTimestamp", // "Begin Timestamp"
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null
        };
    }

    private static CellProcessor[] getProcessors() {

        // Garmin uses "EEE, d MMM yyyy H:mm" while DateTimeFormatter.RFC_1123_DATE_TIME
        // would be    "EEE, d MMM yyyy HH:mm:ss 'GMT'"
        final DateTimeFormatter almostRfc1123 = DateTimeFormatter.ofPattern("EEE, d MMM yyyy H:mm", Locale.US);

        return new CellProcessor[] {
                new UniqueHashCode(), // "Activity ID" (must be unique)
                new NotNull(), // "Activity Name"
                new Optional(), // "Description"
                new ParseLocalDateTime(almostRfc1123), // "Begin Timestamp"

                // use 'null' for columns not mapped in the header
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null
        };
    }
}
