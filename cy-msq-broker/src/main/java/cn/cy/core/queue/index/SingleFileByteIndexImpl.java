package cn.cy.core.queue.index;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.cy.core.persistence.dispatch.PersistentWriteDispatcher;
import cn.cy.core.persistence.exception.DuplicateIndexException;
import cn.cy.core.persistence.exception.PersistenceException;

public class SingleFileByteIndexImpl implements ByteIndexBySeq {

    private Path path;

    private ConcurrentMap<Long, OffsetIndex> lineCache;

    private PersistentWriteDispatcher persistentWriteDispatcher;

    private static Logger LOGGER = LoggerFactory.getLogger(SingleFileByteIndexImpl.class);

    public SingleFileByteIndexImpl() {
    }

    @Override
    public OffsetIndex getIndexBySeq(Long seq) {
        return lineCache.get(seq);
    }

    /**
     * @param seq
     * @param offsetIndex
     */
    @Override
    public void insertIndex(Long seq, OffsetIndex offsetIndex) {

        String indexStr = JSON.toJSONString(offsetIndex) + "\n";

        if (lineCache.putIfAbsent(seq, offsetIndex) != null) {
            LOGGER.error("index duplicated ! content : {}", indexStr);
            throw new DuplicateIndexException(" duplicate index : " + indexStr);
        }

        try {
            persistentWriteDispatcher.dispatchWrite().append(indexStr);
        } catch (IOException e) {
            LOGGER.error("index update failed ! content : {}", indexStr);
            throw new PersistenceException(" index update failed : " + indexStr, e.getCause());
        }
    }
}
