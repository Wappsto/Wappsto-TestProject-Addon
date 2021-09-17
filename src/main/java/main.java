import wappsto.iot.WappstoRPCClient;
import wappsto.iot.ssl.SSLConnection;
import wappsto.iot.ssl.model.WappstoCerts;
import wappsto.rest.iot.NetworkService;
import wappsto.rest.iot.model.NetworkCreatorResponse;
import wappsto.rest.session.User;
import wappsto.rest.session.model.Credentials;

import java.io.InputStream;

abstract class TestThread extends Thread {
    abstract void testTheString(String test);
}

