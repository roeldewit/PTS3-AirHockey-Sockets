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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.jbox2d.dynamics.Body;

/**
 * Deufault class providing basic methods for rendering of the game frame.
 * This class alone is not enough to render the complete game and needs to be extended.
 *
 * @author Sam
 */
abstract class BaseRenderer implements IRenderer {

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
    protected SideBat blueBat;
    protected SideBat greenBat;
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

    /**
     * Constructor
     *
     * @param primaryStage Used to create a window inside which the game will be displayed.
     * @param game The game of the current player.
     */
    public BaseRenderer(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    /**
     * Creates the window where the game is played in and adds all the visible
     * items and listeners.
     *
     * @param encoder The enoder used to send game data to the clients if this
     * is a multiplayer game.
     * @param playerNumber The number assingned to each player in a game, used
     * for item positioning.
     */
    @Override
    public void start(Encoder encoder, int playerNumber) {
        this.encoder = encoder;
        this.playerNumber = playerNumber;

        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setWidth(Utils.WIDTH + 250);
        primaryStage.setHeight(Utils.HEIGHT + 20);
        primaryStage.centerOnScreen();

        scene = new Scene(mainRoot, Utils.WIDTH, Utils.HEIGHT + 20, Color.web(Constants.COLOR_GRAY));

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

    /**
     * Starts a countdown animation on screen before the start of the game.
     * Starts the game when the countdown has finished.
     */
    protected void startCountDown() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1100),
                new CountDownTimer()));
        timeline.setCycleCount(4);
        timeline.play();
        timeline.setOnFinished((ActionEvent t) -> {
            startGameTimer();
        });
    }

    /**
     * Class that initates each frame for the countdown.
     */
    private class CountDownTimer implements EventHandler<ActionEvent> {

        int number = 3;

        @Override
        public void handle(ActionEvent event) {
            if (number == 0) {
                textEffectAnimation("Go!", false);
            } else {
                textEffectAnimation(String.valueOf(number), false);
                number--;
            }
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

    /**
     * Creates the static non-movable items on screen.
     */
    protected void createStaticItems() {
        triangle = new TriangleLine(4f, 5f, 81f, 5f, 45f, 95f);
        triangleLeft = new TriangleLeftLine(4f, 5f, 45f, 95f);

        redGoal = new Goal(Constants.COLOR_RED);
        blueGoal = new Goal(Constants.COLOR_BLUE);
        greenGoal = new Goal(Constants.COLOR_GREEN);

        root.getChildren().addAll(redGoal.collisionNode,
                blueGoal.collisionNode,
                greenGoal.collisionNode);
    }

    /**
     * Creates the game's textFields.
     */
    protected void createTextFields() {
        roundNumberLabel = new Label("1");
        if (playerNumber > 3) {
            spectatorLabel = new Label("SPECTATING");
        } else {
            spectatorLabel = new Label("");
        }

        double textSize = 24.0;

        player1NameLabel = addTextEffects(new Label(), root, Color.web(Constants.COLOR_RED), 850, 10, textSize);
        player2NameLabel = addTextEffects(new Label(), root, Color.web(Constants.COLOR_BLUE), 850, 40, textSize);
        player3NameLabel = addTextEffects(new Label(), root, Color.web(Constants.COLOR_GREEN), 850, 70, textSize);
        player1ScoreLabel = addTextEffects(new Label("20"), root, Color.WHITE, 970, 10, textSize);
        player2ScoreLabel = addTextEffects(new Label("20"), root, Color.WHITE, 970, 40, textSize);
        player3ScoreLabel = addTextEffects(new Label("20"), root, Color.WHITE, 970, 70, textSize);
        roundTextLabel = addTextEffects(new Label("ROUND:"), root, Color.web(Constants.COLOR_ORANGE), 30, 10, textSize);
        roundNumberLabel = addTextEffects(roundNumberLabel, root, Color.WHITE, 140, 10, textSize);
        spectatorLabel = addTextEffects(spectatorLabel, root, Color.WHITE, 30, 40, textSize);
    }

    /**
     * Applies effects to a given textfield label.
     *
     * @param label Label the effects are applied to.
     * @param group Group to add the label to.
     * @param color Text color of the label.
     * @param x x-axis position of the label.
     * @param y y-axis position of the label.
     * @param size Text size of the label.
     * @return The label with all the effects applied to it.
     */
    private Label addTextEffects(Label label, Group group, Color color, int x, int y, double size) {
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(1.0);
        shadow.setOffsetX(1.0);
        shadow.setColor(Color.BLACK);

        label.setFont(Font.font("Roboto", FontWeight.BOLD, size));
        label.setTextFill(color);
        label.relocate(x, y);
        label.setEffect(shadow);
        group.getChildren().add(label);

        return label;
    }

    @Override
    public void setLabelNames(String p1Name, String p2Name, String p3Name) {
        player1NameLabel.setText(p1Name.toUpperCase() + ": ");
        player2NameLabel.setText(p2Name.toUpperCase() + ": ");
        player3NameLabel.setText(p3Name.toUpperCase() + ": ");
    }

    /**
     * Creates and sets up the canvas where all the items will be drawn on.
     */
    protected void createCanvas() {
        canvas = new Canvas(900, 740);

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

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(3);
    }

    /**
     * Updates the canvas with all the new item values.
     */
    protected void updateFrame() {
        //Clear frame
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //Draw traingle sides
        graphicsContext.strokeOval(367.0, 378.0, 200.0, 200.0);
        graphicsContext.strokeLine(triangleLeft.positionXL, triangleLeft.positionYL, triangleLeft.positionXR, triangleLeft.positionYR);
        graphicsContext.strokeLine(triangle.positionXL, triangle.positionYL, triangle.positionXR, triangle.positionYR);
        graphicsContext.strokeLine(triangle.positionXC, triangle.positionYC, triangle.positionXR, triangle.positionYR);

        //Draw Goals
        graphicsContext.drawImage(redGoal.imageNode, redGoal.topLeftX - 22, redGoal.topLeftY + 10);
        graphicsContext.drawImage(blueGoal.imageNode, blueGoal.topLeftX + 35, blueGoal.topLeftY - 120);
        graphicsContext.drawImage(greenGoal.imageNode, greenGoal.topLeftX + 60, greenGoal.topLeftY - 120);

        //Draw puck
        graphicsContext.drawImage(puck.imageNode, puck.getImagePositionX(), puck.getImagePositionY());

        //Draw Bats
        graphicsContext.drawImage(redBat.imageNode, redBat.getImagePositionX(), redBat.getImagePositionY(), (redBat.diameter + 4f), (redBat.diameter + 4f));
        graphicsContext.drawImage(blueBat.imageNode, blueBat.getImagePositionX(), blueBat.getImagePositionY(), (redBat.diameter + 4f), (redBat.diameter + 4f));
        graphicsContext.drawImage(greenBat.imageNode, greenBat.getImagePositionX(), greenBat.getImagePositionY(), (redBat.diameter + 4f), (redBat.diameter + 4f));
    }

    private void clearFrame() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Shows a popup when the game has finished with all the scores of the player's and a reason why the game was stopped.
     *
     * @param reason Reason why the game was stopped.
     */
    protected void showGameOverPopupWindow(String reason) {
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage.getScene().getWindow());
        dialogStage.setResizable(false);
        dialogStage.setOnCloseRequest((WindowEvent event) -> {
            leave();
        });

        Group dialogRoot = new Group();

        Button okButton = new Button("Exit");
        okButton.relocate(260, 150);
        okButton.setOnAction((ActionEvent arg0) -> {
            dialogStage.close();
            leave();
        });

        double textSize = 20.0;

        addTextEffects(new Label(reason.toUpperCase()), dialogRoot, Color.web("#33CCFF"), 90, 20, 24.0);
        addTextEffects(new Label(player1NameLabel.getText()), dialogRoot, Color.web(Constants.COLOR_RED), 100, 70, textSize);
        addTextEffects(new Label(player2NameLabel.getText()), dialogRoot, Color.web(Constants.COLOR_BLUE), 100, 100, textSize);
        addTextEffects(new Label(player3NameLabel.getText()), dialogRoot, Color.web(Constants.COLOR_GREEN), 100, 130, textSize);
        addTextEffects(new Label(player1ScoreLabel.getText()), dialogRoot, Color.WHITE, 180, 70, textSize);
        addTextEffects(new Label(player2ScoreLabel.getText()), dialogRoot, Color.WHITE, 180, 100, textSize);
        addTextEffects(new Label(player3ScoreLabel.getText()), dialogRoot, Color.WHITE, 180, 130, textSize);

        dialogRoot.getChildren().add(okButton);

        Scene dialogScene = new Scene(dialogRoot, 300, 180, Color.web(Constants.COLOR_GRAY));
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    /**
     * Creates a chatbox on the game screen.
     *
     * @return Group with all the chatbox scene items.
     */
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
                } else if (isMultiplayer) {
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

        if (playerNumber == 1 && isMultiplayer) {
            encoder.sendChatBoxLine(line);
        }
    }

    /**
     * Displays a text animation with text expanding in the center of the screen.
     *
     * @param text Text that is displayed in this animation.
     * @param isRoundTransition True if this is a animation for a new round.
     */
    protected void textEffectAnimation(String text, boolean isRoundTransition) {
        int duration;
        final Label textLabel = new Label();

        textLabel.setText(text);
        textLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        textLabel.setTextFill(Color.web(Constants.COLOR_ORANGE));

        if (isRoundTransition) {
            textLabel.relocate(460, 340);
            duration = 2000;
        } else {
            duration = 1000;
            textLabel.relocate(480, 350);
        }

        root.getChildren().add(textLabel);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), textLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(duration), textLabel);
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
        parallelTransition.setOnFinished((ActionEvent t) -> {
            root.getChildren().remove(textLabel);
        });
    }

    @Override
    public void stop(String reason) {
        showGameOverPopupWindow(reason);
        clearFrame();

        Rectangle rect = new Rectangle(0, 0, 0, 0);
        rect.setWidth(Utils.WIDTH);
        rect.setHeight(Utils.HEIGHT);
        rect.setArcWidth(50);

        root.getChildren().add(rect);

        FillTransition ft = new FillTransition(Duration.millis(2000), rect, Color.TRANSPARENT, Color.GRAY);
        ft.playFromStart();
    }

    /**
     * Closes the game screen and returns to the login screen.
     */
    protected void leave() {
        primaryStage.close();
        Login login = new Login();
        login.Login();
    }

    /**
     * Allows moving of the non-static game items on screen.
     */
    public void startGameTimer() {
        //Implemented in child class.
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

}
