package cn.cy.core.persistence;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

import com.google.common.primitives.Bytes;

public class MappedIOTest {

    public static void main(String[] args) throws IOException {

        testMultiMMap();
    }

    public static void testEncoding() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("/Users/shixingying/Desktop/project/cy-msq/msq/src/main/java/cn"
                + "/cy/core/persistence/b.log", "rw");

        FileChannel fileChannel = raf.getChannel();

        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 4096);

        CharBuffer charBuffer = mappedByteBuffer.asCharBuffer();

        // 这时候是utf-8的字节流
        System.out.println(new String(charBuffer.toString().getBytes()));

        List<Byte> a = Bytes.asList(charBuffer.toString().getBytes());

        String utf8Str = new String(charBuffer.toString().getBytes(), Charset.forName("UTF-8"));

        byte[] unicodeBytes = utf8Str.getBytes(Charset.forName("Unicode"));

        String res = new String(unicodeBytes, Charset.defaultCharset());

        List<Byte> b = Bytes.asList(unicodeBytes);

        System.out.println(res);
    }

    /**
     * 如果有多个进程对文件mmap
     */
    public static void testMultiMMap() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("/Users/shixingying/Desktop/project/cy-msq/msq/src/main/java/cn"
                + "/cy/core/persistence/b.log", "rw");

        FileChannel fc = raf.getChannel();

        /**
         * 这里初始化的状态, 读模式
         */
        MappedByteBuffer mbf = fc.map(FileChannel.MapMode.READ_WRITE, 0, 4096);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String res = scanner.next();
            System.out.println("mbf limit is :" + mbf.toString());
            System.out.println("complete content :" + mbf.asCharBuffer().toString());
            // read
            if ("1".equals(res)) {
                System.out.println(mbf.asCharBuffer().toString());
            } else {
                mbf.putChar('a');
            }
        }
    }
}
