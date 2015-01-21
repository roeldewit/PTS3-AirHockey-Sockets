/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author martijn
 */
public class WaitingScreenController implements Initializable {

    Database database = new Database();
    public static final ObservableList items = FXCollections.observableArrayList();
    
    @FXML
    private Button btReturnLobby;
    @FXML
    private ListView<?> lvJoinedPlayers;
    @FXML
    private Button btStartGame;
    
    /**
     * Initializes the controller class.
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {
        try {
            items.add(database.getUser("t").getUsername());
        } catch (SQLException | IOException ex) {
            Logger.getLogger(WaitingScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        lvJoinedPlayers.setItems(items);
    }

    public void startGameHost() {
        Game game = new Game((Stage) btStartGame.getScene().getWindow());
        
        try {
            game.startAsHost(database.getUser("t"));
        } catch (SQLException | IOException ex) {
            Logger.getLogger(WaitingScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
