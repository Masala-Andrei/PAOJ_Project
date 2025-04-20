package Models;

import java.util.ArrayList;

public class User extends Services.Debug{
    private static int idCounter = 0;
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String type;
    private ArrayList<Account> Accounts;

    public User(String name, String email, String phoneNumber, String password, String type) {
        id = idCounter + 1;
        idCounter++;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.type = type;
        this.Accounts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Account> getAccounts() {
        return Accounts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addAccount(Account account) {
        Accounts.add(account);
        if (debug)
            System.out.println("Account " + account.getName() + " added to user " + this.name);
    }

    public void removeAccount(Account account) {
        if (Accounts.contains(account)) {
            Accounts.remove(account);
            System.out.println("Account " + account.getName() + " removed from user " + this.name);
        } else {
            System.out.println("Account not found!");
        }
    }

    public Account findAccountByName(String accountName) {
        for (Account account : Accounts) {
            if (account.getName().equals(accountName)) {
                return account;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", type='" + type + '\'' +
                ", accounts=" + Accounts.size() +
                '}';
    }
}