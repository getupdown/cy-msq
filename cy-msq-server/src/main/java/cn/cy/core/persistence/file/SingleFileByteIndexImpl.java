package cn.cy.core.persistence.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;

import cn.cy.core.persistence.exception.DuplicateIndexException;
import cn.cy.core.persistence.exception.PersistenceException;
import cn.cy.core.queue.index.ByteIndexBySeq;
import cn.cy.core.queue.index.OffsetIndex;

public class SingleFileByteIndexImpl implements ByteIndexBySeq {

    private Path path;

    private ConcurrentMap<Long, OffsetIndex> lineCache;

    private ConcurrentAppendableFile concurrentAppendableFile;

    private static Logger LOGGER = LoggerFactory.getLogger(SingleFileByteIndexImpl.class);

    public SingleFileByteIndexImpl() {
    }

    public SingleFileByteIndexImpl(Path path, Long startSeq) throws IOException {
        this();
        this.path = path;
        concurrentAppendableFile = new ConcurrentAppendableFile(path);
        loadIndex();
    }

    private void loadIndex() throws IOException {

        Iterator<String> iterator = Files.readLines(path.toFile(), Charset.defaultCharset()).iterator();

        while (iterator.hasNext()) {
            String rawIndex = iterator.next();
            OffsetIndex singleIndex = JSON.parseObject(rawIndex, OffsetIndex.class);
            lineCache.put(singleIndex.getMsgOffset(), singleIndex);
        }
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
            concurrentAppendableFile.append(indexStr);
        } catch (IOException e) {
            LOGGER.error("index update failed ! content : {}", indexStr);
            throw new PersistenceException(" index update failed : " + indexStr, e.getCause());
        }
    }
}
