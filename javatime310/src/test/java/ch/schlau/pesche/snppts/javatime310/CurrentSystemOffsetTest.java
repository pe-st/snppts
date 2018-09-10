package ch.schlau.pesche.snppts.javatime310;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.ZoneId;

import org.junit.jupiter.api.Test;

class CurrentSystemOffsetTest {

    @Test
    void offsetInSeconds_system() {

        // this assumes that the test is run in Switzerland
        assertThat(CurrentSystemOffset.offsetInSeconds(ZoneId.systemDefault()), anyOf(is(3600), is(7200)));
    }
}