package com.banasiak.java.janklight.client;

import com.banasiak.java.janklight.JankLight;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.nio.ByteBuffer;

public class KeepAliveTask implements Runnable {

    private static final long INTERVAL = 30000;

    public KeepAliveTask() {
        System.out.println("KeepAliveTask started - will ping server every " + INTERVAL + " seconds");
    }

    @Override
    public void run() {
        while(true) {
            if(Thread.interrupted()) {
                return;
            }

            Session session = JankLightClient.getSession();
            try {
                if(session != null) {
                    System.out.println("Sending keepalive ping");
                    session.getRemote().sendPing(ByteBuffer.wrap(JankLight.PING.getBytes()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
