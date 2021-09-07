package wappsto.rest.session;

public enum Wapp {
    HISTORICAL_DATA(
        "Historical Data",
        "a56aeb91-06ac-4e64-9b24-16f8a7a90ec2"
    ),
    WAPP_CREATOR(
        "Wapp Creator",
        "355994b4-b74c-4d62-8c92-a6e5304e7cc2"
    ),
    IOT_RAPID_PROTOTYPING(
        "IoT Rapid Prototyping",
        "1e54f21e-8514-465a-9f79-5c346155e58a"
    );

    private final String name;
    public final String id;

    Wapp(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public static Wapp from(String name) {
        for (Wapp wapp : values()) {
            if (wapp.name.equals(name)) {
                return wapp;
            }
        }

        throw new IllegalStateException("Wapp not found");
    }
}
