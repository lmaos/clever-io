package org.clever.master.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.clever.core.errors.CleverResponseException;
import org.clever.core.json.JsonUtils;
import org.clever.core.protocol.ProtocolFactory;
import org.clever.core.protocol.Request;
import org.clever.core.protocol.Response;
import org.clever.master.context.MasterContext;
import org.clever.master.session.MasterSession;

public class MasterServerHandler extends SimpleChannelInboundHandler<Request> {

    private final MasterContext masterContext;
    private MasterSession session;

    public MasterServerHandler(MasterContext masterContext) {
        this.masterContext = masterContext;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        session = new MasterSession(ctx, masterContext);
        masterContext.getMasterSessionManager().addSession(session);
        System.out.println("打开会话: " +session.getSessionKey());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        masterContext.getMasterSessionManager().removeSession(session);
        System.out.println("关闭会话: " +session.getSessionKey());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logerror(cause, "会话异常");
        session.closeSession();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
        // 更新会话时间
        session.touch();

        // 心跳, 直接返回心跳就行
        if (msg.isHeartbeat()) {
            ctx.writeAndFlush(new Response.HeartbeatResponse(msg.getId()));
            return;
        }
        // 处理请求
        handleRequest(ctx, msg);
    }


    private void handleRequest(ChannelHandlerContext ctx, Request request) {
        try {
            // 验证请求
            if (masterContext.getAuthentication() != null) {
                masterContext.getAuthentication().verifyRequest(session, request);
            }
            // 请求适配
            if (masterContext.getMasterAdapter() != null) {
                masterContext.getMasterAdapter().handlerRequest(session, request);
            } else {
                // master 没有准备好.
                ctx.writeAndFlush(ProtocolFactory.createResponse(request, "MASTER_NOT_READY"));
                return; // 中断后续
            }
        } catch (CleverResponseException e) {
            // TODO: 错误处理
            ctx.writeAndFlush(e.getResponse());
        } catch (Exception e) {
            // TODO: 错误处理
            ctx.writeAndFlush(ProtocolFactory.createResponse(request, "UNKNOWN_ERROR"));
            logerror(e, "请求出现异常的操作");
        }
    }



    private void logerror(Throwable e, String msg) {

    }
}
