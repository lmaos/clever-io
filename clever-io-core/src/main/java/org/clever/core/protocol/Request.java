package org.clever.core.protocol;

import lombok.Getter;
import lombok.Setter;
import org.clever.core.errors.IllegalDataFormatException;

import java.util.Map;

/**
 * @author clever.cat
 * @date 2021/2/7 16:06
 *
 * 请求协议
 */
public interface Request extends Protocol{

    default String getUri() {
        return getHeader() == null ? "" : getHeader().getUri();
    }

    default void putAttr(String key, Object value) {
        throw new UnsupportedOperationException();
    }
    default Object getAttr(String key) {
        throw new UnsupportedOperationException();
    }

    public static abstract class AbstractRequest extends AbstractProtocol implements Request {
        public AbstractRequest(Packet packet) throws IllegalDataFormatException {
            super(packet);
        }
        public AbstractRequest() {
        }

        private Map<String, Object> attrs;

        public void putAttr(String key, Object value) {
            if (attrs == null) {
                attrs = new java.util.HashMap<>();
            }
            attrs.put(key, value);
        }
        public Object getAttr(String key) {
            if (attrs == null) {
                return null;
            }
            return attrs.get(key);
        }
    }

    public static class ReadRequest extends AbstractRequest implements Request {

        public ReadRequest(Packet packet) throws IllegalDataFormatException {
            super(packet);
        }


    }
    public static class BaseRequest extends AbstractRequest implements Request {
        public BaseRequest(){}
        public BaseRequest(long id){
            setId(id);
        }

        @Override
        public void setId(long id) {
            super.setId(id);
        }

        @Override
        public void setBody(CleverBody body) {
            super.setBody(body);
        }

        @Override
        public void setHeader(CleverHeader header) {
            super.setHeader(header);
        }


    }

    public static class HeartbeatRequest extends HeartbeatProtocol implements Request {
        public HeartbeatRequest(long id, long timestamp) {
            super(id, timestamp);
        }
        public HeartbeatRequest(long id) {
            super(id);
        }
    }

}
