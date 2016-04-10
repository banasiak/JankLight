package com.banasiak.java.janklight.client;

import org.eclipse.jetty.websocket.api.Session;

public interface ConnectionListener {

    void connected(Session session);

    void disconnected();

    void connecting();

    Session getSession();
}
