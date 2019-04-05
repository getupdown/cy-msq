package cn.cy.client.core.producer;

import cn.cy.io.vo.BaseInfo;
import cn.cy.io.vo.request.CommitRequest;

public interface Producer {

    void send(BaseInfo<CommitRequest> message);

}
