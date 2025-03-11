package dataaccess;

import dataaccess.AuthDataAccessMemory;
import dataaccess.GameDataAccessMemory;
import dataaccess.UserDataAccessMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {

    @BeforeEach
    public void clearAllTables() throws Exception{
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    private String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }


    @Test
    public void createUserTest() throws Exception{
        UserDatabase userDatabase = new UserDatabase();

        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var expected = new UserData(username, email, password);

        userDatabase.createUser(expected);

        var actual = userDatabase.getUser(expected);

        assertEquals(username, actual.username());
        assertEquals(email, actual.email());
        assertNotEquals(password, actual.password());
    }

    @Test
    public void createBadUserTest() throws Exception{
        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var expected = new UserData(username, email, password);

        var userDataBase = new UserDatabase();
        userDataBase.createUser(expected);
        assertThrows(Exception.class, () -> {
            userDataBase.createUser(expected);
        });
    }
}

