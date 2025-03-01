package Service;

import DataAccess.AuthDataAccessMemory;
import DataAccess.DataAccessException;
import DataAccess.NotEnoughInfo;
import DataAccess.UserDataAccessMemory;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class LoginService {

    public LoginService(){
    }

    public AuthData login(UserData userData) throws Exception{
        var userDAO = new UserDataAccessMemory();
        var authDao = new AuthDataAccessMemory();
        var tempUser = userDAO.getUser(userData);

        if(tempUser == null){
            throw new DataAccessException("401 Error: unauthorized");
        }

        String clientPassword = userData.password();
        String daoPassword = tempUser.password();
        if(!Objects.equals(clientPassword, daoPassword)){
            throw new DataAccessException("401 Error: unauthorized");
        }

        return authDao.createAuthData(userData);
    }
}
