package cn.cy.io.handler.dispatch;

import cn.cy.io.vo.BaseInfo;
import cn.cy.io.vo.RequestType;

/**
 * 这里处理 {@link cn.cy.io.vo.request.CommitRequest} 这样类型的信息
 */
public class CommitRequestDispatchHandler extends AbstractDispatchHandler {

    @Override
    protected void core(BaseInfo baseInfo) {

    }

    @Override
    protected boolean accepted(BaseInfo baseInfo) {
        return baseInfo.getType() == RequestType.MESSAGE_COMMIT.id;
    }
}
