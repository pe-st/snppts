package ch.schlau.pesche.snppts.csv.garmin.supercsv;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
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

            // only map some columns - setting header elements to null means those columns are ignored
            // use the field names of the bean, not the real header names here!
            final String[] header = new String[] {
                    "Id", // "Activity ID",
                    "Name", // "Activity Name",
                    "Description",
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null }; // there are 41 columns in total

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

    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                new UniqueHashCode(), // id (must be unique)
                new NotNull(), // name
                new Optional(), // description
                // use 'null' for columns not mapped in the header
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null
        };

        return processors;
    }
}
