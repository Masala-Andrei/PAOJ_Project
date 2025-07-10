package Services;

import Connection.DBConnector;
import Models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class MenuService extends DBConnector {
    private UserService userService;
    private AccountService accountService;
    private Scanner scanner;
    private User currentUser;
    private int idUser;
    private TransactionService transactionService;
    private AuditService auditService;

    public MenuService(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = new TransactionService();
        this.auditService = new AuditService();
        this.scanner = new Scanner(System.in);
    }

    public void start() throws SQLException {
        mainMenu();
    }

    private void mainMenu() throws SQLException {
        boolean exit = false;
        DBConnector con = new DBConnector();

        while (!exit) {
            System.out.println("\n===== BANKING SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register New User");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    registerNewUser();
                    break;
                case 3:
                    exit = true;
                    System.out.println("Thank you for using our Banking System. Goodbye!");
                    auditService.logAction("User exited");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void login() throws SQLException {
        System.out.println("\n===== LOGIN =====");
        System.out.print("Email / Name: ");
        String emailName = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userService.authenticateUser(emailName, password)) {
            currentUser = userService.findUserByEmailOrName(emailName);
            System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");

            try {
                String sql = "SELECT * from user where name = ? or email = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, emailName);
                stmt.setString(2, emailName);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                idUser = rs.getInt("id");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (currentUser.getType().equals("ADMIN")) {
                adminMenu();
            } else {
                auditService.logAction("Login user " + currentUser.getName());
                customerMenu();
            }
        } else {
            System.out.println("Invalid email, username or password. Please try again.");
        }

    }

    private void registerNewUser() throws SQLException {
        System.out.println("\n===== REGISTER NEW USER =====");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User newUser = new User(name, email, phone, password, "CUSTOMER", new ArrayList<Account>());
        userService.addUser(newUser);
        auditService.logAction("Registered new user " + newUser.getName());
        System.out.println("Registration successful! You can now login with your credentials.");
    }

    private void adminMenu() {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All Users");
            System.out.println("2. Remove User");
            System.out.println("3. View All Accounts");
            System.out.println("4. Apply Interest to Savings Accounts");
            System.out.println("5. Apply Monthly Fees");
            System.out.println("6. Reset Monthly Withdrawal Counters");
            System.out.println("7. Logout");
            System.out.print("Select an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    userService.displayUsers();
                    break;
                case 2:
                    removeUser();
                    break;
                case 3:
                    displayAllAccounts();
                    break;
                case 4:
                    accountService.applyInterestToAllSavingsAccounts();
                    break;
                case 5:
                    accountService.applyMonthlyFeesToAllTransactionAccounts();
                    break;
                case 6:
                    logout = true;
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void removeUser() {
        System.out.print("Enter the ID of the user to remove: ");
        int userId = Integer.parseInt(scanner.nextLine());
        userService.removeUserAdmin(userId);
    }

    private void displayAllAccounts() {
        System.out.println("\n===== ALL ACCOUNTS =====");
        for (Account account : accountService.getAllAccounts()) {
            System.out.println(account);
            System.out.println("------------------------");
        }
    }

    private void customerMenu() throws SQLException {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("1. View My Accounts");
            System.out.println("2. Create New Account");
            System.out.println("3. Transfer Money");
            System.out.println("4. Show Personal Information");
            System.out.println("5. Show Previous Transactions For An Account");
            System.out.println("6. Close an existing Banking Account");
            System.out.println("7. Close your Account");
            System.out.println("8. Logout");
            System.out.print("Select an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewMyAccounts();
                    break;
                case 2:
                    createNewAccount();
                    break;
                case 3:
                    transferMoney();
                    break;
                case 4:
                    showPersonaInfo();
                    break;
                case 5:
                    showTransactions();
                    break;
                case 6:
                    closeABankingAccount();
                    break;
                case 7:
                    closeYourAccount();
                    logout = true;
                    break;
                case 8:
                    logout = true;
                    auditService.logAction("Logout user " + currentUser.getName());
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewMyAccounts() {
        System.out.println("\n===== MY ACCOUNTS =====");
        if (currentUser.getAccounts().isEmpty()) {
            System.out.println("You don't have any accounts yet.");
            return;
        }

        for (Account account : currentUser.getAccounts()) {
            System.out.println(account);
            System.out.println("------------------------");
        }
        auditService.logAction("Displayed user's " + currentUser.getName() + " accounts");
    }

    private void createNewAccount() {
        System.out.println("\n===== CREATE NEW ACCOUNT =====");
        System.out.println("Select account type:");
        System.out.println("1. Transaction Account");
        System.out.println("2. Savings Account");
        System.out.print("Your choice: ");

        int choice = Integer.parseInt(scanner.nextLine());

        System.out.print("Account Name: ");
        String name = scanner.nextLine();

        Account newAccount;
        String IBAN = AccountService.generateRandomIBAN();

        if (choice == 1) {
            double monthlyFee = 5.0;

            newAccount = new TransactionsAccount("Transactions", name, "0.0", monthlyFee, IBAN);
        } else if (choice == 2) {
            System.out.println("Select commitment period: 6 / 12 / 24 months");
            int commitmentPeriod = Integer.parseInt(scanner.nextLine());
            while (commitmentPeriod != 6 && commitmentPeriod != 12 && commitmentPeriod != 24) {
                System.out.println("Invalid commitment period. Please try again.");
                commitmentPeriod = Integer.parseInt(scanner.nextLine());
            }
            String interestRate = switch (commitmentPeriod) {
                case 6 -> "1.3";
                case 12 -> "1.9";
                case 24 -> "2.5";
                default -> "";
            };
            newAccount = new SavingsAccount("Savings", name, "0.0", interestRate, commitmentPeriod, IBAN);
        } else {
            System.out.println("Invalid choice. Account creation canceled.");
            return;
        }

        accountService.linkAccountToUser(newAccount, currentUser, idUser);
        currentUser.addAccount(newAccount);
        auditService.logAction("Created new account " + newAccount.getName() + " for user " + currentUser.getName());
        System.out.println("Account created successfully!");
    }

    private void showPersonaInfo() {
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        while (!password.equals(currentUser.getPassword())) {
            System.out.println("Invalid password. Retry!");
            password = scanner.nextLine();
        }
        System.out.println("\n===== PERSONAL INFORMATION =====");
        System.out.println("Name: " + currentUser.getName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Phone Number: " + currentUser.getPhoneNumber());
        System.out.println("Password: " + currentUser.getPassword());
        System.out.println("Would you like to make any changes? (y/n)");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            updatePersonalInfo();
        } else if (!answer.equalsIgnoreCase("n")) {
            System.out.println("Invalid answer.");
        }
        auditService.logAction("Showed information for user " + currentUser.getName());
    }

    private void updatePersonalInfo() {
        boolean temp = true;
        String sql, modification = null;
        while (temp) {
            sql = "";
            System.out.println("\n===== UPDATE PERSONAL INFORMATION =====");
            System.out.println("What would you like to update?");
            System.out.println("1. Name: " + currentUser.getName());
            System.out.println("2. Email: " + currentUser.getEmail());
            System.out.println("3. Phone Number: " + currentUser.getPhoneNumber());
            System.out.println("4. Password: " + currentUser.getPassword());
            System.out.print("Your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    modification = scanner.nextLine();
                    currentUser.setName(modification);
                    System.out.println("Information updated successfully!");
                    sql = "UPDATE user SET name = ?";
                    auditService.logAction("Updated user's name to " + modification);
                    break;
                case 2:
                    System.out.print("Enter new email: ");
                    modification = scanner.nextLine();
                    currentUser.setEmail(modification);
                    System.out.println("Information updated successfully!");
                    auditService.logAction("Updated user's email to " + modification);
                    sql = "UPDATE user SET email = ?";
                    break;
                case 3:
                    System.out.print("Enter new phone number: ");
                    modification = scanner.nextLine();
                    currentUser.setPhoneNumber(modification);
                    System.out.println("Information updated successfully!");
                    auditService.logAction("Updated user's phone number to " + modification);
                    sql = "UPDATE user SET phoneNumber = ?";
                    break;
                case 4:
                    System.out.print("Enter new password: ");
                    modification = scanner.nextLine();
                    currentUser.setPassword(modification);
                    System.out.println("Information updated successfully!");
                    auditService.logAction("Updated user's password to " + modification);
                    sql = "UPDATE user SET password = ?";
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
            if (!sql.equals("")) {
                try {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, modification);
                    stmt.executeUpdate();
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Would you like to make any more updates? (y/n)");
            String answer = scanner.nextLine();
            while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")) {
                System.out.println("Invalid choice. Please write y (yes) or n (no).");
                answer = scanner.nextLine();
            }
            if (answer.equalsIgnoreCase("n")) {
                temp = false;
            }
        }

    }


    private void transferMoney() {
        String senderIban, ammount, accountName;
        Account sender = null;

        System.out.println("\n===== TRANSFER TO OTHER ACCOUNT =====");
        for (Account account : currentUser.getAccounts()) {
            if (account instanceof TransactionsAccount) {
                System.out.println(account);
                System.out.println("------------------");
            }
        }
        System.out.println("From what account would you want to transfer? (Account name)");
        accountName = scanner.nextLine();
        boolean found = false;
        while (!found) {
            for (Account account : currentUser.getAccounts()) {
                if (account.getName().equals(accountName)) {
                    sender = account;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Account not found. Try Again!");
                accountName = scanner.nextLine();
            }
        }

        System.out.println("==== Choose from know recipients ====");
        ArrayList<String> info = accountService.getRecipients(idUser);
        int j = 1;
        for (int i = 0; i < info.size(); i = i + 2) {
            System.out.println(j + ") User name:" + info.get(i + 1));
            System.out.println("IBAN:" + info.get(i));
            System.out.println();
            j += 1;
        }
        System.out.println(j + ") New recipient");
        System.out.println();
        System.out.println((j + 1) + ") Abort");

        int choice = Integer.parseInt(scanner.nextLine());
        while (true) {
            if (choice == j) {
                System.out.println("Specify sender Iban: ");
                senderIban = scanner.nextLine();
                break;
            } else if (0 < choice && choice < j) {
                senderIban = info.get((choice - 1) * 2);
                break;
            } else if (choice == j + 1)
                return;
            System.out.println("Invalid option. Try again!");
        }


        Account receiver = accountService.getAccountByIban(senderIban);
        System.out.println("Select ammount: ");
        ammount = scanner.nextLine();
        while (Double.parseDouble(sender.getBalance()) - Double.parseDouble(ammount) < 0) {
            System.out.println("Not enough balance, try again");
            ammount = scanner.nextLine();
        }

        String description = "";
        System.out.println("Add a description to the transaction.");
        description = scanner.nextLine();

        sender.setBalance(Double.toString(Double.parseDouble(sender.getBalance()) - Double.parseDouble(ammount)));
        receiver.setBalance(Double.toString(Double.parseDouble(receiver.getBalance()) + Double.parseDouble(ammount)));
        accountService.updateBalance(receiver);
        accountService.updateBalance(sender);
        int receiverId = 0, senderId = 0;
        try {
            String sql = "SELECT accountId from account where iban = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, receiver.getIBAN());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            receiverId = rs.getInt("accountid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountService.addRecipient(idUser, receiverId);
        auditService.logAction("User " + currentUser.getName() + " transferred from account " + sender.getIBAN() + ammount + " to "
                + receiver.getIBAN() + " and added transaction to database");


        try {
            String sql = "SELECT accountId from account where iban = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, sender.getIBAN());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            senderId = rs.getInt("accountid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Adding the Transaction
        Transaction transaction = new Transaction(Integer.toString(senderId), Integer.toString(receiverId), ammount, description);
        transactionService.addTransaction(transaction);
    }

    private void showTransactions() {
        System.out.println("\n===== Show Transactions =====");
        System.out.println("\n===== ALL ACCOUNTS =====");
        for (Account account : currentUser.getAccounts()) {
            if (account instanceof TransactionsAccount) {
                System.out.println(account);
                System.out.println("-----------------");
            }
        }
        System.out.println("Choose an account");
        String accountName = scanner.nextLine();
        Account account = null;
        boolean found = false;
        while (!found) {
            for (Account a : currentUser.getAccounts()) {
                if (a.getName().equals(accountName)) {
                    account = a;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Account not found. Try Again!");
                accountName = scanner.nextLine();
            }
        }
        int accId = 0;
        try {
            String sql = "SELECT accountId from account where iban = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, account.getIBAN());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            accId = rs.getInt("accountid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<Transaction> transactions = transactionService.getTransactions(accId);
        for (Transaction transaction : transactions) {
            transaction.showDetailedTransaction(accId);
        }
        auditService.logAction("Displayed transactions for user " + currentUser.getName());
    }

    public void closeABankingAccount() {
        System.out.println("\n===== ALL ACCOUNTS =====");
        for (Account account : currentUser.getAccounts()) {
            if (account instanceof TransactionsAccount) {
                System.out.println(account);
                System.out.println("-----------------");
            }
        }
        System.out.println("Choose an account");
        String accountName = scanner.nextLine();
        Account account = null;
        boolean found = false;
        while (!found) {
            for (Account a : currentUser.getAccounts()) {
                if (a.getName().equals(accountName)) {
                    account = a;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Account not found. Try Again!");
                accountName = scanner.nextLine();
            }
        }
        int accId = 0;
        try {
            String sql = "SELECT accountId from account where iban = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, account.getIBAN());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            accId = rs.getInt("accountid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        auditService.logAction("Removed account " + account.getName() + " from user " + currentUser.getName());
        accountService.removeAccount(account, accId);
        currentUser.removeAccount(account);
        auditService.logAction("");
    }

    public void closeYourAccount() throws SQLException {
        System.out.println("Are you sure you want to delete your account? (All your information will be lost)");
        System.out.println("Type Yes to continue or No to abort");
        String answer = scanner.nextLine();
        boolean temp = true;
        while (temp) {
            switch (answer) {
                case "Yes":
                    int accId = 0;
                    try{
                        String sql = "Select id FROM user where name = ?";
                        PreparedStatement stmt = connection.prepareStatement(sql);
                        stmt.setString(1, currentUser.getName());
                        ResultSet rs = stmt.executeQuery();
                        rs.next();
                        accId = rs.getInt("id");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    auditService.logAction("Account " + currentUser.getName() + " deleted");
                    userService.deleteUser(currentUser, accId);
                    temp = false;
                    break;
                case "No":
                    temp = false;
                    break;
                default:
                    System.out.println("Invalid answer. Try Again!");
                    answer = scanner.nextLine();
            }
        }
    }
}