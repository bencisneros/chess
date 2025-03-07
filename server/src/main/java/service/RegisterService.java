package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(){
        this.userDAO = new UserDataAccessMemory();
        this.authDAO = new AuthDataAccessMemory();
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData register(UserData user) throws Exception {
        if(user.username() == null || user.email() == null || user.password() == null){
            throw new NotEnoughInfo("400 Error: bad request");
        }

        if(userDAO.getUser(user) != null){
            throw new DataAccessException("403 Error: already taken");
        }

        userDAO.createUser(user);

        return authDAO.createAuthData(user);
    }
}
