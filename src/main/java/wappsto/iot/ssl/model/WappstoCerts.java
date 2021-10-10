package wappsto.iot.ssl.model;

import wappsto.api.network.model.*;

public class WappstoCerts {
    public String ca;
    public String client;
    public String privateKey;


    public WappstoCerts(String ca, String client, String privateKey) {
        this.ca = ca;
        this.client = client;
        this.privateKey = privateKey;
    }

    public WappstoCerts(CreatorResponse network) {
        this(network.ca, network.certificate, network.privateKey);
    }
    public WappstoCerts() {}
}
