package cn.cy.client.core.connection;

/**
 * 连接创建工厂类
 */
public class ConnectionFactory {

    public Connection createNettyConnection(String name, String host, int port) {
        return new NettyConnection(name, host, port);
    }
}
