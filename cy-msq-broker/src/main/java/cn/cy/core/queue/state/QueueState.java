package cn.cy.core.queue.state;

/**
 * 描述一个队列的状态
 */
public interface QueueState {

    /**
     * 获取队列目前最大的偏移量
     *
     * @return
     */
    Long getMaxOffset();

    /**
     * 偏移量自增, 保证线程安全, 不重复
     *
     * @return 更新后的偏移量
     */
    Long increcOffset();
}
