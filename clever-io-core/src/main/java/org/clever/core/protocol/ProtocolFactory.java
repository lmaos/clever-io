package org.clever.core.protocol;

import java.util.UUID;

public class ProtocolFactory {

    // 创建一个默认的 PacketAttr
    public static CleverHeader createHeader() {
        return createHeader(UUID.randomUUID().toString());
    }
    public static CleverHeader createHeader(String msgId) {
        CleverHeader packetAttr = new CleverHeader();
        packetAttr.setMsgId(msgId);
        return packetAttr;
    }

    public static Response.BaseResponse createResponse(Request request) {
        Response.BaseResponse response = new Response.BaseResponse(request.getId());
        response.setHeader(new CleverHeader(request.getHeader()));
        return response;
    }

    public static Response.BaseResponse createResponse(Request request, String status) {
        Response.BaseResponse response = createResponse(request);
        response.setStatus(status);
        return response;
    }

    /**
     * 创建一个无头的应答.
     * @param status
     * @return
     */
    public static Response.BaseResponse createResponse(String status) {
        Response.BaseResponse response = new Response.BaseResponse(-1);
        response.setHeader(createHeader());
        response.setStatus(status);
        return response;
    }

    // close push
    public static Push.ClosePush createClosePush() {
        return new Push.ClosePush();
    }
}
