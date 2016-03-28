package com.banasiak.java.janklight.client;

import com.banasiak.java.janklight.Colors;
import com.banasiak.java.janklight.JankLight;
import com.banasiak.java.janklight.LedUtil;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.awt.Color;
import java.io.IOException;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class ClientSocket {

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        LedUtil.blinkColor(Color.RED, 2);
        JankLightClient.setSession(null);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to websocket server");
        LedUtil.blinkColor(Color.GREEN, 2);

        JankLightClient.setSession(session);
        try {
            session.getRemote().sendString("Hello server!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        System.out.println("Received message from server: " + message);
        if(message.equals(JankLight.PARTY_MODE)) {
            LedUtil.cycleColors();
        } else {
            Color color = Colors.getColorForName(message);
            if(color != null) {
                LedUtil.setColor(color);
            }
        }
    }
}
