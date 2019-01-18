package cn.cy.core.persistence;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import sun.misc.LRUCache;

public class MappedFile {

    private FileChannel fileChannel;

    private Path path;

    /**
     * 用于追加写的buffer
     */
    private MappedByteBuffer appendBuffer;

    /**
     * 尾部偏移量大小
     */
    private long tailOffset = 0L;

    /**
     * 每一页的大小, 降低锁粒度
     */
    private static final long PAGE_SIZE = 8192;

    public MappedFile(Path path) {
        this.path = path;
    }

    private void ensureOpen() throws IOException {
        if (fileChannel == null) {
            fileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.READ);
            appendBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, tailOffset, 40960);
        }
    }

    /**
     * append the bytes to the file
     *
     * @param bytes the bytes array to be written to the file
     * @param force indicates that if the content will be written to the file immediately
     */
    public void append(Byte[] bytes, boolean force) throws IOException {
        ensureOpen();
        // 预估大小
        if (appendBuffer.position() + bytes.length > appendBuffer.capacity()) {
            // 扩充大小
            appendBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, tailOffset, bytes.length);
        }

        for (Byte aByte : bytes) {
            appendBuffer.put(aByte);
        }

        tailOffset += bytes.length;

        if (force) {
            appendBuffer.force();
        }
    }
}
