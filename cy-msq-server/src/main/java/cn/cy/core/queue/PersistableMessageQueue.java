package cn.cy.core.queue;

import cn.cy.core.msg.QueuedMessage;
import cn.cy.core.persistence.file.msg.MessageFile;

/**
 * 可持久化的消息队列
 */
public class PersistableMessageQueue implements MessageQueue {

    private MessageFile messageFile;

    private QueueState queueState;



    @Override
    public void push(QueuedMessage queuedMessage) throws Exception {

    }

    @Override
    public QueuedMessage consumeMsgAt(int offset) {
        return null;
    }
}
