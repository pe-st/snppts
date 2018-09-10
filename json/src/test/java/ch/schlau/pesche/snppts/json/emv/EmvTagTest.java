package ch.schlau.pesche.snppts.json.emv;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class EmvTagTest {

    @ParameterizedTest
    @EnumSource(EmvTag.class)
    void map_roundtrip(EmvTag tag) {
        assertThat(EmvTag.MAP.get(tag.getValue()), is(tag));
    }

    @Test
    void getValue_works() {
        assertThat(EmvTag.DEDICATED_FILE_NAME.getValue(), is("84"));
    }
}
