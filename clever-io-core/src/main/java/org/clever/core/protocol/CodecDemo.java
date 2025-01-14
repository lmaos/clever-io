package org.clever.core.protocol;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

public class CodecDemo {
    @Getter
    @Setter
    public static class Msg {
        private int length;
        private byte[] data;
    }

    public static void main(String[] args) {
        Msg msg = new Msg();
        msg.setLength(10);
        msg.setData(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(JSON.toJSONString(msg));
    }
}
