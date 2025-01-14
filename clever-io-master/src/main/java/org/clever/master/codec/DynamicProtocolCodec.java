package org.clever.master.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageCodec;
import org.clever.core.codec.BinaryPacketDecoder;
import org.clever.core.codec.PacketEncoder;
import org.clever.core.codec.RequestDecoder;
import org.clever.core.codec.ResponseEncoder;
import org.clever.core.protocol.BinaryPacket;
import org.clever.core.protocol.ClientProtocol;

import java.util.List;

/**
 * @author clever.cat
 * @date 2019-01-03 21:05
 * @description
 * extends ByteToMessageDecoder
 * 动态协议解码器, 解析前两个字节 为 BinaryPacket.MAGIC 时候, 是 TCP 协议, 否则 为 WebSocket 协议.
 */
public class DynamicProtocolCodec extends MessageToMessageCodec<ByteBuf, ClientProtocol> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        if (byteBuf.readableBytes() < 2) {
            return;
        }
        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort(); // 读取前两个字节
        byteBuf.resetReaderIndex();
        ChannelPipeline pipeline = channelHandlerContext.pipeline();

        // 判断是否是TCP协议, 如果是, 使用TCP协议解码器
        if (magic == BinaryPacket.MAGIC) {
            // 使用TCP解码器
            pipeline.addBefore(channelHandlerContext.name(), "binaryPacketDecoder", new BinaryPacketDecoder());
            pipeline.addBefore(channelHandlerContext.name(),"requestDecoder", new RequestDecoder());
            pipeline.addBefore(channelHandlerContext.name(),"binaryPacketEncoder", new PacketEncoder());
            pipeline.addBefore(channelHandlerContext.name(),"responseEncode", new ResponseEncoder());

        } else {
            // TODO 构建HTTP/Websocket解码.
        }
        pipeline.remove(this); // 协议选择成功后移除自身的协议.
        pipeline.fireChannelRead(byteBuf.retain());
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ClientProtocol clientProtocol, List<Object> list) throws Exception {
        throw new UnsupportedOperationException("未进行编码类型的选择!");
    }
}
