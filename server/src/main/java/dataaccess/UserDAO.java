package dataaccess;

import model.UserData;

public interface UserDAO {
    public UserData getUser(UserData user) throws Exception;
    public void createUser(UserData user) throws Exception;
    public void clearUserData() throws Exception;
}
