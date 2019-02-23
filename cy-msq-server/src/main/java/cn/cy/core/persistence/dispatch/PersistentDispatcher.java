package cn.cy.core.persistence.dispatch;

/**
 * 由于一个队列底层不仅仅有一个文件所支持
 * 所以这里为{@link cn.cy.core.queue.MessageQueue} 提供抽象
 * 封装所有 ** 选择文件 ** 去读写的细节
 */
public interface PersistentDispatcher {



}
