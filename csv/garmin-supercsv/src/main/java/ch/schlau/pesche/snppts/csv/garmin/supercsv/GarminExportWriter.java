package ch.schlau.pesche.snppts.csv.garmin.supercsv;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;

public class GarminExportWriter {

    private GarminExportWriter() {
        // Utility classes should not have public constructors
    }

    public static void write(List<Fit> beans, Writer writer) throws IOException {
        ICsvBeanWriter beanWriter = null;
        try {
            beanWriter = new CsvBeanWriter(writer, new CsvPreference
                    .Builder(CsvPreference.EXCEL_PREFERENCE)
                    .useQuoteMode(new AlwaysQuoteMode())
                    .build());

            final String[] header = getHeaderMapping();
            final CellProcessor[] processors = getProcessors();

            beanWriter.writeHeader(header);

            for (final Fit fitBean : beans) {
                beanWriter.write(fitBean, header, processors);
            }
        } finally {
            if (beanWriter != null) {
                beanWriter.close();
            }
        }
    }

    private static String[] getHeaderMapping() {

        return new String[] {
                "Date",
                "Km",
                "Shoes",
                "ElevationGain",
                "MmSs",
                "Minutes",
                "Pace",
                "GradeAdjustedPace",
                "Name",
                "HeartRate",
                "Calories",
                "CaloriesPercent",
                "Notes"
        };
    }

    private static CellProcessor[] getProcessors() {

        return new CellProcessor[] {
                new Optional(), // "Date",
                new Optional(), // "Km",
                new Optional(), // "Shoes",
                new Optional(), // "ElevationGain",
                new Optional(), // "MmSs",
                new Optional(), // "Minutes",
                new Optional(), // "Pace",
                new Optional(), // "GradeAdjustedPace",
                new Optional(), // "Name",
                new Optional(), // "HeartRate",
                new Optional(), // "Calories",
                new Optional(), // "CaloriesPercent",
                new Optional()  // "Notes"
        };
    }
}
