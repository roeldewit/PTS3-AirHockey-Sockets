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
 * Designed for the host.
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
    WaitingScreen waitingScreen = new WaitingScreen();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        screenSetup();
    }

    /**
     * List with users that are joined gets filled.
     */
    public void screenSetup() {
        items.add(waitingScreen.getCurrentUser());
        lvJoinedPlayers.setItems(items);
    }

    /**
     * Gets called after pressing the return to lobby button.
     * It returns the player to the lobby.
     */
    public void returnLobby() {
        try {
            Stage stage = (Stage) lvJoinedPlayers.getScene().getWindow();
            Lobby lobby = new Lobby(stage,waitingScreen.getCurrentUser());
        } catch (NotBoundException | IOException | SQLException ex) {
            Logger.getLogger(WaitingScreenHostController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
