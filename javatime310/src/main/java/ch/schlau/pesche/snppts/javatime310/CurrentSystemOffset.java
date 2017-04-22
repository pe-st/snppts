package ch.schlau.pesche.snppts.javatime310;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class CurrentSystemOffset {

    public static int offsetInSeconds(ZoneId zoneId) {
        return zoneId.getRules().getOffset(LocalDateTime.now()).getTotalSeconds();
    }
}
