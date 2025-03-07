package dataaccess;

import model.UserData;

public class UserDatabase implements UserDAO{

    public UserDatabase() throws Exception{
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.configureDatabase();
    }

    public UserData getUser(UserData user) {
        return null;
    }

    public void createUser(UserData user) {

    }

    public void clearUserData() {

    }
}
