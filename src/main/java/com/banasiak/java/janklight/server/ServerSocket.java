package com.banasiak.java.janklight.server;

import com.banasiak.java.janklight.JankLight;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;

import java.io.IOException;
import java.nio.ByteBuffer;

@WebSocket
public class ServerSocket {

    @OnWebSocketConnect
    public void onWebSocketConnect(Session session) {
        JankLightServer.addSession(session);
        System.out.println(
                "New websocket session connected: " + session.getRemoteAddress().toString());
    }

    @OnWebSocketClose
    public void onWebSocketClose(Session session, int statusCode, String reason) {
        JankLightServer.removeSession(session);
        System.out.println("WebSocket session closed: " + session.getRemoteAddress().toString());
    }

    @OnWebSocketMessage
    public void onWebSocketMessage(String message) throws IOException {
        System.out.println("Received message from client: " + message);
    }

    @OnWebSocketFrame
    public void onWebSocketFrame(Session session, Frame frame) {
        try {
            if(frame.hasPayload() && frame.getPayload().equals(ByteBuffer.wrap(JankLight.PING.getBytes()))) {
                System.out.println("Received keepalive ping");

                // To be correct in your usage of Pong frames, you should return the same byte
                // array data that you received in the Ping frame
                session.getRemote().sendPong(frame.getPayload());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
