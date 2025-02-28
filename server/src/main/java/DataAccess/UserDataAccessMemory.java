package DataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDataAccessMemory implements UserDAO {

    private final HashMap<String, model.UserData> userDataMemory;

    public UserDataAccessMemory(){
        userDataMemory = new HashMap<>();
    }

    public UserData getUser(UserData user){
        return userDataMemory.get(user.username());
    }

    public void createUser(UserData user){
        userDataMemory.put(user.username(), user);
    }
}
