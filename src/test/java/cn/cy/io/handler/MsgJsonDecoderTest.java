package cn.cy.io.handler;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import cn.cy.io.vo.MqMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;

public class MsgJsonDecoderTest {

    private List<MqMessage> mqDatas;

    @Before
    public void init() {
        // 准备数据
        int x = 1000;
        mqDatas = Lists.newArrayList();

        for (int i = 0; i < x; i++) {

            MqMessage mqMessage = new MqMessage();
            mqMessage.setRequestId(String.valueOf(i));

            mqDatas.add(mqMessage);
        }
    }

    @Test
    public void channelRead() {

        ByteBuf byteBuf = Unpooled.buffer();

        for (MqMessage mqData : mqDatas) {
            byteBuf.writeBytes(JSON.toJSONBytes(mqData));
        }

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new JsonObjectDecoder(),
                new MsgJsonDecoder()
        );

        Assert.assertTrue(embeddedChannel.writeInbound(byteBuf.retain()));
        Assert.assertTrue(embeddedChannel.finish());

        // 测试解出来的消息对不对
        int idx = 0;
        while (true) {
            MqMessage mqMessage = embeddedChannel.readInbound();
            if (mqMessage == null) {
                break;
            }
            Assert.assertEquals(mqMessage.getRequestId(), String.valueOf(idx));
            idx++;
        }

        Assert.assertEquals(idx, mqDatas.size());
    }
}