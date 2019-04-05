package cn.cy.client.core.producer;

import cn.cy.client.core.channel.IChannel;
import cn.cy.io.vo.BaseInfo;
import cn.cy.io.vo.request.CommitRequest;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CyProducer implements Producer{

    private static final Logger logger = LoggerFactory.getLogger(CyProducer.class);

    private String id;

    private IChannel channel;

    @Override
    public void send(BaseInfo<CommitRequest> message) {
        try {
            this.channel.write(JSON.toJSONString(message));
        } catch (Exception e) {
            logger.error("Error when sending message, {}", message.toString());
        }
    }

}
