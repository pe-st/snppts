package ch.schlau.pesche.snppts.utf8;

/**
 * Some examples of how to deal with UTF-16 Surrogate Pairs
 */
public class ReplaceSurrogatePairs {

    /**
     * Replace any surrogate pairs in the input with two other chars.
     * <p>
     * The goal is to protect a string from a lenght change before converting it to an encoding
     * like ISO-8859-1 where a surrogate pair is replaced by a single '?' character, thus
     * changing the length of the string.
     *
     * @param in          input string
     * @param replacement character to replace the pair with (will be doubled)
     * @return
     */
    public static String lengthInvariantSurrogateReplace(String in, char replacement) {

        StringBuilder result = new StringBuilder(in.length());

        // the unit test ReplaceSurrogatePairsTest.compare_iterations() suggests
        // that the loop with the offset (see #categoriseByOffsetIteration) is faster
        // than the loop with the codepoint array (see #categoriseByCodepointArrayIteration)
        final int length = in.length();
        for (int offset = 0; offset < length; ) {
            final int codepoint = in.codePointAt(offset);

            if (Character.charCount(codepoint) == 2) {
                result.append(replacement).append(replacement);
            } else {
                result.appendCodePoint(codepoint);
            }

            offset += Character.charCount(codepoint);
        }

        return result.toString();
    }

    /**
     * Categorise each codepoint in the input
     * <ul>
     * <li>1 : codepoint would need 1 UTF-8 byte
     * <li>2 : codepoint would need 2 UTF-8 bytes
     * <li>3 : codepoint would need 3 UTF-8 bytes
     * <li>S : codepoint is a surrogate pair (needs 2 chars in a Java String, would need 4 UTF-8 bytes)
     * <li>X : codepoint > 0x10ffff (invalid unicode, should not happen)
     * </ul>
     *
     * @param in
     * @return
     */
    public static String categoriseByOffsetIteration(String in) {

        StringBuilder result = new StringBuilder(in.length());

        // iterate over the input string as shown in
        // https://stackoverflow.com/a/1527891/3686
        final int length = in.length();
        for (int offset = 0; offset < length; ) {
            final int codepoint = in.codePointAt(offset);

            categoriseCodepoint(result, codepoint);

            offset += Character.charCount(codepoint);
        }

        return result.toString();
    }

    /**
     * Same as {@link #categoriseByOffsetIteration(String)}, but using a different
     * implementation (Java 8 or later)
     *
     * @param in
     * @return
     */
    public static String categoriseByCodepointArrayIteration(String in) {

        StringBuilder result = new StringBuilder(in.length());

        // iterate over the input string using CharSequence#codePoints (Java 8)
        // https://stackoverflow.com/a/27798012/3686
        for (int codepoint : in.codePoints().toArray()) {

            categoriseCodepoint(result, codepoint);
        }

        return result.toString();
    }

    private static void categoriseCodepoint(StringBuilder result, int codepoint) {

        if (Character.charCount(codepoint) > 1) {
            if (Character.isValidCodePoint(codepoint)) {
                result.append('S'); // surrogate pair
            } else {
                result.append('X'); // invalid unicode
            }
        } else {
            if (codepoint <= 0x7f) {
                result.append('1');
            } else if (codepoint <= 0x7ff) {
                result.append('2');
            } else {
                result.append('3');
            }
        }
    }
}
