package wappsto.rest;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

public class AdminSession {
    private static AdminSession instance;
    private String sessionId;

    public static AdminSession instance() {
        if (instance == null) {
            instance = new AdminSession();
        }
        return instance;
    }

    private AdminSession() {
        Client client = ClientBuilder.newClient();


    }

    public UserSession register(String username, String password) {
        return new UserSession();
    }
}
