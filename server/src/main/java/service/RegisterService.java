package service;

import dataaccess.AuthDataAccessMemory;
import dataaccess.DataAccessException;
import dataaccess.NotEnoughInfo;
import dataaccess.UserDataAccessMemory;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class RegisterService {
    private final UserDataAccessMemory userDAO;
    private final AuthDataAccessMemory authDAO;

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
