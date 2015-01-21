/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author martijn
 */
public class WaitingScreenHostController implements Initializable {

    @FXML
    private Button btReturnLobby;

    @FXML
    private ListView lvJoinedPlayers;

    public static final ObservableList items = FXCollections.observableArrayList();
    Database database = new Database();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        screenSetup();
    }

    public void screenSetup() {
        try {
            items.add(database.getUser("t"));
            lvJoinedPlayers.setItems(items);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(WaitingScreenHostController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnLobby() {
        try {
            Stage stage = (Stage) lvJoinedPlayers.getScene().getWindow();
            Lobby lobby = new Lobby(stage);
        } catch (NotBoundException | IOException | SQLException ex) {
            Logger.getLogger(WaitingScreenHostController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
