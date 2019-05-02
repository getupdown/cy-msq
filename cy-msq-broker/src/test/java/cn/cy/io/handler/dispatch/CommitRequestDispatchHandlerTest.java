package cn.cy.io.handler.dispatch;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cn.cy.io.handler.BaseTest;
import cn.cy.io.handler.decoder.JsonRequestDecoder;
import cn.cy.io.vo.BaseInfo;
import cn.cy.io.vo.request.CommitRequest;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;

public class CommitRequestDispatchHandlerTest extends BaseTest {

    private List<BaseInfo> mqDatas;

    private String testStr = "test1";

    @Before
    public void init() {
        // 准备数据
        int x = 1000;
        mqDatas = Lists.newArrayList();

        for (int i = 0; i < x; i++) {

            BaseInfo<CommitRequest> baseInfo = new BaseInfo<>();
            baseInfo.setRequestId(String.valueOf(i));
            baseInfo.setType(0);
            CommitRequest request = new CommitRequest();
            request.setMsg(testStr);
            baseInfo.setData(request);

            mqDatas.add(baseInfo);
        }
    }

    @Test
    public void channelRead() {

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new JsonObjectDecoder(),
                new JsonRequestDecoder(),
                new CommitRequestDispatchHandler()
        );

        writeAndFlushDatas(mqDatas, embeddedChannel);

        // 测试解出来的消息对不对
        int idx = 0;
        while (true) {
            BaseInfo baseInfo = embeddedChannel.readInbound();
            if (baseInfo == null) {
                break;
            }
            Assert.assertEquals(baseInfo.getRequestId(), String.valueOf(idx));
            Assert.assertTrue(baseInfo.getData() instanceof CommitRequest);
            Assert.assertEquals(testStr, ((CommitRequest) baseInfo.getData()).getMsg());
            idx++;
        }

        Assert.assertEquals(idx, mqDatas.size());
    }

}