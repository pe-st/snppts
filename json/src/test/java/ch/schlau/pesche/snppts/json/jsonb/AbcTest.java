package ch.schlau.pesche.snppts.json.jsonb;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import org.junit.jupiter.api.Test;

class AbcTest {

    private static final JsonbConfig JSON_CONFIG = new JsonbConfig()
            .withAdapters(new Abc.Adapter());

    private static final Jsonb JSONB = JsonbBuilder.create(JSON_CONFIG);

    @Test
    void json_roundtrip() {

        String jsonString = JSONB.toJson(Abc.CE);

        assertThat(jsonString, jsonEquals(Abc.CE.getLetter()));

        Abc roundtrip = JSONB.fromJson(jsonString, Abc.class);

        assertThat(roundtrip, is(Abc.CE));
    }

    @Test
    void from_abbreviation() {

        assertThat(Abc.from("b"), is(Abc.BE));
    }
}