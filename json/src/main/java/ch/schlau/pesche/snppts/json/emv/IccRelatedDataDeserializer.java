package ch.schlau.pesche.snppts.json.emv;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * This deserializer for {@link IccRelatedData} accepts two different forms of JSON input:
 * <ul>
 * <li>with a "content" element, e.g. {@code {"content":{"9F1A":"AAI="}}}
 * <li>without "content" element, e.g. {@code {"9F1A":"AAI="}}
 * </ul>
 */
public class IccRelatedDataDeserializer extends StdDeserializer<IccRelatedData> {

    protected IccRelatedDataDeserializer() {
        super(IccRelatedData.class);
    }

    @Override
    public IccRelatedData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonToken t = jp.getCurrentToken();
        if (t != JsonToken.START_OBJECT) {
            throw ctxt.mappingException(IccRelatedData.class, t);
        }

        IccRelatedData iccRelatedData = new IccRelatedData();
        boolean firstField = true;
        boolean hasContent = false;
        while ((t = jp.nextToken()) != JsonToken.END_OBJECT) {

            if (t == JsonToken.FIELD_NAME) {
                String key = jp.getCurrentName();
                t = jp.nextToken();
                if (firstField) {
                    firstField = false;
                    if ("content".equals(key)) {
                        hasContent = true;
                        if (t != JsonToken.START_OBJECT) {
                            throw ctxt.mappingException(IccRelatedData.class, t);
                        }
                        continue;
                    }
                }
                EmvTag tag = EmvTag.MAP.get(key);

                String value;
                EmvData emvData = null;
                if (t != JsonToken.VALUE_NULL) {
                    value = _parseString(jp, ctxt);
                    emvData = new EmvData(Base64.getDecoder().decode(value.getBytes(ISO_8859_1)));
                }

                iccRelatedData.setTag(tag, emvData);
            }
        }
        if (hasContent) {
            t = jp.nextToken();
            if (t != JsonToken.END_OBJECT) {
                throw ctxt.mappingException(IccRelatedData.class, t);
            }
        }

        return iccRelatedData;
    }
}
