package cn.cy.client.core.channel;

import cn.cy.client.core.connection.Connection;

/**
 * 项目内的Channel接口
 *
 * --Todo 继续完善Channel需要的接口
 */
public interface CyChannel {

    Connection getConnection();

    String getId();

}
