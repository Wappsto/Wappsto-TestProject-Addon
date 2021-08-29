import org.junit.jupiter.api.Test;
import wappsto.rest.AdminSession;
import wappsto.rest.Forbidden;
import wappsto.rest.SessionBuilder;
import wappsto.rest.UserSession;
import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {
    AdminSession admin = AdminSession.instance();

    @Test
    public void fails_to_create_new_user_when_admin_privileges_are_not_present() {
        assertThrows(Forbidden.class, () -> {
            new SessionBuilder().create();
        });
    }

    @Test
    public void creates_new_user() {
        UserSession session = new SessionBuilder().create();

        assertEquals(admin.fetch(session.username).name, session.username);
    }
}
