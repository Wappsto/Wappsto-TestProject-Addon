import org.junit.jupiter.api.Test;
import wappsto.rest.*;

import static org.junit.jupiter.api.Assertions.*;


public class SessionTest {
    AdminSession admin = AdminSession.instance();

    @Test
    public void creates_new_user() {
        UserSession session = new SessionBuilder().create();

        assertEquals(
            admin.fetch(session.user.username).username,
            session.user.username);
    }
}
