package DataAccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    public UserData getUser(UserData user);
    public void createUser(UserData user);
}
