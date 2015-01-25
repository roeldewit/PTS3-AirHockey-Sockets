package Airhockey.Main;

import Airhockey.Renderer.Constants;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author martijn
 */
public class CreateAccountController implements Initializable {

    @FXML
    Button btCreate;

    @FXML
    TextField tfUsername;

    @FXML
    TextField tfPassword;

    Database database;
    Stage primaryStage;

    public CreateAccountController() {
        database = new Database();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        primaryStage = new Stage();
        //sbtCreate = new Button();
    }

    public void actionCreateNewAccount() {
        try {
            if (database.insertUser(tfUsername.getText(), tfPassword.getText())) {
                Stage primaryStage = (Stage) tfPassword.getScene().getWindow();
                primaryStage.close();
                Login login = new Login();
                login.Login();
                showPopupWindow("User: " + tfUsername.getText() + " created!", "Ok");
            } else {
                showPopupWindow("User not created!", "Ok");
            }
        } catch (SQLException | IOException | IllegalArgumentException ex) {
            //Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
            showPopupWindow("User not created!", "Ok");
        }
    }

    public void actionCancel() {
        Stage primaryStage = (Stage) tfPassword.getScene().getWindow();
        primaryStage.close();
        Login login = new Login();
        login.Login();
    }

    protected void showPopupWindow(String message, String buttonText) {
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(tfUsername.getScene().getWindow());
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
