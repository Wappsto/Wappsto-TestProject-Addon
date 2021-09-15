package wappsto.iot.model;

public class WappstoCerts {
    public final String ca;
    public final String client;
    public final String privateKey;


    public WappstoCerts(String ca, String client, String privateKey) {
        this.ca = ca;
        this.client = client;
        this.privateKey = privateKey;
    }
}
