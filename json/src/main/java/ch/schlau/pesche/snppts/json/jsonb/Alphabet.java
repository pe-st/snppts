package ch.schlau.pesche.snppts.json.jsonb;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.annotation.JsonbTypeAdapter;

public class Alphabet {

    @JsonbTypeAdapter(Alphabet.AbcAdapter.class)
    private Map<Abc, Integer> letterMap = new EnumMap<>(Abc.class);

    public Map<Abc, Integer> getLetterMap() {
        return letterMap;
    }

    public void setLetterMap(Map<Abc, Integer> letterMap) {
        this.letterMap = letterMap;
    }

    int getValue(Abc a) {
        return letterMap.getOrDefault(a, 0);
    }

    public void setValue(Abc a, int value) {
        letterMap.put(a, value);
    }

    public static class AbcAdapter implements JsonbAdapter<Map<Abc, Integer>, Map<String, Integer>> {
        @Override
        public Map<String, Integer> adaptToJson(Map<Abc, Integer> attrMap) {
            Map<String, Integer> lttrMap = new HashMap<>();
            attrMap.forEach((abc, v) -> lttrMap.put(abc.getLetter(), v));
            return lttrMap;
        }

        @Override
        public Map<Abc, Integer> adaptFromJson(Map<String, Integer> abbrMap) {
            Map<Abc, Integer> abcMap = new EnumMap<>(Abc.class);
            abbrMap.forEach((lttr, v) -> abcMap.put(Abc.from(lttr), v));
            return abcMap;
        }
    }
}
