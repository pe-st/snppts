package ch.schlau.pesche.snppts.zwirbel3b;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ZwirbelTest {

    @Test
    public void equals_hashcode_contract() {
        EqualsVerifier.forClass(Zwirbel.class)
                .withIgnoredFields("currentRotation")
                .verify();
    }
}