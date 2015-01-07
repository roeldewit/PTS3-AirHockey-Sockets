package Airhockey.Renderer;

import Airhockey.Connection.Protocol;
import Airhockey.Elements.*;
import Airhockey.Main.Game;
import Airhockey.Main.Login;
import Airhockey.Properties.PropertiesManager;
import Airhockey.Utils.KeyListener;
import Airhockey.Utils.Utils;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author Sam
 */
public class Renderer extends BaseRenderer {

    private Bat lastHittedBat;

    protected Body batBody;
    protected Body leftBatBody;
    protected Body rightBatBody;

    private Button startButton;

    private Shape redGoalShape;
    private Shape blueGoalShape;
    private Shape greenGoalShape;
    private Shape puckShape;

    private boolean canImpulsPuck = true;
    private boolean canCorrectPuckSpeed = true;
    private final boolean isMultiplayer;
    private boolean canUpdate;

    private final BatController batController;
    private Timeline timeline;
    private final ExecutorService threadPool;
    //private RmiServer rmiServer;

    public Renderer(Stage primaryStage, Game game, boolean isMultiplayer) {
        super(primaryStage, game);
        this.batController = new BatController(this);
        this.isMultiplayer = true;
        this.threadPool = Executors.newCachedThreadPool();

        if (isMultiplayer) {
            //rmiServer = game.getRmiServer();
        }

        start();
    }

