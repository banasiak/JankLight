package com.banasiak.java.janklight.client.tasks;

// because Java's thread API is stupid...
public interface StoppableRunnable extends Runnable {

    void stop();
}
