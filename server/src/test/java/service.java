import DataAccess.DataAccessException;
import Service.RegisterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

public class service {

    @BeforeEach
    public void clearMaps(){

    }

    @Test
    public void correctRegisterTest() throws DataAccessException {
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService register = new RegisterService();
        AuthData auth = register.register(user);

        assertSame(username, auth.username());
        assertNotNull(auth.authToken());
    }

    @Test
    public void invalidUsernameTest() throws DataAccessException{
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService register = new RegisterService();
        AuthData auth = register.register(user);

        try {
            register.register(user);
        } catch (DataAccessException e) {
            assertEquals("403 Error: already taken", e.getMessage());
        }

        assertSame(username, auth.username());
        assertNotNull(auth.authToken());
    }
}
