package ch.schlau.pesche.snppts.json.jsonb;

import java.util.HashMap;
import java.util.Map;

import javax.json.bind.adapter.JsonbAdapter;

public enum Abc {
    A("a"),
    BE("b"),
    CE("c");

    // Initialization-on-demand holder idiom, see https://stackoverflow.com/a/27703839/3686
    private static class Holder {
        static Map<String, Abc> LOOKUP_MAP = new HashMap<>();
    }

    public static class Adapter implements JsonbAdapter<Abc, String> {
        @Override
        public String adaptToJson(Abc attr) {
            return attr.getLetter();
        }

        @Override
        public Abc adaptFromJson(String name) {
            return Abc.from(name);
        }
    }

    String letter;

    Abc(String letter) {
        this.letter = letter;
        Holder.LOOKUP_MAP.put(letter, this);
    }

    public static Abc from(String letter) {
        return Holder.LOOKUP_MAP.get(letter);
    }

    public String getLetter() {
        return letter;
    }
}
