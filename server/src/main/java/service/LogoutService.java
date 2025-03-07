package service;

import dataaccess.AuthDAO;
import dataaccess.AuthDataAccessMemory;
import dataaccess.Unauthorized;
import model.AuthData;

public class LogoutService {
    public LogoutService(){
    }

    public void logout(String authToken) throws Exception{
        AuthDAO authDAO = new AuthDataAccessMemory();
        AuthData authData = authDAO.getAuth(authToken);

        if(authData == null){
            throw new Unauthorized("401 Error: unauthorized");
        }

        authDAO.deleteAuth(authData);
    }
}
