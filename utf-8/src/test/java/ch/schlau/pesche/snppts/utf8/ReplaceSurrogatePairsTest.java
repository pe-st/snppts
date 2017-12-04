package ch.schlau.pesche.snppts.utf8;

import static ch.schlau.pesche.snppts.utf8.ReplaceSurrogatePairs.categoriseByCodepointArrayIteration;
import static ch.schlau.pesche.snppts.utf8.ReplaceSurrogatePairs.categoriseByOffsetIteration;
import static ch.schlau.pesche.snppts.utf8.ReplaceSurrogatePairs.lengthInvariantSurrogateReplace;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.lessThan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReplaceSurrogatePairsTest {

    private static final String FRENCH_ACCENTS = "d\u00e9j\u00e0"; // "déjà" : french word with 2 accents
    private static final String NOT_IN_8859_1 = "\u20ac"; // the Euro sign: "€" is not mapped in ISO-8859-1
    private static final String FACE_WITH_TEARS_OF_JOY = "\uD83D\uDE02"; // Emoji character U+1F602 😂, in UTF-16 a surrogate pair
    private static final String INVALID_UTF_8 = "\u00c4\u00c4"; // "ÄÄ" UTF-8: After a byte with bits 110xxxxx there must a byte 10xxxxxx

    @ParameterizedTest
    @ValueSource(strings = { FRENCH_ACCENTS, NOT_IN_8859_1, FACE_WITH_TEARS_OF_JOY, INVALID_UTF_8 })
    public void lengthInvariantSurrogateReplace_length(String input) {

        assertThat(lengthInvariantSurrogateReplace(input, '.').length(), is(input.length()));
    }

    @Test
    public void lengthInvariantSurrogateReplace_result() {

        // for these examples the function must be an invariant
        assertThat(lengthInvariantSurrogateReplace(FRENCH_ACCENTS, '.'), is(FRENCH_ACCENTS));
        assertThat(lengthInvariantSurrogateReplace(NOT_IN_8859_1, '.'), is(NOT_IN_8859_1));
        assertThat(lengthInvariantSurrogateReplace(INVALID_UTF_8, '.'), is(INVALID_UTF_8));

        // the example with the surrogate pair mustn't contain any surrogates in the result anymore
        assertThat(lengthInvariantSurrogateReplace(FACE_WITH_TEARS_OF_JOY, '.'), is(".."));
    }

    @Test
    public void categoriseByIteration_length_notInvariant() {

        assertThat(categoriseByOffsetIteration(FACE_WITH_TEARS_OF_JOY).length(), is(lessThan(FACE_WITH_TEARS_OF_JOY.length())));
    }

    @Test
    public void categoriseByIteration_offset() {

        assertThat(categoriseByOffsetIteration(FRENCH_ACCENTS), is("1212"));
        assertThat(categoriseByOffsetIteration(NOT_IN_8859_1), is("3"));
        assertThat(categoriseByOffsetIteration(FACE_WITH_TEARS_OF_JOY), is("S"));
        assertThat(categoriseByOffsetIteration(INVALID_UTF_8), is("22"));
    }

    @Test
    public void categoriseByIteration_codepointArray() {

        assertThat(categoriseByCodepointArrayIteration(FRENCH_ACCENTS), is("1212"));
        assertThat(categoriseByCodepointArrayIteration(NOT_IN_8859_1), is("3"));
        assertThat(categoriseByCodepointArrayIteration(FACE_WITH_TEARS_OF_JOY), is("S"));
        assertThat(categoriseByCodepointArrayIteration(INVALID_UTF_8), is("22"));
    }

    @Test
    public void compare_iterations() {

        StringBuilder multi = new StringBuilder(4_000_000);
        for (int i = 0; i < 1_000_000; i++) {
            multi.append(FRENCH_ACCENTS);
        }
        final String multiFrench = multi.toString();

        long offsetTime = 0;
        long arrayTime = 0;
        for (int i = 0; i < 10; i++) {
            long startTime = System.nanoTime();
            assertThat(categoriseByOffsetIteration(multiFrench).length(), is(4_000_000));
            long offsetLap = System.nanoTime();
            assertThat(categoriseByCodepointArrayIteration(multiFrench).length(), is(4_000_000));
            long arrayLap = System.nanoTime();
            offsetTime += offsetLap - startTime;
            arrayTime += arrayLap - offsetLap;
        }

        System.out.printf("time with offset: %d%ntime with array : %d%n", offsetTime, arrayTime);
    }
}