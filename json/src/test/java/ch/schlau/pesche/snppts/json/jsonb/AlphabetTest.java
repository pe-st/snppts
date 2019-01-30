package ch.schlau.pesche.snppts.json.jsonb;

import static ch.schlau.pesche.snppts.json.jsonb.Abc.BE;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlphabetTest {

    private static final Jsonb JSONB = JsonbBuilder.create(new JsonbConfig());

    private Alphabet alphabet;

    @BeforeEach
    private void init() {
        alphabet = new Alphabet();
        alphabet.setValue(Abc.A, 65);
        alphabet.setValue(BE, 66);
        alphabet.setValue(Abc.CE, 67);
    }

    @Test
    void json_roundtrip() {

        String jsonString = JSONB.toJson(alphabet);

        assertThat(jsonString, jsonPartEquals("letterMap." + BE.getLetter(), 66));

        Alphabet roundtrip = JSONB.fromJson(jsonString, Alphabet.class);

        assertThat(roundtrip.getLetterMap().get(BE), is(66));
    }
}