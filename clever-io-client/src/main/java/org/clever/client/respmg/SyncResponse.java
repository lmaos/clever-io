package org.clever.client.respmg;

import org.clever.client.error.ResponseTimeoutException;
import org.clever.core.protocol.Response;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步应答
 */
public class SyncResponse {

    private Response response;

    private long timeout;

    // 阻塞等待锁
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();


    public SyncResponse(long timeout) {
        this.timeout = timeout;
    }

    public void setResponse(Response response) {
        lock.lock();
        this.response = response;
        condition.signalAll();
        lock.unlock();
    }

    public Response getResponse()  throws ResponseTimeoutException {
        try {
            lock.lock();
            if (response == null) {
                condition.await(timeout, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (response == null) {
                    throw  new ResponseTimeoutException();
                }
            }
        } catch (InterruptedException e) {

        } finally {
            lock.unlock();
        }
        return response;
    }
}
