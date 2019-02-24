package cn.cy.core.persistence.dispatch;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cy.common.ConcurrentFinalCache;
import cn.cy.core.persistence.exception.PersistenceException;
import cn.cy.core.persistence.file.QueueMsgFile;
import cn.cy.core.persistence.file.msg.MessageFileFactory;
import cn.cy.core.queue.QueueConfiguration;
import cn.cy.core.queue.QueueState;

/**
 * 读写分配实现
 */
public class PersistentDispatcherImpl implements PersistentDispatcher {

    private static Logger LOGGER = LoggerFactory.getLogger(PersistentDispatcherImpl.class);

    private List<QueueMsgFile> messageFiles;

    // 表示当前消息应该写入哪个文件
    private int writeIndex;

    // 队列配置
    private QueueConfiguration queueConfiguration;

    private final Object sync = new Object();

    // 防止出现构建文件
    private ConcurrentFinalCache<Integer, QueueMsgFile> buildFutureCache = new ConcurrentFinalCache<>();

    // 消息文件工厂
    private MessageFileFactory messageFileFactory;

    /**
     * 根据当前队列状态, 分配消息写入哪个文件
     *
     * @param state
     *
     * @return
     */
    @Override
    public QueueMsgFile dispatchWrite(QueueState state) {

        synchronized(sync) {
            return getWritable();
        }

    }

    @Override
    public QueueMsgFile dispatchRead(QueueState state) {
        throw new IllegalArgumentException("not implemented");
    }

    /**
     * 移动写下标, 直到获得可以写的文件
     */
    private QueueMsgFile getWritable() {

//        while (writeIndex < messageFiles.size()) {
//            if (messageFiles.get(writeIndex).getMsgCntInFile() < queueConfiguration.MAX_MSG_PER_FILE) {
//                return messageFiles.get(writeIndex);
//            }
//            writeIndex++;
//        }

        // 说明需要新建文件
        if (writeIndex == messageFiles.size()) {

            QueueMsgFile file = null;
            try {
                file = buildFutureCache.compute(writeIndex, () -> {
                    return null;
                });
            } catch (ExecutionException e) {
                LOGGER.error("ExecutionException found when build new file ");
                throw new PersistenceException(e);
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted found when build new file ");
                throw new PersistenceException(e);
            }
            messageFiles.add(file);

            return file;
        }

        // 不应执行到这里
        throw new IllegalArgumentException();
    }
}
