package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(UserData user) throws Exception;
    void createUser(UserData user) throws Exception;
    void clearUserData() throws Exception;
    boolean checkPassword(String normalPassword, String hashedPassword);
}
