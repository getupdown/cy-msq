package cn.cy.io.http2;

import static io.netty.handler.logging.LogLevel.INFO;

import io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder;
import io.netty.handler.codec.http2.Http2ConnectionDecoder;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2FrameLogger;
import io.netty.handler.codec.http2.Http2Settings;

public class NettyHttp2HandlerBuilder
        extends AbstractHttp2ConnectionHandlerBuilder<NettyHttp2Handler, NettyHttp2HandlerBuilder> {

    NettyHttp2HandlerBuilder() {
        frameLogger(LOGGER);
    }

    @Override
    protected NettyHttp2Handler build() {
        return super.build();
    }

    @Override
    protected NettyHttp2Handler build(Http2ConnectionDecoder decoder,
                                      Http2ConnectionEncoder encoder,
                                      Http2Settings initialSettings) throws Exception {
        NettyHttp2Handler handler = new NettyHttp2Handler(decoder, encoder, initialSettings);
        frameListener(handler);
        return handler;
    }

    private static final Http2FrameLogger LOGGER = new Http2FrameLogger(INFO, NettyHttp2Handler.class);

}
