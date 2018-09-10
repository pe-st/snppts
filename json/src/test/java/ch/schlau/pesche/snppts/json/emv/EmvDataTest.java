package ch.schlau.pesche.snppts.json.emv;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmvDataTest {

    private EmvData defaultData;

    @BeforeEach
    void init() {
        defaultData = new EmvData(new byte[] { 1, 2, 3, 4 });
    }

    @Test
    void testGetData() throws Exception {
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, defaultData.getData());
    }

    @Test
    void testFromString() throws Exception {
        defaultData = new EmvData("1234", StandardCharsets.US_ASCII);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33, 0x34 }, defaultData.getData());
    }

    @Test
    void testAsHexString() throws Exception {
        defaultData = new EmvData("1234", StandardCharsets.US_ASCII);
        assertEquals("31323334", defaultData.asHexString());
    }
}
