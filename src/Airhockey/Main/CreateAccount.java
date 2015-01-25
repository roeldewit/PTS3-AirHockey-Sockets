/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

/**
 *
 * @author martijn
 */
public class CreateAccount {

    public CreateAccount(Stage primaryStage) {
        createAccountSetUp(primaryStage);
    }

    private void createAccountSetUp(Stage primaryStage) {

        Parent root = null;

        try {
            root = FXMLLoader.load(Lobby.class.getResource("CreateAccount.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
