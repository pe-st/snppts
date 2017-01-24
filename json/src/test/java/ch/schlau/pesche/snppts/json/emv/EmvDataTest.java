package ch.schlau.pesche.snppts.json.emv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

public class EmvDataTest {

    EmvData defaultData;

    @Before
    public void init() {
        defaultData = new EmvData(new byte[] { 1, 2, 3, 4 });
    }

    @Test
    public void testGetData() throws Exception {
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, defaultData.getData());
    }

    @Test
    public void testFromString() throws Exception {
        defaultData = new EmvData("1234", StandardCharsets.US_ASCII);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33, 0x34 }, defaultData.getData());
    }

    @Test
    public void testAsHexString() throws Exception {
        defaultData = new EmvData("1234", StandardCharsets.US_ASCII);
        assertEquals("31323334", defaultData.asHexString());
    }
}
