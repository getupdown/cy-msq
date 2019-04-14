package cn.cy.core.persistence;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.cy.core.msg.QueuedMessage;
import cn.cy.core.persistence.dispatch.IndexReadDispatcher;
import cn.cy.core.persistence.dispatch.PersistentWriteDispatcher;
import cn.cy.core.persistence.exception.CheckSumException;
import cn.cy.core.persistence.exception.FileNotFoundByIndexException;
import cn.cy.core.persistence.exception.PersistenceException;
import cn.cy.core.persistence.file.AppendInfoWithId;
import cn.cy.core.persistence.file.AppendOnlyShardedFile;
import cn.cy.core.persistence.file.RandomAccessible;
import cn.cy.core.queue.state.QueueState;
import cn.cy.core.queue.index.ByteIndexBySeq;
import cn.cy.core.queue.index.OffsetIndex;

/**
 * 这里封装了所有的持久化相关过程
 */
public class PersistenceManager {

    private static Logger LOGGER = LoggerFactory.getLogger(PersistenceManager.class);

    private PersistentWriteDispatcher writeDispatcher;

    private ByteIndexBySeq byteIndexBySeq;

    private IndexReadDispatcher readDispatcher;

    /**
     * 根据队列状态, 把消息写入对应的持久化介质
     *
     * @param state         队列状态
     * @param queuedMessage 入队消息
     */
    public void write(QueueState state, QueuedMessage queuedMessage) {

        String rawMsg = JSON.toJSONString(queuedMessage) + "\n";

        Long nextOffset = state.increcOffset();

        AppendOnlyShardedFile queueMsgFile = writeDispatcher.dispatchWrite();

        try {

            AppendInfoWithId appendInfo = queueMsgFile.append(rawMsg);

            byteIndexBySeq.insertIndex(nextOffset, buildOffsetIndex(queuedMessage, appendInfo, nextOffset));

        } catch (IOException e) {
            LOGGER.error("persistence failed ! ");
            throw new PersistenceException(e);
        }
    }

    /**
     * @param seq 消息的序列号
     *
     * @return
     *
     * @throws FileNotFoundByIndexException
     * @throws IOException
     * @throws CheckSumException
     */
    public QueuedMessage read(Long seq) throws FileNotFoundByIndexException, IOException, CheckSumException {

        OffsetIndex offsetIndex = byteIndexBySeq.getIndexBySeq(seq);

        // dispatchRead
        RandomAccessible randomAccessible = readDispatcher.index(offsetIndex);

        String rawMsg = randomAccessible.randomRead(offsetIndex.getByteOffset(), offsetIndex.getLength());

        QueuedMessage queuedMessage = JSON.parseObject(rawMsg, QueuedMessage.class);

        // 校验和不相等, 抛出异常
        if (!queuedMessage.getCheckSum().equals(offsetIndex.getCheckSum())) {
            throw new CheckSumException();
        }

        return queuedMessage;
    }

    /**
     * 构造索引
     *
     * @param queuedMessage
     * @param appendInfoWithId
     * @param nextOffset
     *
     * @return
     */
    private OffsetIndex buildOffsetIndex(QueuedMessage queuedMessage,
                                         AppendInfoWithId appendInfoWithId,
                                         Long nextOffset) {

        OffsetIndex offsetIndex = new OffsetIndex();

        offsetIndex.setCheckSum(queuedMessage.getCheckSum());

        offsetIndex.setLength(appendInfoWithId.getAppendInfo().getLength());

        offsetIndex.setByteOffset(appendInfoWithId.getAppendInfo().getOffset());

        offsetIndex.setMsgOffset(nextOffset);

        offsetIndex.setFileId(appendInfoWithId.getId());

        return offsetIndex;
    }
}
