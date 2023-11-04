package com.example.zyberbank.Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Client {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty recipient;
    private final ObjectProperty<Account> chequeAccount;
    private final ObjectProperty<Account> savingsAccount;
    private final ObjectProperty<LocalDate> dateCreated;

    public Client(String fName,String lName,String pAddress, Account cAccount,Account sAccount, LocalDate date){
        this.firstName = new SimpleStringProperty(this,"FirstName",fName);
        this.lastName = new SimpleStringProperty(this,"LastName",lName);
        this.recipient = new SimpleStringProperty(this,"Recipient",pAddress);
        this.chequeAccount = new SimpleObjectProperty<>(this,"Cheque Account",cAccount);
        this.savingsAccount = new SimpleObjectProperty<>(this,"Savings Account",sAccount);
        this.dateCreated = new SimpleObjectProperty<>(this,"Date",date);
    }

    public StringProperty firstNameProperty(){return firstName;}
    public StringProperty lastNameProperty(){return lastName;}
    public StringProperty pAddressProperty(){return recipient;}
    public ObjectProperty<Account> chequeAccountProperty(){return chequeAccount;}
    public ObjectProperty<Account> savingsAccountProperty(){return savingsAccount;}
    public ObjectProperty<LocalDate> dateProperty(){return dateCreated;}



}
