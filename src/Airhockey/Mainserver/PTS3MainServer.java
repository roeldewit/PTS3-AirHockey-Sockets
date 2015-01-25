package Airhockey.Mainserver;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *  this is the main server
 * @author stijn
 */
public class PTS3MainServer extends Application {

    private MainLobby mainLobby = null;

    @Override
    public void start(Stage primaryStage) {
        mainLobby = new MainLobby();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
