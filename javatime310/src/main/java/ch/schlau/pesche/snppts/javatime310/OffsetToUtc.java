package ch.schlau.pesche.snppts.javatime310;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetToUtc {

    /**
     * Converts a time with offset to UTC
     *
     * @param odt
     * @return
     */
    public LocalDateTime mapToUtc(OffsetDateTime odt) {
        if (odt == null) {
            return null;
        }
        return odt.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
