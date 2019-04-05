package cn.cy.client.core;

import cn.cy.client.core.channel.CyChannel;
import cn.cy.client.core.channel.IChannel;
import cn.cy.client.exceptions.ChannelUnInitializedException;
import cn.cy.client.exceptions.ConnectionFailException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty连接，一个bootstrap对应一个连接，一个连接可以创建多个channel，
 * 之后将Netty的channel封装一下桥接到项目内的channel,通过netty已封装的一些方法就可以实现这些功能
 */
public class NettyClient implements Client {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private final String name;

    private Bootstrap conn;

    //--Todo 创建一个连接配置类来包装连接相应的配置
    private static final int CONNECTION_TIME_OUT = 30000;

    //连接所属的channel
    //--Todo 考虑竞争
    private Map<String, io.netty.channel.Channel> channels;

    public NettyClient(String name) {
        this.name = name;
        this.channels = new ConcurrentHashMap<>();
    }

    /**
     * 启动netty并建立连接，当服务端断开连接时关闭
     */
    public void start() {
        logger.debug("NettyClient starting...");
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            this.conn = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIME_OUT)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder(Charset.defaultCharset()));
                            pipeline.addLast(new StringEncoder(Charset.defaultCharset()));
                        }
                    });

        } catch (Exception e) {
            logger.error("Error when client starting");
            workerGroup.shutdownGracefully();
        }
    }

    public Map<String, io.netty.channel.Channel> getChannels() {
        if (channels == null) {
            throw new ChannelUnInitializedException();
        }
        return channels;
    }

    public IChannel connect(String host, int port) throws InterruptedException {
        ChannelFuture channelFuture = conn.connect().addListener(x -> {
            logger.info("Msq Client connect successfully");
        }).sync();

        if (channelFuture.isSuccess()) {
            io.netty.channel.Channel channel = channelFuture.channel();
            channels.put(channel.id().asShortText(), channel);
            return new CyChannel(channel);
        } else {
            logger.error("Fail to connect to host : {}, port : {}", host, port);
            throw new ConnectionFailException("Fail to connect to server");
        }
    }
}
