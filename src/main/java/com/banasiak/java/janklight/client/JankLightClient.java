package com.banasiak.java.janklight.client;

import com.banasiak.java.janklight.Colors;
import com.banasiak.java.janklight.client.led.Blink1Provider;
import com.banasiak.java.janklight.client.led.BoblightProvider;
import com.banasiak.java.janklight.client.led.LedProviderInterface;
import com.banasiak.java.janklight.client.led.LedProviders;
import com.banasiak.java.janklight.client.tasks.ClientConnectTask;
import com.banasiak.java.janklight.client.tasks.LedPulseTask;
import com.banasiak.java.janklight.client.tasks.StoppableRunnable;

import org.eclipse.jetty.websocket.api.Session;

public class JankLightClient implements ConnectionListener {

    private Session session;

    private StoppableRunnable ledTask;

    private Thread ledThread;

    private String server;

    private static LedProviderInterface ledProvider;

    public JankLightClient(String server, String ledProviderName) {
        this.server = server;

        LedProviders ledProvider = LedProviders.getProviderForName(ledProviderName);
        switch (ledProvider) {
            case BOBLIGHT:
                JankLightClient.ledProvider = new BoblightProvider();
                break;
            case BLINK1:
            default:
                JankLightClient.ledProvider = new Blink1Provider();
                break;
        }

        System.out.println("Client instantiated with " + ledProvider + " LED provider");
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
        ledProvider.blinkColor(Colors.RED, 2);
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

    public static LedProviderInterface getLedProvider() {
        return ledProvider;
    }
}
