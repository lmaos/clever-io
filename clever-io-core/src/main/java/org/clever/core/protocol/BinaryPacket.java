package org.clever.core.protocol;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

/**
 * @author clever.cat
 * @since 2023-01-06
 * 二进制包.
 */
@Getter
@Setter
public class BinaryPacket implements Packet {

    public static final short MAGIC = 0x0F0F;
    // Charset.forName("UTF-8")
    public static final Charset CHARSET = Charset.forName("UTF-8");

    // 消息帧头, 判断是否是这个消息. 不可变.
    private short magic = MAGIC;
    // 版本
    private byte version = 1; // byte 1
    private byte type; // byte 1
    private long id; // 当前消息的唯一ID. 请求和应答保持一致
    private long timestamp;
    // 属性
    private short headerLength;
    private byte[] header = ZERO_BYTES; // 消息ID在这里哦.
    // 业务数据. 可以不用管
    private short dataLength;
    private byte[] data = ZERO_BYTES;
    private int checksum;

    /**
     * 设置消息头
     * @param header
     */
    public void setHeader(byte[] header) {
        this.header = header;
        this.headerLength = (short) header.length;
    }

    /**
     * 设置数据
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
        this.dataLength = (short) data.length;
    }

    // 获得数据长度, 不包含 magic的总长度. totalLength
    public int getTotalLength() {
        return dataLength + headerLength + 18;
    }



    @Override
    public CleverHeader getPacketHeader() {
        if (header == null) {return new CleverHeader();}
        return JSON.parseObject(header,0, headerLength, CHARSET, CleverHeader.class);
    }


    @Override
    public String toString() {
        return "binary-packet: " + header != null ?  new String(header, CHARSET) : "";
    }
}
