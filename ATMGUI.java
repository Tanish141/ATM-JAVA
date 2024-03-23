import java.util.*;
import java.lang.Double;
import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;


class UserClass 
{
    private String userId;
    private String userPin;
    private double userBalance;
    private String userName;
    private long accountNumber;

    UserClass()
    {
        this.userId = " ";
        this.userPin = " ";
        this.userBalance = 0.0;
        this.userName = " ";
        this.accountNumber = 0;
    }

    UserClass(String userId, String userPin,String userName)
    {
        this.userId = userId;
        this.userPin = userPin;
        this.userBalance = 0.0;
        this.userName = userName;
    }

    UserClass(String userId, String userPin,double userBalance,String userName,long accountNumber)
    {
        this.userId=userId;
        this.userPin=userPin;
        this.userBalance=userBalance;
        this.userName = userName;
        this.accountNumber = accountNumber;
    }

    public String getUserName()
    {
        return userName;
    }

    public long getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserPin()
    {
        return userPin;
    }

    public double getUserBalance()
    {
        return userBalance;
    }

    public void setUserBalance(double userBalance) 
    {
        this.userBalance = userBalance;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public void setUserPin(String userPin) 
    {
        this.userPin = userPin;
    }
}


enum TransactionType
    {
        WITHDRAWAL,
        DEPOSIT,
        TRANSFER
    }

class Transaction 
{
    private String transactionId;
    private TransactionType transactionType;
    private double amount;
    private Date date;

    Transaction(TransactionType transactionType,double amount)
    {
        this.transactionId = generateTransactionId();
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = new Date();
    }

    private String generateTransactionId()
    {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int maxLength = 13;
        for(int i=0;i<maxLength;i++)
        {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public String getTransactionId() 
    {
        return transactionId;
    }

    public double getAmount() 
    {
        return amount;
    }

    public TransactionType getTransactionType() 
    {
        return transactionType;
    }

    public Date getDate() 
    {
        return date;
    }
    
}


class ATM
{
    private HashMap<Long,UserClass> Users = new HashMap<>();
    ArrayList<Transaction> transactions;

    public ATM()
    {
        this.transactions = new ArrayList<>();
    }

    public void addUser(String userId, String userPin,double userBalance,String userName,long accountNumber)
    {
        UserClass user = new UserClass(userId,userPin,userBalance,userName,accountNumber);
        Users.put(accountNumber,user);
    }


    public UserClass getUser(long accountNumber)
    {
        for (UserClass user : Users.values()) 
        {
            if (user.getAccountNumber() == accountNumber) 
            {
                return user;
            }
        }
        return null;
    }

    public void deposit(long accountNumber,double amount)
    {
        UserClass user = getUser(accountNumber);
        if (user != null) 
        {
            Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount);
            transactions.add(transaction);
            double currentBalance = user.getUserBalance();
            user.setUserBalance(currentBalance + amount);
            JOptionPane.showMessageDialog(null, "Amount deposited. Current Balance: " + user.getUserBalance());
        }
    }

    public void withdraw(long accountNumber,double amount)
    {
        UserClass user = getUser(accountNumber);
        if (user != null) 
        {
            if (user.getUserBalance() >= amount) 
            {
                Transaction transaction = new Transaction(TransactionType.WITHDRAWAL, amount);
                transactions.add(transaction);
                user.setUserBalance(user.getUserBalance() - amount);
                JOptionPane.showMessageDialog(null, "Amount Withdrawn: " + amount + ". Current Balance: " + user.getUserBalance());
            } 
            else 
            {
                JOptionPane.showMessageDialog(null, "Insufficient balance: " + user.getUserBalance());
            }
        }
    }

    public void transfer(long senderAccountNumber,long recipientAccountNumber,double amount)
    {
        UserClass sender = getUser(senderAccountNumber);
        UserClass recipient = getUser(recipientAccountNumber);
        if (sender == null) 
        {
            JOptionPane.showMessageDialog(null, "Sender account not found.");
        }
        if (recipient == null) 
        {
            JOptionPane.showMessageDialog(null, "Recipient account not found.");
        }
        if (sender != null && recipient != null) 
        {
            if (sender.getUserBalance() >= amount) 
            {
                Transaction transaction = new Transaction(TransactionType.TRANSFER, amount);
                transactions.add(transaction);
                sender.setUserBalance(sender.getUserBalance() - amount);
                recipient.setUserBalance(recipient.getUserBalance() + amount);
                JOptionPane.showMessageDialog(null, "Transfer successful");
            } 
            else 
            {
                JOptionPane.showMessageDialog(null, "Insufficient balance in your account! Current balance: "
                + sender.getUserBalance());
            }
        }
    }

    public void displayTransactionHistory(long accountNumber)
    {
        UserClass user = getUser(accountNumber);
        if (user != null) {
            StringBuilder history = new StringBuilder();
            for (Transaction transaction : transactions) 
            {
                if (transaction.getTransactionType() != TransactionType.TRANSFER) 
                {
                    history.append("\nTransaction ID: ").append(transaction.getTransactionId());
                    history.append("\nType: ").append(transaction.getTransactionType());
                    history.append("\nAmount: ").append(transaction.getAmount());
                    history.append("\nDate: ").append(transaction.getDate()).append("\n");
                }
            }
            JOptionPane.showMessageDialog(null, "TRANSACTION HISTORY:\n" + history.toString());
        } 
        else 
        {
            JOptionPane.showMessageDialog(null, "User not found.");
        }
    }

    public double checkBalance(long accountNumber)
    {
        UserClass user = Users.get(accountNumber);
        if(user != null)
        {
            return user.getUserBalance();
        }
        else
        {
            return -1;
        }
    }

    public void changeUserPin(long accountNumber,String newPin)
    {
        UserClass user = Users.get(accountNumber);
        if(user != null)
        {
            user.setUserPin(newPin);
            JOptionPane.showMessageDialog(null, "PIN changed successfully!");
        }
    }

    public void generateReceipt(Transaction transaction)
    {
        JOptionPane.showMessageDialog(null, "Transaction ID: " + transaction.getTransactionId() +
                "\nTransaction Type: " + transaction.getTransactionType() +
                "\nAmount: " + transaction.getAmount() +
                "\nDate: " + transaction.getDate());
    }

    public boolean authenticateUser(long accountNumber, String pin)
    {
        UserClass user = Users.get(accountNumber);
        return user != null && user.getUserPin().equals(pin);
    }

}

public class ATMGUI extends JFrame implements ActionListener 
{

