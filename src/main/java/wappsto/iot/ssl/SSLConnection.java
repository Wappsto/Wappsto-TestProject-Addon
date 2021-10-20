package wappsto.iot.ssl;

import wappsto.iot.*;
import wappsto.iot.ssl.model.*;

import javax.net.ssl.*;
import java.io.*;

/**
 * Adapter class for the SSL socket
 */
public class SSLConnection implements Connection {
    private final SSLSocket socket;
    public final OutputStream toServer;

    /**
     * Create a connection over SSL
     * @param address socket address
     * @param port socket port
     * @param certs certificates to use with the connection
     * @throws Exception
     */
    public SSLConnection(
        String address,
        int port,
        WappstoCerts certs
    )
        throws Exception
    {
        SSLContext sc = init(certs);
        socket = (SSLSocket) sc.getSocketFactory().createSocket(address, port);
        toServer = socket.getOutputStream();
    }

    private SSLContext init(WappstoCerts certs) throws Exception {
        CertProvider certProvider = new CertProvider(certs);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(certProvider.keyManagers, certProvider.trustManagers, null);
        return sc;
    }

    /**
     * Check connection status
     * @return is connected
     */
    public boolean connected() {
        return socket.getSession().isValid();
    }

    /**
     * Close the connection
     * @throws IOException
     */
    public void disconnect() throws IOException {
        socket.close();
    }

    /**
     * Open the connection
     * @param messageCallback message callback passed to the input reader
     * @param errorCallback error callbakc passed to the input reader
     * @throws Exception
     */
    @Override
    public void start(Callback messageCallback, Callback errorCallback)
        throws Exception
    {
        try {
            socket.startHandshake();
        } catch (IOException e) {
            throw new Exception(
                "Failed to connect to socket: " + e.getMessage()
            );
        }

        try {
            InputReader reader = new InputReader(
                socket.getInputStream(),
                messageCallback,
                errorCallback
            );
            reader.start();
        } catch (IOException e) {
            System.out.println("Input stream closed");
        }
    }

    /**
     * Sends a string message through the socket
     * @param message message sent
     * @throws IOException
     */
    @Override
    public void send(String message) throws IOException {
        toServer.write(message.getBytes());
    }
}
