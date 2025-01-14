package org.clever.client.respmg;

import org.clever.client.error.ResponseTimeoutException;
import org.clever.core.destroy.Destroy;
import org.clever.core.protocol.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明: 异步请求应答管理器
 */
public class AsyncResponseManage implements Destroy {

    private Map<Long, AsyncResponse> asyncResponseMap = new HashMap<>();

    /**
     * 添加一个异步应答
     * @param id 当前客户端请求/应答的唯一ID
     * @param asyncResponse  异步应答处理的对象
     */
    public void addAsyncResponse(long id, AsyncResponse asyncResponse) {
        asyncResponseMap.put(id, asyncResponse);
    }

    public boolean asyncResponse(Response response) {
        AsyncResponse asyncResponse = asyncResponseMap.remove(response.getId());
        if (asyncResponse != null) {
            try {
                asyncResponse.getCallback().onSuccess(asyncResponse.getRequest(), response);
            } catch (Exception e) {
                try {
                    asyncResponse.getCallback().onFailure(asyncResponse.getRequest(), e);
                } catch (Exception fe) {
                    logerror(fe, "失败监听调用异常");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 调用一次监听一次. 超时移除.
     */
    public void onceListenerAsyncResponse() {
        // for asyncResponseMap, 应答超时的, 移除.
        for (Map.Entry<Long, AsyncResponse> entry : asyncResponseMap.entrySet()) {
            timeout(entry.getKey(), entry.getValue());
        }
    }


    private void timeout(long id, AsyncResponse asyncResponse) {
        if (System.currentTimeMillis() - asyncResponse.getRequestTime() > asyncResponse.getTimeout()) {
            asyncResponse = asyncResponseMap.remove(id);
            if (asyncResponse != null) {
                AsyncResponseCallback callback = asyncResponse.getCallback();
                try {
                    callback.onFailure(asyncResponse.getRequest(), new ResponseTimeoutException());
                } catch (Exception e) {
                    logerror(e, "失败监听调用异常");
                }
            }
        }
    }


    private void logerror(Throwable e, String message) {

    }

    @Override
    public void destroy() {
        asyncResponseMap.clear();
    }
}
