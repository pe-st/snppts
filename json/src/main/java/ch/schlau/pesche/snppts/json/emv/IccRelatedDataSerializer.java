package ch.schlau.pesche.snppts.json.emv;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class IccRelatedDataSerializer extends StdSerializer<IccRelatedData> {

    protected IccRelatedDataSerializer() {
        super(IccRelatedData.class);
    }

    /**
     * Compare two EMV tags by comparing their hex tag (not the enum name)
     */
    private static final Comparator<EmvTag> TAG_COMPARATOR = new Comparator<EmvTag>() {
        @Override
        public int compare(EmvTag o1, EmvTag o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    };

    @Override
    public void serialize(IccRelatedData value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        // implementation inspired by com.fasterxml.jackson.databind.ser.std.MapSerializer.serialize()

        jgen.writeStartObject();

        Map<EmvTag, EmvData> map;
        if (provider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)) {
            // sort keys by tag hex value
            map = new TreeMap<>(TAG_COMPARATOR);
            map.putAll(value.getContent());
        } else {
            map = value.getContent();
        }

        for (Map.Entry<EmvTag, EmvData> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                jgen.writeBinaryField(entry.getKey().getValue(), entry.getValue().getData());
            } else {
                jgen.writeNullField(entry.getKey().getValue());
            }
        }

        jgen.writeEndObject();
    }
}
