/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Mainserver;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
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
