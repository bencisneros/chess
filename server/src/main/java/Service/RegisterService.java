package Service;

import DataAccess.AuthDataAccess;
import DataAccess.DataAccessException;
import DataAccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class RegisterService {
    private UserDataAccess userDataAccess;
    private AuthDataAccess authDataAccess;
    public RegisterService(){
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData register(UserData user) throws DataAccessException {
        if(userDataAccess.getUser(user) != null){
            throw new DataAccessException("400 Error: username already taken");
        }

        userDataAccess.createUser(user);

        AuthData auth = new AuthData(generateToken(), user.username());
        authDataAccess.createAuthData(auth);

        return auth;
    }
}
