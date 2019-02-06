package cn.cy.core.persistence;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;

public class MappedFile {

    private FileChannel fileChannel;

    private Path path;

    /**
     * 用于追加写的buffer
     */
    private MappedByteBuffer appendBuffer;

    /**
     * 页管理
     */
    private LinkedHashMap<Long, MappedByteBuffer> cachePages = new LinkedHashMap<>();

    /**
     * 尾部偏移量大小
     */
    private long tailOffset = 0L;

    /**
     * {@link MappedFile#appendBuffer} 的头偏移量
     */
    private long headOffset = 0L;

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
     * must be called in the sync field
     *
     * @param bytes the bytes array to be written to the file
     * @param force indicates that if the content will be written to the file immediately
     */
    public void append(Byte[] bytes, boolean force) throws IOException {
        ensureOpen();
        // 预估大小
        if (appendBuffer.position() + bytes.length > appendBuffer.capacity()) {
            // 扩充大小
            appendBuffer = allocateAppendableBuffer(tailOffset, bytes.length);
        }

        for (Byte aByte : bytes) {
            appendBuffer.put(aByte);
        }

        tailOffset += bytes.length;

        if (force) {
            appendBuffer.force();
        }
    }

    /**
     * allocate a new region for the {@link MappedFile#appendBuffer}
     *
     * @param startOffset
     * @param length
     *
     * @return
     *
     * @throws IOException
     */
    private MappedByteBuffer allocateAppendableBuffer(long startOffset, long length) throws IOException {
        headOffset = tailOffset;
        return fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, length);
    }

    /**
     * allocate a readonly buffer
     *
     * @param startOffset
     * @param length
     *
     * @return
     *
     * @throws IOException
     */
    private MappedByteBuffer allocateReadonlyBuffer(long startOffset, long length) throws IOException {
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);
    }

    /**
     * read the file
     * must be called in the sync field
     */
    public void read(long offset, long length) throws IOException {
        ensureOpen();

        MappedByteBuffer readOnlyBuffer = getReadBuffer(offset);

    }

    /**
     * getReadBuffer
     */
    private MappedByteBuffer getReadBuffer(long offset) throws IOException {

        // 特判appendBuffer
        if (offset >= headOffset) {
            return appendBuffer;
        }

        //
        Long pageNo = offset / PAGE_SIZE;
        MappedByteBuffer res = readFromCacheByIndex(pageNo);

        if (res == null) {
            res = allocateReadonlyBuffer(offset, PAGE_SIZE);
            updateCache(pageNo, res);
        }

        return res;
    }

    private MappedByteBuffer readFromCacheByIndex(Long index) {
        return cachePages.getOrDefault(index, null);
    }

    private void updateCache(Long index, MappedByteBuffer content) {
        cachePages.put(index, content);

        //todo 淘汰策略
    }
}
