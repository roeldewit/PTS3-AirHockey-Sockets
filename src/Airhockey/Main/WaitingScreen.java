/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import Airhockey.User.User;
import Airhockey.Utils.Database;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author martijn
 */
public class WaitingScreen {

    Database database = new Database();
    Game game;
    private User CurrentUser;

    public WaitingScreen(Stage stage, boolean isHost, User currentUser) throws Exception {
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
            game.startAsClient(currentUser, "127.0.0.1");
        }
    }

    public WaitingScreen() {
    }

    public User getCurrentUser() {
        return CurrentUser;
    }
}
