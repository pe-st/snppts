package ch.schlau.pesche.snppts.testing;

import java.io.IOException;
import java.net.URISyntaxException;

public class ResourceUtils {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ResourceUtils.class);

    /**
     * Read a file into a String, without checked exceptions
     *
     * @param filename name of the file, relative to the resources directory
     * @return contents of the file
     *
     * @throws IllegalStateException any problem with accessing the file is wrapped into a IllegalStateException
     */
    public String stringFromResourceFile(String filename) {

        java.net.URL url = getClass().getClassLoader().getResource(filename);
        try {
            java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
            return new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        } catch (URISyntaxException e) {
            logger.error("Problem with the filename URI", e);
            throw new IllegalStateException("Problem with the filename URI", e);
        } catch (IOException e) {
            logger.error("Problem while reading the file", e);
            throw new IllegalStateException("Problem while reading the file", e);
        }
    }
}
