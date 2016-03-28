package com.banasiak.java.janklight;

import java.awt.Color;

import thingm.blink1.Blink1;

public class LedUtil {

    private static final int DELAY = 1000;

    private LedUtil() {
        // static class
    }

    public static void cycleColors() {
        Blink1 led = Blink1.open();
        for(Colors color : Colors.values()) {
            led.setRGB(color.getValue());
            Blink1.pause(DELAY);
        }
        led.setRGB(Color.BLACK);
        led.close();
    }

    public static void blinkColor(Color color, int repeat) {
        Blink1 led = Blink1.open();
        for(int i=0; i < repeat; i++) {
            led.setRGB(color);
            Blink1.pause(DELAY/2);
            led.setRGB(Color.BLACK);
            Blink1.pause(DELAY/2);
        }
        led.close();
    }

    public static void setColor(Color color) {
        Blink1 led = Blink1.open();
        led.setRGB(color);
        led.close();
    }
}
