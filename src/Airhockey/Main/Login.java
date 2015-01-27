package Airhockey.Main;

import Airhockey.Properties.PropertiesManager;
import Airhockey.Renderer.Constants;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
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
public class Login extends Application {

    @FXML
    Button btLogin;

    @FXML
    RadioButton rbEasyLeft;
    @FXML
    RadioButton rbNormalLeft;
    @FXML
    RadioButton rbHardLeft;
    @FXML
    RadioButton rbVeryHardLeft;

    @FXML
    RadioButton rbEasyRight;
    @FXML
    RadioButton rbNormalRight;
    @FXML
    RadioButton rbHardRight;
    @FXML
    RadioButton rbVeryHardRight;

    @FXML
    TextField tfUsername;

    @FXML
    PasswordField tfPassword;

    @FXML
    Button btStartSingleGame;

    Database database;
    Stage primaryStage;
    final ToggleGroup group = new ToggleGroup();

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        Login();
    }

    public void Login() {
        primaryStage = new Stage();
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startSingleGame() {
        setDifficulty();
        primaryStage = (Stage) btLogin.getScene().getWindow();
        primaryStage.close();
        Game game = new Game(primaryStage, null);
        game.startSinglePlayer();
    }

    public void actionlogin() {
        try {
            database = new Database();
            if (database.loginCheck(tfUsername.getText(), tfPassword.getText())) {
                primaryStage = (Stage) btLogin.getScene().getWindow();
                primaryStage.close();
                Lobby lobby = new Lobby(primaryStage, database.getUser(tfUsername.getText()));
                System.out.println("User: " + tfUsername.getText() + " logged in!");
            } else {
                showPopupWindow("Invalid login combination!", "Ok");
                System.out.println("Logged in (no user)!");
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            showPopupWindow("Field is empty!", "Ok");
        }
    }

    public void actionCreateAccount() {
        primaryStage = (Stage) btLogin.getScene().getWindow();
        primaryStage.close();
        CreateAccount createAccount = new CreateAccount(primaryStage);
    }

    private void setDifficulty() {
        if (rbEasyLeft.isSelected()) {
            System.out.println("1");
            PropertiesManager.saveProperty("LEB-Difficulty", "EASY");
        } else if (rbNormalLeft.isSelected()) {
            System.out.println("2");
            PropertiesManager.saveProperty("LEB-Difficulty", "MEDIUM");
        } else if (rbHardLeft.isSelected()) {
            System.out.println("3");
            PropertiesManager.saveProperty("LEB-Difficulty", "HARD");
        } else if (rbVeryHardLeft.isSelected()) {
            System.out.println("4");
            PropertiesManager.saveProperty("LEB-Difficulty", "VERY_HARD");
            System.out.println("vh");
        }
        if (rbEasyRight.isSelected()) {
            System.out.println("5");
            PropertiesManager.saveProperty("REB-Difficulty", "EASY");
        } else if (rbNormalRight.isSelected()) {
            System.out.println("6");
            PropertiesManager.saveProperty("REB-Difficulty", "MEDIUM");
        } else if (rbHardRight.isSelected()) {
            System.out.println("7");
            PropertiesManager.saveProperty("REB-Difficulty", "HARD");
        } else if (rbVeryHardRight.isSelected()) {
            System.out.println("8");
            PropertiesManager.saveProperty("REB-Difficulty", "VERY_HARD");
        }
    }

    protected void showPopupWindow(String message, String buttonText) {
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(tfUsername.getScene().getWindow());
        dialogStage.centerOnScreen();

        Button okButton = new Button(buttonText);
        okButton.setOnAction((ActionEvent arg0) -> {
            dialogStage.close();
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

    public static void main(String[] args) {
        launch(Login.class, args);
    }
}
