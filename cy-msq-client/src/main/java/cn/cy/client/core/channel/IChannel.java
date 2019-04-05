package cn.cy.client.core.channel;

/**
 * 项目内的Channel接口
 *
 * --Todo 继续完善Channel需要的接口
 */
public interface IChannel {

    String getId();

    void write(String msg);

}
