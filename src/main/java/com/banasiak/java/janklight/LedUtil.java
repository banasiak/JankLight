package com.banasiak.java.janklight;

import java.awt.*;

import thingm.blink1.Blink1;

public class LedUtil {

    private static final int DELAY = 1000;

    private LedUtil() {
        // static class
    }

    public static void cycleColors() {
        System.out.println("Cycling defined colors");
        Blink1 led = Blink1.open();

        for(Colors color : Colors.values()) {
            led.setRGB(color.getValue());
            Blink1.pause(DELAY);
        }

        System.out.println("Cycle complete - turning off LED");
        led.setRGB(Color.BLACK);
        led.close();
    }

    public static void blinkColor(Color color, int repeat) {
        Blink1 led = Blink1.open();
        for(int i=0; i < repeat; i++) {
            led.setRGB(color);
            Blink1.pause(DELAY);
            led.setRGB(Color.BLACK);
            Blink1.pause(DELAY);
        }
        led.close();
    }
}
