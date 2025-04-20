package Models;

import java.util.UUID;

public abstract class Account {
    private static int staticAccountId;
    protected int accountId;
    protected String type;
    protected String name;
    protected String balance;
    protected boolean active;

    static{
        staticAccountId = 1;
    }

    public Account(String type, String name, String balance) {
        this.accountId = staticAccountId++;
        this.type = type;
        this.name = name;
        this.balance = balance;
        this.active = true;
    }

    public int getIdAccount() {
        return accountId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addBalance(String amount) {
        double currentBalance = Double.parseDouble(this.balance);
        double amountToAdd = Double.parseDouble(amount);
        double newBalance = currentBalance + amountToAdd;
        this.setBalance(String.valueOf(newBalance));
    }

    public abstract void withdraw(String amount);
    public abstract void deposit(String amount);
    public abstract String getAccountInfo();
}