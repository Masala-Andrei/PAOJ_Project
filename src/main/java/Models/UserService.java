package Models;

import java.util.ArrayList;

public class UserService {
    private ArrayList<User> Users;

    public UserService() {
        this.Users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return Users;
    }

    public void addUser(User user) {
        Users.add(user);
        System.out.println("User " + user.getName() + " added");
    }

    public void removeUser(User requester, User user) {
        if (requester.getType().equals("Admin")) {
            try{
                Users.remove(user);
            }catch(Exception e){
                System.out.println("User " + user.getName() + " not found");
            }
        }
    }

    public void removeUserAdmin()
}
