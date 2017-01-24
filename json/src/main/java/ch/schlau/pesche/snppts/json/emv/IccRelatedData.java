package ch.schlau.pesche.snppts.json.emv;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Collection of ICC related data (a.k.a. EMV data)
 */
@JsonDeserialize(using = IccRelatedDataDeserializer.class)
@JsonSerialize(using = IccRelatedDataSerializer.class)
public final class IccRelatedData {

    private Map<EmvTag, EmvData> content = new HashMap<>();

    /**
     * Sets the value of tag to value, if the tag is absent, it is added, if it is present, it is replaced
     *
     * @param tag
     * @param value
     */
    public void setTag(EmvTag tag, EmvData value) {
        content.put(tag, value);
    }

    public Map<EmvTag, EmvData> getContent() {
        return content;
    }

    /**
     * @return All the tags that are set in this IccRelatedData instance
     */
    public Set<EmvTag> tagSet() {
        return content.keySet();
    }

    /**
     * @param tag
     * @return true if tag is set
     */
    public boolean hasTag(EmvTag tag) {
        return content.containsKey(tag);
    }

    /**
     * @param tag
     * @return the value corresponding to tag, or {@code null} if it is not set
     */
    public EmvData getData(EmvTag tag) {
        return content.get(tag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("content", content)
                .toString();
    }
}
