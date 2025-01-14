package org.clever.client.respmg;

import org.clever.core.protocol.Push;
@FunctionalInterface
public interface PushSubscribe {
    void onMessage(String topic, Push push) throws Exception;
    default void onFail(String topic, Push push, Throwable e) throws Exception {}
    public interface Future {
        public void cancel();
    }
}
