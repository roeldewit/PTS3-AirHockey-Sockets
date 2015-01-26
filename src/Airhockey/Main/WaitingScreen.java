/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import Airhockey.Serializable.SerializableGame;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

/**
 * Gets decided which fxml contoller gets loaded.
 *
 * @author martijn
 */
public class WaitingScreen {

    Database database = new Database();
    Game game;
    private User CurrentUser;
    SerializableGame SerGame;

    /**
     * Constructor
     *
     * Gets decided which FXML controller gets loaded.
     *
     * @param stage is the current stage
     * @param isHost if host or not
     * @param currentUser of the session
     * @throws Exception
     */
    public WaitingScreen(Stage stage, boolean isHost, User currentUser, Lobby lobby, String ipHost) throws Exception {
        this.CurrentUser = currentUser;
        
        if (isHost == true) {
            Parent root = FXMLLoader.load(getClass().getResource("WaitingScreenHost.fxml"));

            stage.setTitle("Host");
            stage.setScene(new Scene(root));
            stage.show();
            game = new Game(stage);
            game.startAsHost(currentUser);
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("WaitingScreenClient.fxml"));

            stage.setTitle("Client");
            stage.setScene(new Scene(root));
            stage.show();
            game = new Game(stage);
            game.startAsClient(currentUser, ipHost);
        }
    }
    
    /**
     * Sets the new serializable game
     * @param sergame
     */
    public void setSerGame(SerializableGame sergame)
    {
        this.SerGame = sergame;
    }
    
    /**
     * @return list of users
     */
    public ArrayList getSerGameUsernames()
    {
        return this.SerGame.usernames;
    }

    /**
     * Constructor
     */
    public WaitingScreen() {
    }

    /**
     *
     * @return the current user of the session.
     */
    public User getCurrentUser() {
        return CurrentUser;
    }
}
