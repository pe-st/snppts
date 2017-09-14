package ch.schlau.pesche.snppts.utf8;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

public class DoubleEncode {

    public static boolean detect(String input) {

        byte[] inputBytes = input.getBytes(StandardCharsets.ISO_8859_1);

        String decoded;
        try {
            decoded = decodeToString(inputBytes);
        } catch (CharacterCodingException e) {
            // if decoding fails, it wasn't a double encoding
            return false;
        }

        // if the decoded string isn't shorter than the candidate, the candidate contains only ASCII
        return decoded.length() < input.length();
    }

    /**
     * Decode a byte array containing UTF-8 into a String
     * <p>
     * Similar to java.lang.String#String(byte[], StandardCharsets.UTF_8)
     *
     * @param inputBytes
     * @return
     *
     * @throws CharacterCodingException when the input isn't valid UTF-8
     */
    public static String decodeToString(byte[] inputBytes) throws CharacterCodingException {

        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .reset();

        CharBuffer decodedBuffer = decoder.decode(ByteBuffer.wrap(inputBytes));
        return decodedBuffer.toString();
    }
}
