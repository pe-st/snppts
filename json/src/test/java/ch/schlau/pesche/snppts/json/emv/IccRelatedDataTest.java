package ch.schlau.pesche.snppts.json.emv;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class IccRelatedDataTest {

    private static final String BASE64_39 = Base64.getEncoder().encodeToString("39".getBytes(StandardCharsets.ISO_8859_1));
    private static final String BASE64_123 = Base64.getEncoder().encodeToString("123".getBytes(StandardCharsets.ISO_8859_1));

    // this is an example JSON content from an older file
    private static final String JSON_WITH_CONTENT = "{\"content\" : {\"8A\" : \"" + BASE64_39 + "\", \"71\" : \"" + BASE64_123 + "\"}}";

    // this is an example JSON content with the "content" clutter removed
    private static final String JSON_WITHOUT_CONTENT = "{\"8A\" : \"" + BASE64_39 + "\", \"71\" : \"" + BASE64_123 + "\"}";

    private final ResourceReader resourceReader = new ResourceReader();
    private ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    void unmarshal_example_file() throws IOException {

        ByteArrayInputStream bis = resourceReader.streamFromResourceFile("json/icc-related.json");
        JsonParser parser = jsonMapper.getFactory().createParser(bis);
        JsonNode jsonNode = parser.readValueAsTree();
        JsonNode jsonIcc = jsonNode.get("iccRelatedData");
        IccRelatedData iccRelatedData = jsonMapper.treeToValue(jsonIcc, IccRelatedData.class);

        assertThat(iccRelatedData.getData(EmvTag.AUTHORIZATION_RESPONSE_CODE).asString(), is("39"));
        assertThat(iccRelatedData.getData(EmvTag.ISSUER_SCRIPT_TEMPLATE_1).asString(), is("123"));
    }

    @Test
    void unmarshal_example_old() throws IOException {

        ByteArrayInputStream bis = new ByteArrayInputStream(JSON_WITH_CONTENT.getBytes(StandardCharsets.UTF_8));
        IccRelatedData iccRelatedData = jsonMapper.readValue(bis, IccRelatedData.class);

        assertThat(iccRelatedData.getData(EmvTag.AUTHORIZATION_RESPONSE_CODE).asString(), is("39"));
        assertThat(iccRelatedData.getData(EmvTag.ISSUER_SCRIPT_TEMPLATE_1).asString(), is("123"));
    }

    @Test
    void unmarshal_compact() throws IOException {

        ByteArrayInputStream bis = new ByteArrayInputStream(JSON_WITHOUT_CONTENT.getBytes(StandardCharsets.UTF_8));
        IccRelatedData iccRelatedData = jsonMapper.readValue(bis, IccRelatedData.class);

        assertThat(iccRelatedData.getData(EmvTag.AUTHORIZATION_RESPONSE_CODE).asString(), is("39"));
        assertThat(iccRelatedData.getData(EmvTag.ISSUER_SCRIPT_TEMPLATE_1).asString(), is("123"));
    }

    @Test
    void unmarshal_malformed() throws IOException {

        ByteArrayInputStream bis = new ByteArrayInputStream("{\"8A\" : null".getBytes(StandardCharsets.UTF_8));

        assertThrows(JsonParseException.class, () -> jsonMapper.readValue(bis, IccRelatedData.class));
    }

    @Test
    void marshal_content() throws IOException {
        // given
        IccRelatedData iccRelatedData = new IccRelatedData();
        iccRelatedData.setTag(EmvTag.TERMINAL_COUNTRY_CODE, new EmvData(new byte[] { 0, 2 }));

        // when
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        jsonMapper.writeValue(bos, iccRelatedData);
        String jsonString = bos.toString(StandardCharsets.UTF_8.name());

        // then
        assertThat(jsonString, jsonPartEquals("9F1A", "AAI="));
    }

    @Test
    void marshal_nullValue() throws IOException {
        // given
        IccRelatedData iccRelatedData = new IccRelatedData();
        iccRelatedData.setTag(EmvTag.TERMINAL_COUNTRY_CODE, null);

        // when
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        jsonMapper.writeValue(bos, iccRelatedData);
        String jsonString = bos.toString(StandardCharsets.UTF_8.name());

        // then
        assertThat(jsonString, jsonPartEquals("9F1A", "null"));
    }

    @Test
    void json_roundtrip() throws IOException {
        // given
        IccRelatedData iccRelatedData = new IccRelatedData();
        iccRelatedData.setTag(EmvTag.TERMINAL_COUNTRY_CODE, new EmvData(new byte[] { 0, 2 }));
        iccRelatedData.setTag(EmvTag.TERMINAL_CAPABILITIES, new EmvData("3".getBytes()));

        // when
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        jsonMapper.writeValue(bos, iccRelatedData);
        String jsonString = bos.toString(StandardCharsets.UTF_8.name());

        ByteArrayInputStream bis = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        IccRelatedData roundtrip = jsonMapper.readValue(bis, IccRelatedData.class);

        // then
        assertThat(roundtrip.getData(EmvTag.TERMINAL_COUNTRY_CODE).asHexString(), is("0002"));
        assertThat(roundtrip.getData(EmvTag.TERMINAL_CAPABILITIES).asString(), is("3"));
    }
}