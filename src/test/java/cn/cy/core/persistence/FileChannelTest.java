package cn.cy.core.persistence;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

import org.junit.Assert;
import org.junit.Test;

public class FileChannelTest extends BasePersistenceTest {

    /**
     * 测试对于同一个区域
     * 用{@link java.nio.channels.FileChannel.MapMode#READ_WRITE} 和
     * {@link java.nio.channels.FileChannel.MapMode#READ_ONLY}
     * 同时映射, 后者是不是会看到前者的改变
     *
     * 事实证明readonly这个是可以看到的
     */
    @Test
    public void testReadOnlyAccessible() throws IOException {

        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);

        MappedByteBuffer writeBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 8192);

        MappedByteBuffer readOnlyBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, 8192);

        for (int i = 0; i < 5 ; i ++) {
            writeBuffer.put((byte) i);
        }

        for (int i = 0;i < 5; i ++) {
            Assert.assertEquals(readOnlyBuffer.get(), (byte) i);
        }
    }
}
