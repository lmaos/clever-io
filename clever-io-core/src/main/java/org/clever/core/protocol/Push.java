package org.clever.core.protocol;

import lombok.NonNull;
import org.clever.core.errors.IllegalDataFormatException;

/**
 * @author clever.cat
 * 应答协议
 */
public interface Push extends Protocol, ClientProtocol {

//    public static String STATUS_CLOSE = "close";

    public static final String TOPIC_ALL = "**";
    public static final String TOPIC_CLOSE = "client/close";

//    default String getStatus() {
//        return getHeader() != null ? getHeader().getStatus() : null;
//    };
    default String getTopic() {
        return getHeader() != null ? getHeader().getUri() : null;
    }

    public static class ReadPush extends AbstractProtocol implements Push {
        public ReadPush(Packet packet) throws IllegalDataFormatException {
            super(packet);
        }
    }
    public static class BasePush extends AbstractProtocol implements Push {
//        private String status;
        private String topic = Push.TOPIC_ALL;
//        public BasePush(String status) {
//            this(status, Push.TOPIC_ALL);
//        }
        public BasePush(@NonNull String topic) {
            setHeader(ProtocolFactory.createHeader());
            getHeader().setUri(topic);
        }

        @Override
        public void setBody(CleverBody body) {
            super.setBody(body);
        }

        @Override
        public void setHeader(CleverHeader header) {
            super.setHeader(header);
//            if (header.getStatus() == null || header.getStatus().isEmpty()) {
//                header.setStatus(status);
//            }
            if (header.getUri() == null || header.getUri().isEmpty()) {
                header.setUri(topic);
            }
        }

        public void setTopic(String topic) {
            this.topic = topic;
            super.getHeader().setUri(topic);
        }
    }

    public static class ClosePush extends BasePush implements Push {
        public ClosePush() {
            super(Push.TOPIC_CLOSE);
        }
    }
}
