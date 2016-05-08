package com.banasiak.java.janklight.client.led;

import com.banasiak.java.janklight.Colors;

import thingm.blink1.Blink1;

public class Blink1Provider implements LedProviderInterface {

    @Override
    public void cycleColors() {
        Blink1 led = Blink1.open();
        for (Colors color : Colors.values()) {
            led.setRGB(color.getValue());
            Blink1.pause(DELAY);
        }
        led.setRGB(Colors.BLACK.getValue());
        led.close();
    }

    @Override
    public void blinkColor(Colors color, int repeat) {
        Blink1 led = Blink1.open();
        for (int i = 0; i < repeat; i++) {
            led.setRGB(color.getValue());
            Blink1.pause(DELAY / 2);
            led.setRGB(Colors.BLACK.getValue());
            Blink1.pause(DELAY / 2);
        }
        led.close();
    }

    @Override
    public void setColor(Colors color) {
        Blink1 led = Blink1.open();
        led.setRGB(color.getValue());
        led.close();
    }

    @Override
    public void fadeToColor(Colors color) {
        Blink1 led = Blink1.open();
        led.setRGB(Colors.BLACK.getValue());
        led.fadeToRGB(2000, color.getValue());
        Blink1.pause(1000);
        led.fadeToRGB(2000, Colors.BLACK.getValue());
        Blink1.pause(2000);
        led.close();
    }
}
