package Airhockey.Renderer;

import Airhockey.Connection.Encoder;
import Airhockey.Elements.*;
import Airhockey.Main.Game;
import Airhockey.Main.Login;
import Airhockey.Utils.Utils;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Sam
 */
class BaseRenderer implements IRenderer {

    protected Label player1NameLabel;
    protected Label player2NameLabel;
    protected Label player3NameLabel;
    protected Label player1ScoreLabel;
    protected Label player2ScoreLabel;
    protected Label player3ScoreLabel;
    protected Label roundTextLabel;
    protected Label roundNumberLabel;
    protected Label spectatorLabel;

    protected final Group root = new Group();
    protected final Group mainRoot = new Group();
    protected Scene scene;
    protected BorderPane mainBorderPane;
    private ListView chatBox;

    protected final Stage primaryStage;
    protected final Game game;

    protected int playerNumber;
    protected boolean isMultiplayer;

    protected Puck puck;
    protected Bat redBat;
    protected LeftBat blueBat;
    protected RightBat greenBat;
    protected TriangleLine triangle;
    protected TriangleLeftLine triangleLeft;
    protected Goal redGoal;
    protected Goal blueGoal;
    protected Goal greenGoal;

    protected Body puckBody;

    protected Encoder encoder;

    protected GraphicsContext graphicsContext;
    private Canvas canvas;

    protected ParallelTransition parallelTransition;
    private ObservableList<String> chatBoxData;

    double centerPointX;
    double centerPointY;

    public BaseRenderer(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    @Override
    public void start(Encoder encoder, int playerNumber) {
        this.encoder = encoder;
        this.playerNumber = playerNumber;

        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setWidth(Utils.WIDTH + 250);
        primaryStage.setHeight(Utils.HEIGHT);
        primaryStage.centerOnScreen();

        scene = new Scene(mainRoot, Utils.WIDTH, Utils.HEIGHT, Color.web(Constants.COLOR_GRAY));

        mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(root);
        mainBorderPane.setRight(createChatBox());
        mainBorderPane.getCenter().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            System.out.println("GAINED FOCUS");
            mainBorderPane.getCenter().requestFocus();
        });

