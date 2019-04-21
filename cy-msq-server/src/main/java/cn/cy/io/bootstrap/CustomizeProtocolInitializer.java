package cn.cy.io.bootstrap;

import cn.cy.io.handler.decoder.JsonRequestDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;

/**
 * 自定义协议初始化
 * <p>
 * 由于分环境测试, 对于特定的测试, 使用特定的initializer
 */
public class CustomizeProtocolInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 所有的场景下, 都需要的handler
     *
     * @param ch
     */
    protected void addCommonHandlers(SocketChannel ch) {

        // 这个类帮助把网络传输的原形式, 转换为一或多个json字符串, 并转给后面的handler
        ch.pipeline().addLast(new JsonObjectDecoder());

        // 把json字符串转换为业务对象
        ch.pipeline().addLast(new JsonRequestDecoder());

    }

    /**
     * 每一个环境使用自己的handler
     *
     * @param ch
     */
    protected void customizedHandlers(SocketChannel ch) {
        
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        addCommonHandlers(ch);

        customizedHandlers(ch);
    }
}
