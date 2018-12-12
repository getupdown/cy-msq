package cn.cy.io.handler.dispatch;

import cn.cy.io.vo.BaseInfo;
import cn.cy.io.vo.RequestType;
import cn.cy.io.vo.request.CommitRequest;

import static cn.cy.io.queue.BasicQueue.BASIC_QUEUE;

/**
 * 这里处理 {@link cn.cy.io.vo.request.CommitRequest} 这样类型的信息
 */
public class CommitRequestDispatchHandler extends AbstractDispatchHandler {

    @Override
    protected void core(BaseInfo baseInfo) {
        CommitRequest request = (CommitRequest) baseInfo.getData();
        if(request.getMsg() == null) {
            //-TODO 添加空处理
            return;
        }
        BASIC_QUEUE.push(request.getMsg());
    }

    @Override
    protected boolean accepted(BaseInfo baseInfo) {
        return baseInfo.getType() == RequestType.MESSAGE_COMMIT.id;
    }
}
