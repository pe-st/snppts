package ch.schlau.pesche.snppts.javatime310;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class OffsetToLocal {

    /**
     * Converts a time with offset to the corresponding local time (for the system time zone)
     *
     * @param odt  time with offset
     * @return time with offset
     */
    public LocalDateTime mapToLocal(OffsetDateTime odt) {
        if (odt == null) {
            return null;
        }
        return LocalDateTime.ofInstant(odt.toInstant(), ZoneId.systemDefault());
    }
}
