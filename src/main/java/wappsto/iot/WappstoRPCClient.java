package wappsto.iot;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import wappsto.iot.ssl.CertProvider;
import wappsto.iot.ssl.model.WappstoCerts;

import javax.net.ssl.*;

public class WappstoRPCClient {
    private SSLSocket socket;

    public WappstoRPCClient(
        String address,
        WappstoCerts certs,
        int port
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
