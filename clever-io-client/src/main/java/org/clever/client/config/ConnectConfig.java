package org.clever.client.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectConfig {
    private String host = "127.0.0.1";
    private int port = 9661;
}
