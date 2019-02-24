package cn.cy.core.queue;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 描述一个队列的状态
 */
public interface QueueState {

    /**
     * 获取队列目前最大的偏移量
     *
     * @return
     */
    AtomicLong getMaxOffset();

    /**
     * 获取sync对象
     */
    Object getSync();
}
