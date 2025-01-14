package org.clever.core.protocol;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.clever.core.errors.IllegalDataFormatException;

@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class AbstractProtocol implements Protocol {
    protected long id;
    protected boolean heartbeat;
    protected long timestamp;
    protected CleverHeader header;
    protected CleverBody body;

    protected AbstractProtocol(Packet packet) throws IllegalDataFormatException {
        this.id = packet.getId();
        this.heartbeat = packet.getType() == Packet.TYPE_HEARTBEAT;
        this.timestamp = packet.getTimestamp();
        if (!this.heartbeat) {
            CleverHeader packetHeader = packet.getPacketHeader();
            byte[] dataBytes = PacketUtils.getDataBytes(packet,
                    packetHeader == null ? null : packetHeader.getContentType());
            BytesCleverBody requestBody = new BytesCleverBody(dataBytes);
            this.header = packetHeader;
            this.body = requestBody;
        }
    }

    protected AbstractProtocol() {
        this.timestamp = System.currentTimeMillis();
    }

}
