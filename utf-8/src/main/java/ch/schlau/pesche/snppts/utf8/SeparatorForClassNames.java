package ch.schlau.pesche.snppts.utf8;

public class SeparatorForClassNames {

    public static String displayValidCharacters(int codepointFrom, int codepointTo) {
        StringBuilder builder = new StringBuilder();
        for (int i = codepointFrom; i < codepointTo; i++) {
            if (Character.isJavaIdentifierPart(i) &&
                    Character.getType(i) == Character.CONNECTOR_PUNCTUATION) {
                System.out.printf("codepoint %x\n", i);
                builder.appendCodePoint(i);
            }
        }
        return builder.toString();
    }
}
