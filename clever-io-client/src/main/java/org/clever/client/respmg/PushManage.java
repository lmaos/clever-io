package org.clever.client.respmg;

import org.clever.client.context.ClientContext;
import org.clever.core.destroy.Destroy;
import org.clever.core.protocol.Push;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * push管理. 订阅 push
 */
public class PushManage implements Destroy {


    /**
     * 订阅 push
     */
    private Map<String, Set<PushSubscribe>> pushSubscribeMap = new ConcurrentHashMap<>(128);
    private ClientContext clientContext;
    public PushManage(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    public PushSubscribe.Future subscribe(PushSubscribe pushSubscribe) {
        return subscribe(Push.TOPIC_ALL, pushSubscribe);
    }
    public PushSubscribe.Future subscribe(String topic, PushSubscribe pushSubscribe) {
        pushSubscribeMap.computeIfAbsent(topic, k -> ConcurrentHashMap.newKeySet()).add(pushSubscribe);
        return new PushSubscribe.Future() {
            @Override
            public void cancel() {
                pushSubscribeMap.get(topic).remove(pushSubscribe);
            }
        };
    }

    // close 订阅
    public PushSubscribe.Future closeSubscribe(PushSubscribe pushSubscribe) {
        return subscribe(Push.TOPIC_CLOSE, pushSubscribe);
    }

    public void onMessage(Push push) {
        String topic = push.getTopic();
        Set<PushSubscribe> pushSubscribes = pushSubscribeMap.get(topic);
        if (pushSubscribes != null) {
            pushSubscribes.forEach(pushSubscribe -> {
                try {
                    pushSubscribe.onMessage(topic, push);
                } catch (Exception e) {
                    try {
                        pushSubscribe.onFail(topic, push, e);
                    } catch (Exception fe) {
                        logerror(fe, "");
                    }
                }
            });
        }
    }

    private void logerror(Exception e, String msg) {

    }

    @Override
    public void destroy() {
        pushSubscribeMap.clear();

    }
}
