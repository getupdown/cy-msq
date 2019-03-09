package cn.cy.core.persistence.file.msg;

import java.io.IOException;

import cn.cy.core.persistence.file.AppendInfoWithId;
import cn.cy.core.persistence.file.AppendOnlyShardedFile;
import cn.cy.core.persistence.file.ConcurrentAppendableFile;

/**
 * 索引文件
 */
public class ByteIndexFile implements AppendOnlyShardedFile {

    private Integer id;

    private ConcurrentAppendableFile concurrentAppendableFile;

    @Override
    public AppendInfoWithId append(CharSequence seq) throws IOException {
        return new AppendInfoWithId(concurrentAppendableFile.append(seq), id);
    }

    @Override
    public Integer getId() {
        return id;
    }
}
