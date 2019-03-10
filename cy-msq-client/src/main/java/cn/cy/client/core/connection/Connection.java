package cn.cy.client.core.connection;

import cn.cy.client.core.channel.CyChannel;

/**
 * --Todo 继续完善接口
 */
public interface Connection {


    String getAddr();

    int getPort();

    String getName();

    CyChannel createChannel();

}
