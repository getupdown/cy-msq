package cn.cy.core.persistence.dispatch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.cy.core.persistence.file.RandomAccessible;
import cn.cy.core.queue.QueueConfiguration;
import cn.cy.core.queue.index.OffsetIndex;

/**
 * 用来读取消息队列里的信息
 */
public class IndexReadDispatcherImpl implements IndexReadDispatcher {

    private ConcurrentMap<Integer, RandomAccessible> idToFileMap = new ConcurrentHashMap<>();

    private QueueConfiguration queueConfiguration;

    @Override
    public RandomAccessible index(OffsetIndex offsetIndex) {

        RandomAccessible accessible = idToFileMap.getOrDefault(offsetIndex.getFileId(), null);

        if (accessible == null) {
            // 首先试图去读取

            // 如果没有读取到, 抛出异常
        }

        return accessible;
    }
}
