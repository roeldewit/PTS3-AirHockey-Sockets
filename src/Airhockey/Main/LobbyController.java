package Airhockey.Main;

import Airhockey.Renderer.Constants;
import Airhockey.User.Player;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author martijn
 */
public class LobbyController implements Initializable {

    @FXML
    Button btStartGame;

    @FXML
    TextField tfChatbox;

    @FXML
    ListView lvChatbox;

    @FXML
    ListView lvRatingTable;

    @FXML
    TextField tfDescription;

    @FXML
    ListView lvOpenGames;

    Stage primaryStage;
    ObservableList<String> chatItems;
    ObservableList<String> ratingItems;
    ObservableList<String> gameItems;
    ArrayList<User> users;
    Database database;
    User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLobbyLists();

    }

    public LobbyController() {
        database = new Database();

        chatItems = FXCollections.observableArrayList();
        ratingItems = FXCollections.observableArrayList();
        gameItems = FXCollections.observableArrayList();
    }

    public void startGame() {
        gameItems.add(tfDescription.getText());
        lvOpenGames.setItems(gameItems);

//        primaryStage = (Stage) btStartGame.getScene().getWindow();
//        primaryStage.close();
        try {
            User user = database.getUser("TestUser5");
            ArrayList<Player> playerList = new ArrayList();
            Player player = new Player(0, user);
            playerList.add(player);
            playerList.add(new Player(1, new User("TestUser6")));
            playerList.add(new Player(2, new User("TestUser7")));
            //Game g = new Game(primaryStage, false, false);
            //Game multiGame = new Game(primaryStage, playerList, new ArrayList());
            showPopupWindow("Under Construction", "Return to Lobby");
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chatboxSend() {
        if (tfChatbox.getText() != "") {
            chatItems.add(tfChatbox.getText());
            lvChatbox.setItems(chatItems);
            tfChatbox.clear();
        }
    }

    private void setLobbyLists() {
        try {
            users = database.getUsers();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (User user : users) {
            ratingItems.add(user.getUsername() + " : " + user.getRating());
        }
        lvRatingTable.setItems(ratingItems);
    }
    
       protected void showPopupWindow(String message, String buttonText) {
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(tfDescription.getScene().getWindow());
        dialogStage.centerOnScreen();

        Button okButton = new Button(buttonText);
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                dialogStage.close();
            }
        });

        Label label = new Label(message);
        label.setFont(Font.font("Roboto", 24.0));
        label.setTextFill(Color.web(Constants.COLOR_GREEN));
        label.setPadding(new Insets(0, 0, 20, 0));
        label.relocate(100, 10);

        VBox vBox = new VBox();
        vBox.getChildren().add(label);
        vBox.getChildren().add(okButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20, 40, 20, 40));

        Scene dialogScene = new Scene(vBox);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

}
