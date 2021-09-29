package actions;

import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.rest.network.*;
import wappsto.rest.network.model.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;
import wappsto.rest.session.model.*;

public class Utils {
    public static void logIn(WebDriver browser, String session) {
        browser.manage().addCookie(
            new Cookie("sessionID", session)
        );
    }

    public static String getSessionFrom(WebDriver browser) {
        try {
            return browser
                .manage()
                .getCookieNamed("sessionID")
                .getValue();
        } catch (NullPointerException e) {
            throw new NoSuchCookieException("Session cookie not found");
        }
    }

    public static CreatorResponse getCreator(WebDriver browser, String serviceUrl) throws FailureException {
        String sessionId;
        User session;
        try {
            sessionId = getSessionFrom(browser);
            session = new User(sessionId, serviceUrl);
        } catch (NoSuchCookieException e) {
            throw new FailureException("Browser is not logged in");
        } catch (HttpException e) {
            throw new FailureException(
                "Failed to create user session from session cookie"
            );
        } catch (Exception e) {
            throw new FailureException("Unknown error: " + e.getMessage());
        }
        NetworkService service = new NetworkService(session);
        CreatorResponse creator;
        try {
            creator = service.getCreator();
        } catch (Exception e) {
            throw new FailureException(
                "Failed to get creator: " + e.getMessage()
            );
        }
        return creator;
    }

    public static VirtualIoTNetwork createVirtualIoTNetwork(
        CreatorResponse creator,
        int min,
        int max,
        int stepSize,
        String type,
        String port,
        String socketUrl
    )
        throws FailureException
    {
        int socketPort = Integer.parseInt(port);

        RPCClient client;
        try {
            client = new RPCClient.Builder(creator)
                .connectingTo(socketUrl, socketPort)
                .build();
        } catch (Exception e) {
            throw new FailureException(
                "Failed to connect to socket: " + e.getMessage()
            );
        }

        NetworkSchema schema = new NetworkSchema.Builder(
            "Test",
            creator.network.id
        ).addDevice("Test")
            .addValue("Test", ValuePermission.RW)
            .withNumberSchema(min, max, stepSize, type)
            .addToDevice()
            .addToNetwork()
            .build();

        VirtualIoTNetwork network;
        try {
            network = new VirtualIoTNetwork(schema, client);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to start client: " + e.getMessage()
            );
        }
        return network;
    }

    public static User registerNewUser(
        AdminCredentials adminCredentials,
        Credentials userCredentials,
        String serviceUrl
    ) throws FailureException
    {
        Admin admin;
        try {
            admin = new Admin(adminCredentials, serviceUrl);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to create admin session: " + e.getMessage()
            );
        }

        User session;
        try {
            session = new User.Builder(admin, serviceUrl)
                .withCredentials(userCredentials)
                .create();
        } catch (Exception e) {
            throw new FailureException(
                "Failed to register user: " + e.getMessage()
            );
        }

        return session;
    }
}
