package cn.cy.core.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;
import com.google.common.primitives.Bytes;

public class MappedFileTest {

    private static Path path;

    private MappedFile mappedFile;

    public MappedFileTest() throws URISyntaxException {
    }

    @Before
    public void before() throws IOException, URISyntaxException {

        path = Paths.get(
                getClass().getClassLoader().getResource("testdata/persistence/input_1.log").toURI());

        mappedFile = new MappedFile(path);
    }

    private String readFileAsString(int size) throws IOException {
        FileChannel fileChannel = FileChannel.open(path);
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        int readSize = fileChannel.read(byteBuffer);

        System.out.println(readSize);

        byteBuffer.flip();

        // 这里不能使用byteBuffer.asCharBuffer()
        return new String(byteBuffer.array());
    }

    /**
     * check the content of the file
     */
    private void assertFileContent(String realContent, String should) {
        Assert.assertEquals(realContent, should);
    }

    @Test
    public void testReadFromFile() throws IOException {
        String res = readFileAsString(100);
        System.out.println(res);
    }

    @Test
    public void testNormalAppend() throws IOException {

        int repeatTime = 10000;

        String s = "abcdefg";

        for (int i = 0; i < repeatTime; i++) {
            try {
                mappedFile.append(Bytes.asList(s.getBytes()).toArray(new Byte[0]), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String s1 = readFileAsString(s.length() * repeatTime);

        assertFileContent(s1, Strings.repeat(s, repeatTime));
    }
}