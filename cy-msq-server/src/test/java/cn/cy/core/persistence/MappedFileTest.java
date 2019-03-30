package cn.cy.core.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;
import com.google.common.primitives.Bytes;

import cn.cy.core.persistence.file.MappedFile;

public class MappedFileTest extends BasePersistenceTest {

    private MappedFile mappedFile;

    private String writtenStr;

    public MappedFileTest() throws URISyntaxException {
    }

    @Before
    public void before() throws IOException, URISyntaxException {
        createFile();
        mappedFile = new MappedFile(path, false);
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
                mappedFile.append(s.getBytes(), true);
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

        mappedFile.append(writtenStr.getBytes(), true);

        String s1 = readFileAsString(writtenStr.length());

        assertFileContent(s1, writtenStr);
    }

    private byte[] writeRandomBytes() throws IOException {
        int length = 10000;

        byte[] bytes = new byte[length];

        Random random = new Random();

        random.nextBytes(bytes);

        mappedFile.append(bytes, true);

        return bytes;
    }

    /**
     * 测试读方法
     */
    @Test
    public void testRead() throws IOException {
        byte[] res = writeRandomBytes();

        int sampleCnt = 100000;

        for (int i = 0; i < sampleCnt; i++) {
            Random random = new Random();

            int start = random.nextInt(9999);

            int length = Math.min(1000, 10000 - start);

            Byte[] cmp1 = Bytes.asList(mappedFile.read(start, length)).toArray(new Byte[0]);

            Byte[] cmp2 = Arrays.copyOfRange(Bytes.asList(res).toArray(new Byte[0]), start, start + length);

            Assert.assertArrayEquals(cmp1, cmp2);
        }
    }

    /**
     * 测试文件的size返回是否正确
     */
    @Test
    public void testSize() throws IOException {
        String input = "abcdefg";
        mappedFile.append(input.getBytes(), false);

        mappedFile = new MappedFile(path, false);
    }
}