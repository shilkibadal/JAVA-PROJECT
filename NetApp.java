import javax.swing.*;                    // GUI components
import javax.swing.table.DefaultTableModel; // Table data model
import java.awt.*;                       // Layout managers and graphics
import java.awt.event.*;                 // Event handling
import java.io.*;                        // Input/Output operations
import java.net.*;                       // Networking (not fully implemented)
import java.util.*;                      // Utility classes
import java.util.Date;                   // Date handling
import java.util.List;                   // List interface
import java.util.ArrayList;              // List implementation
import java.util.HashMap;                // Map implementation
import java.util.Map;                    // Map interface

// ===== OBJECT-ORIENTED DESIGN =====

abstract class BankAccount {
    protected String accountNumber;
    protected String accountHolder;
    protected double balance;
    protected String accountType;
    protected Date createdDate;
    protected String email;
    protected String phone;
    protected String address;
    protected String dob;
    protected String gender;

    public BankAccount(String accountNumber, String accountHolder, double initialBalance, String accountType) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.createdDate = new Date();
    }

    // Enhanced constructor with additional fields
    public BankAccount(String accountNumber, String accountHolder, double initialBalance, String accountType, 
                      String email, String phone, String address, String dob, String gender) {
        this(accountNumber, accountHolder, initialBalance, accountType);
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
    }

    // Abstract methods
    public abstract double calculateInterest();
    public abstract double getMinimumBalance();

    // Concrete methods
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= getMinimumBalance()) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(BankAccount toAccount, double amount) {
        if (this.withdraw(amount)) {
            toAccount.deposit(amount);
            return true;
        }
        return false;
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public Date getCreatedDate() { return createdDate; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getDob() { return dob; }
    public String getGender() { return gender; }

    @Override
    public String toString() {
        return String.format("Account[%s, Holder: %s, Balance: Rs%.2f, Type: %s]", 
                           accountNumber, accountHolder, balance, accountType);
    }
}

class SavingsAccount extends BankAccount {
    private static final double INTEREST_RATE = 0.02;
    private static final double MIN_BALANCE = 100.0;

    public SavingsAccount(String accountNumber, String accountHolder, double initialBalance) {
        super(accountNumber, accountHolder, initialBalance, "Savings");
    }

    public SavingsAccount(String accountNumber, String accountHolder, double initialBalance,
                         String email, String phone, String address, String dob, String gender) {
        super(accountNumber, accountHolder, initialBalance, "Savings", email, phone, address, dob, gender);
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }

    @Override
    public double getMinimumBalance() {
        return MIN_BALANCE;
    }
}

class CurrentAccount extends BankAccount {
    private static final double INTEREST_RATE = 0.01;
    private static final double MIN_BALANCE = 5000.0;

    public CurrentAccount(String accountNumber, String accountHolder, double initialBalance) {
        super(accountNumber, accountHolder, initialBalance, "Current");
    }

    public CurrentAccount(String accountNumber, String accountHolder, double initialBalance,
                         String email, String phone, String address, String dob, String gender) {
        super(accountNumber, accountHolder, initialBalance, "Current", email, phone, address, dob, gender);
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }

    @Override
    public double getMinimumBalance() {
        return MIN_BALANCE;
    }
}

class BankTransaction {
    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private Date timestamp;
    private String description;

    public BankTransaction(String accountNumber, String type, double amount, String description) {
        this.transactionId = UUID.randomUUID().toString().substring(0, 8);
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = new Date();
        this.description = description;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public Date getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
}

class BankUser {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private java.util.List<BankAccount> accounts;

    public BankUser(String userId, String username, String password, String email, String phone) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public BankAccount getAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public java.util.List<BankAccount> getAccounts() { return accounts; }
}

class BankingService {
    private static BankingService instance;
    private Map<String, BankUser> users;
    private Map<String, BankAccount> accounts;
    private java.util.List<BankTransaction> transactions;

    private BankingService() {
        users = new HashMap<>();
        accounts = new HashMap<>();
        transactions = new ArrayList<>();
        initializeSampleData();
    }

    public static synchronized BankingService getInstance() {
        if (instance == null) {
            instance = new BankingService();
        }
        return instance;
    }

    private void initializeSampleData() {
        // Create sample users and accounts with sufficient balance
        BankUser user1 = new BankUser("U001", "srisha", "password123", "sri@email.com", "1234567890");
        BankUser user2 = new BankUser("U002", "shilki", "password456", "shilki@email.com", "0987654321");

        // Create accounts with sufficient balance for testing transfers
        SavingsAccount acc1 = new SavingsAccount("ACC001", "srisha", 50000.0);
        CurrentAccount acc2 = new CurrentAccount("ACC002", "shilki", 100000.0);

        user1.addAccount(acc1);
        user2.addAccount(acc2);

        users.put(user1.getUserId(), user1);
        users.put(user2.getUserId(), user2);
        accounts.put(acc1.getAccountNumber(), acc1);
        accounts.put(acc2.getAccountNumber(), acc2);

        // Add some initial transactions for testing
        transactions.add(new BankTransaction("ACC001", "DEPOSIT", 50000.0, "Initial deposit"));
        transactions.add(new BankTransaction("ACC002", "DEPOSIT", 100000.0, "Initial deposit"));
    }

    public BankUser authenticateUser(String username, String password) {
        for (BankUser user : users.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // New method to register user
    public boolean registerUser(String username, String password, String email, String phone, 
                               BankAccount account) {
        // Check if username already exists
        for (BankUser user : users.values()) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }

        // Create new user
        String userId = "U" + String.format("%03d", users.size() + 1);
        BankUser newUser = new BankUser(userId, username, password, email, phone);
        newUser.addAccount(account);

        users.put(userId, newUser);
        accounts.put(account.getAccountNumber(), account);

        // Add initial deposit transaction of Rs 10,000
        transactions.add(new BankTransaction(account.getAccountNumber(), "DEPOSIT", 10000.0, "Account opening bonus"));

        return true;
    }

    public boolean transferMoney(String fromAccount, String toAccount, double amount, String description) {
        System.out.println("Attempting transfer: " + fromAccount + " -> " + toAccount + " Amount: " + amount);
        
        BankAccount fromAcc = accounts.get(fromAccount);
        BankAccount toAcc = accounts.get(toAccount);

        if (fromAcc == null) {
            System.out.println("From account not found: " + fromAccount);
            return false;
        }
        
        if (toAcc == null) {
            System.out.println("To account not found: " + toAccount);
            return false;
        }

        if (fromAcc.equals(toAcc)) {
            System.out.println("Cannot transfer to same account");
            return false;
        }

        System.out.println("From account balance: " + fromAcc.getBalance());
        System.out.println("To account balance: " + toAcc.getBalance());

        if (fromAcc.withdraw(amount)) {
            toAcc.deposit(amount);
            
            // Record transactions
            transactions.add(new BankTransaction(fromAccount, "DEBIT", amount, 
                "Transfer to " + toAccount + " - " + description));
            transactions.add(new BankTransaction(toAccount, "CREDIT", amount, 
                "Transfer from " + fromAccount + " - " + description));
            
            System.out.println("Transfer successful!");
            System.out.println("New from balance: " + fromAcc.getBalance());
            System.out.println("New to balance: " + toAcc.getBalance());
            return true;
        } else {
            System.out.println("Withdrawal failed - insufficient funds or below minimum balance");
            return false;
        }
    }

    public java.util.List<BankTransaction> getTransactionHistory(String accountNumber) {
        java.util.List<BankTransaction> accountTransactions = new ArrayList<>();
        for (BankTransaction t : transactions) {
            if (t.getAccountNumber().equals(accountNumber)) {
                accountTransactions.add(t);
            }
        }
        return accountTransactions;
    }

    public BankAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public java.util.List<BankAccount> getUserAccounts(String userId) {
        BankUser user = users.get(userId);
        return user != null ? user.getAccounts() : new ArrayList<>();
    }

    // Method to get all accounts for transfer
    public java.util.List<BankAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    // Method to get all accounts for debugging
    public void printAllAccounts() {
        System.out.println("=== ALL ACCOUNTS ===");
        for (BankAccount account : accounts.values()) {
            System.out.println(account.getAccountNumber() + " - " + account.getAccountHolder() + " - Balance: " + account.getBalance());
        }
    }
}

// Simple DatabaseService without SQLite
class DatabaseService {
    public DatabaseService() {
        System.out.println("Database service initialized (in-memory mode)");
    }

    public void saveTransaction(BankTransaction transaction) {
        System.out.println("Saving transaction: " + transaction.getDescription());
    }
}

// Simple BankServer without networking
class BankServer {
    private BankingService bankingService;
    private boolean running;

    public BankServer() {
        bankingService = BankingService.getInstance();
        running = true;
        System.out.println("Bank for Women Server simulation started");
    }

    public void startServer() {
        System.out.println("Server running in simulation mode...");
        // No actual networking, just simulation
    }

    public void stopServer() {
        running = false;
        System.out.println("Server stopped");
    }
}

// Registration Page
class RegistrationPage extends JFrame {
    private BankingService bankingService;
    private JPanel mainPanel;
    private JTextField usernameField, fullNameField, emailField, phoneField, addressField, dobField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> accountTypeCombo, genderCombo;
    private JButton registerButton, backButton;

    public RegistrationPage() {
        bankingService = BankingService.getInstance();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Bank for Women - New User Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 240, 245));

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(255, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Header
        JLabel headerLabel = new JLabel("WOMEN'S BANK - REGISTRATION", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(219, 112, 147));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(headerLabel, gbc);

        JLabel subtitleLabel = new JLabel("Empowering Women Through Banking", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(199, 21, 133));
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);

        gbc.gridwidth = 1;

        // Form fields
        String[] labels = {"Username:", "Password:", "Confirm Password:", "Full Name:", 
                          "Email:", "Phone:", "Address:", "Date of Birth:", "Account Type:", "Gender:"};
        JTextField[] fields = {usernameField = new JTextField(20), null, null, 
                              fullNameField = new JTextField(20), emailField = new JTextField(20),
                              phoneField = new JTextField(20), addressField = new JTextField(20),
                              dobField = new JTextField(20), null, null};

        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        accountTypeCombo = new JComboBox<>(new String[]{"Savings Account", "Current Account"});
        genderCombo = new JComboBox<>(new String[]{"Female", "Male", "Other", "Prefer not to say"});

        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i + 2;
            gbc.gridx = 0;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(new Color(75, 0, 130));
            mainPanel.add(label, gbc);

            gbc.gridx = 1;
            Component field;
            if (i == 1) field = passwordField;
            else if (i == 2) field = confirmPasswordField;
            else if (i == 8) field = accountTypeCombo;
            else if (i == 9) field = genderCombo;
            else field = fields[i];

            if (field instanceof JTextField) {
                ((JTextField) field).setBackground(new Color(255, 250, 250));
            } else if (field instanceof JComboBox) {
                ((JComboBox<?>) field).setBackground(Color.WHITE);
            }
            mainPanel.add(field, gbc);
        }

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(255, 240, 245));

        registerButton = new JButton("CREATE ACCOUNT");
        styleButton(registerButton, new Color(219, 112, 147));
        registerButton.addActionListener(e -> performRegistration());
        buttonPanel.add(registerButton);

        backButton = new JButton("BACK TO LOGIN");
        styleButton(backButton, new Color(199, 21, 133));
        backButton.addActionListener(e -> goBackToLogin());
        buttonPanel.add(backButton);

        gbc.gridy = labels.length + 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void performRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String dob = dobField.getText();
        String accountType = (String) accountTypeCombo.getSelectedItem();
        String gender = (String) genderCombo.getSelectedItem();

        // Validation
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || 
            email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generate account number
        String accountNumber = "ACC" + (System.currentTimeMillis() % 100000);
        
        // Create account based on type with Rs 10,000 initial balance
        BankAccount newAccount;
        if (accountType.equals("Savings Account")) {
            newAccount = new SavingsAccount(accountNumber, fullName, 10000.0, email, phone, address, dob, gender);
        } else {
            newAccount = new CurrentAccount(accountNumber, fullName, 10000.0, email, phone, address, dob, gender);
        }

        // Register user
        boolean success = bankingService.registerUser(username, password, email, phone, newAccount);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Registration Successful!\n\n" +
                "Your account has been created.\n" +
                "Account Number: " + accountNumber + "\n" +
                "Initial Balance: Rs 10,000.00\n" +
                "You can now login with your credentials.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            goBackToLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBackToLogin() {
        this.dispose();
    }
}

