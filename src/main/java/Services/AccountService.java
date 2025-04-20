package Services;

import Models.Account;
import Models.SavingsAccount;
import Models.TransactionsAccount;
import Models.User;

import java.security.Provider;
import java.util.ArrayList;

public class AccountService extends Debug {
    private ArrayList<Account> accounts;

    public AccountService() {
        this.accounts = new ArrayList<>();
    }

    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
        if (debug)
            System.out.println("Account " + account.getName() + " added to the system.");
    }

    public void removeAccount(Account account) {
        if (accounts.contains(account)) {
            accounts.remove(account);
            System.out.println("Account " + account.getName() + " removed from the system.");
        } else {
            System.out.println("Account not found in the system.");
        }
    }

    public SavingsAccount createSavingsAccount(String name, String initialBalance, String interestRate, int commitmentPeriod) {
        SavingsAccount account = new SavingsAccount("Savings", name, initialBalance, interestRate, commitmentPeriod);
        addAccount(account);
        return account;
    }

    public TransactionsAccount createTransactionsAccount(String name, String initialBalance, double monthlyFee, int withdrawalLimit) {
        TransactionsAccount account = new TransactionsAccount("Transaction", name, initialBalance, monthlyFee, withdrawalLimit);
        addAccount(account);
        return account;
    }

    public void linkAccountToUser(Account account, User user) {
        user.addAccount(account);
    }

    public void transferBetweenAccounts(TransactionsAccount fromAccount, Account toAccount, String amount) {
        try {
            double transferAmount = Double.parseDouble(amount);
            double fromBalance = Double.parseDouble(fromAccount.getBalance());

            if (transferAmount <= 0) {
                System.out.println("Transfer amount must be positive.");
                return;
            }

            if (transferAmount > fromBalance) {
                System.out.println("Insufficient funds for transfer.");
                return;
            }

            fromAccount.withdraw(amount);


            toAccount.deposit(amount);

            System.out.println("Transfer of " + amount + " completed successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please enter a valid number.");
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

    public void resetMonthlyWithdrawalCounters() {
        for (Account account : accounts) {
            if (account instanceof TransactionsAccount) {
                ((TransactionsAccount) account).resetWithdrawalsCount();
            }
        }
        System.out.println("Monthly withdrawal counters reset for all transaction accounts.");
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