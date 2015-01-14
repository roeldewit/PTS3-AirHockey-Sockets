package Airhockey.Renderer;

import Airhockey.Connection.Encoder;
import Airhockey.Elements.*;
import Airhockey.Main.*;
import Airhockey.Utils.KeyListener;
import Airhockey.Utils.Utils;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Sam
 */
public final class ClientRenderer extends BaseRenderer {

    private RenderUtilities rendererUtilities;
    private Position position;
    private boolean isSpectator;

    public ClientRenderer(Stage primaryStage, Game game, boolean isSpectator) {
        super(primaryStage, game);

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
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setWidth(Utils.WIDTH + 250);
        primaryStage.setHeight(Utils.HEIGHT);
        primaryStage.centerOnScreen();

        final Scene scene = new Scene(mainRoot, Utils.WIDTH, Utils.HEIGHT, Color.web(Constants.COLOR_GRAY));

        if (!isSpectator) {
            KeyListener keyListener = new KeyListener(null, playerNumber, encoder);
            scene.setOnKeyPressed(keyListener);
            scene.setOnKeyReleased(keyListener);
        }

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(root);
        mainBorderPane.setRight(createChatBox());
        mainRoot.getChildren().add(mainBorderPane);

        drawShapes();
        createStaticItems();
        createMovableItems(false);
        createOtherItems();

        rendererUtilities = new RenderUtilities(triangle);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createMovableItems(boolean itemsAlreadyOnScreen) {
        puck = new Puck();
        root.getChildren().addAll(puck.node, puck.imageNode);

        if (!itemsAlreadyOnScreen) {
            redBat = new Bat(50f, 15f, Constants.COLOR_RED);

            blueBat = new LeftBat(31f, 50f, Constants.COLOR_BLUE);
            greenBat = new RightBat(67.5f, 50f, Constants.COLOR_GREEN);

            root.getChildren().addAll(redBat.node, redBat.imageNode);
            root.getChildren().addAll(blueBat.node, blueBat.imageNode);
            root.getChildren().addAll(greenBat.node, greenBat.imageNode);
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
        root.getChildren().removeAll(puck.node, puck.imageNode);
        newRoundTransition(round);
        createMovableItems(true);
    }
}
