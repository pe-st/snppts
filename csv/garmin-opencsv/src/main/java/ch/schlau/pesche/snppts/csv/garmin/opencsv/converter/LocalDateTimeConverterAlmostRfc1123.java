package ch.schlau.pesche.snppts.csv.garmin.opencsv.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

/**
 * Custom converter to Java 8 {@link LocalDateTime} for the time stamps used in the Garmin Export.
 * <p>
 * Garmin uses "EEE, d MMM yyyy H:mm" while DateTimeFormatter.RFC_1123_DATE_TIME
 * would be    "EEE, d MMM yyyy HH:mm:ss 'GMT'"
 *
 * @param <T>
 */
public class LocalDateTimeConverterAlmostRfc1123<T> extends AbstractBeanField<T> {

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

        // Garmin uses "EEE, d MMM yyyy H:mm" while DateTimeFormatter.RFC_1123_DATE_TIME
        // would be    "EEE, d MMM yyyy HH:mm:ss 'GMT'"
        final DateTimeFormatter almostRfc1123 = DateTimeFormatter.ofPattern("EEE, d MMM yyyy H:mm", Locale.US);

        try {
            return LocalDateTime.parse(value, almostRfc1123);
        } catch (DateTimeParseException e) {
            CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(
                    value, field.getType(), "Input wasn't in the expected timestamp format.");
            csve.initCause(e);
            throw csve;
        }
    }
}
