package ch.schlau.pesche.snppts.javatime310;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class LocalToSystemOffset {

    /**
     * Converts a local time (without offset) to a time with offset, using the
     * system time zone
     *
     * @param ldt  local time w/o offset
     * @return time with offset
     */
    public OffsetDateTime mapTimeOffset(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return OffsetDateTime.of(ldt, ZoneId.systemDefault().getRules().getOffset(ldt));
    }
}
