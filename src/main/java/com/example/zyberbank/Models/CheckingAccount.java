package com.example.zyberbank.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CheckingAccount extends Account{

    //The number of transaction a client is allowed to do per day.
    private final IntegerProperty transactionLimit;

    public CheckingAccount(String owner,String acccountNumber,double balance, int tLimit){
        super(owner,acccountNumber,balance);
        this.transactionLimit = new SimpleIntegerProperty(this,"Transaction Limit",tLimit);
    }

    public IntegerProperty transactionLimitProperty(){return transactionLimit;}

    @Override
    public String toString(){
        return accountNumberProperty().get();
    }
}
