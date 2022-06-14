package dev.teamhelios.stonepowered.clientHandling;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.utils.HeliosLogger;
import io.javalin.http.sse.SseClient;

import java.util.LinkedList;

public class ClientHandler {

    private final LinkedList<SseClient> clients = new LinkedList<>();

    public StonePowered stonePowered;

    public ClientHandler(StonePowered stonePowered) {
        this.stonePowered = stonePowered;
    }

    public void register(SseClient client) {
        clients.add(client);
        HeliosLogger.warn("Registered client");
        //TODO: clear Console and resend History
    }

    public void unregister(SseClient client) {
        clients.remove(client);
        HeliosLogger.warn("Client disconnected!");
    }
}
