package Airhockey.Renderer;

import Airhockey.Connection.Encoder;
import Airhockey.Elements.*;
import Airhockey.Main.*;
import Airhockey.Utils.KeyListener;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Class extending the BaseRenderer and adding functionallity for a game played by a client.
 * This class does not have any game physics calculations. Only used do execute any commands received from the host.
 *
 * @author Sam
 */
public final class ClientRenderer extends BaseRenderer {

    private final boolean isSpectator;

    /**
     * Constructor
     *
     * @param primaryStage Used to create a window inside which the game will be displayed.
     * @param game The game of the current client player.
     * @param isSpectator True if the current player is a spectator and therefore doesn't participate in this game.
     */
    public ClientRenderer(Stage primaryStage, Game game, boolean isSpectator) {
        super(primaryStage, game);
        this.isSpectator = isSpectator;
        this.isMultiplayer = true;

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            encoder.sendLeavingGame(playerNumber);
            game.leaveGame();
            super.leave();
        });
    }

    @Override
    public void start(Encoder encoder, int playerNumber) {
        super.start(encoder, playerNumber);

        if (isSpectator) {
            primaryStage.setTitle("Airhockey Spectating");
        } else {
            primaryStage.setTitle("Airhockey");
        }

        if (!isSpectator) {
            KeyListener keyListener = new KeyListener(null, playerNumber, encoder);
            scene.setOnKeyPressed(keyListener);
            scene.setOnKeyReleased(keyListener);
        }

        createCanvas();
        createStaticItems();
        createMovableItems(false);
        createTextFields();
    }

    /**
     * Creates the movable items on the screen which include the puck and the
     * three bats.
     *
     * @param itemsAlreadyOnScreen True if the items are already show on the screen
     * and therefore do not have to be created again.
     */
    private void createMovableItems(boolean itemsAlreadyOnScreen) {
        puck = new Puck();
        root.getChildren().addAll(puck.node);

        if (!itemsAlreadyOnScreen) {
            redBat = new Bat(48f, 15f, Bat.CENTER_BAT);

            blueBat = new SideBat(31f, 50f, Bat.LEFT_BAT, false);
            greenBat = new SideBat(64.5f, 50f, Bat.RIGHT_BAT, false);

            root.getChildren().addAll(redBat.node);
            root.getChildren().addAll(blueBat.node);
            root.getChildren().addAll(greenBat.node);
        }
    }

    @Override
    public void setPuckLocation(int x, int y) {
        puck.setPosition(x, y);
        updateFrame();
    }

    @Override
    public void setRedBatLocation(int x, int y) {
        redBat.setPosition(x, y);
    }

    @Override
    public void setBlueBatLocation(int x, int y) {
        blueBat.setPosition(x, y);
    }

    @Override
    public void setGreenBatLocation(int x, int y) {
        greenBat.setPosition(x, y);
    }

    @Override
    public void setGoalMade(int newRound, int scorer, int scorerScore, int against, int againstScore) {
        setTextFields(scorer, scorerScore);
        setTextFields(against, againstScore);

        resetRound(newRound);
    }

    @Override
    public void setUpGame(String p1Name, String p2Name, String p3Name) {
        super.setLabelNames(p1Name, p2Name, p3Name);
        super.updateFrame();
        if (!isSpectator) {
            super.startCountDown();
        }
    }

    @Override
    public void resetRound(int round) {
        root.getChildren().removeAll(puck.node);
        roundNumberLabel.setText(Integer.toString(round));
        textEffectAnimation("Round " + round, true);
        createMovableItems(true);
    }
}
