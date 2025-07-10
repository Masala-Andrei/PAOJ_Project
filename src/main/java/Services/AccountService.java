package Services;

import Connection.DBConnector;
import Models.Account;
import Models.SavingsAccount;
import Models.TransactionsAccount;
import Models.User;

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
        accounts.clear();
        try{
            String sql = "SELECT * FROM account";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public void removeAccount(Account account, int accId) {
        accounts.remove(account);
        try{
            String sql = "DELETE FROM account where accountId = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, accId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
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
                stmt.setString(2, (Double.toString(((TransactionsAccount) account).getMonthlyFee())));
            }
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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


    public TransactionsAccount getAccountByIban(String IBAN) {
        try {
            String sql = "SELECT account.name, account.balance, account.iban,account.type, transactionsaccount.monthlyFee from account inner join " +
                    "transactionsaccount on account.accountId = transactionsaccount.accountId " +
                    "where account.iban = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, IBAN);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return new TransactionsAccount(
                    rs.getString("account.type"),
                    rs.getString("account.name"),
                    rs.getString("account.balance"),
                    rs.getDouble("transactionsaccount.monthlyFee"),
                    rs.getString("account.iban")
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBalance(Account account) {
        try {
            String sql = "UPDATE account SET balance = ? WHERE IBAN = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, account.getBalance());
            stmt.setString(2, account.getIBAN());
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRecipient(int sender, int receiver) {
        try {
            String sql = "SELECT * FROM recipientBook where userId = ? and recipientId = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, Integer.toString(sender));
            stmt.setString(2, Integer.toString(receiver));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return;
            }

            sql = "INSERT INTO recipientBook (userId, recipientid) values(?,?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, Integer.toString(sender));
            stmt.setString(2, Integer.toString(receiver));
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getRecipients(int userId) {
        ArrayList<String> recipients = new ArrayList<>();
        try {
            String sql = "SELECT account.iban, recipient_user.name FROM recipientbook INNER JOIN account ON recipientbook.recipientId = account.accountId " +
            "INNER JOIN user AS recipient_user ON account.userId = recipient_user.id WHERE recipientbook.userId = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, Integer.toString(userId));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                recipients.add(rs.getString("iban"));
                recipients.add(rs.getString("name"));
            }
            stmt.close();
            return recipients;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}