    public final void start() {
        primaryStage.setTitle("Airhockey");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setWidth(Utils.WIDTH + 250);
        primaryStage.setHeight(Utils.HEIGHT);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest((WindowEvent windowEvent) -> {
            shutDown();
            primaryStage.close();
        });

        final Scene scene = new Scene(mainRoot, Utils.WIDTH, Utils.HEIGHT, Color.web(Constants.COLOR_GRAY));

        KeyListener keyListener = new KeyListener(batController);
        scene.setOnKeyPressed(keyListener);
        scene.setOnKeyReleased(keyListener);
        Utils.world.setContactListener(new BatPuckContactListener());

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(root);
        mainBorderPane.setRight(createChatBox());
        mainRoot.getChildren().add(mainBorderPane);

        Duration duration = Duration.seconds(1.0 / 60.0);
        MyHandler eventHandler = new MyHandler();
        KeyFrame frame = new KeyFrame(duration, eventHandler, null, null);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(frame);

        PropertiesManager.saveProperty("REB-Difficulty", "HARD");

        drawShapes();
        createFixedItems();
        createMovableItems();
        createScreenStuff();
        linkPlayersToBats();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void linkPlayersToBats() {
        game.addPlayerToBat(1, bat);
        game.addPlayerToBat(2, leftBat);
        game.addPlayerToBat(3, rightBat);
    }

    @Override
    protected void createScreenStuff() {
        super.createScreenStuff();

        startButton = new Button();
        startButton.setLayoutX(30);
        startButton.setLayoutY((45));
        startButton.setText("Start");
        startButton.setStyle("-fx-font: 14px Roboto;  -fx-padding: 5 10 5 10; -fx-background-color: #D23641; -fx-text-fill: white;  -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 1,1,1,1 );");
        startButton.setOnAction((ActionEvent event) -> {
            canUpdate = true;
            timeline.playFromStart();
            startButton.setDisable(true);
            startButton.setVisible(false);
        });

        root.getChildren().add(startButton);
    }

    private void createMovableItems() {
        puck = new Puck(50, 45);

        if (batBody != null) {
            bat = new Bat(batBody.getPosition().x, batBody.getPosition().y, Constants.COLOR_RED);
        } else {
            bat = new Bat(50f, 15f, Constants.COLOR_RED);
        }
        leftBat = new LeftBat(31f, 50f, Constants.COLOR_BLUE);
        rightBat = new RightBat(67.5f, 50f, Constants.COLOR_GREEN);

        root.getChildren().addAll(puck.node);
        root.getChildren().addAll(bat.node, bat.imageNode);
        root.getChildren().addAll(leftBat.node, leftBat.imageNode);
        root.getChildren().addAll(rightBat.node, rightBat.imageNode);

        puckShape = (Shape) puck.node;

        puckBody = (Body) puck.node.getUserData();
        batBody = (Body) bat.node.getUserData();
        leftBatBody = (Body) leftBat.node.getUserData();
        rightBatBody = (Body) rightBat.node.getUserData();
    }

    @Override
    protected void createFixedItems() {
        super.createFixedItems();

        redGoalShape = (Shape) redGoal.collisionNode;
        blueGoalShape = (Shape) blueGoal.collisionNode;
        greenGoalShape = (Shape) greenGoal.collisionNode;
    }

    private void checkGoal() {
        Shape redGoalIntersect = Shape.intersect(redGoalShape, puckShape);
        Shape blueGoalIntersect = Shape.intersect(blueGoalShape, puckShape);
        Shape greenGoalIntersect = Shape.intersect(greenGoalShape, puckShape);

        if (redGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, bat);
        } else if (blueGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, leftBat);
        } else if (greenGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, leftBat);
        }
    }

    private class MyHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            //Create time step. Set Iteration count 8 for velocity and 3 for positions
            if (!threadPool.isShutdown()) {
                Utils.world.step(1.0f / 40.f, 8, 3);

                threadPool.execute(new CalulationTask());
            }
        }
    }

    private class CalulationTask extends Task<Void> {

        private float puckBodyPosX;
        private float puckBodyPosY;
        private float batBodyPosX;
        private float batBodyPosY;
        private float leftBatBodyPosX;
        private float leftBatBodyPosY;
        private float rightBatBodyPosX;
        private float rightBatBodyPosY;

        public CalulationTask() {
            call();
        }

        @Override
        protected Void call() {
            if (canImpulsPuck) {
                Random randomizer = new Random();
                int horizontalMovement = randomizer.nextInt(8) - 4;
                horizontalMovement = (horizontalMovement > -1) ? horizontalMovement + 50 : horizontalMovement - 50;

                int verticalMovement = randomizer.nextInt(8) - 4;
                verticalMovement = (verticalMovement > -1) ? verticalMovement + 50 : verticalMovement - 50;

                puckBody.applyLinearImpulse(new Vec2((float) horizontalMovement, (float) verticalMovement), puckBody.getWorldCenter());
                canImpulsPuck = false;
            }

            puckBodyPosX = Utils.toPixelPosX(puckBody.getPosition().x);
            puckBodyPosY = Utils.toPixelPosY(puckBody.getPosition().y);

            batBodyPosX = Utils.toPixelPosX(batBody.getPosition().x);
            batBodyPosY = Utils.toPixelPosY(batBody.getPosition().y);

            leftBatBodyPosX = Utils.toPixelPosX(leftBatBody.getPosition().x);
            leftBatBodyPosY = Utils.toPixelPosX(leftBatBody.getPosition().y);

            rightBatBodyPosX = Utils.toPixelPosX(rightBatBody.getPosition().x);
            rightBatBodyPosY = Utils.toPixelPosX(rightBatBody.getPosition().y);

            if (isMultiplayer) {
                encoder.sendPuckLocation(puckBodyPosX, puckBodyPosY);
                encoder.sendBottomBatLocation(batBodyPosX, batBodyPosY);
                encoder.sendLeftBatLocation(leftBatBodyPosX, leftBatBodyPosY);
                encoder.sendRightBatLocation(rightBatBodyPosX, rightBatBodyPosY);
            }

            if (canCorrectPuckSpeed) {
                correctPuckSpeed();
                canCorrectPuckSpeed = false;
            } else {
                canCorrectPuckSpeed = true;
            }

            checkGoal();
            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            threadCallback(puckBodyPosX, puckBodyPosY, batBodyPosX, batBodyPosY);
        }
    }

    private synchronized void threadCallback(float puckBodyPosX, float puckBodyPosY, float batBodyPosX, float batBodyPosY) {
        if (canUpdate) {
            puck.setPosition(puckBodyPosX, puckBodyPosY);
            bat.setPosition(batBodyPosX, batBodyPosY);

            batController.controlCenterBat(batBodyPosX);

            if (isMultiplayer) {
                batController.controlLeftBat(Utils.toPixelPosY(leftBatBody.getPosition().y));
                batController.controlRightBat(Utils.toPixelPosY(rightBatBody.getPosition().y));
            } else {
                moveLeftEnemyBat(puckBodyPosY);
                moveRightEnemyBat(puckBodyPosY);
            }
        }
    }

    private void moveLeftEnemyBat(float puckBodyPosY) {
        Body leftEnemyBatBody = (Body) leftBat.node.getUserData();
        float leftEnemyBatPositionY = Utils.toPixelPosY(leftEnemyBatBody.getPosition().y);

        leftBat.stop();
        if (puckBodyPosY > leftEnemyBatPositionY) {
            if (leftEnemyBatPositionY < Constants.BAT_MIN_Y) {
                leftBat.moveDown(puckBody);
            }
        } else if (puckBodyPosY < leftEnemyBatPositionY) {
            if (leftEnemyBatPositionY > Constants.BAT_MAX_Y) {
                leftBat.moveUp(puckBody);
            }
        }
    }

    private void moveRightEnemyBat(float puckBodyPosY) {
        Body rightEnemyBatBody = (Body) rightBat.node.getUserData();
        float rightEnemyBatPositionY = Utils.toPixelPosY(rightEnemyBatBody.getPosition().y);

        rightBat.stop();
        if (puckBodyPosY - 5 > rightEnemyBatPositionY + 5) {
            if (rightEnemyBatPositionY < Constants.BAT_MIN_Y) {
                rightBat.moveDown(puckBody);
            }
        } else if (puckBodyPosY + 5 < rightEnemyBatPositionY - 5) {
            if (rightEnemyBatPositionY > Constants.BAT_MAX_Y) {
                rightBat.moveUp(puckBody);
            }
        }
    }

    @Override
    public void resetRound(int round) {
        canUpdate = false;
        timeline.stop();

        Utils.world.destroyBody(puckBody);
        Utils.world.destroyBody(batBody);
        Utils.world.destroyBody(leftBat.getBody());
        Utils.world.destroyBody(rightBat.getBody());

        root.getChildren().removeAll(puck.node, puck.imageNode);
        root.getChildren().removeAll(bat.node, bat.imageNode);
        root.getChildren().removeAll(leftBat.node, leftBat.imageNode);
        root.getChildren().removeAll(rightBat.node, rightBat.imageNode);

        newRoundTransition(round);
        roundNumberLabel.setText(Integer.toString(round));

        createMovableItems();
        linkPlayersToBats();
    }

    @Override
    protected void newRoundTransition(int round) {
        super.newRoundTransition(round);
        parallelTransition.setOnFinished(new OnAnimationCompletionListener());
    }

    private class OnAnimationCompletionListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            canImpulsPuck = true;
            canUpdate = true;
            timeline.playFromStart();
        }
    }

    protected void stop() {
        Rectangle rect = new Rectangle(0, 0, 0, 0);
        rect.setWidth(Utils.WIDTH);
        rect.setHeight(Utils.HEIGHT);
        rect.setArcWidth(50);

        root.getChildren().add(rect);

        FillTransition ft = new FillTransition(Duration.millis(2000), rect, Color.TRANSPARENT, Color.GRAY);
        ft.playFromStart();

        shutDown();
    }

    private void shutDown() {
        canUpdate = false;
        timeline.stop();
        threadPool.shutdownNow();

        primaryStage.close();
        Login login = new Login();
        login.Login();
        System.out.println("shutdonwl");
    }

    private class BatPuckContactListener implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture fA = contact.getFixtureA();
            Fixture fB = contact.getFixtureB();

            if (fA == puck.getFixture() || fB == puck.getFixture()) {
                if (fA == bat.getFixture() || fB == bat.getFixture()) {
                    lastHittedBat = bat;
                } else if (fA == leftBat.getFixture() || fB == leftBat.getFixture()) {
                    lastHittedBat = leftBat;
                } else if (fA == rightBat.getFixture() || fB == rightBat.getFixture()) {
                    lastHittedBat = rightBat;
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }
    }

    public BatController getBatController() {
        return batController;
    }
}
