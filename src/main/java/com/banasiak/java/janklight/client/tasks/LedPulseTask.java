package com.banasiak.java.janklight.client.tasks;

import com.banasiak.java.janklight.Colors;
import com.banasiak.java.janklight.client.JankLightClient;

public class LedPulseTask implements StoppableRunnable {

    private volatile boolean shouldStop = false;

    @Override
    public void run() {
        while (true) {
            if (shouldStop) {
                // blink green and end this thread because, because we're connected
                JankLightClient.getLedProvider().blinkColor(Colors.GREEN, 2);
                return;
            } else {
                JankLightClient.getLedProvider().fadeToColor(Colors.CYAN);
            }
        }
    }

    @Override
    public void stop() {
        shouldStop = true;
    }
}
