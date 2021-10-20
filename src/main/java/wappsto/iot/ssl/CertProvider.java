package wappsto.iot.ssl;

import org.bouncycastle.jce.provider.*;
import wappsto.iot.ssl.model.*;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.security.spec.*;
import java.util.*;

/**
 * Sets the certificates required to establish an SSL connection
 */
public class CertProvider {
    public final TrustManager[] trustManagers;
    public final KeyManager[] keyManagers;

    public CertProvider(WappstoCerts certs) throws Exception {
        Certificate[] chain = createCertificateChain(certs);
        PrivateKey privateKey = generatePrivateKey(certs);
        KeyStore keyStore = createKeyStore(chain, privateKey);
        keyManagers = createKeyManagers(keyStore);
        trustManagers = createTrustManagers(keyStore);
    }

    private Certificate[] createCertificateChain(WappstoCerts certs) throws CertificateException, IOException {
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

    private PrivateKey generatePrivateKey(WappstoCerts certs) throws Exception {

        //Otherwise, generating the keyspec throws an error
        //Don't ask
        if (java.security.Security.getProvider("BC") == null) {
            java.security.Security.addProvider(
                new BouncyCastleProvider()
            );
        }

        String encodedPrivateKey = certs.privateKey;
        encodedPrivateKey = strip(encodedPrivateKey);

        byte[] encodedBytes = Base64.getDecoder().decode(encodedPrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pk = kf.generatePrivate(keySpec);
        return pk;
    }

    private String strip(String encodedPrivateKey) {
        return encodedPrivateKey
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replaceAll("\\s+","");
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

    private KeyManager[] createKeyManagers(KeyStore ks) throws Exception {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "".toCharArray());
        return kmf.getKeyManagers();
    }

    private TrustManager[] createTrustManagers(KeyStore ks) throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        tmf.init(ks);
        return tmf.getTrustManagers();
    }
}
