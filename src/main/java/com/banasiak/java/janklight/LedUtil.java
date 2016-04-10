package com.banasiak.java.janklight;

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
        led.setRGB(Colors.BLACK.getValue());
        led.close();
    }

    public static void blinkColor(Colors color, int repeat) {
        Blink1 led = Blink1.open();
        for(int i=0; i < repeat; i++) {
            led.setRGB(color.getValue());
            Blink1.pause(DELAY/2);
            led.setRGB(Colors.BLACK.getValue());
            Blink1.pause(DELAY/2);
        }
        led.close();
    }

    public static void setColor(Colors color) {
        Blink1 led = Blink1.open();
        led.setRGB(color.getValue());
        led.close();
    }

    public static void fadeToColor(Colors color) {
        Blink1 led = Blink1.open();
        led.setRGB(Colors.BLACK.getValue());
        led.fadeToRGB(2000, color.getValue());
        Blink1.pause(1000);
        led.fadeToRGB(2000, Colors.BLACK.getValue());
        Blink1.pause(2000);
        led.close();
    }
}
