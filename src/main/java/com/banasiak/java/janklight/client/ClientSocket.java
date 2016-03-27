package com.banasiak.java.janklight.client;

import com.banasiak.java.janklight.Colors;
import com.banasiak.java.janklight.LedUtil;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.awt.*;
import java.io.IOException;

import thingm.blink1.Blink1;

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
        Color color = Colors.getColorForName(message);
        if(color != null) {
            Blink1 led = Blink1.open();
            led.setRGB(color);
            led.close();
        }
    }
}