public class NetApp extends JFrame {
    private BankingService bankingService;
    private BankUser currentUser;
    private BankAccount currentAccount;

    // GUI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // Login Panel
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    // Dashboard Panel
    private JLabel welcomeLabel;
    private JComboBox<String> accountComboBox;
    private JLabel balanceLabel;
    private JTable transactionTable;
    private DefaultTableModel transactionModel;
    
    // Transfer Panel
    private JTextField toAccountField;
    private JTextField amountField;
    private JTextArea descriptionArea;

    // Profile Panel
    private JLabel profileNameLabel, profileEmailLabel, profilePhoneLabel, 
                   profileAddressLabel, profileDobLabel, profileGenderLabel;

    // Map to store account selection
    private Map<String, BankAccount> accountMap;

    public NetApp() {
        bankingService = BankingService.getInstance();
        accountMap = new HashMap<>();
        initializeGUI();
        setupEventListeners();
    }

    private void initializeGUI() {
        setTitle("Bank for Women - Net Banking Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 240, 245));

        // Main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(255, 240, 245));

        // Create different panels
        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createDashboardPanel(), "DASHBOARD");
        mainPanel.add(createTransferPanel(), "TRANSFER");
        mainPanel.add(createProfilePanel(), "PROFILE");

        add(mainPanel);
        showLoginPanel();
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 240, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header
        JLabel titleLabel = new JLabel("BANK FOR WOMEN", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(219, 112, 147));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Empowering Women Through Banking", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(199, 21, 133));
        gbc.gridy = 1;
        panel.add(subtitleLabel, gbc);

        // Login Form Container
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(219, 112, 147), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 10, 10, 10);
        formGbc.fill = GridBagConstraints.HORIZONTAL;

