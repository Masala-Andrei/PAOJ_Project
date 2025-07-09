package Services;

import Connection.DBConnector;
import Models.Account;
import Models.SavingsAccount;
import Models.TransactionsAccount;
import Models.User;

import java.security.Provider;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

public class AccountService extends DBConnector {
    private final ArrayList<Account> accounts;

    public AccountService() {
        this.accounts = new ArrayList<>();
    }

    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        if (accounts.contains(account)) {
            accounts.remove(account);
            System.out.println("Account " + account.getName() + " removed from the system.");
        } else {
            System.out.println("Account not found in the system.");
        }
    }


    public static String generateRandomIBAN() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        sb.append(random.nextInt(9) + 1);

        for (int i = 1; i < 22; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public void linkAccountToUser(Account account, User user, int idUser) {
        try {
            String sql = "INSERT INTO account(name, balance, iban, type, userId) values(?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, account.getName());
            stmt.setString(2, account.getBalance());
            stmt.setString(3, account.getIBAN());
            stmt.setString(4, account.getType());
            stmt.setString(5, Integer.toString(idUser));
            stmt.execute();

            sql = "Select max(accountId) from account";
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String maxId = rs.getString(1);

            if (account.getType().equals("Savings")) {
                sql = "INSERT INTO savingsaccount(accountId, interestRate, commitmentPeriod) values(?,?,?)";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, maxId);
                stmt.setString(2, ((SavingsAccount) account).getInterestRate());
                stmt.setInt(3, ((SavingsAccount) account).getCommitmentPeriod());

            } else {
                sql = "INSERT INTO transactionsaccount(accountId, monthlyFee) values(?,?)";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, maxId);
                stmt.setString(2, (Double.toString(((TransactionsAccount)account).getMonthlyFee())));
            }
            stmt.execute();
            stmt.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


//    public void transferBetweenAccounts(TransactionsAccount fromAccount, Account toAccount, String amount) {
//        try {
//            double transferAmount = Double.parseDouble(amount);
//            double fromBalance = Double.parseDouble(fromAccount.getBalance());
//
//            if (transferAmount <= 0) {
//                System.out.println("Transfer amount must be positive.");
//                return;
//            }
//
//            if (transferAmount > fromBalance) {
//                System.out.println("Insufficient funds for transfer.");
//                return;
//            }
//
//            fromAccount.withdraw(amount);
//
//
//            toAccount.deposit(amount);
//
//            System.out.println("Transfer of " + amount + " completed successfully.");
//        } catch (NumberFormatException e) {
//            System.out.println("Invalid amount format. Please enter a valid number.");
//        }
//    }

    public void applyInterestToAllSavingsAccounts() {
        for (Account account : accounts) {
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).calculateInterest();
                System.out.println("Interest applied to " + account.getName());
            }
        }
    }

    public void applyMonthlyFeesToAllTransactionAccounts() {
        for (Account account : accounts) {
            if (account instanceof TransactionsAccount) {
                ((TransactionsAccount) account).applyMonthlyFee();
            }
        }
    }


    public Account findAccountByName(String accountName) {
        for (Account account : accounts) {
            if (account.getName().equals(accountName)) {
                return account;
            }
        }
        return null;
    }
}