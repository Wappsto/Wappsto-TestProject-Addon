package wappsto.iot.ssl;

import wappsto.iot.Callback;
import wappsto.iot.Connection;
import wappsto.iot.ssl.model.WappstoCerts;
import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SSLConnection implements Connection {
    private SSLSocket socket;
    private IncomingDataHandler fromServer;
    public final OutputStream toServer;

    public SSLConnection(
        String address,
        int port,
        WappstoCerts certs
    ) throws Exception {
        SSLContext sc = init(certs);
        socket = (SSLSocket) sc.getSocketFactory().createSocket(address, port);
        socket.startHandshake();
        toServer = socket.getOutputStream();

        fromServer = new IncomingDataHandler(socket.getInputStream());
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

    public void disconnect() throws IOException {
        socket.close();
    }

    @Override
    public void setIncomingCallback(Callback callback) {
        fromServer.setCallBack(callback);
    }
}
