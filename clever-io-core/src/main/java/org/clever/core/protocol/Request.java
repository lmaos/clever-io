package org.clever.core.protocol;

import lombok.Getter;
import org.clever.core.errors.IllegalDataFormatException;

/**
 * @author clever.cat
 * @date 2021/2/7 16:06
 *
 * 请求协议
 */
public interface Request extends Protocol{

    public static class ReadRequest extends AbstractProtocol implements Request {

        public ReadRequest(Packet packet) throws IllegalDataFormatException {
            super(packet);
        }
    }
    public static class BaseRequest extends AbstractProtocol implements Request {
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
