package cn.cy.core.persistence.dispatch;

import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.cy.core.persistence.exception.FileNotFoundByIndexException;
import cn.cy.core.persistence.file.RandomAccessible;
import cn.cy.core.persistence.file.msg.MessageFileFactory;
import cn.cy.core.queue.QueueConfiguration;
import cn.cy.core.queue.index.OffsetIndex;

/**
 * 用来读取消息队列里的信息
 */
public class IndexReadDispatcherImpl implements IndexReadDispatcher {

    private ConcurrentMap<Integer, RandomAccessible> idToFileMap = new ConcurrentHashMap<>();

    private QueueConfiguration queueConfiguration;

    private MessageFileFactory messageFileFactory;

    @Override
    public RandomAccessible index(OffsetIndex offsetIndex) throws FileNotFoundByIndexException {

        RandomAccessible accessible = idToFileMap.getOrDefault(offsetIndex.getFileId(), null);

        if (accessible == null) {
            // 首先试图去读取
            try {

                accessible = messageFileFactory.loadMessageFileIntoMemory(offsetIndex.getFileId());

            } catch (FileNotFoundException e) {
                throw new FileNotFoundByIndexException(e);
            }

        }

        return accessible;
    }
}
