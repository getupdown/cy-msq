package cn.cy.core.persistence.file;

import java.io.IOException;

/**
 * 消息队列部分的文件
 */
public interface QueueMsgFile {

    /**
     * 队列里某个文件的id
     *
     * @return
     */
    Integer getId();

    /**
     * 把消息插入文件
     *
     * @param seq
     *
     * @return
     *
     * @throws IOException
     */
    QueueAppendInfo append(CharSequence seq) throws IOException;

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
}
