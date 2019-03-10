package cn.cy.core.persistence.file;

import java.io.IOException;

/**
 * 消息队列部分的文件
 */
public interface QueueMsgFile extends AppendOnlyShardedFile {

    /**
     * 从文件里随机读消息
     *
     * @param offset
     * @param length
     *
     * @return
     *
     * @throws IOException
     */
    String randomRead(Long offset, Long length) throws IOException;

    /**
     * 获取文件内的消息个数
     *
     * @return
     */
    Long getContentCnt();
}
