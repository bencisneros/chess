package Service;

import DataAccess.AuthDataAccess;
import DataAccess.DataAccessException;
import DataAccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class RegisterService {
    private UserDataAccess userDAO;
    private AuthDataAccess authDAO;

    public RegisterService(){
        this.userDAO = new UserDataAccess();
        this.authDAO = new AuthDataAccess();
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData register(UserData user) throws DataAccessException {
        if(userDAO.getUser(user) != null){
            throw new DataAccessException("403 Error: already taken");
        }

        userDAO.createUser(user);

        AuthData auth = new AuthData(generateToken(), user.username());
        authDAO.createAuthData(auth);

        return auth;
    }
}
