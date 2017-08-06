package ch.schlau.pesche.snppts.javatime310;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

public class OffsetToUtcTest {

    OffsetToUtc mapper = new OffsetToUtc();

    @Test
    public void mapToUtc() throws Exception {
        OffsetDateTime odt = OffsetDateTime.of(2016, 03, 15, 4, 12, 23, 0, ZoneOffset.ofHours(3));
        assertThat(mapper.mapToUtc(odt), is(LocalDateTime.of(2016, 03, 15, 1, 12, 23)));
    }
}