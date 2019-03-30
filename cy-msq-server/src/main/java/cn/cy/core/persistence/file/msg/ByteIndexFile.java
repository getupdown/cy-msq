package cn.cy.core.persistence.file.msg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import cn.cy.core.persistence.file.AppendInfoWithId;
import cn.cy.core.persistence.file.AppendOnlyShardedFile;
import cn.cy.core.persistence.file.ConcurrentAppendableFile;

/**
 * 索引文件
 */
public class ByteIndexFile implements AppendOnlyShardedFile {

    private Integer id;

    private ConcurrentAppendableFile concurrentAppendableFile;

    private Path path;

    public ByteIndexFile(Integer id, Path path, boolean assertExist) throws FileNotFoundException {
        this.id = id;
        this.path = path;
        concurrentAppendableFile = new ConcurrentAppendableFile(path, assertExist);
    }

    @Override
    public AppendInfoWithId append(CharSequence seq) throws IOException {
        return new AppendInfoWithId(concurrentAppendableFile.append(seq), id);
    }

    @Override
    public Integer getId() {
        return id;
    }
}
