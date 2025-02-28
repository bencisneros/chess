package DataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class UserDataAccessMemory implements UserDAO {

    private static final HashMap<String, UserData> userDataMemory = new HashMap<>();

    public UserData getUser(UserData user){
        return userDataMemory.get(user.username());
    }

    public void createUser(UserData user){
        userDataMemory.put(user.username(), user);
    }

    public void clearUserData(){
        userDataMemory.clear();
    }

    public HashMap<String, UserData> getUserMap(){
        return userDataMemory;
    }
}
