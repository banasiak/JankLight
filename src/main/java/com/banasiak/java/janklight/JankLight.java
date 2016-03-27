package com.banasiak.java.janklight;

import com.banasiak.java.janklight.client.JankLightClient;
import com.banasiak.java.janklight.server.JankLightServer;

public class JankLight {

    public static final String PING = "PING";
    public static final String PONG = "PONG";

    public static void main(String[] args) {
        System.out.println("Welcome to JankLight!");

        if (args.length < 1) {
            System.out.println("Usage: JankLight client <api.cyberguyenterprises.com>");
            System.out.println("Usage: JankLight server <keystore.jks> <password>");
            return;
        }

        switch(args[0]) {
            case "client":
                System.out.println("Activating client mode...");
                JankLightClient client = new JankLightClient(args[1]);
                client.connectToServer();
                break;
            case "server":
                System.out.println("Activating server mode...");
                JankLightServer server = new JankLightServer(args[1], args[2]);
                server.start();
                break;
            default:
                System.out.println("Invalid option: " + args[0]);
        }
    }

}
