package Airhockey.Main;

import Airhockey.Renderer.Constants;
import Airhockey.User.*; 
import Airhockey.Utils.Database;
import java.net.URL;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;

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
    Lobby lobby;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLobbyLists();
    }

    public LobbyController() {
//        database = new Database();

        chatItems = FXCollections.observableArrayList();
        ratingItems = FXCollections.observableArrayList();
        gameItems = FXCollections.observableArrayList();
    }

    public void startGame() {
        lobby.addWaitingGame(tfDescription.getText());
        lobby.startGame();
    }

    public void chatboxSend() {
        lobby.writeLine(tfChatbox.getText());
        tfChatbox.clear();
    }

    public void StartGameList() {
        lobby.startGameList();
    }

    public void updateChatbox(String person, String text) {
        chatItems.add(person + ":" + text);
        lvChatbox.setItems(chatItems);
    }

    public void updateGameList(String description, String id) {
        gameItems.add(id + ":" + description);
        lvOpenGames.setItems(gameItems);
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    private void setLobbyLists() {
//        try {
//            users = database.getUsers();
//        } catch (IOException | SQLException ex) {
//            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        for (User user : users) {
//            ratingItems.add(user.getUsername() + " : " + user.getRating());
//        }
//        lvRatingTable.setItems(ratingItems);
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
