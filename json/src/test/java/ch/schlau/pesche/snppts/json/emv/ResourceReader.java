package ch.schlau.pesche.snppts.json.emv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class ResourceReader {

    public ByteArrayInputStream streamFromResourceFile(String filename) throws IOException {

        java.net.URL url = getClass().getClassLoader().getResource(filename);
        String jsonString;
        try {
            java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
            jsonString = new String(java.nio.file.Files.readAllBytes(resPath), StandardCharsets.UTF_8);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Problem with the filename URI", e);
        } catch (IOException e) {
            throw new IllegalStateException("Problem while reading the file", e);
        }

        return new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
    }
}
