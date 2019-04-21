package cn.cy.io.handler.test;

import cn.cy.core.persistence.PersistenceManager;
import cn.cy.io.vo.request.CommitRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 测试持久化的handler
 */
public class PersistenceTestHandler extends ChannelInboundHandlerAdapter {

    private PersistenceManager persistenceManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof CommitRequest) {
            
        }
    }
}
