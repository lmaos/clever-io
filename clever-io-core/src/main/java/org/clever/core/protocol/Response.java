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


    public static interface WriteResponse extends Response {
        void setId(long id);
        void setStatus(String status);
        void setBody(CleverBody body);
        void setHeader(CleverHeader header);
    }

    public static class ReadResponse extends AbstractProtocol implements Response {

        public ReadResponse(Packet packet) throws IllegalDataFormatException {
            super(packet);
        }

    }

    /**
     * 基础应答
     */
    public static class BaseResponse extends AbstractProtocol implements WriteResponse {
        private String status = "ok";
        public BaseResponse(long id) {
            setId(id);
        }
        public BaseResponse() {
            setId(-1);
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

        @Override
        public void setId(long id) {
            super.setId(id);
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
