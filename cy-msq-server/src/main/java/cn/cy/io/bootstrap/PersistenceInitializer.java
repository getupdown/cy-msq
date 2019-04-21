package cn.cy.io.bootstrap;

import cn.cy.io.handler.test.PersistenceTestHandler;
import io.netty.channel.socket.SocketChannel;

public class PersistenceInitializer extends CustomizeProtocolInitializer {

    @Override
    protected void customizedHandlers(SocketChannel ch) {
        ch.pipeline().addLast(new PersistenceTestHandler());
    }
}
