package Airhockey.Main;

import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class Designed for the host.
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
     * Gets called after pressing the return to lobby button. It returns the
     * player to the lobby.
     */
    public void returnLobby() {

        Stage stage = (Stage) lvJoinedPlayers.getScene().getWindow();
        Lobby lobby = new Lobby(stage, waitingScreen.getCurrentUser());

    }

}
