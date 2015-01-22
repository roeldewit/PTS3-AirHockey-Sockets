package Airhockey.Renderer;

import Airhockey.Connection.Encoder;
import Airhockey.Elements.*;
import Airhockey.Main.Game;
import Airhockey.Properties.PropertiesManager;
import Airhockey.Utils.KeyListener;
import Airhockey.Utils.Utils;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
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
    private boolean canUpdate;
    private boolean canMoveItems;

    private final BatController batController;
    private Timeline timeline;
    private ExecutorService threadPool;

    public Renderer(Stage primaryStage, Game game, boolean isMultiplayer) {
        super(primaryStage, game);
        this.batController = new BatController(this);
        this.isMultiplayer = isMultiplayer;
        this.threadPool = Executors.newCachedThreadPool();

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            if (isMultiplayer) {
                game.leaveGame();
            }

            shutDown();
            super.leave();
        });
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
    public final void start(Encoder encoder, int playerNumber) {
        super.start(encoder, playerNumber);

        primaryStage.setTitle("Airhockey");

        KeyListener keyListener = new KeyListener(batController, playerNumber, encoder);
        mainRoot.setOnKeyPressed(keyListener);
        mainRoot.setOnKeyReleased(keyListener);

        Utils.world.setContactListener(new BatPuckContactListener());

        Duration duration = Duration.seconds(1.0 / 60.0);
        FrameTimer eventHandler = new FrameTimer();
        KeyFrame frame = new KeyFrame(duration, eventHandler, null, null);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(frame);

        PropertiesManager.saveProperty("REB-Difficulty", "HARD");

        drawShapes();
        createStaticItems();
        createMovableItems();
        linkPlayersToBats();
        super.createOtherItems();

        canUpdate = true;
        timeline.playFromStart();

        startCountDown();

        //createOtherItems();
    }

    @Override
    public void startGameTimer() {
        canMoveItems = true;
    }

    private void linkPlayersToBats() {
        game.addPlayerToBat(1, redBat);
        game.addPlayerToBat(2, blueBat);
        game.addPlayerToBat(3, greenBat);
    }

    @Override
    protected void createOtherItems() {
        super.createOtherItems();

        startButton = new Button();
//        startButton.setLayoutX(30);
//        startButton.setLayoutY((45));
        startButton.setLayoutX(970);
        startButton.setLayoutY((100));
        startButton.setText("Start");
        startButton.setStyle("-fx-font: 14px Roboto;  -fx-padding: 5 10 5 10; -fx-background-color: #D23641; -fx-text-fill: white;  -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 2,2,1,1 );");
        startButton.setOnAction((ActionEvent event) -> {
            canUpdate = true;
            timeline.playFromStart();
            startButton.setDisable(true);
            startButton.setVisible(false);
        });

        root.getChildren().add(startButton);
    }

    /**
     * Creates the movable items on the screen which include the puck and the
     * three bats.
     */
    private void createMovableItems() {
        puck = new Puck();

        if (batBody != null) {
            redBat = new Bat(batBody.getPosition().x, batBody.getPosition().y, Constants.COLOR_RED);
        } else {
            redBat = new Bat(48f, 15f, Constants.COLOR_RED);
        }

        if (leftBatBody != null) {
            blueBat = new LeftBat(leftBatBody.getPosition().x, leftBatBody.getPosition().y, Constants.COLOR_BLUE);
        } else {
            blueBat = new LeftBat(31f, 50f, Constants.COLOR_BLUE);
        }

        if (rightBatBody != null) {
            greenBat = new RightBat(rightBatBody.getPosition().x, rightBatBody.getPosition().y, Constants.COLOR_GREEN);
        } else {
            greenBat = new RightBat(64.5f, 50f, Constants.COLOR_GREEN);
        }

        root.getChildren().addAll(puck.node,
                redBat.node,
                blueBat.node,
                greenBat.node);

        puckShape = (Shape) puck.node;

        puckBody = (Body) puck.node.getUserData();
        batBody = (Body) redBat.node.getUserData();
        leftBatBody = (Body) blueBat.node.getUserData();
        rightBatBody = (Body) greenBat.node.getUserData();
    }

    /**
     * Creates the static items on the screen which include the goals.
     */
    @Override
    protected void createStaticItems() {
        super.createStaticItems();
        redGoalShape = (Shape) redGoal.collisionNode;
        blueGoalShape = (Shape) blueGoal.collisionNode;
        greenGoalShape = (Shape) greenGoal.collisionNode;
    }

    /**
     * This method is called on every game update frame. It checks whether a
     * goal has been made and gives a notice to the game with the scorer and the
     * one the goal was against.
     */
    private synchronized void checkGoal() {
        Shape redGoalIntersect = Shape.intersect(redGoalShape, puckShape);
        Shape blueGoalIntersect = Shape.intersect(blueGoalShape, puckShape);
        Shape greenGoalIntersect = Shape.intersect(greenGoalShape, puckShape);

        if (redGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, redBat, encoder);
        } else if (blueGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, blueBat, encoder);
        } else if (greenGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, greenBat, encoder);
        }
    }

    /**
     * Class that initates each frame.
     */
    private class FrameTimer implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            //Create time step. Set Iteration count 8 for velocity and 3 for positions
            if (canMoveItems) {
                if (!threadPool.isShutdown()) {
                    Utils.world.step(1.0f / 40.f, 8, 3);
                    //Utils.world.step(1.0f / 80.f, 16, 3);

                    threadPool.execute(new CalulationTask());
                }
            } else {
                updateFrame();
            }
        }
    }

    /**
     * Background tast that does all the calculation-heavy work. This class also
     * takes care of sending the data to the clients if this game is a
     * multiplayer game.
     */
    private class CalulationTask extends Task<Void> {

        private float puckBodyPosX;
        private float puckBodyPosY;
        private float batBodyPosX;
        private float batBodyPosY;
        private float leftBatBodyPosX;
        private float leftBatBodyPosY;
        private float rightBatBodyPosX;
        private float rightBatBodyPosY;

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
            leftBatBodyPosY = Utils.toPixelPosY(leftBatBody.getPosition().y);

            rightBatBodyPosX = Utils.toPixelPosX(rightBatBody.getPosition().x);
            rightBatBodyPosY = Utils.toPixelPosY(rightBatBody.getPosition().y);

            if (isMultiplayer) {
                encoder.sendPuckLocation((int) puckBodyPosX, (int) puckBodyPosY);
                encoder.sendRedBatLocation((int) batBodyPosX, (int) batBodyPosY);
                encoder.sendBlueBatLocation((int) leftBatBodyPosX, (int) leftBatBodyPosY);
                encoder.sendLeftBatLocation((int) rightBatBodyPosX, (int) rightBatBodyPosY);
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

    /**
     * Receives the calculated data from the background task
     *
     * @param puckBodyPosX
     * @param puckBodyPosY
     * @param batBodyPosX
     * @param batBodyPosY
     */
    private synchronized void threadCallback(float puckBodyPosX, float puckBodyPosY, float batBodyPosX, float batBodyPosY) {
        if (canUpdate) {
            puck.setPosition(puckBodyPosX, puckBodyPosY);
            redBat.setPosition(batBodyPosX, batBodyPosY);

            batController.controlCenterBat(batBodyPosX);

            if (isMultiplayer) {
                batController.controlLeftBat(Utils.toPixelPosY(leftBatBody.getPosition().y));
                batController.controlRightBat(Utils.toPixelPosY(rightBatBody.getPosition().y));
            } else {
                moveLeftAIBat(puckBodyPosY);
                moveRightAIEnemyBat(puckBodyPosY);
            }

            updateFrame();
        }
    }

    @Override
    public void moveClientBat(int playerNumber, int direction) {
        if (direction == BatController.STOP) {
            switch (playerNumber) {
                case 2:
                    batController.stopLeftBatMovement();
                    break;
                case 3:
                    batController.stopRightBatMovement();
                    break;
            }
        } else {
            switch (playerNumber) {
                case 2:
                    batController.startLeftBatMovement(direction);
                    break;
                case 3:
                    batController.startRightBatMovement(direction);
                    break;
            }
        }
    }

    /**
     * This method controls the movement of the LeftAIBat. The bat is positioned
     * according to the puck's location within a certain height-range.
     *
     * @param puckBodyPosY the y-coordinate of the puck
     */
    private void moveLeftAIBat(float puckBodyPosY) {
        Body leftAIBatBody = (Body) blueBat.node.getUserData();
        float leftAIBatPositionY = Utils.toPixelPosY(leftAIBatBody.getPosition().y);

        blueBat.stop();
        if (puckBodyPosY > leftAIBatPositionY) {
            if (leftAIBatPositionY < Constants.SIDE_BAT_MIN_Y) {
                blueBat.moveDown(puckBody);
            }
        } else if (puckBodyPosY < leftAIBatPositionY) {
            if (leftAIBatPositionY > Constants.SIDE_BAT_MAX_Y) {
                blueBat.moveUp(puckBody);
            }
        }
    }

    /**
     * This method controls the movement of the RightAIBat. The bat is
     * positioned according to the puck's location within a certain
     * height-range.
     *
     * @param puckBodyPosY the y-coordinate of the puck
     */
    private void moveRightAIEnemyBat(float puckBodyPosY) {
        Body rightAIBatBody = (Body) greenBat.node.getUserData();
        float rightAIBatPositionY = Utils.toPixelPosY(rightAIBatBody.getPosition().y);

        greenBat.stop();
        if (puckBodyPosY - 5 > rightAIBatPositionY + 5) {
            if (rightAIBatPositionY < Constants.SIDE_BAT_MIN_Y) {
                greenBat.moveDown(puckBody);
            }
        } else if (puckBodyPosY + 5 < rightAIBatPositionY - 5) {
            if (rightAIBatPositionY > Constants.SIDE_BAT_MAX_Y) {
                greenBat.moveUp(puckBody);
            }
        }
    }

    /**
     * This method gets called after a goal has been made. It resets all movable
     * elements to their original position and calls a animation.
     *
     * @param round The number of the next round.
     */
    @Override
    public void resetRound(int round) {
        shutDown();

        lastHittedBat = null;

        Utils.world.destroyBody(puckBody);
        Utils.world.destroyBody(batBody);
        Utils.world.destroyBody(blueBat.getBody());
        Utils.world.destroyBody(greenBat.getBody());

        root.getChildren().removeAll(puck.node,
                redBat.node,
                blueBat.node,
                greenBat.node);

        roundNumberLabel.setText(String.valueOf(round));
        textEffectTransition("Round " + round);

        createMovableItems();
        linkPlayersToBats();

        threadPool = Executors.newCachedThreadPool();
    }

    /**
     * Displays an animation with the number of the new round.
     *
     * @param text
     * @param round The number of the new round.
     */
    @Override
    protected void textEffectTransition(String text) {
        super.textEffectTransition(text);
        parallelTransition.setOnFinished(new OnAnimationCompletionListener());
    }

    /**
     * Checks when the round animation has finished and starts the frame timer
     * again.
     */
    private class OnAnimationCompletionListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            canImpulsPuck = true;
            canUpdate = true;
            timeline.playFromStart();
        }
    }

    /**
     * Shows a popup on the screen with the reason the game was stopped
     *
     * @param reason
     */
    @Override
    public void stop(String reason) {
        super.stop(reason);
        shutDown();
    }

    /**
     * Stops the timer and the background thread's from updating the game.
     */
    private void shutDown() {
        canUpdate = false;
        timeline.stop();
        threadPool.shutdownNow();
    }

    /**
     * Checks collisions between the puck and the bats to determine the last bat
     * that hit the puck in the event of a goal.
     */
    private class BatPuckContactListener implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture fA = contact.getFixtureA();
            Fixture fB = contact.getFixtureB();

            if (fA == puck.getFixture() || fB == puck.getFixture()) {
                if (fA == redBat.getFixture() || fB == redBat.getFixture()) {
                    lastHittedBat = redBat;
                } else if (fA == blueBat.getFixture() || fB == blueBat.getFixture()) {
                    lastHittedBat = blueBat;
                } else if (fA == greenBat.getFixture() || fB == greenBat.getFixture()) {
                    lastHittedBat = greenBat;
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
            //Not used
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            //Not used
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            //Not used
        }
    }

    @Override
    public void addChatBoxLine(String line) {
        super.addChatBoxLine(line);
        if (isMultiplayer) {
            System.out.println("O HELL NO");
            encoder.sendChatBoxLine(line);
        }
    }
}
