package org.clever.core.protocol;

import org.clever.core.errors.IllegalDataFormatException;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author clever.cat
 *
 * 包的一些处理方式
 *
 */
public class PacketUtils {

    /**
     * 获得数据字节数组
     * @param packet
     * @param dataFormat  json | binary; binary: JsonPacket时候是 base64 编码. 默认都是 json
     * @return
     * @throws IllegalDataFormatException
     */
    public static byte[] getDataBytes(Packet packet, String dataFormat) throws IllegalDataFormatException {
        if (packet instanceof BinaryPacket) {
            return ((BinaryPacket) packet).getData();
        } else {
            if (((JsonPacket) packet).getData() == null || ((JsonPacket) packet).getData().isEmpty()) {
                return Packet.ZERO_BYTES;
            }
            if (dataFormat == null || dataFormat.isEmpty() || "json".equalsIgnoreCase(dataFormat)) {
                return ((JsonPacket) packet).getData().getBytes(Packet.UTF8);
            } else if ("binary".equalsIgnoreCase(dataFormat)) { // base64
                return Base64.getDecoder().decode(((JsonPacket) packet).getData());
            } else {
                throw new IllegalDataFormatException("不支持的数据格式: " + dataFormat);
            }
        }
    }


    public static String encoderDataString(byte[] data, String dataFormat) throws IllegalDataFormatException {
        if (data == null || data.length == 0){
            return "";
        }
        if (dataFormat == null || dataFormat.isEmpty() || "json".equalsIgnoreCase(dataFormat)) {
            return new String(data, Packet.UTF8);
        } else if ("binary".equalsIgnoreCase(dataFormat)) { // base64
            return Base64.getEncoder().encodeToString(data);
        } else {
            throw new IllegalDataFormatException("不支持的数据格式: " + dataFormat);
        }
    }
}
