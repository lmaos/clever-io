package org.clever.core.protocol;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serial;

/**
 * JSON结构的数据包, WebSocket使用.
 */
@Data
public class JsonPacket implements Packet{ // WebSocket等环境使用
    @Serial
    private static final long serialVersionUID = 1L;
    /*
        {"version":1, "type": 1 } 心跳包
     */

    /**
     * 协议版本. 1 默认协议版本
     */
    private byte version = 1;
    /**
     * 1 心跳, 20 业务请求, 21 业务响应
     */
    private byte type;
    /**
     * 时间戳
     */
    private long timestamp;

    private long id;
    /**
     * 消息时的固定属性.  一般结构比较简单, 和小巧. JSON 格式
     * {
     *     "uri":   "/api/v1/room/123/456" ,
     *     "userId": "123",
     *     "roomId": "456",
     *     "token": "789",
     *     “msgId”: "12345",
     *     “msgFrame”: "json" // 默认为 json ,消息结构,
     * }
     */
    private CleverHeader header;
    /**
     * 数据. 包含的数据内容. 包含的结构复杂, 大小比较大. JSON 格式.
     */
    private String data;


    @Override
    public CleverHeader getPacketHeader() {
        if (header == null)
            return null;
        return header;
    }

    public String toString() {
        return "json-packet: " + JSON.toJSONString(this);
    }


}
