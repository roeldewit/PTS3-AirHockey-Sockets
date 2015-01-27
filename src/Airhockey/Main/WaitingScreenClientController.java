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
import javafx.stage.Stage;

/**
 * FXML Controller class Designed for the client.
 *
 * @author martijn
 */
public class WaitingScreenClientController implements Initializable {

    @FXML
    private Button btReturnLobbyClient;

    @FXML
    private ListView lvJoinedPlayersClient;

    private final Database database = new Database();
    WaitingScreen waitingScreen = new WaitingScreen();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //screenSetup();
    }

    /**
     * Gets called after pressing the return to lobby button. It returns the
     * player to the lobby.
     */
    public void returnLobby() {

        Stage stage = (Stage) lvJoinedPlayersClient.getScene().getWindow();
        Lobby lobby = new Lobby(stage, waitingScreen.getCurrentUser());

    }

    /**
     * List with users that are joined gets filled.
     */
    public void screenSetup() {
        
        items.add(waitingScreen.getSerGameUsernames());
        lvJoinedPlayersClient.setItems(items);
    }
}
