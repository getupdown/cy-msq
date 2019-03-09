package cn.cy.core.persistence.file;

import java.io.IOException;

/**
 * 描述一类文件
 * 1. 只能以行为单位追加写
 * 2. 分片
 */
public interface AppendOnlyShardedFile {

    /**
     * 把消息插入文件
     *
     * @param seq
     *
     * @return
     *
     * @throws IOException
     */
    AppendInfoWithId append(CharSequence seq) throws IOException;

    /**
     * 文件的id
     *
     * @return
     */
    Integer getId();
}
