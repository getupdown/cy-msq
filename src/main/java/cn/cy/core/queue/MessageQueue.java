package cn.cy.core.queue;

import cn.cy.core.msg.QueuedMessage;

/**
 * 约定消息队列的接口
 */
public interface MessageQueue {

    /**
     * 消息队列接受消息
     *
     * @throws Exception
     */
    void push(QueuedMessage queuedMessage) throws Exception;

    /**
     * 消费指定offset的消息
     *
     * @throws {@link QueueAccessOutOfIndexException}
     */
    QueuedMessage consumeMsgAt(int offset);
}
