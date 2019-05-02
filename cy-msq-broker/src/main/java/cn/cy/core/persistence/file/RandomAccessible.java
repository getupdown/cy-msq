package cn.cy.core.persistence.file;

import java.io.IOException;

public interface RandomAccessible {
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
