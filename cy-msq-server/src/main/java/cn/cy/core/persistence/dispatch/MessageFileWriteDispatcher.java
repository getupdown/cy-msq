package cn.cy.core.persistence.dispatch;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import cn.cy.common.ConcurrentFinalCache;
import cn.cy.core.config.QueueConfiguration;
import cn.cy.core.persistence.exception.PersistenceException;
import cn.cy.core.persistence.file.QueueMsgFile;
import cn.cy.core.persistence.file.msg.MessageFileFactory;

/**
 * 消息文件读写分配实现
 */
public class MessageFileWriteDispatcher extends AbstractPersistentWriteDispatcher {

    private static Logger LOGGER = LoggerFactory.getLogger(MessageFileWriteDispatcher.class);

    protected final List<QueueMsgFile> messageFiles;

    // 表示当前消息应该写入哪个文件
    protected volatile int writeIndex;

    // 队列配置
    private final QueueConfiguration queueConfiguration;

    // 防止出现重复构建文件
    private final ConcurrentFinalCache<Integer, QueueMsgFile> buildFutureCache = new ConcurrentFinalCache<>();

    // 消息文件工厂
    private final MessageFileFactory messageFileFactory;

    public MessageFileWriteDispatcher(QueueConfiguration queueConfiguration) {
        this(Lists.newArrayList(), queueConfiguration);
    }

    public MessageFileWriteDispatcher(List<QueueMsgFile> messageFiles,
                                      QueueConfiguration queueConfiguration) {
        this(messageFiles, 0, queueConfiguration);
    }

    public MessageFileWriteDispatcher(List<QueueMsgFile> messageFiles, int writeIndex,
                                      QueueConfiguration queueConfiguration) {
        this.messageFiles = messageFiles;
        this.writeIndex = writeIndex;
        this.queueConfiguration = queueConfiguration;
        this.messageFileFactory = new MessageFileFactory(queueConfiguration);
    }

    /**
     * 移动写下标, 直到获得可以写的文件
     */
    protected QueueMsgFile getWritable() {

        synchronized(this) {
            while (writeIndex < messageFiles.size()) {
                // 虽然这里代码是这么写了, 但是经过测试发现, 还是有概率出现文件大小超过这个阈值的情况, 这点误差可以容忍
                if (messageFiles.get(writeIndex).getContentCnt() < queueConfiguration.MAX_MSG_PER_FILE) {
                    return messageFiles.get(writeIndex);
                }
                writeIndex++;
            }
        }

        // 说明需要新建文件
        QueueMsgFile file = null;
        try {
            file = buildFutureCache.compute(writeIndex, () -> {
                QueueMsgFile newMsgFile = createNewMsgFile();
                messageFiles.add(newMsgFile);
                return newMsgFile;
            });
        } catch (ExecutionException e) {
            LOGGER.error("ExecutionException found when build new file ");
            throw new PersistenceException(e);
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted found when build new file ");
            throw new PersistenceException(e);
        }

        return file;
    }

    protected QueueMsgFile createNewMsgFile() {
        return messageFileFactory.buildMessageFile(writeIndex);
    }
}
