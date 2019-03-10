package cn.cy.client.core.connection;

import cn.cy.client.core.channel.CyChannel;
import cn.cy.client.core.channel.NettyChannel;
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

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Netty连接，一个bootstrap对应一个连接，一个连接可以创建多个channel，
 * 之后将Netty的channel封装一下桥接到项目内的channel,通过netty已封装的一些方法就可以实现这些功能
 */
public class NettyConnection implements Connection{

    private static final Logger logger = LoggerFactory.getLogger(NettyConnection.class);

    private final String name;

    private final String host;

    private final int port;

    private Bootstrap conn;

    //--Todo 创建一个连接配置类来包装连接相应的配置
    private int connectionTimeout;

    //连接所属的channel
    //--Todo 考虑竞争
    private Map<String, Channel> channels;

    public NettyConnection(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    /**
     * 启动netty并建立连接，当服务端断开连接时关闭
     */
    public void start() {
        logger.debug("NettyConnection starting, remote host : {}, port : {}", host, port);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            Bootstrap conn = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder(Charset.defaultCharset()));
                            pipeline.addLast(new StringEncoder(Charset.defaultCharset()));
                        }
                    });

            this.conn = conn;

            ChannelFuture channelFuture = conn.connect().addListener(x -> {
                logger.info("Msq Client connect successfully to remote host : {}, port : {}", host, port);
            }).sync();

            if (channelFuture.isSuccess()) {
                Channel channel = channelFuture.channel();
                this.channels.put(channel.id().asShortText(), channel);
            } else {
                logger.error("Fail to connect to host : {}, port : {}", host, port);
                throw new ConnectionFailException("Fail to connect to server");
            }

            channelFuture.channel().closeFuture().addListener(x -> {
                logger.info("Msq Client lose server connection, exiting");
            }).sync();

        } catch (Exception e) {
            logger.error("Error when client starting");
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public Map<String, Channel> getChannels() {
        if (channels == null) {
            throw new ChannelUnInitializedException();
        }
        return channels;
    }

    @Override
    public String getAddr() {
        return this.host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    public String getName() {
        return this.name;
    }

    //--Todo 验证重复bind同一host和port的逻辑
    @Override
    public CyChannel createChannel() {
        return new NettyChannel(this, this.conn.bind(host, port).channel());
    }
}
