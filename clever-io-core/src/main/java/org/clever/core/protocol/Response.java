package org.clever.core.protocol;

import org.clever.core.errors.IllegalDataFormatException;

/**
 * @author clever.cat
 * 应答协议
 */
public interface Response extends Protocol, ClientProtocol {


    default String getStatus() {
        return getHeader().getStatus();
    }

    public static class ReadResponse extends AbstractProtocol implements Response {

        public ReadResponse(Packet packet) throws IllegalDataFormatException {
            super(packet);
        }

    }
    public static class BaseResponse extends AbstractProtocol implements Response {
        private String status = "ok";
        public BaseResponse(long id) {
            setId(id);
        }

        @Override
        public void setBody(CleverBody body) {
            super.setBody(body);
        }

        @Override
        public void setHeader(CleverHeader header) {
            super.setHeader(header);
            if (header.getStatus() == null || header.getStatus().isEmpty()) {
                header.setStatus(status);
            }
        }

        public void setStatus(String status) {
            if (header == null) {
                header = ProtocolFactory.createHeader();
            }
            header.setStatus(status);
            this.status = status;
        }

    }

    public static class HeartbeatResponse extends HeartbeatProtocol implements Response {
        public HeartbeatResponse(long id, long timestamp) {
            super(id, timestamp);
        }

        public HeartbeatResponse(long id) {
            super(id);
        }
    }

}
