package com.banasiak.java.janklight;

import java.awt.*;

public enum Colors {
    BLUE("blue", Color.BLUE),
    CYAN("cyan", Color.CYAN),
    GREEN("green", Color.GREEN),
    MAGENTA("magenta", Color.MAGENTA),
    ORANGE("orange", Color.ORANGE),
    PINK("pink", Color.PINK),
    RED("red", Color.RED),
    YELLOW("yellow", Color.YELLOW),
    WHITE("white", Color.WHITE),
    BLACK("black", Color.BLACK),
    ON("on", Color.WHITE),
    OFF("off", Color.BLACK);

    String name;
    Color value;

    Colors(String name, Color value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Color getValue() {
        return value;
    }

    public static Colors getColorForName(String name) {
        for(Colors color : Colors.values()) {
            if(color.name.equals(name)) {
                return color;
            }
        }
        return null;
    }
}
