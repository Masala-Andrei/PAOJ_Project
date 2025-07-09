package Models;

import java.util.UUID;

public abstract class Account {
    private static int staticAccountId;
    protected int accountId;
    protected String type;
    protected String name;
    protected String balance;
    protected String IBAN;

    static{
        staticAccountId = 1;
    }

    public Account(String type, String name, String balance, String IBAN) {
        this.type = type;
        this.accountId = staticAccountId++;
        this.name = name;
        this.balance = balance;
        this.IBAN = IBAN;
    }

    public String getIBAN() {
        return IBAN;
    }

    public String getType() {
        return type;
    }

    public int getIdAccount() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public String getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


    public void addBalance(String amount) {
        double currentBalance = Double.parseDouble(this.balance);
        double amountToAdd = Double.parseDouble(amount);
        double newBalance = currentBalance + amountToAdd;
        this.setBalance(String.valueOf(newBalance));
    }


    public abstract String getAccountInfo();
}