package com.banasiak.java.janklight.client;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;

public class JankLightClient {

    private static Session webSocketSession;

    private Thread keepAliveThread;

    String server;

    public JankLightClient(String server) {
        this.server = server;

        keepAliveThread = new Thread(new KeepAliveTask());
        keepAliveThread.start();

        System.out.println("Client instantiated");
    }

    public void connectToServer() {
        // ignore the self-signed certificate for this prototype
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setTrustAll(true);

        WebSocketClient client = new WebSocketClient(sslContextFactory);
        ClientSocket socket = new ClientSocket();

        try{
            client.start();
            URI uri = new URI("wss://" + server + "/socket/");
            ClientUpgradeRequest request = new ClientUpgradeRequest();

            System.out.println("Connecting to: " + uri);
            client.connect(socket, uri, request);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }



    public static void setSession(Session session) {
        webSocketSession = session;
    }

    public static Session getSession() {
        return webSocketSession;
    }
}
