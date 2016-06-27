package ch.schlau.pesche.snppts.zwirbel3b;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class SpielzeugTest {

    @Test
    public void equals_hashcode_contract() {
        EqualsVerifier.forClass(Spielzeug.class)
                .verify();
    }
}