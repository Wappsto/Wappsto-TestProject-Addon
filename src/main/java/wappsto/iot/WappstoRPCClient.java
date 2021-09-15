package wappsto.iot;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import wappsto.iot.model.WappstoCerts;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class WappstoRPCClient {
    private SSLSocket socket;

    public WappstoRPCClient(
        String address,
        WappstoCerts certs,
        int port
    ) throws Exception {

        if (java.security.Security.getProvider("BC") == null) {
            java.security.Security.addProvider(
                new BouncyCastleProvider()
            );
        }

        SSLContext sc = init(certs);

        socket = (SSLSocket) sc.getSocketFactory().createSocket(address, port);
        socket.startHandshake();
    }

    private SSLContext init(WappstoCerts certs) throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        Certificate[] certChain = createCertificates(certs);
        PrivateKey pk = generatePrivateKey(certs);
        KeyStore ks = createKeyStore(certChain, pk);

        KeyManagerFactory kmf = initializeKeyManager(ks);

        TrustManagerFactory tmf = initializeTrustManager(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return sc;
    }

    private TrustManagerFactory initializeTrustManager(KeyStore ks) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        tmf.init(ks);
        return tmf;
    }

    private KeyManagerFactory initializeKeyManager(KeyStore ks) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "".toCharArray());
        return kmf;
    }

    private PrivateKey generatePrivateKey(WappstoCerts certs) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String encodedPrivateKey = certs.privateKey;
        encodedPrivateKey = strip(encodedPrivateKey);

        byte[] encodedBytes = Base64.getDecoder().decode(encodedPrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pk = kf.generatePrivate(keySpec);
        return pk;
    }

    private KeyStore createKeyStore(Certificate[] certChain, PrivateKey pk) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, "".toCharArray());
        ks.setEntry(
            "private_key",
            new KeyStore.PrivateKeyEntry(pk, certChain),
            new KeyStore.PasswordProtection("".toCharArray())
        );
        ks.setCertificateEntry("ca", certChain[0]);
        ks.setCertificateEntry("client", certChain[1]);
        return ks;
    }

    private String strip(String encodedPrivateKey) {
        return encodedPrivateKey
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replaceAll("\\s+","");
    }

    private Certificate[] createCertificates(WappstoCerts certs) throws CertificateException, IOException {
        Certificate[] chain = {};

        InputStream certStream = new ByteArrayInputStream(
            (
                certs.client +
                certs.ca
            ).getBytes()
        );

        chain = CertificateFactory.getInstance("X.509")
            .generateCertificates(certStream)
            .toArray(chain);
        return chain;
    }

    public boolean connected() {
        System.out.println(socket.getSession().getId().toString());
        return socket.getSession().isValid();
    }
}
