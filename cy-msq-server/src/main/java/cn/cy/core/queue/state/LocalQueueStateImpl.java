package cn.cy.core.queue.state;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于内存的本地队列状态
 */
public class LocalQueueStateImpl implements QueueState {

    private AtomicLong maxOffset = new AtomicLong(0);

    @Override
    public Long getMaxOffset() {
        return maxOffset.get();
    }

    @Override
    public Long increcOffset() {
        return maxOffset.incrementAndGet();
    }
}
