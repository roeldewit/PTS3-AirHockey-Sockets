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
                    renderer.setPuckLocation(Float.parseFloat(splitter[1]), Float.parseFloat(splitter[2]));
                });
                break;

            case Protocol.BOTTOM_BAT_LOCATION:
                Platform.runLater(() -> {
                    renderer.setBottomBatLocation(Float.parseFloat(splitter[1]), Float.parseFloat(splitter[2]));
                });
                break;

            case Protocol.LEFT_BAT_LOCATION:
                Platform.runLater(() -> {
                    renderer.setLeftBatLocation(Float.parseFloat(splitter[1]), Float.parseFloat(splitter[2]));
                });
                break;

            case Protocol.RIGHT_BAT_LOCATION:
                Platform.runLater(() -> {
                    renderer.setRightBatLocation(Float.parseFloat(splitter[1]), Float.parseFloat(splitter[2]));
                });
                break;

            case Protocol.GOAL_MADE:
                Platform.runLater(() -> {
                    renderer.setGoalMade();
                });
                break;
            case Protocol.SET_UP_GAME:
                Platform.runLater(() -> {
                    renderer.setUpGame(splitter[1], splitter[1], splitter[1]);
                });
                break;
        }
    }

}
