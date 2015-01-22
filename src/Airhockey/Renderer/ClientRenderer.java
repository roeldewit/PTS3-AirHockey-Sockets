package Airhockey.Renderer;

import Airhockey.Connection.Encoder;
import Airhockey.Elements.*;
import Airhockey.Main.*;
import Airhockey.Utils.KeyListener;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Sam
 */
public final class ClientRenderer extends BaseRenderer {

    private RenderUtilities rendererUtilities;
    private Position position;
    private final boolean isSpectator;

    public ClientRenderer(Stage primaryStage, Game game, boolean isSpectator) {
        super(primaryStage, game);
        this.isSpectator = isSpectator;
        this.isMultiplayer = true;

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
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
            primaryStage.setTitle("Airhockey Client");
        }

        if (!isSpectator) {
            KeyListener keyListener = new KeyListener(null, playerNumber, encoder);
            scene.setOnKeyPressed(keyListener);
            scene.setOnKeyReleased(keyListener);
        }

        drawShapes();
        createStaticItems();
        createMovableItems(false);
        createOtherItems();

        rendererUtilities = new RenderUtilities(triangle);
    }

    private void createMovableItems(boolean itemsAlreadyOnScreen) {
        puck = new Puck();
        root.getChildren().addAll(puck.node);

        if (!itemsAlreadyOnScreen) {
            redBat = new Bat(50f, 15f, Constants.COLOR_RED);

            blueBat = new LeftBat(31f, 50f, Constants.COLOR_BLUE);
            greenBat = new RightBat(67.5f, 50f, Constants.COLOR_GREEN);

            root.getChildren().addAll(redBat.node);
            root.getChildren().addAll(blueBat.node);
            root.getChildren().addAll(greenBat.node);
        }
    }

    @Override
    public void setPuckLocation(int x, int y) {
        if (playerNumber > 3) {
            puck.setPosition(x, y);
        } else {
            position = rendererUtilities.serverPuckToBlueClientPuck(x, y);
            puck.setPosition(position.x, position.y);
        }
    }

    @Override
    public void setRedBatLocation(int x, int y) {
        if (playerNumber == 2) {
            position = rendererUtilities.batPositionBottomToRight(x);
            redBat.setPosition(position.x, position.y);
        } else if (playerNumber == 3) {
            position = rendererUtilities.batPositionBottomToLeft(x);
            redBat.setPosition(position.x, position.y);
        } else if (playerNumber > 3) {
            redBat.setPosition(x, y);
        }
    }

    @Override
    public void setBlueBatLocation(int x, int y) {
        if (playerNumber == 2) {
            System.out.println("CLIENT RENDERER SBBL");
            blueBat.setPosition(rendererUtilities.batPositionSideToBottom(y), Constants.CENTER_BAT_Y);
        } else {
            blueBat.setPosition(x, y);
        }
    }

    @Override
    public void setGreenBatLocation(int x, int y) {
        if (playerNumber == 3) {
            greenBat.setPosition(rendererUtilities.batPositionSideToBottom(y), Constants.CENTER_BAT_Y);
        } else {
            greenBat.setPosition(x, y);
        }
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
    }

    @Override
    public void resetRound(int round) {
        root.getChildren().removeAll(puck.node);
        newRoundTransition(round);
        createMovableItems(true);
    }
}
