package com.banasiak.java.janklight.client;

import com.banasiak.java.janklight.Colors;
import com.banasiak.java.janklight.JankLight;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class ClientSocket {

    private ConnectionListener connectionListener;

    public ClientSocket(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        connectionListener.disconnected();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to websocket server!");
        connectionListener.connected(session);
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
            JankLightClient.getLedProvider().cycleColors();
        } else {
            Colors color = Colors.getColorForName(message);
            if(color != null) {
                JankLightClient.getLedProvider().setColor(color);
            }
        }
    }
}
