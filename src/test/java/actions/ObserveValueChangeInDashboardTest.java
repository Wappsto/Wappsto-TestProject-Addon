package actions;

import org.junit.jupiter.api.*;
import wappsto.rest.request.exceptions.*;

import static util.Utils.admin;
import static util.Utils.defaultUser;

@Disabled
public class ObserveValueChangeInDashboardTest {
    @BeforeAll
    public static void setup() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
