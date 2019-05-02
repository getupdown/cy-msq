package cn.cy.core.persistence;

import java.io.IOException;
import java.nio.channels.FileChannel;

import cn.cy.core.persistence.file.AppendInfo;

/**
 * 持久化消息接口
 */
public interface PersistenceProcessor {

    AppendInfo append(byte[] target, boolean forceFlush) throws IOException;

    byte[] read(long start, long size) throws IOException;

    void close() throws IOException;
}
