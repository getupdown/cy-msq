package cn.cy.io.handler.test;

import cn.cy.core.config.QueueConfiguration;
import cn.cy.core.msg.QueuedMessage;
import cn.cy.core.persistence.PersistenceManager;
import cn.cy.core.persistence.dispatch.IndexReadDispatcher;
import cn.cy.core.persistence.dispatch.IndexReadDispatcherImpl;
import cn.cy.core.persistence.dispatch.MessageFileWriteDispatcher;
import cn.cy.core.persistence.dispatch.PersistentWriteDispatcher;
import cn.cy.core.queue.index.ByteIndexBySeq;
import cn.cy.core.queue.index.MemoryByteIndexImpl;
import cn.cy.core.queue.state.LocalQueueStateImpl;
import cn.cy.core.queue.state.QueueState;
import cn.cy.io.vo.request.CommitRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 测试持久化的handler
 */
@ChannelHandler.Sharable
public class PersistenceTestHandler extends ChannelInboundHandlerAdapter {

    private final PersistenceManager persistenceManager;

    public PersistenceTestHandler(QueueConfiguration queueConfiguration) {
        PersistentWriteDispatcher persistentWriteDispatcher = new MessageFileWriteDispatcher(queueConfiguration);
        ByteIndexBySeq byteIndexBySeq = new MemoryByteIndexImpl();
        IndexReadDispatcher indexReadDispatcher = new IndexReadDispatcherImpl(queueConfiguration);
        QueueState queueState = new LocalQueueStateImpl();

        this.persistenceManager =
                new PersistenceManager(persistentWriteDispatcher, byteIndexBySeq, indexReadDispatcher, queueState);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof CommitRequest) {

            String rawMsg = ((CommitRequest) msg).getMsg();

            QueuedMessage queuedMessage = new QueuedMessage();

            queuedMessage.setContent(rawMsg);

            persistenceManager.write(queuedMessage);

        } else {
            throw new IllegalArgumentException();
        }
    }
}
