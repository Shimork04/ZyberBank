package com.example.zyberbank.Controllers.Admin;

//import com.isfsc.isfsc_bank.Models.Model;
import com.example.zyberbank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener((observableValue, s, t1) -> {

            switch (t1){
                case CLIENTS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getClientsView());
                case DEPOSIT -> admin_parent.setCenter(Model.getInstance().getViewFactory().getDepositView());
                default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getCreateClientView());
            }
        });
    }
}
