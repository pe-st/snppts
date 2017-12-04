package ch.schlau.pesche.snppts.utf8;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class SeparatorForClassNamesTest {

    @Test
    public void displayValidCharacters_block_punctuation() {

        // 2000..206F is the "General Punctuation" block
        assertThat(SeparatorForClassNames.displayValidCharacters(0x2000, 0x206F), is("" +
                "\u203F" + // UNDERTIE
                "\u2040" + // CHARACTER TIE
                "\u2054"   // INVERTED UNDERTIE
        ));
    }

    @Test
    public void displayValidCharacters_block_f() {

        assertThat(SeparatorForClassNames.displayValidCharacters(0xf000, 0xffff), is("" +
                "\uFE33" + // PRESENTATION FORM FOR VERTICAL LOW LINE
                "\uFE34" + // PRESENTATION FORM FOR VERTICAL WAVY LOW LINE
                "\uFE4D" + // DASHED LOW LINE
                "\uFE4E" + // CENTRELINE LOW LINE
                "\uFE4F" + // WAVY LOW LINE
                "\uFF3F"   // FULLWIDTH LOW LINE
        ));
    }
}