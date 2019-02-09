package cn.cy.core.persistence;

import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 持久化消息接口
 */
public interface PersistenceProcessor {

    void append(Byte[] target, boolean forceFlush) throws IOException;

    Byte[] read(long start, long size) throws IOException;

    FileChannel getFileChannel() throws IOException;
}
