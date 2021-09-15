package wappsto.iot.ssl;

import wappsto.iot.Connection;
import wappsto.iot.ssl.model.WappstoCerts;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import java.io.IOException;

public class SSLConnection implements Connection {
    private SSLSocket socket;

    public SSLConnection(
        String address,
        int port,
        WappstoCerts certs
    ) throws Exception {
        SSLContext sc = init(certs);

        socket = (SSLSocket) sc.getSocketFactory().createSocket(address, port);
        socket.startHandshake();
    }

    private SSLContext init(WappstoCerts certs) throws Exception {
        CertProvider certProvider = new CertProvider(certs);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(certProvider.keyManagers, certProvider.trustManagers, null);
        return sc;
    }

    public boolean connected() {
        System.out.println(socket.getSession().getId().toString());
        return socket.getSession().isValid();
    }
}