        mainRoot.getChildren().add(mainBorderPane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    protected void startCountDown() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                new CountDownTimer()));
        timeline.setCycleCount(3);
        timeline.play();
        timeline.setOnFinished((ActionEvent t) -> {
            startGameTimer();
        });
    }

    /**
     * Class that initates each frame.
     */
    private class CountDownTimer implements EventHandler<ActionEvent> {

        int number = 3;

        @Override
        public void handle(ActionEvent event) {
            textEffectTransition(String.valueOf(number));
            number--;
        }
    }

    @Override
    public void setTextFields(int field, int score) {
        String value = String.valueOf(score);

        switch (field) {
            case Constants.P1_SCORE:
                player1ScoreLabel.setText(value);
                break;
            case Constants.P2_SCORE:
                player2ScoreLabel.setText(value);
                break;
            case Constants.P3_SCORE:
                player3ScoreLabel.setText(value);
                break;
        }
    }

    protected void createOtherItems() {
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(1.0);
        shadow.setOffsetX(1.0);
        shadow.setColor(Color.BLACK);

        player1NameLabel = new Label();
        player2NameLabel = new Label();
        player3NameLabel = new Label();
        player1ScoreLabel = new Label("20");
        player2ScoreLabel = new Label("20");
        player3ScoreLabel = new Label("20");
        roundTextLabel = new Label("ROUND:");
        roundNumberLabel = new Label("1");
        if (playerNumber > 3) {
            spectatorLabel = new Label("SPECTATING");
        } else {
            spectatorLabel = new Label("");
        }

        player1NameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player2NameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player3NameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player1ScoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player2ScoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player3ScoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        roundTextLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        roundNumberLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        spectatorLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));

        player1NameLabel.setTextFill(Color.web(Constants.COLOR_RED));
        player2NameLabel.setTextFill(Color.web(Constants.COLOR_BLUE));
        player3NameLabel.setTextFill(Color.web(Constants.COLOR_GREEN));
        player1ScoreLabel.setTextFill(Color.WHITE);
        player2ScoreLabel.setTextFill(Color.WHITE);
        player3ScoreLabel.setTextFill(Color.WHITE);
        roundTextLabel.setTextFill(Color.web(Constants.COLOR_ORANGE));
        roundNumberLabel.setTextFill(Color.WHITE);
        spectatorLabel.setTextFill(Color.WHITE);

        player1NameLabel.relocate(850, 10);
        player2NameLabel.relocate(850, 40);
        player3NameLabel.relocate(850, 70);
        player1ScoreLabel.relocate(970, 10);
        player2ScoreLabel.relocate(970, 40);
        player3ScoreLabel.relocate(970, 70);
        roundTextLabel.relocate(30, 10);
        roundNumberLabel.relocate(140, 10);
        spectatorLabel.relocate(30, 40);

        player1NameLabel.setEffect(shadow);
        player2NameLabel.setEffect(shadow);
        player3NameLabel.setEffect(shadow);
        player1ScoreLabel.setEffect(shadow);
        player2ScoreLabel.setEffect(shadow);
        player3ScoreLabel.setEffect(shadow);
        roundTextLabel.setEffect(shadow);
        roundNumberLabel.setEffect(shadow);
        spectatorLabel.setEffect(shadow);

        root.getChildren().addAll(player1NameLabel,
                player2NameLabel,
                player3NameLabel,
                player1ScoreLabel,
                player2ScoreLabel,
                player3ScoreLabel,
                roundTextLabel,
                roundNumberLabel,
                spectatorLabel);
    }

    @Override
    public void setLabelNames(String p1Name, String p2Name, String p3Name) {
        player1NameLabel.setText(p1Name.toUpperCase() + ": ");
        player2NameLabel.setText(p2Name.toUpperCase() + ": ");
        player3NameLabel.setText(p3Name.toUpperCase() + ": ");
    }

    protected void drawShapes() {
        canvas = new Canvas(900, 740);
        // playerNumber = 1;

        double rotation = 0;
        if (playerNumber == 2) {
            canvas.relocate(-70.0, 175.0);
            rotation = canvas.getRotate() - 121.0;
        } else if (playerNumber == 3) {
            canvas.relocate(105.0, 140.0);
            rotation = canvas.getRotate() + 118.5;
        }

        canvas.setRotate(rotation);

        graphicsContext = canvas.getGraphicsContext2D();
        mainRoot.getChildren().add(canvas);

        centerPointX = Utils.WIDTH / 2;
        centerPointY = Utils.HEIGHT / 2;

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(3);
        graphicsContext.strokeOval(centerPointX - 145.0, centerPointY - 6, 200, 200);
    }

    protected void updateFrame() {
        //Clear frame
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //Draw traingle sides
        graphicsContext.strokeOval(centerPointX - 145.0, centerPointY - 6, 200, 200);
        graphicsContext.strokeLine(triangleLeft.positionXL - 22.0f, triangleLeft.positionYL + 12.0f, triangleLeft.positionXR - 22.0f, triangleLeft.positionYR + 12.0f);
        graphicsContext.strokeLine(triangle.positionXL - 22.0f, triangle.positionYL + 12.0f, triangle.positionXR - 22.0f, triangle.positionYR + 12.0f);
        graphicsContext.strokeLine(triangle.positionXC - 22.0f, triangle.positionYC + 12.0f, triangle.positionXR - 22.0f, triangle.positionYR + 12.0f);

        //Draw Goals
        graphicsContext.drawImage(redGoal.imageNode, redGoal.topLeftX - 22, redGoal.topLeftY + 10);
        graphicsContext.drawImage(blueGoal.imageNode, blueGoal.topLeftX + 35, blueGoal.topLeftY - 110);
        graphicsContext.drawImage(greenGoal.imageNode, greenGoal.topLeftX + 60, greenGoal.topLeftY - 120);

        //Draw puck
        graphicsContext.drawImage(puck.imageNode, puck.getImagePositionX(), puck.getImagePositionY());

        //Draw Bats
        graphicsContext.drawImage(redBat.imageNode, redBat.getImagePositionX(), redBat.getImagePositionY(), (redBat.diameter + 4f), (redBat.diameter + 4f));
        graphicsContext.drawImage(blueBat.imageNode, blueBat.getImagePositionX(), blueBat.getImagePositionY(), (redBat.diameter + 4f), (redBat.diameter + 4f));
        graphicsContext.drawImage(greenBat.imageNode, greenBat.getImagePositionX(), greenBat.getImagePositionY(), (redBat.diameter + 4f), (redBat.diameter + 4f));
    }

    protected void showPopupWindow(String message) {
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage.getScene().getWindow());
        dialogStage.centerOnScreen();

        Button okButton = new Button("Close");
        okButton.setOnAction((ActionEvent arg0) -> {
            dialogStage.close();
            leave();
        });

        Label label = new Label(message.toUpperCase());
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

    protected Group createChatBox() {
        chatBoxData = FXCollections.observableArrayList();

        chatBox = new ListView();
        chatBox.setEditable(false);
        chatBox.setItems(chatBoxData);

        TextField inputField = new TextField();
        Button sendButton = new Button();

        sendButton.setText("Send");
        sendButton.setStyle("-fx-font: 14px Roboto;  -fx-padding: 5 10 5 10; -fx-background-color: #D23641; -fx-text-fill: white;  -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 1,1,1,1 );");

        BorderPane chatBoxBorderPane = new BorderPane();
        HBox bottom = HBoxBuilder.create().children(inputField, sendButton).build();
        chatBoxBorderPane.setCenter(chatBox);
        chatBoxBorderPane.setBottom(bottom);
        chatBoxBorderPane.setMinHeight(Utils.HEIGHT - 40);
        chatBoxBorderPane.setMaxWidth(244);

        sendButton.setOnAction((ActionEvent e) -> {
            if (!inputField.getText().equals("")) {
                String line = game.getUsername() + ":    " + inputField.getText();

                if (playerNumber == 1) {
                    addChatBoxLine(line);
                }

                if (isMultiplayer) {
                    encoder.sendChatBoxLine(line);
                }

                inputField.clear();
                mainBorderPane.getCenter().requestFocus();
            }
        });

        Group chatBoxGroup = new Group();
        chatBoxGroup.getChildren().add(chatBoxBorderPane);

        return chatBoxGroup;
    }

    @Override
    public void addChatBoxLine(String line) {
        chatBoxData.add(line);
        chatBox.setItems(chatBoxData);
    }

    protected void textEffectTransition(String text) {
        Label textLabel = new Label();
        textLabel.setText(text);
        textLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        textLabel.setTextFill(Color.web(Constants.COLOR_ORANGE));
        textLabel.relocate(460, 340);

        root.getChildren().add(textLabel);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000), textLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), textLabel);
        scaleTransition.setFromX(2f);
        scaleTransition.setFromY(2f);
        scaleTransition.setToX(8f);
        scaleTransition.setToY(8f);

        parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                scaleTransition
        );

        parallelTransition.playFromStart();
    }

    protected void createStaticItems() {
        triangle = new TriangleLine(0, 4f, 5f, 81f, 5f, 45f, 95f);
        triangleLeft = new TriangleLeftLine(0, 4f, 5f, 45f, 95f);

        redGoal = new Goal(Constants.COLOR_RED);
        blueGoal = new Goal(Constants.COLOR_BLUE);
        greenGoal = new Goal(Constants.COLOR_GREEN);

        root.getChildren().addAll(redGoal.collisionNode,
                blueGoal.collisionNode,
                greenGoal.collisionNode);
    }

    protected void correctPuckSpeed() {
        Vec2 vec = puckBody.getLinearVelocity();
        Vec2 puckBodyCenter = puckBody.getWorldCenter();

        if (Math.abs(vec.x) > Math.abs(vec.y)) {
            if (vec.x > 20) {
                puckBody.applyLinearImpulse(new Vec2(-1.0f, 0.0f), puckBodyCenter);
            } else if (vec.x >= 0 && vec.x < 12) {
                puckBody.applyLinearImpulse(new Vec2(1.0f, 1.0f), puckBodyCenter);
            } else if (vec.x > -12 && vec.x <= 0) {
                puckBody.applyLinearImpulse(new Vec2(-1.0f, -1.0f), puckBodyCenter);
            } else if (vec.x < -20) {
                puckBody.applyLinearImpulse(new Vec2(1.0f, 0.0f), puckBodyCenter);
            }
        } else {
            if (vec.y > 20) {
                puckBody.applyLinearImpulse(new Vec2(0.0f, -1.0f), puckBodyCenter);
            } else if (vec.y >= 0 && vec.y < 12) {
                puckBody.applyLinearImpulse(new Vec2(1.0f, 1.0f), puckBodyCenter);
            } else if (vec.y > -12 && vec.y <= 0) {
                puckBody.applyLinearImpulse(new Vec2(-1.0f, -1.0f), puckBodyCenter);
            } else if (vec.y < -20) {
                puckBody.applyLinearImpulse(new Vec2(0.0f, 1.0f), puckBodyCenter);
            }
        }
    }

    @Override
    public void stop(String reason) {
        showPopupWindow(reason);

        Rectangle rect = new Rectangle(0, 0, 0, 0);
        rect.setWidth(Utils.WIDTH);
        rect.setHeight(Utils.HEIGHT);
        rect.setArcWidth(50);

        root.getChildren().add(rect);

        FillTransition ft = new FillTransition(Duration.millis(2000), rect, Color.TRANSPARENT, Color.GRAY);
        ft.playFromStart();
    }

    protected void leave() {
        primaryStage.close();
        Login login = new Login();
        login.Login();
    }

    @Override
    public void resetRound(int round) {
        //Implemented in child class.    
    }

    @Override
    public void setPuckLocation(int x, int y) {
        //Implemented in child class.
    }

    @Override
    public void setRedBatLocation(int x, int y) {
        //Implemented in child class.
    }

    @Override
    public void setBlueBatLocation(int x, int y) {
        //Implemented in child class.
    }

    @Override
    public void setGreenBatLocation(int x, int y) {
        //Implemented in child class.
    }

    @Override
    public void setGoalMade(int newRound, int scorer, int scorerScore, int against, int againstScore) {
        //Implemented in child class.
    }

    @Override
    public void setUpGame(String p1Name, String p2Name, String p3Name) {
        //Implemented in child class.
    }

    @Override
    public void moveClientBat(int playerNumber, int direction) {
        //Implemented in child class.
    }

    public void startGameTimer() {
        //Implemented in child class.
    }
}
