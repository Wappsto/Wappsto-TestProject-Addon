package wappsto.rest.session;

import wappsto.rest.request.HttpResponse;

public enum Wapp {
    HISTORICAL_DATA("Historical data","a56aeb91-06ac-4e64-9b24-16f8a7a90ec2");

    private final String name;
    public final String id;

    Wapp(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public static Wapp from(String name) {
        for (Wapp wapp : values()) {
            if (wapp.name == name) {
                return wapp;
            }
        }

        throw new IllegalStateException("Wapp not found");
    }
}