    private ATM atm;
    
    public ATMGUI(ATM atm) 
    {
        this.atm = atm;
        setTitle("ATM");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        JButton newAccountButton = new JButton("New Account");
        newAccountButton.addActionListener(this);
        newAccountButton.setPreferredSize(new Dimension(50, 50));
        add(newAccountButton);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        newAccountButton.setPreferredSize(new Dimension(50, 50));
        add(loginButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getActionCommand().equals("New Account")) 
        {
            displayNewAccountForm();
        } 
        else if (e.getActionCommand().equals("Login")) 
        {
            displayLoginForm();
        }
    }

    private void displayNewAccountForm() 
    {
        JFrame newAccountFrame = new JFrame("New Account");
        newAccountFrame.setSize(400, 200);
        newAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newAccountFrame.setLayout(new GridLayout(4, 2));

        JLabel userIdLabel = new JLabel("User ID:");
        JTextField userIdField = new JTextField();
        JLabel userNameLabel = new JLabel("User Name:");
        JTextField userNameField = new JTextField();
        JLabel userPinLabel = new JLabel("PIN:");
        JPasswordField userPinField = new JPasswordField();
        JLabel accountNumberLabel = new JLabel("Account Number:"); 
        JTextField accountNumberField = new JTextField(); 

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String userId = userIdField.getText();
                String userName = userNameField.getText();
                String userPin = new String(userPinField.getPassword());
                long accountNumber = Long.parseLong(accountNumberField.getText());
                atm.addUser(userId, userPin, 0.0, userName, accountNumber);
                JOptionPane.showMessageDialog(newAccountFrame, "Account created successfully!");
                newAccountFrame.dispose();
            }
        });

        newAccountFrame.add(userIdLabel);
        newAccountFrame.add(userIdField);
        newAccountFrame.add(userNameLabel);
        newAccountFrame.add(userNameField);
        newAccountFrame.add(userPinLabel);
        newAccountFrame.add(userPinField);
        newAccountFrame.add(createAccountButton);
        newAccountFrame.add(accountNumberLabel); 
        newAccountFrame.add(accountNumberField);
        

