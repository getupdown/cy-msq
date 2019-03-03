package cn.cy.core.persistence.dispatch;

import cn.cy.core.persistence.file.QueueMsgFile;
import cn.cy.core.queue.QueueState;

public abstract class AbstractPersistentDispatcher implements PersistentDispatcher {

    private Object sync = new Object();

    @Override
    public QueueMsgFile dispatchWrite(QueueState state) {
        synchronized(sync) {
            return getWritable();
        }
    }

    @Override
    public QueueMsgFile dispatchRead(QueueState state) {
        return null;
    }

    protected abstract QueueMsgFile getWritable();

    protected abstract QueueMsgFile createNewMsgFile();
}
