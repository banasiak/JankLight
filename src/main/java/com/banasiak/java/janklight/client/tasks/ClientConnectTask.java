package com.banasiak.java.janklight.client.tasks;

import com.banasiak.java.janklight.JankLight;
import com.banasiak.java.janklight.client.ClientSocket;
import com.banasiak.java.janklight.client.ConnectionListener;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class ClientConnectTask implements StoppableRunnable {

    private static final long CONNECT_TIMEOUT = 10000;

    private static final long IDLE_TIMEOUT = 43200000;

    private static final long KEEPALIVE_INTERVAL = 30000;

    private static final long RECONNECT_INTERVAL = 10000;

    private URI uri;

    private ConnectionListener connectionListener;

    private volatile boolean shouldStop = false;

    public ClientConnectTask(String server, ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
        try {
            uri = new URI("wss://" + server + "/socket/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println(
                "ServerConnectTask started - will attempt to reconnect every " + RECONNECT_INTERVAL
                        + " ms");
    }

    @Override
    public void run() {
        // ignore the self-signed certificate for this prototype
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setTrustAll(true);

        WebSocketClient client = new WebSocketClient(sslContextFactory);
        client.setConnectTimeout(CONNECT_TIMEOUT);
        client.setMaxIdleTimeout(IDLE_TIMEOUT);

        ClientSocket socket = new ClientSocket(connectionListener);

        while (true) {
            if (shouldStop) {
                System.out.println("ServerConnectTask interrupted!");
                return;
            }

            Session session = connectionListener.getSession();
            if (session == null) {
                // attempt to connect to server
                connectionListener.connecting();
                try {
                    client.start();
                    ClientUpgradeRequest request = new ClientUpgradeRequest();
                    System.out.println("Connecting to: " + uri.toString());
                    client.connect(socket, uri, request);
                } catch (Exception e) {
                    System.out.println("Unable to connect to: " + uri.toString());
                    e.printStackTrace();
                }
            } else {
                // server connected - send keepalive ping
                try {
                    System.out.println("Sending keepalive ping to: " + uri.toString());
                    session.getRemote().sendPing(ByteBuffer.wrap(JankLight.PING.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (session == null) {
                    System.out.println(
                            "ServerConnectTask attempting reconnect in " + RECONNECT_INTERVAL
                                    + " ms");
                    Thread.sleep(RECONNECT_INTERVAL);
                } else {
                    System.out.println(
                            "ServerConnectTask sending next keepalive ping in " + KEEPALIVE_INTERVAL
                                    + " ms");
                    Thread.sleep(KEEPALIVE_INTERVAL);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        shouldStop = true;
    }
}
