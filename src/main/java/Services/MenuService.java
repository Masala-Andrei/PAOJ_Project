package Services;

import Connection.DBConnector;
import Models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuService extends DBConnector {
    private UserService userService;
    private AccountService accountService;
    private Scanner scanner;
    private User currentUser;
    private int idUser;

    public MenuService(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
    }

    public void start() throws SQLException {
        mainMenu();
    }

    private void mainMenu() throws SQLException {
        boolean exit = false;
        DBConnector con = new DBConnector();
        System.out.println(con.connect());

        while (!exit) {
            System.out.println("\n===== BANKING SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register New User");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = getIntInput();

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
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void login() {
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

            int choice = getIntInput();

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
        int userId = getIntInput();
        userService.removeUserAdmin(userId);
    }

    private void displayAllAccounts() {
        System.out.println("\n===== ALL ACCOUNTS =====");
        for (Account account : accountService.getAllAccounts()) {
            System.out.println(account.getAccountInfo());
            System.out.println("------------------------");
        }
    }

    private void customerMenu() {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("1. View My Accounts");
            System.out.println("2. Create New Account");
            System.out.println("3. Transfer Between Accounts");
            System.out.println("4. Transfer to other Recipient");
            System.out.println("5. Show Personal Information");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewMyAccounts();
                    break;
                case 2:
                    createNewAccount();
                    break;
                case 3:
                    transferBetweenAccounts();
                    break;
                case 4:
                    transferToDifferentUser();
                    break;
                case 5:
                    showPersonaInfo();
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

    private void viewMyAccounts() {
        System.out.println("\n===== MY ACCOUNTS =====");
        if (currentUser.getAccounts().isEmpty()) {
            System.out.println("You don't have any accounts yet.");
            return;
        }

        for (Account account : currentUser.getAccounts()) {
            System.out.println(account.getAccountInfo());
            System.out.println("------------------------");
        }
    }

    private void createNewAccount() {
        System.out.println("\n===== CREATE NEW ACCOUNT =====");
        System.out.println("Select account type:");
        System.out.println("1. Transaction Account");
        System.out.println("2. Savings Account");
        System.out.print("Your choice: ");

        int choice = getIntInput();

        System.out.print("Account Name: ");
        String name = scanner.nextLine();

        Account newAccount;
        String IBAN = AccountService.generateRandomIBAN();

        if (choice == 1) {
            double monthlyFee = 5.0;

            newAccount = new TransactionsAccount("Transactions", name, "0.0", monthlyFee, IBAN);
        } else if (choice == 2) {
            System.out.println("Select commitment period: 6 / 12 / 24 months");
            int commitmentPeriod = scanner.nextInt();
            while (commitmentPeriod != 6 && commitmentPeriod != 12 && commitmentPeriod != 24) {
                System.out.println("Invalid commitment period. Please try again.");
                commitmentPeriod = scanner.nextInt();
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
        System.out.println("Account created successfully!");
    }

    private void transferBetweenAccounts() {
        if (currentUser.getAccounts().size() < 2) {
            System.out.println("You need at least two accounts to make a transfer.");
            return;
        }

        System.out.println("\n===== TRANSFER BETWEEN ACCOUNTS =====");
        Account fromAccount = selectAccount("transfer from");
        if (fromAccount == null) return;

        Account toAccount = selectAccount("transfer to");
        if (toAccount == null) return;

        while (fromAccount == toAccount) {
            System.out.println("Cannot transfer to the same account.");
            fromAccount = selectAccount("transfer from");
            toAccount = selectAccount("transfer to");
        }

        System.out.print("Enter amount to transfer: ");
        String amount = scanner.nextLine();

        if (fromAccount instanceof TransactionsAccount) {
            ((TransactionsAccount) fromAccount).transfer((TransactionsAccount) toAccount, amount);
        } else {
            System.out.println("Transfers can only be initiated from Transaction accounts.");
        }
    }


    //to expand
//    private void requestCreditCard() {
//        System.out.println("In order to have a Credit card, you have to create a special Credit Account. Would you like to proceed?");
//    }
//
//    private void transferToExternalCard() {
//        System.out.println("\n===== TRANSFER TO RECIPIENT =====");
//
//        TransactionsAccount fromAccount = null;
//        boolean foundTransactionAccount = false;
//
//        for (Account account : currentUser.getAccounts()) {
//            if (account instanceof TransactionsAccount) {
//                foundTransactionAccount = true;
//                break;
//            }
//        }
//
//        if (!foundTransactionAccount) {
//            System.out.println("You need a Transaction Account to make external transfers.");
//            return;
//        }
//
//        Account selectedAccount = selectAccount("transfer from");
//        if (selectedAccount == null) return;
//
//        while (!(selectedAccount instanceof TransactionsAccount)) {
//            System.out.println("External transfers can only be initiated from Transaction accounts.");
//            selectedAccount = selectAccount("transfer to");
//        }
//
//        fromAccount = (TransactionsAccount) selectedAccount;
//
//        System.out.print("Enter recipient's card number (16 digits): ");
//        String recipientCardNumber = scanner.nextLine().replaceAll("\\s", ""); // Scap de spatii
//
//        if (recipientCardNumber.length() != 16) {
//            System.out.println("Invalid card number. Card number must be 16 digits.");
//            return;
//        }
//
//        System.out.print("Enter amount to transfer: ");
//        String amount = scanner.nextLine();
//
//        try {
//            double transferAmount = Double.parseDouble(amount);
//            if (transferAmount <= 0) {
//                System.out.println("Transfer amount must be positive.");
//                return;
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Invalid amount format.");
//            return;
//        }
//
//        // Get transfer description
//        System.out.print("Enter transfer description (optional): ");
//        String description = scanner.nextLine();
//        if (description.trim().isEmpty()) {
//            description = "External transfer";
//        }
//
//        // Perform the transfer
//        boolean success = accountService.transferToExternalCard(fromAccount, recipientCardNumber, amount, description, cardService);
//
//        if (success) {
//            System.out.println("External transfer completed successfully!");
//        } else {
//            System.out.println("External transfer failed. Please check the details and try again.");
//        }
//    }


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
            int choice = getIntInput();
            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    modification = scanner.nextLine();
                    currentUser.setName(modification);
                    System.out.println("Information updated successfully!");
                    sql = "UPDATE user SET name = ?";
                    break;
                case 2:
                    System.out.print("Enter new email: ");
                    modification = scanner.nextLine();
                    currentUser.setEmail(modification);
                    System.out.println("Information updated successfully!");
                    sql = "UPDATE user SET email = ?";
                    break;
                case 3:
                    System.out.print("Enter new phone number: ");
                    modification = scanner.nextLine();
                    currentUser.setPhoneNumber(modification);
                    System.out.println("Information updated successfully!");
                    sql = "UPDATE user SET phoneNumber = ?";
                    break;
                case 4:
                    System.out.print("Enter new password: ");
                    modification = scanner.nextLine();
                    currentUser.setPassword(modification);
                    System.out.println("Information updated successfully!");
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

    private Account selectAccount(String action) {
        System.out.println("\nSelect an account to " + action + ":");

        if (currentUser.getAccounts().isEmpty()) {
            System.out.println("You don't have any accounts yet.");
            return null;
        }

        for (int i = 0; i < currentUser.getAccounts().size(); i++) {
            Account account = currentUser.getAccounts().get(i);
//            System.out.println((i + 1) + ". " + account.getName() + " (" + account.getType() + ") - Balance: " + account.getBalance());
        }

        System.out.print("Your choice (number): ");
        int accountIndex = getIntInput();

        while (accountIndex < 1 || accountIndex > currentUser.getAccounts().size()) {
            System.out.println("Invalid selection. Try again");
            accountIndex = getIntInput();
        }

        return currentUser.getAccounts().get(accountIndex - 1);
    }

    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void transferToDifferentUser(Account sender, Account receiver){

    }
}