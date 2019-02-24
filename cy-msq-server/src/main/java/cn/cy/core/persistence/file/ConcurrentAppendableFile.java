package cn.cy.core.persistence.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    /**
     * 写锁
     */
    private final Lock appendLock = new ReentrantLock();

    public ConcurrentAppendableFile(Path path) {
        this.path = path;
        mappedFile = new MappedFile(path);
    }

    @Override
    public AppendInfo append(CharSequence csq) throws IOException {
        Lock lock = appendLock;
        try {
            lock.lock();
            return mappedFile.append(csq.toString().getBytes(), false);
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
}
