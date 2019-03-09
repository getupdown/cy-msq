package cn.cy.core.persistence.dispatch;

import cn.cy.core.persistence.file.AppendOnlyShardedFile;

public abstract class AbstractPersistentWriteDispatcher implements PersistentWriteDispatcher {

    private Object sync = new Object();

    @Override
    public AppendOnlyShardedFile dispatchWrite() {
        synchronized(sync) {
            return getWritable();
        }
    }

    protected abstract AppendOnlyShardedFile getWritable();
}
