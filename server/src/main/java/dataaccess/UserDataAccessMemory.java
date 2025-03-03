package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserDataAccessMemory implements UserDAO {

    private static final HashMap<String, UserData> USER_DATA_MEMORY = new HashMap<>();

    public UserData getUser(UserData user){
        return USER_DATA_MEMORY.get(user.username());
    }

    public void createUser(UserData user){
        USER_DATA_MEMORY.put(user.username(), user);
    }

    public void clearUserData(){
        USER_DATA_MEMORY.clear();
    }

    public HashMap<String, UserData> getUserMap(){
        return USER_DATA_MEMORY;
    }
}
