package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Objects;

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

    public boolean checkPassword(String normalPassword, String hashedPassword) {
        return Objects.equals(normalPassword, hashedPassword);
    }

    public HashMap<String, UserData> getUserMap(){
        return USER_DATA_MEMORY;
    }
}
