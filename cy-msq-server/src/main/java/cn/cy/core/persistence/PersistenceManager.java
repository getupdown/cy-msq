package cn.cy.core.persistence;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.cy.core.msg.QueuedMessage;
import cn.cy.core.persistence.dispatch.PersistentWriteDispatcher;
import cn.cy.core.persistence.exception.PersistenceException;
import cn.cy.core.persistence.file.AppendInfoWithId;
import cn.cy.core.persistence.file.QueueMsgFile;
import cn.cy.core.queue.QueueState;
import cn.cy.core.queue.index.ByteIndexBySeq;
import cn.cy.core.queue.index.OffsetIndex;

/**
 * 这里封装了所有的持久化相关过程
 */
public class PersistenceManager {

    private static Logger LOGGER = LoggerFactory.getLogger(PersistenceManager.class);

    private PersistentWriteDispatcher messageWriteDispatcher;

    private ByteIndexBySeq byteIndexBySeq;

    /**
     * 根据队列状态, 把消息写入对应的持久化介质
     *
     * @param state         队列状态
     * @param queuedMessage 入队消息
     */
    private void write(QueueState state, QueuedMessage queuedMessage) {

        String rawMsg = JSON.toJSONString(queuedMessage) + "\n";

        Long nextOffset = state.increcOffset();

        QueueMsgFile queueMsgFile = (QueueMsgFile) messageWriteDispatcher.dispatchWrite();

        try {

            AppendInfoWithId appendInfo = queueMsgFile.append(rawMsg);

            byteIndexBySeq.insertIndex(nextOffset, buildOffsetIndex(queuedMessage, appendInfo, nextOffset));

        } catch (IOException e) {
            LOGGER.error("persistence failed ! ");
            throw new PersistenceException(e);
        }
    }

    /**
     *
     */
    private QueuedMessage read(Long seq, QueueState state) {
        return null;
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
