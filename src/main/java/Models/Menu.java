package Models;

import Services.*;

public class Menu {
    private UserService userService;
    private AccountService accountService;
    private CardService cardService;
    private MenuService menuService;

    public Menu() {
        initialize();
    }

    private void initialize() {
        userService = new UserService();
        accountService = new AccountService();
        cardService = new CardService();
        menuService = new MenuService(userService, accountService, cardService);
    }

    public void start() {
        createSampleData();
        menuService.start();
    }

    private void createSampleData() {
        User admin = new User("Admin User", "admin@bank.com", "123-456-7890", "admin123", "ADMIN");
        User customer1 = new User("John Doe", "john@example.com", "555-123-4567", "password123", "CUSTOMER");
        User customer2 = new User("Jane Smith", "jane@example.com", "555-765-4321", "password456", "CUSTOMER");

        userService.addUser(admin);
        userService.addUser(customer1);
        userService.addUser(customer2);

        TransactionsAccount johnChecking = accountService.createTransactionsAccount("John's Checking", "1500.00", 5.0, 30);
        SavingsAccount johnSavings = accountService.createSavingsAccount("John's Savings", "5000.00", "2.5", 12);

        TransactionsAccount janeChecking = accountService.createTransactionsAccount("Jane's Checking", "2500.00", 5.0, 30);
        SavingsAccount janeSavings = accountService.createSavingsAccount("Jane's Savings", "10000.00", "3.0", 24);

        accountService.linkAccountToUser(johnChecking, customer1);
        accountService.linkAccountToUser(johnSavings, customer1);
        accountService.linkAccountToUser(janeChecking, customer2);
        accountService.linkAccountToUser(janeSavings, customer2);

        DebitCard johnDebitCard = cardService.issueDebitCard("John Doe", johnChecking);
        CreditCard johnCreditCard = cardService.issueCreditCard("John Doe", johnChecking, 2000.0, 18.5);

        DebitCard janeDebitCard = cardService.issueDebitCard("Jane Smith", janeChecking);
        CreditCard janeCreditCard = cardService.issueCreditCard("Jane Smith", janeChecking, 5000.0, 15.9);

    }
}