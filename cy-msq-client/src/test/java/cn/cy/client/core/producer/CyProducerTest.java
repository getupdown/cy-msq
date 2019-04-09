package cn.cy.client.core.producer;

import cn.cy.client.core.channel.CyChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CyProducerTest {

    private class TestCyChannel extends CyChannel {

        private EmbeddedChannel channel;

        public TestCyChannel(EmbeddedChannel channel) {
            super(channel);
            this.channel = channel;
        }

        @Override
        public void asyncWrite(String msg) {
            channel.writeInbound(msg);
        }

    }

    private class TestInBoundHandler extends ChannelInboundHandlerAdapter {

        public int messageCount = 0;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            messageCount++;
        }

    }

    @Test
    public void send() throws InterruptedException {
        TestInBoundHandler inBoundHandler = new TestInBoundHandler();
        EmbeddedChannel channel = new EmbeddedChannel(
                inBoundHandler
        );
        IProducer producer = new CyProducer(new TestCyChannel(channel));
        assertEquals(0, inBoundHandler.messageCount);

        for (int i = 0; i < 100; i++) {
            producer.send("hello");
        }

        Thread.sleep(500);
        assertEquals(100, inBoundHandler.messageCount);
    }
}