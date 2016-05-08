package com.banasiak.java.janklight.client.led;

import com.banasiak.java.janklight.Colors;

public interface LedProviderInterface {

    int DELAY = 1000;

    void cycleColors();

    void blinkColor(Colors color, int repeat);

    void setColor(Colors color);

    void fadeToColor(Colors color);

}
