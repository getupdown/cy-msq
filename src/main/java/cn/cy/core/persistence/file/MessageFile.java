package cn.cy.core.persistence.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;

import com.google.common.primitives.Chars;

import cn.cy.core.persistence.cache.PageLockFactory;

/**
 * 描述存储着{@link cn.cy.core.msg.QueuedMessage}的文件
 * <p>
 * 文件格式：
 * 每一条消息以"行"的形式存储，即一行一条消息
 */
public class MessageFile implements Appendable {

    /**
     * 文件路径
     */
    private Path path;

    /**
     * 底层对应的真实文件
     */
    private MappedFile mappedFile;

    /**
     * 分页锁
     */
    private final PageLockFactory pageLockFactory;

    /**
     * 用作追加文件的写锁
     */
    private Integer APPEND_LOCK = Integer.MAX_VALUE;

    private MessageFile() {
        pageLockFactory = new PageLockFactory();
    }

    public MessageFile(Path path) {
        this();
        this.path = path;
        mappedFile = new MappedFile(path);
    }

    private Lock offerLock(Integer key) {
        try {
            return pageLockFactory.offerLock(key);
        } catch (ExecutionException | InterruptedException e) {
            // ignore
        }
        return null;
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {

        Lock lock = offerLock(APPEND_LOCK);
        assert lock != null;
        try {
            lock.lock();
            mappedFile.append(csq.toString().getBytes(), false);
        } finally {
            lock.unlock();
        }
        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        Lock lock = offerLock(APPEND_LOCK);
        assert lock != null;
        try {
            lock.lock();
            mappedFile.append(csq.subSequence(start, end).toString().getBytes(), false);
        } finally {
            lock.unlock();
        }
        return this;
    }

    @Override
    public Appendable append(char c) throws IOException {
        Lock lock = offerLock(APPEND_LOCK);
        assert lock != null;
        try {
            lock.lock();
            mappedFile.append(Chars.toByteArray(c), false);
        } finally {
            lock.unlock();
        }
        return this;
    }
}
