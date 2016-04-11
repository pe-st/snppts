package ch.schlau.pesche.snppts.javatime310;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.Test;

/**
 * Test the java.time behaviour for summer time/daylight time border cases
 */
public class LocalToSystemOffsetTest {

    LocalToSystemOffset mapper = new LocalToSystemOffset();

    @Test
    public void mapTimeOffset_winterTime() throws Exception {
        LocalDateTime dateTimeInWinter = LocalDateTime.of(2016, 03, 15, 4, 12, 23);
        // This assumes the time zone of Biel/Bienne, of course
        assertThat(mapper.mapTimeOffset(dateTimeInWinter), is(dateTimeInWinter.atOffset(ZoneOffset.ofHours(1))));
    }

    @Test
    public void mapTimeOffset_summerTime() throws Exception {
        LocalDateTime dateTimeInSummer = LocalDateTime.of(2015, 07, 24, 10, 32, 32);
        // This assumes the time zone of Biel/Bienne, of course
        assertThat(mapper.mapTimeOffset(dateTimeInSummer), is(dateTimeInSummer.atOffset(ZoneOffset.ofHours(2))));
    }

    @Test
    public void mapTimeOffset_autumnOverlap() throws Exception {
        // in 2015 the switch back from summer to winter time for Biel/Bienne was at October 25
        LocalDateTime ambiguousDateTime = LocalDateTime.of(2015, 10, 25, 2, 30, 30);
        LocalDateTime stillSummer = ambiguousDateTime.minusHours(1);
        LocalDateTime alreadyWinter = ambiguousDateTime.plusHours(1);

        // this is to assert the usual behavior at a non-ambiguous time
        assertThat(mapper.mapTimeOffset(stillSummer), is(stillSummer.atOffset(ZoneOffset.ofHours(2))));
        assertThat(mapper.mapTimeOffset(alreadyWinter), is(alreadyWinter.atOffset(ZoneOffset.ofHours(1))));

        // this records the fact that in cases with two possible offsets,
        // ZoneRules.getOffset uses the offset *before* the offset switch
        assertThat(mapper.mapTimeOffset(ambiguousDateTime), is(ambiguousDateTime.atOffset(ZoneOffset.ofHours(2))));
    }

    @Test
    public void mapTimeOffset_springHole() throws Exception {

        // in 2016 the switch from winter to summer time for Biel/Bienne was at March 27
        LocalDateTime notExistingDateTime = LocalDateTime.of(2016, 3, 27, 2, 30, 30);
        LocalDateTime stillWinter = notExistingDateTime.minusHours(1);
        LocalDateTime alreadySummer = notExistingDateTime.plusHours(1);

        // this is to assert the usual behavior at a non-ambiguous time
        assertThat(mapper.mapTimeOffset(stillWinter), is(stillWinter.atOffset(ZoneOffset.ofHours(1))));
        assertThat(mapper.mapTimeOffset(alreadySummer), is(alreadySummer.atOffset(ZoneOffset.ofHours(2))));

        // this records the fact that in cases with zero possible offsets (invalid time),
        // ZoneRules.getOffset uses the offset *before* the offset switch
        assertThat(mapper.mapTimeOffset(notExistingDateTime), is(notExistingDateTime.atOffset(ZoneOffset.ofHours(1))));
    }
}