package cn.cy.io.handler;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.cy.io.vo.MqMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 这个 {@link JsonObjectDecoder} 只会把东西按照括号分组
 * 分组完了之后，会<b>依次</b>触发ctx的channelRead事件, 也就是把这些对象接着往下传递
 * <p>
 * 这里是把那些对象,变成业务可以理解的对象的过程
 */
@ChannelHandler.Sharable
public class MsgJsonDecoder extends ChannelInboundHandlerAdapter {

    /**
     * 处理真实的对象
     *
     * @param ctx ctx
     * @param msg ByteBuf对象
     *
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 到这里的时候, 已经是分好组的json, 每个都是ByteBuf
        if (msg instanceof ByteBuf) {
            ByteBuf input = (ByteBuf) msg;

            String rawStr = (String) input.readCharSequence((input).readableBytes(), Charset.defaultCharset());

            MqMessage mqMessage = JSON.parseObject(rawStr, MqMessage.class);

            attachMsg(ctx, mqMessage);

            ((ByteBuf) msg).release();

            ctx.fireChannelRead(mqMessage);
        }
    }

    private void attachMsg(ChannelHandlerContext ctx, MqMessage mqMessage) {

        Attribute<List<MqMessage>> attribute = ctx.channel().attr(AttributeKey.valueOf("msg"));

        List<MqMessage> messageList = attribute.get();

        if (messageList == null) {
            messageList = new ArrayList<>();
            messageList.add(mqMessage);
            attribute.set(messageList);
        } else {
            messageList.add(mqMessage);
        }

    }
}
