package cn.cy.core.persistence.dispatch;

import java.util.concurrent.ExecutionException;

import cn.cy.common.ConcurrentFinalCache;
import cn.cy.core.persistence.exception.FileNotFoundByIndexException;
import cn.cy.core.persistence.file.RandomAccessible;
import cn.cy.core.persistence.file.msg.MessageFileFactory;
import cn.cy.core.queue.QueueConfiguration;
import cn.cy.core.queue.index.OffsetIndex;

/**
 * 用来读取消息队列里的信息
 */
public class IndexReadDispatcherImpl implements IndexReadDispatcher {

    private ConcurrentFinalCache<Integer, RandomAccessible> idToFileMap = new ConcurrentFinalCache<>();

    private MessageFileFactory messageFileFactory;

    public IndexReadDispatcherImpl(QueueConfiguration queueConfiguration) {
        this.messageFileFactory = new MessageFileFactory(queueConfiguration);
    }

    /**
     * 文件会分片, 这里会索引出特定的文件分片
     * 并且保证多线程不会重复构建
     *
     * @param offsetIndex
     *
     * @return
     *
     * @throws FileNotFoundByIndexException
     */
    @Override
    public RandomAccessible index(OffsetIndex offsetIndex) throws FileNotFoundByIndexException {

        try {

            return idToFileMap.compute(offsetIndex.getFileId(),
                    () -> messageFileFactory.loadMessageFileIntoMemory(offsetIndex.getFileId()));

        } catch (ExecutionException e) {
            Throwable t = e.getCause();
            if (t instanceof FileNotFoundByIndexException) {
                // 在文件系统中没有找到文件
                throw (FileNotFoundByIndexException) t;
            }

            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            // ignore
            return null;
        }
    }
}
