package Services;

import Connection.DBConnector;
import Models.Account;
import Models.SavingsAccount;
import Models.TransactionsAccount;
import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserService extends DBConnector {
    private ArrayList<User> Users;

    public UserService() {
        this.Users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return Users;
    }

    public void addUser(User user) throws SQLException {
        try {
            String sql = "Insert into user(name, email, phoneNumber, password, type) values(?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating user");
        }
    }

    public void deleteUser(User user, int accId)  {
        Users.remove(user);
        try{
            String sql = "DELETE FROM user WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, accId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            String sql = "SELECT * from user where name = ? or email = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, emailName);
            stmt.setString(2, emailName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ArrayList<Account> temp = new ArrayList();
                sql = "SELECT account.name, account.balance, account.iban, account.type, transactionsaccount.monthlyFee from transactionsaccount INNER JOIN account " +
                        "on transactionsaccount.accountId = account.accountId where account.userId = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, rs.getInt("id"));
                ResultSet rs2 = stmt.executeQuery();
                while (rs2.next()) {
                    temp.add(new TransactionsAccount(
                                    "Transactions",
                                    rs2.getString("name"),
                                    rs2.getString("balance"),
                                    Integer.parseInt(rs2.getString("monthlyFee")),
                                    rs2.getString("iban")
                            )
                    );
                }

                sql = "SELECT account.name, account.balance, account.iban, account.type, savingsaccount.interestRate, savingsaccount.commitmentPeriod from savingsaccount INNER JOIN account " +
                        "on savingsaccount.accountId = account.accountId where account.userId = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, rs.getInt("id"));
                ResultSet rs3 = stmt.executeQuery();
                while (rs3.next()) {
                    temp.add(new SavingsAccount(
                                    "Savings",
                                    rs3.getString("name"),
                                    rs3.getString("balance"),
                                    rs3.getString("interestRate"),
                                    Integer.parseInt(rs3.getString("commitmentPeriod")),
                                    rs3.getString("iban")

                            )
                    );
                }
                User user = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("password"),
                        rs.getString("type"),
                        temp
                );
                stmt.close();
                return user;
            } else
                return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean authenticateUser(String email, String password) {
        User user = findUserByEmailOrName(email);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}