package ch.schlau.pesche.snppts.json.emv;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.EnumSet;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class EmvTagTest {

    @DataPoints("all")
    public static Set<EmvTag> all() {
        return EnumSet.allOf(EmvTag.class);
    }

    @Theory
    public void map_roundtrip(@FromDataPoints("all") EmvTag tag) {
        assertThat(EmvTag.MAP.get(tag.getValue()), is(tag));
    }

    @Test
    public void getValue_works() throws Exception {
        assertThat(EmvTag.DEDICATED_FILE_NAME.getValue(), is("84"));
    }
}
