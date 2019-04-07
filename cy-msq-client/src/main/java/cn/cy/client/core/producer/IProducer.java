package cn.cy.client.core.producer;

import cn.cy.client.core.channel.IChannel;

public interface IProducer {

    void send(String message);

    IChannel getChannel();

    String getId();
}
