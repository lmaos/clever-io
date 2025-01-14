package org.clever.core.protocol;

/**
 * @author clever.cat
 * @date 2020/08/08 16:01
 * 定义了通信协议接口.
 *
 * 具体实现参考: Request, Response
 */
public interface Protocol {
    /**
     * 是否是心跳包
     * @return true: 是心跳包, false: 不是心跳包
     */
    default boolean isHeartbeat() {
        return false;
    }

    /**
     * 获取时间戳
     * @return 时间戳
     */
    default long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取协议头
     * @return 协议头
     */
    default CleverHeader getHeader() {
        return null;
    }

    /**
     * 获取协议体
     * @return 协议体
     */
    default CleverBody getBody() {
        return null;
    }

    /**
     * 获取协议ID
     * @return 协议ID
     */
    long getId();

}
