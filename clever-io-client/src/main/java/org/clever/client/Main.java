package org.clever.client;

import com.alibaba.fastjson.JSON;
import org.clever.client.config.ClientConfig;
import org.clever.client.error.ClientCloseException;
import org.clever.client.error.ResponseTimeoutException;
import org.clever.client.respmg.ClientRequest;
import org.clever.core.protocol.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ClientConfig clientConfig = new ClientConfig();

        CleverIOClient clevelClient = new CleverIOClient(clientConfig);
        clevelClient.start();

        clevelClient.getClientContext().getPushManage()
                .closeSubscribe((topic, push) -> {
                    System.out.println(topic + "...服务器通知客户端关闭了!");
                });


        while (clevelClient.isStarted()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入要发送的内容:");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            try {
                ClientRequest request = clevelClient.createRequest();
                Response send = request.send();
                System.out.println("发送成功!" + JSON.toJSONString(send));
            } catch (ResponseTimeoutException e) {
                System.out.println("发送超时!");
            } catch (ClientCloseException e) {
                System.out.println("客户端已经关闭!");
            }
        }
        clevelClient.close();
    }
}