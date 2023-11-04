package com.example.zyberbank.Models;


import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection conn;

    public DatabaseDriver() {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/zyberbank","root","1234");
            System.out.println("Connected to Database.");
        } catch (SQLException e){
            System.out.println("Connection failed! Check DatabaseDriver.java");
            e.printStackTrace();
        }
    }

    /*
     * Client Section
     * */
    public ResultSet getClientData(String pAddress, String password) {
        Statement statement;
        ResultSet resultSet=null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM clients WHERE PayeeAddress='" + pAddress + "' AND Password='" + password + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getTransactions(String pAddress, int limit) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            if (limit >= 0) {
                resultSet = statement.executeQuery("SELECT * FROM transactions WHERE Sender='" + pAddress + "' OR Receiver='" + pAddress + "' LIMIT " + limit + ";");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

//    public ResultSet getTransactions(String pAddress, int limit) {
//        Statement statement;
//        ResultSet resultSet = null;
//        try {
//            statement = this.conn.createStatement();
//            if(limit>=0){
//                resultSet = statement.executeQuery("SELECT * FROM transactions WHERE Sender='" + pAddress + "' OR Receiver='" + pAddress + "' LIMIT " + limit + ";");
//            }
////            resultSet = statement.executeQuery("SELECT * FROM transactions WHERE Sender='" + pAddress + "' OR Receiver='" + pAddress + "' LIMIT " + limit + ";");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return resultSet;
//    }

    // Method returns savings account balance
    public double getSavingsAccountBalance(String pAddress) {
        Statement statement;
        ResultSet resultSet;
        double balance = 0;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM savingsAccounts WHERE Owner='" + pAddress + "';");
            if (resultSet.next()) {
                balance = resultSet.getDouble("Balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    // Method to either add or subtract from balance given operation
    public void updateBalance(String pAddress, double amount, String operation) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM savingsAccounts WHERE Owner='" + pAddress + "';");
            double newBalance;
            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("Balance");
                if (operation.equals("ADD")) {
                    newBalance = currentBalance + amount;
                } else {
                    if (currentBalance >= amount) {
                        newBalance = currentBalance - amount;
                    } else {
                        // Handle insufficient balance error
                        System.err.println("Insufficient balance for withdrawal.");
                        return;
                    }
                }
                statement.executeUpdate("UPDATE savingsAccounts SET Balance=" + newBalance + " WHERE Owner='" + pAddress + "';");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Rest of your methods

    // Creates and records new transaction
    public void newTransaction(String sender, String receiver, double amount, String message) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            LocalDate date = LocalDate.now();
            statement.executeUpdate("INSERT INTO " + "transactions(Sender, Receiver, Amount, Date, Message) " + "VALUES ('"+sender+"', '"+receiver+"', "+amount+", '"+date+"', '"+message+"');");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Admin Section
    public ResultSet getAdminData(String username, String password) {
        Statement statement;
        ResultSet resultSet = null;
        try {
//            username = "Admin";
//            password = "123456";
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM admins WHERE Username='"+username+"' AND Password='"+password+"';");
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " + "clients (FirstName, LastName, PayeeAddress, Password, Date)" + "VALUES ('"+fName+"', '"+lName+"', '"+pAddress+"', '"+password+"', '"+date.toString()+"');");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " + "checkingAccounts (Owner, AccountNumber, TransactionLimit, Balance)" + " VALUES ('"+owner+"', '"+number+"', "+tLimit+", "+balance+")");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " + "savingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance)" + " VALUES ('"+owner+"', '"+number+"', "+wLimit+", "+balance+")");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet getAllClientsData() {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM clients;");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void depositSavings(String pAddress, double amount) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("UPDATE savingsAccounts SET Balance="+amount+" WHERE Owner='"+pAddress+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Utility Methods

    public ResultSet searchClient(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM clients WHERE PayeeAddress='"+pAddress+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

//    public int getLastClientsId() {
//        Statement statement;
//        ResultSet resultSet;
//        int id = 0;
//        try {
//            statement = this.conn.createStatement();
//            resultSet = statement.executeQuery("SELECT * FROM sqlite_sequence WHERE name='clients';");
//            id = resultSet.getInt("seq");
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return id;
//    }

    public ResultSet getCheckingAccountData(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM checkingAccounts WHERE Owner='"+pAddress+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSavingsAccountData(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM savingsAccounts WHERE Owner='"+pAddress+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
}
