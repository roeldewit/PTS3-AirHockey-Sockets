/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import static Airhockey.Main.WaitingScreenHostController.items;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.*;
import javafx.fxml.*;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author martijn
 */
public class WaitingScreenClientController implements Initializable {

    @FXML
    private Button btReturnLobbyClient;

    @FXML
    private ListView lvJoinedPlayersClient;

    private Database database = new Database();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void screenSetup() {
        try {
            items.add(database.getUser("t"));
            lvJoinedPlayersClient.setItems(items);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(WaitingScreenHostController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