        formGbc.gridx = 0; formGbc.gridy = 0; formGbc.gridwidth = 2;
        JLabel formTitle = new JLabel("Secure Login", JLabel.CENTER);
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(75, 0, 130));
        formPanel.add(formTitle, formGbc);

        formGbc.gridwidth = 1;
        formGbc.gridy = 1; formGbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(new Color(75, 0, 130));
        formPanel.add(userLabel, formGbc);
        
        formGbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setBackground(new Color(255, 250, 250));
        formPanel.add(usernameField, formGbc);

        formGbc.gridy = 2; formGbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(new Color(75, 0, 130));
        formPanel.add(passLabel, formGbc);
        
        formGbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(255, 250, 250));
        formPanel.add(passwordField, formGbc);

        formGbc.gridy = 3; formGbc.gridx = 0; formGbc.gridwidth = 2;
        JPanel loginButtonPanel = new JPanel(new FlowLayout());
        loginButtonPanel.setBackground(Color.WHITE);
        
        JButton loginButton = new JButton("LOGIN");
        styleButton(loginButton, new Color(219, 112, 147));
        loginButton.addActionListener(e -> performLogin());
        loginButtonPanel.add(loginButton);

        JButton registerButton = new JButton("NEW USER REGISTRATION");
        styleButton(registerButton, new Color(199, 21, 133));
        registerButton.addActionListener(e -> showRegistrationPage());
        loginButtonPanel.add(registerButton);

        formPanel.add(loginButtonPanel, formGbc);

        // Add form panel to main panel
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(formPanel, gbc);

        // Demo credentials
        JLabel demoLabel = new JLabel("Demo: srisha/password123 | shilki/password456", JLabel.CENTER);
        demoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        demoLabel.setForeground(new Color(120, 120, 120));
        gbc.gridy = 3;
        panel.add(demoLabel, gbc);

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(255, 240, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 240, 245));
        
        welcomeLabel = new JLabel("Welcome to Bank for Women!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(219, 112, 147));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JPanel headerButtonPanel = new JPanel(new FlowLayout());
        headerButtonPanel.setBackground(new Color(255, 240, 245));
        
        JButton profileButton = new JButton(" Profile");
        styleButton(profileButton, new Color(186, 85, 211));
        profileButton.addActionListener(e -> showProfilePanel());
        headerButtonPanel.add(profileButton);

        JButton logoutButton = new JButton(" Logout");
        styleButton(logoutButton, new Color(255, 105, 180));
        logoutButton.addActionListener(e -> performLogout());
        headerButtonPanel.add(logoutButton);

        headerPanel.add(headerButtonPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Account Info Panel
        JPanel accountPanel = new JPanel(new BorderLayout(10, 10));
        accountPanel.setBackground(Color.WHITE);
        accountPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(219, 112, 147), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel accountSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        accountSelectionPanel.setBackground(Color.WHITE);
        accountSelectionPanel.add(new JLabel("Select Account:"));
        accountComboBox = new JComboBox<>();
        accountComboBox.addActionListener(e -> {
            String selected = (String) accountComboBox.getSelectedItem();
            if (selected != null && accountMap.containsKey(selected)) {
                currentAccount = accountMap.get(selected);
                updateAccountInfo();
            }
        });
        accountSelectionPanel.add(accountComboBox);

        balanceLabel = new JLabel("Balance: Rs 0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(199, 21, 133));
        accountSelectionPanel.add(balanceLabel);

        accountPanel.add(accountSelectionPanel, BorderLayout.NORTH);

        // Transactions Table
        String[] columns = {"Date", "Type", "Amount", "Description"};
        transactionModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(transactionModel);
        transactionTable.setBackground(new Color(255, 250, 250));
        transactionTable.setSelectionBackground(new Color(255, 182, 193));
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        accountPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(accountPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(new Color(255, 240, 245));
        
        JButton transferButton = new JButton("Make Transfer");
        styleButton(transferButton, new Color(219, 112, 147));
        transferButton.addActionListener(e -> showTransferPanel());
        actionPanel.add(transferButton);

        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton, new Color(147, 112, 219));
        refreshButton.addActionListener(e -> updateAccountInfo());
        actionPanel.add(refreshButton);

        JButton loanButton = new JButton(" Apply for Loan");
        styleButton(loanButton, new Color(255, 105, 180));
        loanButton.addActionListener(e -> showLoanOptions());
        actionPanel.add(loanButton);

        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTransferPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 240, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Fund Transfer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(219, 112, 147));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Form Container
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(219, 112, 147), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 10, 10, 10);
        formGbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"From Account:", "To Account:", "Amount:", "Description:"};
        for (int i = 0; i < labels.length; i++) {
            formGbc.gridy = i;
            formGbc.gridx = 0;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(new Color(75, 0, 130));
            formPanel.add(label, formGbc);

            formGbc.gridx = 1;
            if (i == 0) {
                // Show current user's account number with balance and holder name
                String fromAccountText = currentAccount != null ? 
                    currentAccount.getAccountNumber() + " (" + currentAccount.getAccountHolder() + ") - Balance: Rs" + 
                    String.format("%.2f", currentAccount.getBalance()) : "No account selected";
                JLabel fromAccountLabel = new JLabel(fromAccountText);
                fromAccountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                fromAccountLabel.setForeground(new Color(199, 21, 133));
                formPanel.add(fromAccountLabel, formGbc);
            } else if (i == 1) {
                toAccountField = new JTextField(20);
                toAccountField.setBackground(new Color(255, 250, 250));
                formPanel.add(toAccountField, formGbc);
            } else if (i == 2) {
                amountField = new JTextField(20);
                amountField.setBackground(new Color(255, 250, 250));
                formPanel.add(amountField, formGbc);
            } else {
                descriptionArea = new JTextArea(3, 20);
                descriptionArea.setBackground(new Color(255, 250, 250));
                formPanel.add(new JScrollPane(descriptionArea), formGbc);
            }
        }

        // Help text - show available accounts for transfer
        formGbc.gridy = labels.length;
        formGbc.gridx = 0;
        formGbc.gridwidth = 2;
        
        // Get all available accounts for transfer
        StringBuilder availableAccounts = new StringBuilder("Available accounts for transfer: ");
        java.util.List<BankAccount> allAccounts = bankingService.getAllAccounts();
        boolean first = true;
        for (BankAccount acc : allAccounts) {
            if (currentAccount != null && !acc.getAccountNumber().equals(currentAccount.getAccountNumber())) {
                if (!first) {
                    availableAccounts.append(", ");
                }
                availableAccounts.append(acc.getAccountNumber())
                               .append(" (")
                               .append(acc.getAccountHolder())
                               .append(")");
                first = false;
            }
        }
        
        JLabel helpLabel = new JLabel("<html>" + availableAccounts.toString() + "</html>");
        helpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        helpLabel.setForeground(new Color(120, 120, 120));
        formPanel.add(helpLabel, formGbc);

        // Buttons
        formGbc.gridy = labels.length + 1;
        formGbc.gridx = 0;
        formGbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton transferButton = new JButton("Transfer Funds");
        styleButton(transferButton, new Color(219, 112, 147));
        transferButton.addActionListener(e -> performTransfer());
        buttonPanel.add(transferButton);

        JButton backButton = new JButton("Back to Dashboard");
        styleButton(backButton, new Color(255, 105, 180));
        backButton.addActionListener(e -> showDashboardPanel());
        buttonPanel.add(backButton);

        formPanel.add(buttonPanel, formGbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(formPanel, gbc);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(255, 240, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel titleLabel = new JLabel("Account Profile", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(219, 112, 147));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Profile Info Container
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(219, 112, 147), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Full Name:", "Email:", "Phone:", "Address:", "Date of Birth:", "Gender:"};
        JLabel[] valueLabels = {
            profileNameLabel = new JLabel(),
            profileEmailLabel = new JLabel(),
            profilePhoneLabel = new JLabel(),
            profileAddressLabel = new JLabel(),
            profileDobLabel = new JLabel(),
            profileGenderLabel = new JLabel()
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i;
            gbc.gridx = 0;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(new Color(75, 0, 130));
            infoPanel.add(label, gbc);

            gbc.gridx = 1;
            valueLabels[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            valueLabels[i].setForeground(Color.BLACK);
            infoPanel.add(valueLabels[i], gbc);
        }

        panel.add(infoPanel, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(255, 240, 245));
        
        JButton backButton = new JButton("Back to Dashboard");
        styleButton(backButton, new Color(219, 112, 147));
        backButton.addActionListener(e -> showDashboardPanel());
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    private void setupEventListeners() {
        // Enter key for login
        passwordField.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        currentUser = bankingService.authenticateUser(username, password);
        if (currentUser != null) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome to Bank for Women.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            initializeUserDashboard();
            showDashboardPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials! Please try again.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performLogout() {
        currentUser = null;
        currentAccount = null;
        usernameField.setText("");
        passwordField.setText("");
        accountMap.clear();
        showLoginPanel();
    }

    private void initializeUserDashboard() {
        welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
        
        // Populate account combo box
        accountComboBox.removeAllItems();
        accountMap.clear();
        
        java.util.List<BankAccount> accounts = bankingService.getUserAccounts(currentUser.getUserId());
        for (BankAccount account : accounts) {
            String displayText = account.getAccountNumber() + " - " + account.getAccountType() + " - Balance: Rs" + account.getBalance();
            accountComboBox.addItem(displayText);
            accountMap.put(displayText, account);
        }
        
        if (!accounts.isEmpty()) {
            String firstItem = (String) accountComboBox.getItemAt(0);
            currentAccount = accountMap.get(firstItem);
            updateAccountInfo();
        }
    }

    private void updateAccountInfo() {
        if (currentAccount != null) {
            balanceLabel.setText(String.format("Balance: Rs %.2f", currentAccount.getBalance()));
            updateTransactionHistory();
        }
    }

    private void updateTransactionHistory() {
        transactionModel.setRowCount(0);
        if (currentAccount != null) {
            java.util.List<BankTransaction> transactions = bankingService.getTransactionHistory(currentAccount.getAccountNumber());
            // Sort transactions by timestamp (newest first)
            transactions.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));
            
            for (BankTransaction t : transactions) {
                transactionModel.addRow(new Object[]{
                    t.getTimestamp(),
                    t.getType(),
                    String.format("Rs %.2f", t.getAmount()),
                    t.getDescription()
                });
            }
        }
    }

    private void updateProfileInfo() {
        if (currentAccount != null) {
            profileNameLabel.setText(currentAccount.getAccountHolder());
            profileEmailLabel.setText(currentAccount.getEmail() != null ? currentAccount.getEmail() : "Not provided");
            profilePhoneLabel.setText(currentAccount.getPhone() != null ? currentAccount.getPhone() : "Not provided");
            profileAddressLabel.setText(currentAccount.getAddress() != null ? currentAccount.getAddress() : "Not provided");
            profileDobLabel.setText(currentAccount.getDob() != null ? currentAccount.getDob() : "Not provided");
            profileGenderLabel.setText(currentAccount.getGender() != null ? currentAccount.getGender() : "Not provided");
        }
    }

    private void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void showDashboardPanel() {
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    private void showTransferPanel() {
        // Ensure current account is selected
        if (currentAccount == null && currentUser != null) {
            java.util.List<BankAccount> accounts = bankingService.getUserAccounts(currentUser.getUserId());
            if (!accounts.isEmpty()) {
                currentAccount = accounts.get(0);
            }
        }
        cardLayout.show(mainPanel, "TRANSFER");
    }

    private void showProfilePanel() {
        updateProfileInfo();
        cardLayout.show(mainPanel, "PROFILE");
    }

    private void showRegistrationPage() {
        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.setVisible(true);
    }

    private void showLoanOptions() {
        String[] loanOptions = {
            "Women Entrepreneurship Loan",
            "Education Loan for Women",
            "Home Loan (Special Women Scheme)",
            "Personal Loan for Women",
            "Business Loan for Women"
        };

        String selectedLoan = (String) JOptionPane.showInputDialog(this,
            "Select a loan option:",
            "Women Empowerment Loans",
            JOptionPane.QUESTION_MESSAGE,
            null,
            loanOptions,
            loanOptions[0]);

        if (selectedLoan != null) {
            JOptionPane.showMessageDialog(this,
                "Loan Application Submitted!\n\n" +
                "Loan Type: " + selectedLoan + "\n" +
                "We will contact you within 2 business days.\n" +
                "Special interest rates available for women applicants!",
                "Application Received",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void performTransfer() {
        try {
            String toAccount = toAccountField.getText().trim();
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionArea.getText();

            if (currentAccount == null) {
                JOptionPane.showMessageDialog(this, "No account selected!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (toAccount.isEmpty() || amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter valid transfer details!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if transferring to same account
            if (currentAccount.getAccountNumber().equals(toAccount)) {
                JOptionPane.showMessageDialog(this, "Cannot transfer to the same account!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check sufficient balance
            if (amount > currentAccount.getBalance()) {
                JOptionPane.showMessageDialog(this, 
                    "Insufficient balance!\nAvailable: Rs" + currentAccount.getBalance() + 
                    "\nRequired: Rs" + amount, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = bankingService.transferMoney(
                currentAccount.getAccountNumber(), toAccount, amount, description);

            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Transfer completed successfully!\n\n" +
                    "From: " + currentAccount.getAccountNumber() + "\n" +
                    "To: " + toAccount + "\n" +
                    "Amount: Rs" + amount + "\n" +
                    "New Balance: Rs" + currentAccount.getBalance(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                toAccountField.setText("");
                amountField.setText("");
                descriptionArea.setText("");
                updateAccountInfo();
                showDashboardPanel();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Transfer failed!\n\n" +
                    "Possible reasons:\n" +
                    "- Recipient account not found\n" +
                    "- Insufficient balance\n" +
                    "- Amount below minimum balance requirement\n" +
                    "- Invalid account number",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to start the application
    public static void main(String[] args) {
        // Start the bank server in simulation mode
        BankServer server = new BankServer();
        server.startServer();

        // Start the GUI
        SwingUtilities.invokeLater(() -> {
            new NetApp().setVisible(true);
        });
    }
}