package wappsto.iot.ssl.model;

import wappsto.rest.network.model.*;

public class WappstoCerts {
    public final String ca;
    public final String client;
    public final String privateKey;


    public WappstoCerts(String ca, String client, String privateKey) {
        this.ca = ca;
        this.client = client;
        this.privateKey = privateKey;
    }

    public WappstoCerts(CreatorResponse network) {
        this(network.ca, network.certificate, network.privateKey);
    }
}
