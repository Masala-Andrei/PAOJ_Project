package Services;

import Models.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuService extends Debug {
    private UserService userService;
    private AccountService accountService;
    private CardService cardService;
    private Scanner scanner;
    private User currentUser;

    public MenuService(UserService userService, AccountService accountService, CardService cardService) {
        this.userService = userService;
        this.accountService = accountService;
        this.cardService = cardService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        mainMenu();
    }

    private void mainMenu() {
        boolean exit = false;

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
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userService.authenticateUser(email, password)) {
            currentUser = userService.findUserByEmailOrName(email);
            System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");

            if (currentUser.getType().equals("ADMIN")) {
                adminMenu();
            } else {
                customerMenu();
            }
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    private void registerNewUser() {
        System.out.println("\n===== REGISTER NEW USER =====");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User newUser = new User(name, email, phone, password, "CUSTOMER");
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
                    accountService.resetMonthlyWithdrawalCounters();
                    break;
                case 7:
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
            System.out.println("3. Make a Deposit");
            System.out.println("4. Make a Withdrawal");
            System.out.println("5. Transfer Between Accounts");
            System.out.println("6. Request a Card");
            System.out.println("7. View My Cards");
            System.out.println("8. Make Credit Card Payment");
            System.out.println("9. Update Personal Information");
            System.out.println("10. Logout");
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
                    makeDeposit();
                    break;
                case 4:
                    makeWithdrawal();
                    break;
                case 5:
                    transferBetweenAccounts();
                    break;
                case 6:
                    requestCard();
                    break;
                case 7:
                    viewMyCards();
                    break;
                case 8:
                    makeCreditCardPayment();
                    break;
                case 9:
                    updatePersonalInfo();
                    break;
                case 10:
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
        System.out.print("Choose a card from which to make the deposit: ");

        System.out.print("Initial Deposit: ");
        String initialBalance = scanner.nextLine();
        // De verificat daca am destui bani pe card sa fac un account cu deposit

        Account newAccount;

        if (choice == 1) {
            double monthlyFee = 5.0;
            int withdrawalLimit = 30;
            newAccount = accountService.createTransactionsAccount(name, initialBalance, monthlyFee, withdrawalLimit);
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
            newAccount = accountService.createSavingsAccount(name, initialBalance, interestRate, commitmentPeriod);
        } else {
            System.out.println("Invalid choice. Account creation canceled.");
            return;
        }

        accountService.linkAccountToUser(newAccount, currentUser);
        System.out.println("Account created successfully!");
    }

    private void makeDeposit() {
        Account account = selectAccount("deposit to");
        if (account == null) return;

        System.out.print("Enter amount to deposit: ");
        String amount = scanner.nextLine();

        try {
            account.deposit(amount);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void makeWithdrawal() {
        Account account = selectAccount("withdraw from");
        if (account == null) return;

        System.out.print("Enter amount to withdraw: ");
        String amount = scanner.nextLine();

        try {
            account.withdraw(amount);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
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

        if (fromAccount == toAccount) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        System.out.print("Enter amount to transfer: ");
        String amount = scanner.nextLine();

        if (fromAccount instanceof TransactionsAccount) {
            ((TransactionsAccount) fromAccount).transfer((TransactionsAccount) toAccount, amount);
        } else {
            System.out.println("Transfers can only be initiated from Transaction accounts.");
        }
    }

    private void requestCard() {
        Account account = selectAccount("link to the card");
        if (account == null) return;

        System.out.println("\n===== CARD REQUEST =====");
        System.out.println("Select card type:");
        System.out.println("1. Debit Card");
        System.out.println("2. Credit Card");
        System.out.print("Your choice: ");

        int choice = getIntInput();

        if (choice == 1) {
            DebitCard card = cardService.issueDebitCard(currentUser.getName(), account);
            if (card != null) {
                System.out.println("Debit card issued successfully!");
                System.out.println("Card Number: " + card.getMaskedCardNumber());
            }
        } else if (choice == 2) {
            System.out.print("Enter desired credit limit: ");
            double creditLimit = getDoubleInput();
            CreditCard card = cardService.issueCreditCard(currentUser.getName(), account, creditLimit, 18.5);
            if (card != null) {
                System.out.println("Credit card issued successfully!");
                System.out.println("Card Number: " + card.getMaskedCardNumber());
            }
        } else {
            System.out.println("Invalid choice. Card request canceled.");
        }
    }

    private void viewMyCards() {
        System.out.println("\n===== MY CARDS =====");
        boolean foundCards = false;

        for (Account account : currentUser.getAccounts()) {
            for (Card card : cardService.getCardsByAccountId(Integer.toString(account.getIdAccount()))) {
                System.out.println(card.toString());
                System.out.println("------------------------");
                foundCards = true;
            }
        }

        if (!foundCards) {
            System.out.println("You don't have any cards yet.");
        }
    }

    private void makeCreditCardPayment() {
        System.out.println("\n===== CREDIT CARD PAYMENT =====");
        boolean foundCreditCards = false;
        ArrayList<CreditCard> creditCards = new ArrayList<>();

        for (Account account : currentUser.getAccounts()) {
            for (Card card : cardService.getCardsByAccountId(Integer.toString(account.getIdAccount()))) {
                if (card instanceof CreditCard) {
                    creditCards.add((CreditCard) card);
                    System.out.println(creditCards.size() + ". " + card.getMaskedCardNumber() +
                            " (Current Balance: " + ((CreditCard) card).getCurrentBalance() + ")");
                    foundCreditCards = true;
                }
            }
        }

        if (!foundCreditCards) {
            System.out.println("You don't have any credit cards.");
            return;
        }

        System.out.print("Select a credit card (number): ");
        int cardIndex = getIntInput();

        if (cardIndex < 1 || cardIndex > creditCards.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        CreditCard selectedCard = creditCards.get(cardIndex - 1);
        System.out.print("Enter payment amount: ");
        double amount = getDoubleInput();

        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }

        try {
            cardService.processCardPayment(selectedCard, amount);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updatePersonalInfo() {
        System.out.println("\n===== UPDATE PERSONAL INFORMATION =====");
        System.out.println("What would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Email");
        System.out.println("3. Phone Number");
        System.out.println("4. Password");
        System.out.print("Your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                System.out.print("Enter new name: ");
                String name = scanner.nextLine();
                currentUser.setName(name);
                break;
            case 2:
                System.out.print("Enter new email: ");
                String email = scanner.nextLine();
                currentUser.setEmail(email);
                break;
            case 3:
                System.out.print("Enter new phone number: ");
                String phone = scanner.nextLine();
                currentUser.setPhoneNumber(phone);
                break;
            case 4:
                System.out.print("Enter new password: ");
                String password = scanner.nextLine();
                currentUser.setPassword(password);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("Information updated successfully!");
    }

    private Account selectAccount(String action) {
        System.out.println("\nSelect an account to " + action + ":");

        if (currentUser.getAccounts().isEmpty()) {
            System.out.println("You don't have any accounts yet.");
            return null;
        }

        for (int i = 0; i < currentUser.getAccounts().size(); i++) {
            Account account = currentUser.getAccounts().get(i);
            System.out.println((i + 1) + ". " + account.getName() + " (" + account.getType() + ") - Balance: " + account.getBalance());
        }

        System.out.print("Your choice (number): ");
        int accountIndex = getIntInput();

        if (accountIndex < 1 || accountIndex > currentUser.getAccounts().size()) {
            System.out.println("Invalid selection.");
            return null;
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
}