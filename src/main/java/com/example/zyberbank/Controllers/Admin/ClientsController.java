package com.example.zyberbank.Controllers.Admin;

import com.example.zyberbank.Models.Client;
import com.example.zyberbank.Models.Model;
import com.example.zyberbank.Views.ClientCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initClients();
        clients_listview.setItems(Model.getInstance().getClients());
        clients_listview.setCellFactory(e -> new ClientCellFactory());

    }

    private void initClients(){
        if(Model.getInstance().getClients().isEmpty()){
            Model.getInstance().setClients();
        }
    }
}