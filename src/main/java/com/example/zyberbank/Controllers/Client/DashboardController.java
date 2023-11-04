//package com.example.zyberbank.Controllers.Client;
//
//import com.example.zyberbank.Models.Model;
//import com.example.zyberbank.Models.Transaction;
//import com.example.zyberbank.Views.TransactionCellFactory;
//import javafx.beans.binding.Bindings;
//import javafx.fxml.Initializable;
//import javafx.scene.control.*;
//import javafx.scene.text.Text;
//
//import java.net.URL;
//import java.sql.ResultSet;
//import java.time.LocalDate;
//import java.util.ResourceBundle;
//
//public class DashboardController implements Initializable {
//    public Text user_name;
//    public Label login_date;
//    public Label cheque_bal;
//    public Label cheque_acc_num;
//    public Label savings_bal;
//    public Label savings_acc_num;
//    public Label income_lbl;
//    public Label expense_lbl;
//    public ListView<Transaction> transaction_listview;
//    public TextField payee_fld;
//    public TextField amount_fld;
//    public Button send_money_btn;
//    public TextArea message_fld;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        bindData();
//        iniLatestTransactionsList();
//        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
//        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
//        send_money_btn.setOnAction(event -> onSendMoney());
//        accountSummary();
//    }
//
//    private void bindData() {
//        user_name.textProperty().bind(Bindings.concat("Hi, ").concat(Model.getInstance().getClient().firstNameProperty()));
//        login_date.setText("Today, " + LocalDate.now());
//        cheque_bal.setText("Your text here");
//        cheque_bal.textProperty().bind(Model.getInstance().getClient().chequeAccountProperty().get().balanceProperty().asString());
//        cheque_acc_num.textProperty().bind(Model.getInstance().getClient().chequeAccountProperty().get().accountNumberProperty());
//        savings_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
//        savings_acc_num.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());
//    }
//
//    private void iniLatestTransactionsList(){
//        if(Model.getInstance().getLatestTransactions().isEmpty()){
//            Model.getInstance().setLatestTransactions();
//        }
//    }
//
//    private void onSendMoney() {
//        String receiver = payee_fld.getText();
//        double amount = Double.parseDouble(amount_fld.getText());
//        String message = message_fld.getText();
//        String sender = Model.getInstance().getClient().pAddressProperty().get();
//        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
//        try {
//            if (resultSet.isBeforeFirst()){
//                Model.getInstance().getDatabaseDriver().updateBalance(receiver, amount, "ADD");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        // Subtract from sender's savings account
//        Model.getInstance().getDatabaseDriver().updateBalance(sender, amount, "SUB");
//        // Update the savings account balance in the client object
//        Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(sender));
//        // Record new transaction
//        Model.getInstance().getDatabaseDriver().newTransaction(sender, receiver, amount, message);
//        // Clear the fields
//        payee_fld.setText("");
//        amount_fld.setText("");
//        message_fld.setText("");
//    }
//
//    // Method calculates all expenses and income
//    private void accountSummary() {
//        double income = 0;
//        double expenses = 0;
//        if (Model.getInstance().getAllTransactions().isEmpty()){
//            Model.getInstance().setAllTransactions();
//        }
//        for (Transaction transaction: Model.getInstance().getAllTransactions()) {
//            if (transaction.senderProperty().get().equals(Model.getInstance().getClient().pAddressProperty().get())){
//                expenses = expenses + transaction.amountProperty().get();
//            } else {
//                income = income + transaction.amountProperty().get();
//            }
//        }
//        income_lbl.setText("+ Rs." + income);
//        expense_lbl.setText("- Rs." + expenses);
//    }
//
//}

package com.example.zyberbank.Controllers.Client;

import com.example.zyberbank.Models.Model;
import com.example.zyberbank.Models.Transaction;
import com.example.zyberbank.Views.TransactionCellFactory;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_date;
    public Label cheque_bal;
    public Label cheque_acc_num;
    public Label savings_bal;
    public Label savings_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView<Transaction> transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public Button send_money_btn;
    public TextArea message_fld;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        initLatestTransactionsList();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
        send_money_btn.setOnAction(event -> onSendMoney());
        accountSummary();
    }

    private void bindData() {
        user_name.textProperty().bind(Bindings.concat("Hi, ").concat(Model.getInstance().getClient().firstNameProperty()));
        login_date.setText("Today, " + LocalDate.now());
        // Ensure that the following lines are inside a null check to avoid NPE if the client or accounts are null.
        if (Model.getInstance().getClient() != null) {
            Model.getInstance().getClient().chequeAccountProperty().addListener((obs, oldAccount, newAccount) -> {
                if (newAccount != null) {
                    cheque_bal.textProperty().bind(newAccount.balanceProperty().asString());
                    cheque_acc_num.textProperty().bind(newAccount.accountNumberProperty());
                }
            });
            Model.getInstance().getClient().savingsAccountProperty().addListener((obs, oldAccount, newAccount) -> {
                if (newAccount != null) {
                    savings_bal.textProperty().bind(newAccount.balanceProperty().asString());
                    savings_acc_num.textProperty().bind(newAccount.accountNumberProperty());
                }
            });
        }
    }

    private void initLatestTransactionsList() {
        if (Model.getInstance().getLatestTransactions().isEmpty()) {
            Model.getInstance().setLatestTransactions();
        }
    }

    private void onSendMoney() {
        String receiver = payee_fld.getText();
        double amount = Double.parseDouble(amount_fld.getText());
        String message = message_fld.getText();
        String sender = Model.getInstance().getClient().pAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
        try {
            if (resultSet.isBeforeFirst()) {
                Model.getInstance().getDatabaseDriver().updateBalance(receiver, amount, "ADD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Subtract from sender's savings account
        Model.getInstance().getDatabaseDriver().updateBalance(sender, amount, "SUB");
        // Update the savings account balance in the client object
        Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(sender));
        // Record a new transaction
        Model.getInstance().getDatabaseDriver().newTransaction(sender, receiver, amount, message);
        // Clear the fields
        payee_fld.setText("");
        amount_fld.setText("");
        message_fld.setText("");
    }

    // Method calculates all expenses and income
    private void accountSummary() {
        double income = 0;
        double expenses = 0;
        if (Model.getInstance().getAllTransactions().isEmpty()) {
            Model.getInstance().setAllTransactions();
        }
        for (Transaction transaction : Model.getInstance().getAllTransactions()) {
            if (transaction.senderProperty().get().equals(Model.getInstance().getClient().pAddressProperty().get())) {
                expenses = expenses + transaction.amountProperty().get();
            } else {
                income = income + transaction.amountProperty().get();
            }
        }
        income_lbl.setText("+ Rs." + income);
        expense_lbl.setText("- Rs." + expenses);
    }
}
