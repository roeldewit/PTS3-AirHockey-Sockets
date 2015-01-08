package Airhockey.Renderer;

//import Airhockey.Utils.ClientKeyListener;
import Airhockey.Connection.Encoder;
import Airhockey.Elements.*;
import Airhockey.Main.*;
import Airhockey.Utils.Utils;
import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Sam
 */
public final class ClientRenderer extends BaseRenderer {

    private int xPosition = 100;
    private int yPosition = 100;

    private RenderUtilities rendererUtilities;

    public ClientRenderer(Stage primaryStage, Game game) {
        super(primaryStage, game);
    }

    @Override
    public void start(Encoder encoder) {
        super.start(encoder);

        rendererUtilities = new RenderUtilities(triangle);

        primaryStage.setTitle("AirhockeyClient");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setWidth(Utils.WIDTH + 250);
        primaryStage.setHeight(Utils.HEIGHT);
        primaryStage.centerOnScreen();

        final Scene scene = new Scene(mainRoot, Utils.WIDTH, Utils.HEIGHT, Color.web(Constants.COLOR_GRAY));

        //ClientKeyListener keyListener = new ClientKeyListener(this, clientController);
//        scene.setOnKeyPressed(keyListener);
//        scene.setOnKeyReleased(keyListener);
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(root);
        mainBorderPane.setRight(createChatBox());
        mainRoot.getChildren().add(mainBorderPane);

        drawShapes();
        createMovableItems();
        createFixedItems();
        createScreenStuff();

        //setUpGame();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    public void setItemPosition(Location location) {
//
//    }
//    public void startTimer() {
//        Timer t = new Timer();
//        t.scheduleAtFixedRate(new timerTaskZ(), 0, 1000 / 60);
//    }
//
//    public class timerTaskZ extends TimerTask {
//
//        @Override
//        public void run() {
//
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    leftBat.setPosition(xPosition, yPosition);
//
//                    bat.setPosition(rendererUtilities.batPositionSideToBottom(yPosition), 600);
//                    yPosition++;
//
//                }
//            });
//        }
//    }
    private void createMovableItems() {
        puck = new Puck(50, 45);

        bat = new Bat(50f, 15f, Constants.COLOR_RED);

        leftBat = new LeftBat(31f, 50f, Constants.COLOR_BLUE);
        rightBat = new RightBat(67.5f, 50f, Constants.COLOR_GREEN);

        root.getChildren().addAll(puck.node, puck.imageNode);
        root.getChildren().addAll(bat.node, bat.imageNode);
        root.getChildren().addAll(leftBat.node, leftBat.imageNode);
        root.getChildren().addAll(rightBat.node, rightBat.imageNode);
    }

    @Override
    public void setPuckLocation(int x, int y) {
        puck.setPosition(x, y);
    }

    @Override
    public void setBottomBatLocation(int x, int y) {
        bat.setPosition(x, y);
    }

    @Override
    public void setLeftBatLocation(int x, int y) {
        leftBat.setPosition(rendererUtilities.batPositionSideToBottom((int) y), Constants.CENTER_BAT_Y);
    }

    @Override
    public void setRightBatLocation(int x, int y) {
        rightBat.setPosition(x, y);
    }

    @Override
    public void setGoalMade(int newRound, int scorer, int against) {
        resetRound(newRound);
    }

    @Override
    public void resetRound(int round) {
        root.getChildren().removeAll(puck.node, puck.imageNode);
        root.getChildren().removeAll(bat.node, bat.imageNode);
        root.getChildren().removeAll(leftBat.node, leftBat.imageNode);
        root.getChildren().removeAll(rightBat.node, rightBat.imageNode);

        newRoundTransition(round);

        createMovableItems();
    }

    protected void stop() {
        Rectangle rect = new Rectangle(0, 0, 0, 0);
        rect.setWidth(Utils.WIDTH);
        rect.setHeight(Utils.HEIGHT);
        rect.setArcWidth(50);

        root.getChildren().add(rect);

        FillTransition ft = new FillTransition(Duration.millis(2000), rect, Color.TRANSPARENT, Color.GRAY);
        ft.playFromStart();
    }

}
