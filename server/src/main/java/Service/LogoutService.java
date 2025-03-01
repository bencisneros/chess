package Service;

import DataAccess.AuthDataAccessMemory;
import DataAccess.DataAccessException;
import DataAccess.UserDataAccessMemory;
import jdk.jshell.spi.ExecutionControl;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class LogoutService {
    public LogoutService(){
    }

    public void logout(String authToken) throws Exception{
        AuthDataAccessMemory authDAO = new AuthDataAccessMemory();
        AuthData authData = authDAO.getAuth(authToken);

        if(authData == null){
            throw new DataAccessException("401 Error: unauthorized");
        }

        authDAO.deleteAuth(authData);
    }
}
