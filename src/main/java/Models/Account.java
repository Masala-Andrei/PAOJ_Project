package Models;

import java.util.UUID;

public abstract class Account {
    protected String type;
    protected String name;
    protected String balance;
    protected String IBAN;

    public Account(String type, String name, String balance, String IBAN) {
        this.type = type;
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

    public abstract String toString();
}