package cn.cy.core.persistence;

import cn.cy.core.msg.QueuedMessage;
import cn.cy.core.queue.QueueState;

/**
 * 一个PersistenceManager 对应 一个
 */
public class PersistenceManager {

    /**
     * 根据队列状态, 把消息写入对应的持久化介质
     *
     * @param state         队列状态
     * @param queuedMessage 入队消息
     */
    private void write(QueueState state, QueuedMessage queuedMessage) {

    }

    /**
     *
     */
    private QueuedMessage read(Long seq, QueueState state) {
        return null;
    }
}