        newAccountFrame.setVisible(true);
        
    }

    private JTextField accountNumberField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JLabel accountNumberLabel;
    private JLabel pinLabel;

    private void displayLoginForm() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(400, 200);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLayout(null);
    
        accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setBounds(20, 20, 120, 20);
        loginFrame.add(accountNumberLabel);
    
        accountNumberField = new JTextField();
        accountNumberField.setBounds(150, 20, 200, 20);
        loginFrame.add(accountNumberField);
    
        pinLabel = new JLabel("PIN:");
        pinLabel.setBounds(20, 50, 120, 20);
        loginFrame.add(pinLabel);
    
        pinField = new JPasswordField();
        pinField.setBounds(150, 50, 200, 20);
        loginFrame.add(pinField);
    
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 80, 100, 30);
        loginButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                long accountNumber = Long.parseLong(accountNumberField.getText());
                String pin = new String(pinField.getPassword());
                if (atm.authenticateUser(accountNumber, pin)) 
                {
                    displayMenu(atm, accountNumber);
                    loginFrame.dispose(); 
                } 
                else 
                {
                    JOptionPane.showMessageDialog(loginFrame, "Authentication failed. Invalid account number or PIN.");
                }
            }
        });
        loginFrame.add(loginButton);
    
        loginFrame.setVisible(true);
    }


    private void displayMenu(ATM atm, long accountNumber) 
    {
        JFrame menuFrame = new JFrame("ATM Menu");
        menuFrame.setSize(400, 300);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1));
    

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
    {
        String amountString = JOptionPane.showInputDialog(menuFrame, "Enter deposit amount:");
        if (amountString != null) {
            try 
            {
                double amount = Double.parseDouble(amountString);
                atm.deposit(accountNumber, amount);
            } 
            catch (NumberFormatException ex) 
            {
                JOptionPane.showMessageDialog(menuFrame, "Invalid input. Please enter a valid amount.");
            }
        }
    }
});

    JButton withdrawButton = new JButton("Withdraw");
    withdrawButton.addActionListener(new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            String amountString = JOptionPane.showInputDialog(menuFrame, "Enter withdrawal amount:");
            if (amountString != null) 
            {
                double amount = Double.parseDouble(amountString);
                atm.withdraw(accountNumber, amount);
            }
        }
    });

    JButton viewTransactionButton = new JButton("View Transaction History");
    viewTransactionButton.addActionListener(new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            atm.displayTransactionHistory(accountNumber);
        }
    });

    JButton transferButton = new JButton("Transfer");
    transferButton.addActionListener(new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            String recipientAccountString = JOptionPane.showInputDialog(menuFrame, "Enter recipient account number:");
            if (recipientAccountString != null) 
            {
                long recipientAccountNumber = Long.parseLong(recipientAccountString);
                String amountString = JOptionPane.showInputDialog(menuFrame, "Enter transfer amount:");
                if (amountString != null) 
                {
                    try 
                    {
                        double amount = Double.parseDouble(amountString);
                        atm.transfer(accountNumber, recipientAccountNumber, amount);
                    } 
                    catch (NumberFormatException ex) 
                    {
                        JOptionPane.showMessageDialog(menuFrame, "Invalid input. Please enter a valid amount.");
                    }
                }
            }
        }
    });

    JButton checkBalanceButton = new JButton("Check Balance");
    checkBalanceButton.addActionListener(new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            double balance = atm.checkBalance(accountNumber);
            if (balance != -1) 
            {
                JOptionPane.showMessageDialog(menuFrame, "Your current balance is: $" + balance);
            } 
            else 
            {
                JOptionPane.showMessageDialog(menuFrame, "Account not found.");
            }
        }
    });

    JButton changePinButton = new JButton("Change PIN");
    changePinButton.addActionListener(new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            String newPin = JOptionPane.showInputDialog(menuFrame, "Enter new PIN:");
            if (newPin != null) 
            {
                atm.changeUserPin(accountNumber, newPin);
                JOptionPane.showMessageDialog(menuFrame, "PIN changed successfully!");
            }
        }
    });

    menuPanel.add(depositButton);
    menuPanel.add(withdrawButton);
    menuPanel.add(viewTransactionButton);
    menuPanel.add(transferButton);
    menuPanel.add(checkBalanceButton);
    menuPanel.add(changePinButton);

    menuFrame.add(menuPanel);
    menuFrame.setVisible(true);

    
    dispose();
}

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> 
        {
            ATM atm = new ATM();
            ATMGUI atmGUI = new ATMGUI(atm);
            atmGUI.setVisible(true);
        });
    }
}
