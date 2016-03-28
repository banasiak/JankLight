package com.banasiak.java.janklight.server;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.banasiak.java.janklight.Colors;
import com.banasiak.java.janklight.JankLight;

import org.eclipse.jetty.websocket.api.Session;

import java.awt.Color;
import java.io.IOException;

import spark.Spark;

public class JankLightServer {

    private static final String APPLICATION_ID = "amzn1.echo-sdk-ams.app.a8e14363-aca2-4c1b-b8cf-0d43306972ce";

    private static final String ERROR_MESSAGE = "I'm sorry Dave, I'm afraid I can't do that";
    private static final String CONNECTED_MESSAGE = "Jank Light is connected";
    private static final String DISCONNECTED_MESSAGE = "Jank Light is not connected";

    String keystoreFile;
    String keystorePassword;

    private static Session webSocketSession;

    public JankLightServer(String keystoreFile, String keystorePassword) {
        this.keystoreFile = keystoreFile;
        this.keystorePassword = keystorePassword;

        System.out.println("Server instantiated");
    }

    public void start() {
        System.out.println("Initializing endpoints");
        Spark.secure(keystoreFile, keystorePassword, null, null);

        // start the websocket used by the client
        Spark.webSocket("/socket", ServerSocket.class);

        // start the endpoint used by Alexa
        Spark.post("/janklight", (req, res) -> {
            String responseMessage = ERROR_MESSAGE;
            IntentRequest request = parseSpeechletRequest(req.body());
            if(request != null) {
                responseMessage = handleIntent(request.getIntent());
            }
            return getSpeechResponse(responseMessage);
        });

        System.out.println("Endpoints initialized");
    }

    private String handleIntent(Intent intent) {
        switch(intent.getName()) {
            case "ChangeColorIntent":
                String colorName = intent.getSlot("Color").getValue();
                if(changeLightColor(colorName)) {
                    return "Jank Light is now " + colorName;
                }
                break;
            case "CycleColorsIntent":
                if(cycleLightColors()) {
                    return "Welcome to the jank party!";
                }
                break;
            case "IsConnectedIntent":
                return (webSocketSession != null) ? CONNECTED_MESSAGE : DISCONNECTED_MESSAGE;
            default:
                return ERROR_MESSAGE;
        }
        return ERROR_MESSAGE;
    }

    private boolean changeLightColor(String colorName) {
        Color color = Colors.getColorForName(colorName);
        if(color != null) {
            if(webSocketSession != null) {
                try {
                    System.out.println("Set light color to: " + colorName);
                    webSocketSession.getRemote().sendString(colorName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No device connected to websocket");
            }
            return true;
        } else {
            System.out.println("ERROR: Cannot set light to: " + colorName);
            return false;
        }
    }

    private boolean cycleLightColors() {
        if (webSocketSession != null) {
            try {
                System.out.println("Cycle light colors");
                webSocketSession.getRemote().sendString(JankLight.PARTY_MODE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else {
            System.out.println("No device connected to websocket");
            return false;
        }
    }

    private IntentRequest parseSpeechletRequest(String body) {
        try {
            SpeechletRequestEnvelope envelope = SpeechletRequestEnvelope.fromJson(body);
            String appId = envelope.getSession().getApplication().getApplicationId();
            if(appId.equals(APPLICATION_ID) && envelope.getRequest() instanceof IntentRequest) {
                return (IntentRequest) envelope.getRequest();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getSpeechResponse(String responseMessage) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(responseMessage);
        SpeechletResponseEnvelope envelope = new SpeechletResponseEnvelope();
        envelope.setResponse(SpeechletResponse.newTellResponse(speech));
        try {
            return envelope.toJsonString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setSession(Session session) {
        webSocketSession = session;
    }

    public static Session getSession() {
        return webSocketSession;
    }
}
