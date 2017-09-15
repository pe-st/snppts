package ch.schlau.pesche.snppts.utf8;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;

class DoubleEncodeTest {

    private static final String EURO_SIGN = "\u20ac";
    private static final String INVALID_UTF_8 = "\u00c4\u00c4"; // "ÄÄ" UTF-8: After a byte with bits 110xxxxx there must be a byte 10xxxxxx

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

        assertThat(DoubleEncode.detect(INVALID_UTF_8), is(false));
    }

    @Test
    public void decodeToString_invalid() throws CharacterCodingException {

        assertThrows(MalformedInputException.class,
                () -> DoubleEncode.decodeToString(INVALID_UTF_8.getBytes(StandardCharsets.ISO_8859_1)));
    }
}