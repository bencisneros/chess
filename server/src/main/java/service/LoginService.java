package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class LoginService {

    public LoginService(){
    }

    public AuthData login(UserData userData) throws Exception{
        UserDAO userDAO = new UserDatabase(); // new UserDataAccessMemory();
        AuthDAO authDao = new AuthDatabase(); // new AuthDataAccessMemory();
        var tempUser = userDAO.getUser(userData);

        if(tempUser == null){
            throw new DataAccessException("401 Error: unauthorized");
        }

        String clientPassword = userData.password();
        String daoPassword = tempUser.password();

        if(!userDAO.checkPassword(clientPassword, daoPassword)){
            throw new DataAccessException("401 Error: unauthorized");
        }

        return authDao.createAuthData(userData);
    }
}
