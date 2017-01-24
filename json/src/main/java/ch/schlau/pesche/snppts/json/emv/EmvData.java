package ch.schlau.pesche.snppts.json.emv;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Holds an EMV Field
 */
public class EmvData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] data;

    /**
     * Creates an instance from a string, interpreted using encoding.
     * @param data
     * @param encoding
     */
    public EmvData(String data, Charset encoding) {
        this.data = data.getBytes(encoding);
    }

    /**
     * Creates an instance with a copy of data
     * @param data
     */
    public EmvData(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    /**
     * Creates an instance from a string, interpreted as a hex string.
     * @param hex
     * @return
     */
    public static EmvData fromHexString(String hex) {
        return new EmvData(DatatypeConverter.parseHexBinary(hex));
    }

    /**
     *
     * @return a copy of the raw data
     */
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    /**
     *
     * @return the raw data as hex string
     */
    public String asHexString() {
        return DatatypeConverter.printHexBinary(data);
    }

    /**
     * Interpret the {@link #getData()} as string in ISO-8859 character set
     *
     * The EMV specification allows different ISO-8859 character sets depending on
     * the terminal capabilities
     *
     * @return
     */
    public String asString() { return new String(data, StandardCharsets.ISO_8859_1); }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("data", asHexString())
                .toString();
    }
}
