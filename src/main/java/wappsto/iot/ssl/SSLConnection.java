package wappsto.iot.ssl;

import wappsto.iot.*;
import wappsto.iot.ssl.model.*;

import javax.net.ssl.*;
import java.io.*;

public class SSLConnection implements Connection {
    private final SSLSocket socket;
    public final OutputStream toServer;

    public SSLConnection(
        String address,
        int port,
        WappstoCerts certs
    )
        throws Exception
    {
        SSLContext sc = init(certs);
        socket = (SSLSocket) sc.getSocketFactory().createSocket(address, port);
        socket.startHandshake();
        toServer = socket.getOutputStream();
    }

    private SSLContext init(WappstoCerts certs) throws Exception {
        CertProvider certProvider = new CertProvider(certs);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(certProvider.keyManagers, certProvider.trustManagers, null);
        return sc;
    }

    public boolean connected() {
        return socket.getSession().isValid();
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    @Override
    public void start(Callback messageCallback, Callback errorCallback) {
        try {
            InputReader reader = new InputReader(
                socket.getInputStream(),
                messageCallback,
                errorCallback
            );
            reader.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String s) throws IOException {
        toServer.write(s.getBytes());
    }
}
