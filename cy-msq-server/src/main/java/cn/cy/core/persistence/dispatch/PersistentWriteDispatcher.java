package cn.cy.core.persistence.dispatch;

import cn.cy.core.persistence.file.AppendOnlyShardedFile;

/**
 * 由于一个队列底层不仅仅有一个文件所支持
 * 封装所有 ** 选择文件 ** 去读写的细节
 */
public interface PersistentWriteDispatcher {

    /**
     * 分配写队列操作
     */
    AppendOnlyShardedFile dispatchWrite();

}
