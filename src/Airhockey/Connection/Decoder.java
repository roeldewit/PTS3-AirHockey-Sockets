package Airhockey.Connection;

import Airhockey.Renderer.IRenderer;
import javafx.application.Platform;

/**
 *
 * @author Sam
 */
public class Decoder {

    private final IRenderer renderer;

    public Decoder(IRenderer renderer) {
        this.renderer = renderer;
    }

    protected void receiveCommand(String command) {
        String[] splitter = command.split(Protocol.SEPERATOR);

        switch (splitter[0]) {
            case Protocol.PUCK_LOCATION:
                Platform.runLater(() -> {
                    renderer.setPuckLocation(Integer.parseInt(splitter[1]), Integer.parseInt(splitter[2]));
                });
                break;

            case Protocol.BOTTOM_BAT_LOCATION:
                Platform.runLater(() -> {
                    renderer.setBottomBatLocation(Integer.parseInt(splitter[1]), Integer.parseInt(splitter[2]));
                });
                break;

            case Protocol.LEFT_BAT_LOCATION:
                Platform.runLater(() -> {
                    renderer.setLeftBatLocation(Integer.parseInt(splitter[1]), Integer.parseInt(splitter[2]));
                });
                break;

            case Protocol.RIGHT_BAT_LOCATION:
                Platform.runLater(() -> {
                    renderer.setRightBatLocation(Integer.parseInt(splitter[1]), Integer.parseInt(splitter[2]));
                });
                break;

            case Protocol.GOAL_MADE:
                Platform.runLater(() -> {
                    renderer.setGoalMade(Integer.parseInt(splitter[1]), Integer.parseInt(splitter[2]), Integer.parseInt(splitter[3]));
                });
                break;
            case Protocol.SET_UP_GAME:
                Platform.runLater(() -> {
                    renderer.setUpGame(splitter[1], splitter[2], splitter[3]);
                });
                break;
        }
    }

}
