package cn.cy.core.persistence.file;

/**
 * 消息队列部分的文件
 */
public interface QueueMsgFile extends AppendOnlyShardedFile, RandomAccessible {

    /**
     * 获取文件内的消息个数
     *
     * @return
     */
    Long getContentCnt();
}
