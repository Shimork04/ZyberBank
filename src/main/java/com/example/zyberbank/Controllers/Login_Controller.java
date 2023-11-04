package com.example.zyberbank.Controllers;

import com.example.zyberbank.Models.Model;
import com.example.zyberbank.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Login_Controller implements Initializable {

    public ChoiceBox<AccountType> acc_selector;
    public Label payee_address_lbl;
    public TextField payee_address_fld;
    public PasswordField password_fld;
    public Button login_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT,AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> setAcc_selector());
        login_btn.setOnAction(event -> onLogin());
//        login_btn.setOnAction(event -> Model.getInstance().getViewFactory().showClientWindow());
    }

    private void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        if(Model.getInstance().getViewFactory().getLoginAccountType() ==AccountType.CLIENT){
            //Evaluate Client Login Credentials
            Model.getInstance().evaluateClientCred(payee_address_fld.getText(),password_fld.getText());
            if (Model.getInstance().getClientLoginSuccessFlag()){
                Model.getInstance().getViewFactory().showClientWindow();
                //Close the Login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            }else {
                payee_address_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("No such Login Credentials.");

            }
        }else {
            //Evaluate Admin Login Credentials
            Model.getInstance().evaluateAdminCred(payee_address_fld.getText(),password_fld.getText());
            if (Model.getInstance().getAdminLoginSuccessFlag()){
                Model.getInstance().getViewFactory().showAdminWindow();
                //Close the Login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            }else {
                payee_address_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("No such Login Credentials.");
            }
        }
    }

    public void setAcc_selector() {
        Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue());
        //Change choice box accordingly
        if (acc_selector.getValue() ==AccountType.ADMIN){
            payee_address_lbl.setText("Username:");
        }else {
            payee_address_lbl.setText("Recipient address:");
        }
    }
}
