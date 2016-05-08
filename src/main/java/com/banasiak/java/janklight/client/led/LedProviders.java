package com.banasiak.java.janklight.client.led;

public enum LedProviders {
    BLINK1(1, "blink1"),
    BOBLIGHT(2, "boblight");

    private final int id;

    private final String name;

    LedProviders(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static LedProviders getProviderForId(int id) {
        for (LedProviders provider : LedProviders.values()) {
            if (provider.id == id) {
                return provider;
            }
        }
        throw new IllegalArgumentException("No LedProvider for id=" + id);
    }

    public static LedProviders getProviderForName(String name) {
        for (LedProviders provider : LedProviders.values()) {
            if (provider.name.equals(name)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("No LedProvider for name=" + name);
    }
}
