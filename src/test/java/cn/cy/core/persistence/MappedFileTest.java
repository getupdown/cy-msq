package cn.cy.core.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;
import com.google.common.primitives.Bytes;

public class MappedFileTest extends BasePersistenceTest {

    private static Path path;

    private MappedFile mappedFile;

    private String writtenStr;

    public MappedFileTest() throws URISyntaxException {
    }

    @Before
    public void before() throws IOException, URISyntaxException {
        createFile();
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
    public void testNormalAppend1() throws IOException {

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

    @Test
    public void testNormalAppend2() throws IOException {
        // 一次性大量
        writtenStr = Strings.repeat("qweasdzxcasdqweasdzxcasdqweasdzxc", 100000);

        mappedFile.append(Bytes.asList(writtenStr.getBytes()).toArray(new Byte[0]), true);

        String s1 = readFileAsString(writtenStr.length());

        assertFileContent(s1, writtenStr);
    }

}