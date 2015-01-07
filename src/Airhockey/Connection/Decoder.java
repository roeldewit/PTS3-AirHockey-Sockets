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
                renderer.setBottomBatLocation(Float.parseFloat(splitter[1]), Float.parseFloat(splitter[2]));
                break;

            case Protocol.LEFT_BAT_LOCATION:
                renderer.setLeftBatLocation(Float.parseFloat(splitter[1]), Float.parseFloat(splitter[2]));
                break;

            case Protocol.RIGHT_BAT_LOCATION:
                renderer.setRightBatLocation(Float.parseFloat(splitter[1]), Float.parseFloat(splitter[2]));
                break;

        }
    }

}
