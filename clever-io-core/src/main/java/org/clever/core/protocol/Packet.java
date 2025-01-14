package org.clever.core.protocol;


import org.clever.core.json.JsonUtils;

import java.nio.charset.Charset;

/**
 * 消息包
 */
public interface Packet extends Transport {

    public final static byte TYPE_REQUEST = 20;
    public final static byte TYPE_RESPONSE = 21;
    public final static byte TYPE_HEARTBEAT = 1;
    public final static byte TYPE_PUSH = 22;

    // bytes zero
    public final static byte[] ZERO_BYTES = {};

    public final static Charset UTF8 = Charset.forName("UTF-8");


    byte getVersion();
    byte getType();
    long getTimestamp();
    long getId();
    CleverHeader getPacketHeader();

    default byte[] toAttrBytes(){
        return JsonUtils.objectToJsonByteUTF8(getPacketHeader());
    }
}
