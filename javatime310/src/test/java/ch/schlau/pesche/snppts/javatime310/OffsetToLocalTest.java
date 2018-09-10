package ch.schlau.pesche.snppts.javatime310;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class OffsetToLocalTest {

    private OffsetToLocal mapper = new OffsetToLocal();

    @Test
    void mapToLocal_winterTime() throws Exception {
        OffsetDateTime dateTimeInWinter = OffsetDateTime.of(2016, 03, 15, 4, 12, 23, 0, ZoneOffset.ofHours(3));
        // This assumes the time zone of Biel/Bienne, of course
        assertThat(mapper.mapToLocal(dateTimeInWinter), is(LocalDateTime.of(2016, 03, 15, 2, 12, 23)));
    }

    @Test
    void mapToLocal_summerTime() throws Exception {
        OffsetDateTime dateTimeInSummer = OffsetDateTime.of(2015, 07, 24, 10, 32, 32, 0, ZoneOffset.ofHours(3));
        // This assumes the time zone of Biel/Bienne, of course
        assertThat(mapper.mapToLocal(dateTimeInSummer), is(LocalDateTime.of(2015, 07, 24, 9, 32, 32)));
    }
}