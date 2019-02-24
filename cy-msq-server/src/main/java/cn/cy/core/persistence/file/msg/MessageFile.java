package cn.cy.core.persistence.file.msg;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import cn.cy.core.persistence.file.AppendInfo;
import cn.cy.core.persistence.file.ConcurrentAppendableFile;
import cn.cy.core.persistence.file.QueueAppendInfo;
import cn.cy.core.persistence.file.QueueMsgFile;

/**
 * {@link cn.cy.core.msg.QueuedMessage}
 * <p>
 * 这里是以"行"为单位存储的每一条消息实体
 */
public class MessageFile implements QueueMsgFile {

    private Integer id;

    private ConcurrentAppendableFile concurrentAppendableFile;

    private final Path path;

    public MessageFile(Path path, Integer id, Long msgCnt) {
        this.path = path;
        this.id = id;
        concurrentAppendableFile = new ConcurrentAppendableFile(this.path);
    }

    // 读取这个里面的第几条消息
    @Override
    public String randomRead(Long offset, Long length) throws IOException {
        return concurrentAppendableFile.readFromBytes(offset, length);
    }

    @Override
    public QueueAppendInfo append(CharSequence csq) throws IOException {
        AppendInfo appendInfo = concurrentAppendableFile.append(csq);
        return new QueueAppendInfo(appendInfo, id);
    }

    @Override
    public Integer getId() {
        return id;
    }
}
