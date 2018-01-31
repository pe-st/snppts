package ch.schlau.pesche.snppts.testing;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ResourceUtilsTest {

    @Test
    public void stringFromResourceFile_42() {

        ResourceUtils resourceUtils = new ResourceUtils();
        assertThat(resourceUtils.stringFromResourceFile("42.txt"), is("42"));
    }

    @Test
    public void stringFromResourceFileIO_42() {

        ResourceUtils resourceUtils = new ResourceUtils();
        assertThat(resourceUtils.stringFromResourceFileIO("42.txt"), is("42"));
    }
}