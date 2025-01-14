package org.clever.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.clever.core.errors.EncodeException;
import org.clever.core.errors.IllegalDataFormatException;
import org.clever.core.errors.MagicException;
import org.clever.core.json.JsonUtils;

import static org.clever.core.protocol.BinaryPacket.MAGIC;

public class ProtocolUtils {

    public static BinaryPacket toBinaryPacket(ByteBuf byteBuf) throws MagicException {
        // byteBuf 获得当前的操作副本.
        ByteBuf byteBuf_ = byteBuf.duplicate();
        try {
            if (byteBuf_.readableBytes() == 0) {
                return null;
            }
            short magic = byteBuf_.readShort();
            if (magic != MAGIC) {
                throw new MagicException("magic error");
            }
            int length = byteBuf_.readInt();
            if (byteBuf_.readableBytes() < length) {
                return null;
            }
            if (byteBuf_.readableBytes() >= 4) {
                byte version = byteBuf_.readByte();
                byte type = byteBuf_.readByte();
                long id = byteBuf_.readLong();
                long timestamp = byteBuf_.readLong();
                short attrLength = byteBuf_.readShort();
                byte[] attr = new byte[attrLength];
                if (attrLength > 0) {
                    byteBuf_.readBytes(attr);
                }
                short dataLength = byteBuf_.readShort();
                byte[] data = new byte[dataLength];
                if (dataLength > 0) {
                    byteBuf_.readBytes(data);
                }
                int checksum = byteBuf_.readInt();

                BinaryPacket binaryPacket = new BinaryPacket();
                binaryPacket.setMagic(magic);
                binaryPacket.setVersion(version);
                binaryPacket.setType(type);
                binaryPacket.setId(id);
                binaryPacket.setTimestamp(timestamp);
                binaryPacket.setHeaderLength(attrLength);
                binaryPacket.setHeader(attr);
                binaryPacket.setDataLength(dataLength);
                binaryPacket.setData(data);
                binaryPacket.setChecksum(checksum);

                // byteBuf_ 读取完成了,  去释放: byteBuf

                byteBuf.readerIndex(byteBuf_.readerIndex());
                return binaryPacket;
            }

            return null;
        } finally {
//            ReferenceCountUtil.release(byteBuf_);
        }
    }

    // 编码到ByteBuf
    public static void encodeToByteBuf(BinaryPacket binaryPacket, ByteBuf byteBuf) {

        byteBuf.writeShort(binaryPacket.getMagic());
        byteBuf.writeInt(binaryPacket.getTotalLength());
        byteBuf.writeByte(binaryPacket.getVersion());
        byteBuf.writeByte(binaryPacket.getType());
        byteBuf.writeLong(binaryPacket.getId());
        byteBuf.writeLong(binaryPacket.getTimestamp());
        byteBuf.writeShort(binaryPacket.getHeaderLength());
        byteBuf.writeBytes(binaryPacket.getHeader());
        byteBuf.writeShort(binaryPacket.getDataLength());
        byteBuf.writeBytes(binaryPacket.getData());
        byteBuf.writeInt(binaryPacket.getChecksum());

    }

    // webSocket 的TextWebSocketFrame解析JSON称 Json Packet
    public static JsonPacket toJsonPacket(TextWebSocketFrame textWebSocketFrame) {
        String json = textWebSocketFrame.text();
        return JsonUtils.jsonToObject(json, JsonPacket.class);
    }

    public static TextWebSocketFrame toTextWebSocketFrame(JsonPacket jsonPacket) {
        String json = JsonUtils.objectToJson(jsonPacket);
        return new TextWebSocketFrame(json);
    }

    // JsonPacket 转 BinaryPacket
    public static BinaryPacket toBinaryPacket(JsonPacket jsonPacket) throws EncodeException {
        CleverHeader packetAttr = jsonPacket.getPacketHeader();
        BinaryPacket binaryPacket = new BinaryPacket();
        binaryPacket.setVersion(jsonPacket.getVersion());
        binaryPacket.setType(jsonPacket.getType());
        binaryPacket.setTimestamp(jsonPacket.getTimestamp());
        binaryPacket.setHeader(JsonUtils.objectToJsonByteUTF8(packetAttr));
        try {
            binaryPacket.setData(PacketUtils.getDataBytes(jsonPacket,
                    packetAttr == null ? null :
                    packetAttr.getContentType()));
        } catch (Exception e) {
            throw new EncodeException(e);
        }
        return binaryPacket;
    }


    // Protocol 转 BinaryPacket
    public static BinaryPacket toBinaryPacket(Protocol protocol)  {
        CleverHeader header = protocol.getHeader();
        CleverBody body = protocol.getBody();
        boolean heartbeat = protocol.isHeartbeat();
        long timestamp = protocol.getTimestamp();
        long id = protocol.getId();

        BinaryPacket binaryPacket = new BinaryPacket();
        binaryPacket.setId(id);
        binaryPacket.setTimestamp(timestamp);
        if (heartbeat) {
            binaryPacket.setType(Packet.TYPE_HEARTBEAT);
        } else if (protocol instanceof Request){
            binaryPacket.setType(Packet.TYPE_REQUEST);
        } else if (protocol instanceof Response){
            binaryPacket.setType(Packet.TYPE_RESPONSE);
        } else if (protocol instanceof Push){
            binaryPacket.setType(Packet.TYPE_PUSH);
        } else {
            binaryPacket.setType(Packet.TYPE_HEARTBEAT);
        }
        binaryPacket.setHeader(JsonUtils.objectToJsonByteUTF8(header));
        binaryPacket.setData(body == null ? Packet.ZERO_BYTES : body.getData());

        return binaryPacket;
    }


    // Protocol 转 JsonPacket
    public static JsonPacket toJsonPacket(Protocol protocol) throws IllegalDataFormatException {

        // {id:0,type:0,timestamp:0,header:{},data:""}

        JsonPacket jsonPacket = new JsonPacket();
        CleverHeader header = protocol.getHeader();
        CleverBody body = protocol.getBody();
        // 协议心跳
        jsonPacket.setType(protocol.isHeartbeat() ? Packet.TYPE_HEARTBEAT
                : protocol instanceof Request ? Packet.TYPE_REQUEST: Packet.TYPE_RESPONSE);
        jsonPacket.setId(protocol.getId()); // 协议的标准 ID
        jsonPacket.setTimestamp(protocol.getTimestamp()); // 协议时间戳
        jsonPacket.setHeader(header);
        if (body != null) {
            jsonPacket.setData(PacketUtils.encoderDataString(body.getData(), header.getContentType()));
        }
        return jsonPacket;
    }
}
