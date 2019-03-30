package cn.cy.core.persistence.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.cy.core.persistence.file.msg.MappedFileInfo;

/**
 * 可以并行读写的文件
 */
public class ConcurrentAppendableFile implements WriteByAppend {

    /**
     * 文件路径
     */
    private Path path;

    /**
     * 底层对应的真实文件
     */
    private MappedFile mappedFile;

    private MappedFileInfo mappedFileInfo;

    /**
     * 写锁
     */
    private final Lock appendLock = new ReentrantLock();

    public ConcurrentAppendableFile(Path path, boolean assertExist) throws FileNotFoundException {
        this.path = path;
        mappedFile = new MappedFile(path, assertExist);
        this.mappedFileInfo = new MappedFileInfo();
    }

    public ConcurrentAppendableFile(Path path, Long tailOffset, boolean assertExist) throws FileNotFoundException {
        this.path = path;
        this.mappedFileInfo = new MappedFileInfo();
        mappedFileInfo.setNextWritableOffset(tailOffset);
        mappedFile = new MappedFile(path, tailOffset, assertExist);
    }

    @Override
    public AppendInfo append(CharSequence csq) throws IOException {
        Lock lock = appendLock;
        try {
            lock.lock();
            AppendInfo appendInfo = mappedFile.append(csq.toString().getBytes(), false);

            updateMappedFileInfo(mappedFileInfo, appendInfo);

            return appendInfo;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param byteOffset 字节的偏移量
     * @param byteLength 字节的长度
     *
     * @return
     */
    public String readFromBytes(long byteOffset, long byteLength) throws IOException {
        return new String(mappedFile.read(byteOffset, byteLength));
    }

    private void updateMappedFileInfo(MappedFileInfo mappedFileInfo, AppendInfo appendInfo) {
        mappedFileInfo.setNextWritableOffset(mappedFileInfo.getNextWritableOffset() + appendInfo.getLength());
    }

    public Long getNextWritableOffset() {
        return mappedFileInfo.getNextWritableOffset();
    }
}
