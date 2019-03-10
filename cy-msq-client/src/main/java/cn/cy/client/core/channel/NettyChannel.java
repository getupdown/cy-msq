package cn.cy.client.core.channel;

import cn.cy.client.core.connection.Connection;
import io.netty.channel.Channel;

public class NettyChannel implements CyChannel{

    private Channel channel;

    private Connection connection;

    public NettyChannel(Connection connection, Channel channel) {
        this.connection = connection;
        this.channel = channel;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public String getId() {
        return this.channel.id().asShortText();
    }

}
