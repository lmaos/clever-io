package org.clever.master;

import org.clever.master.config.MasterServerConfig;
import org.clever.master.server.MasterServer;

public class Main {
    public static void main(String[] args) {



        MasterServerConfig masterServerConfig = new MasterServerConfig();
        masterServerConfig.setPort(9661);
        MasterServer masterServer = new MasterServer(masterServerConfig);
        masterServer.start();

        System.out.println("服务启动成功!");
    }
}