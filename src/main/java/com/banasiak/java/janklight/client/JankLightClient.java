package com.banasiak.java.janklight.client;

import com.banasiak.java.janklight.Colors;
import com.banasiak.java.janklight.LedUtil;
import com.banasiak.java.janklight.client.tasks.ClientConnectTask;
import com.banasiak.java.janklight.client.tasks.LedPulseTask;
import com.banasiak.java.janklight.client.tasks.StoppableRunnable;

import org.eclipse.jetty.websocket.api.Session;

public class JankLightClient implements ConnectionListener {

    private Session session;

    private StoppableRunnable ledTask;

    private Thread ledThread;

    private String server;

    public JankLightClient(String server) {
        this.server = server;
        System.out.println("Client instantiated");
    }

    public void connectToServer() {
        Thread connectionThread = new Thread(new ClientConnectTask(server, this));
        connectionThread.start();
    }

    @Override
    public void connected(Session session) {
        this.session = session;
        stopLedThread();
    }

    @Override
    public void disconnected() {
        this.session = null;
        LedUtil.blinkColor(Colors.RED, 2);
    }

    @Override
    public void connecting() {
        startLedThread();
    }

    @Override
    public Session getSession() {
        return session;
    }

    private void startLedThread() {
        if (ledThread == null && ledTask == null) {
            ledTask = new LedPulseTask();
            ledThread = new Thread(ledTask);
            ledThread.start();
        }
    }

    private void stopLedThread() {
        if (ledThread != null && ledTask != null) {
            ledTask.stop();
        }
        ledTask = null;
        ledThread = null;
    }
}
