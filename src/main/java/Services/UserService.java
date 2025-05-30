package Services;

import Models.User;

import java.util.ArrayList;
import java.util.Scanner;

public class UserService extends Debug {
    private ArrayList<User> Users;

    public UserService() {
        this.Users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return Users;
    }

    public void addUser(User user) {
        Users.add(user);
        if (debug)
            System.out.println("User " + user.getName() + " added");
    }

    public void deleteUser(User user) {
        System.out.println("Are you sure you want to delete your account?");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        if (answer.equalsIgnoreCase("yes")) {
            try{
                Users.remove(user);
                System.out.println("Your account has been deleted");
            }catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    public void displayUsers() {
        if (Users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }

        System.out.println("List of all users:");
        for (User user : Users) {
            System.out.println(user);
        }
    }

    public void removeUserAdmin(int userId) {
        User userToRemove = null;

        for (User user : Users) {
            if (user.getId() == userId) {
                userToRemove = user;
                break;
            }
        }

        if (userToRemove != null) {
            Users.remove(userToRemove);
            System.out.println("User with ID " + userId + " has been removed by admin.");
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }
    }

    public User findUserByEmailOrName(String emailName) {
        for (User user : Users) {
            if (user.getEmail().equals(emailName) || user.getName().equals(emailName)) {
                return user;
            }
        }
        return null;
    }

    public User findUserById(int id) {
        for (User user : Users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public boolean authenticateUser(String email, String password) {
        User user = findUserByEmailOrName(email);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}