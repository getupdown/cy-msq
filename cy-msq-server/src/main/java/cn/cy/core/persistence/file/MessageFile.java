package cn.cy.core.persistence.file;

import java.io.IOException;
import java.nio.file.Path;

import cn.cy.core.queue.index.ByteIndexBySeq;
import cn.cy.core.queue.index.OffsetIndex;

/**
 * {@link cn.cy.core.msg.QueuedMessage}
 * <p>
 * 这里是以"行"为单位存储的每一条消息实体
 */
public class MessageFile implements WriteByAppend {

    private Integer id;

    private ConcurrentAppendableFile concurrentAppendableFile;

    private final Path path;

    private ByteIndexBySeq byteIndexBySeq;

    public MessageFile(Path path, ByteIndexBySeq byteIndexBySeq) {
        this.path = path;
        this.byteIndexBySeq = byteIndexBySeq;
        concurrentAppendableFile = new ConcurrentAppendableFile(this.path);
    }

    // 读取这个里面的第几条消息
    public String readBySeq(Long seq) throws IOException {
        OffsetIndex offsetIndex = byteIndexBySeq.getIndexBySeq(seq);

        return concurrentAppendableFile
                .readFromBytes(offsetIndex.getByteOffset(), offsetIndex.getLength());
    }

    @Override
    public void append(CharSequence csq) throws IOException {
        concurrentAppendableFile.append(csq);
    }
}
