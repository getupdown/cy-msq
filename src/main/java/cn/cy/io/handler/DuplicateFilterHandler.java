package cn.cy.io.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 用于过滤重复的请求
 */
@ChannelHandler.Sharable
public class DuplicateFilterHandler extends ChannelInboundHandlerAdapter {

    // todo 现阶段不必要

}
