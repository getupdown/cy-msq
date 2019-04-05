package cn.cy.client.core.channel;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;

public class CyChannel implements IChannel {

    private static final AtomicInteger ID_SEQ = new AtomicInteger(1);

    private Channel channel;

    public CyChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String getId() {
        return this.channel.id().asShortText();
    }

    public void write(String msg) {
        this.channel.writeAndFlush(msg);
    }

}
