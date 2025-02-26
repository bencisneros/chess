package DataAccess;

import java.util.HashMap;

public class UserData {

    private HashMap<String, model.UserData> users = new HashMap<>();

    public model.UserData getUser(model.UserData user){
        return users.get(user.username());
        
    }
}
