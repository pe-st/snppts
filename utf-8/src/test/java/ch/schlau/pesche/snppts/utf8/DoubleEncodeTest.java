package ch.schlau.pesche.snppts.utf8;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;

class DoubleEncodeTest {

    private static final String EURO_SIGN = "\u20ac";

    @Test
    public void detect_single_euro() {

        assertThat(DoubleEncode.detect(EURO_SIGN), is(false));
    }

    @Test
    public void detect_double_euro() {

        // given
        byte[] euroBytes = EURO_SIGN.getBytes(StandardCharsets.UTF_8);
        assertThat(DatatypeConverter.printHexBinary(euroBytes), is("E282AC"));

        String doubleEuro = new String(euroBytes, StandardCharsets.ISO_8859_1);
        assertThat(doubleEuro, is("\u00e2\u0082\u00ac"));

        // then
        assertThat(DoubleEncode.detect(doubleEuro), is(true));
    }

    @Test
    public void detect_invalid() {

        String invalidUtf8 = "\u00c4\u00c4"; // "ÄÄ" UTF-8: After a byte with bits 110xxxxx there must be a byte 10xxxxxx
        assertThat(DoubleEncode.detect(invalidUtf8), is(false));
    }
}