package cn.cy.client.core.channel;

import io.netty.channel.Channel;

public class CyChannel implements IChannel {

    private Channel channel;

    public CyChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String getId() {
        return this.channel.id().asShortText();
    }

    public void asyncWrite(String msg) {
        this.channel.writeAndFlush(msg);
    }

}
