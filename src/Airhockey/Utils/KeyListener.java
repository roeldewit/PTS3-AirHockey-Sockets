package Airhockey.Utils;

import Airhockey.Connection.Encoder;
import Airhockey.Renderer.BatController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Class containing the key listener
 *
 * @author Sam
 */
public class KeyListener implements EventHandler<KeyEvent> {

    private final BatController batController;
    private final int playerNumber;
    private final Encoder encoder;
    private boolean keyAlreadyPressed;

    /**
     * Constructor
     *
     * @param batController BatController
     * @param playerNumber
     * @param encoder
     */
    public KeyListener(BatController batController, int playerNumber, Encoder encoder) {
        this.batController = batController;
        this.playerNumber = playerNumber;
        this.encoder = encoder;
    }

    /**
     * Handle a key event
     *
     * @param event Key event
     */
    @Override
    public void handle(KeyEvent event) {
        final KeyCode keyCode = event.getCode();

        if (!keyAlreadyPressed) {
            if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                keyAlreadyPressed = true;
                if (keyCode == KeyCode.LEFT) {
                    switch (playerNumber) {
                        case 1:
                            batController.startBatMovement(BatController.LEFT);
                            break;
                        case 2:
                            encoder.sendClientBatMovementDirection(playerNumber, BatController.UP);
                            break;
                        case 3:
                            encoder.sendClientBatMovementDirection(playerNumber, BatController.DOWN);
                            break;
                    }
                } else if (keyCode == KeyCode.RIGHT) {
                    switch (playerNumber) {
                        case 1:
                            batController.startBatMovement(BatController.RIGHT);
                            break;
                        case 2:
                            encoder.sendClientBatMovementDirection(playerNumber, BatController.DOWN);
                            break;
                        case 3:
                            encoder.sendClientBatMovementDirection(playerNumber, BatController.UP);
                            break;
                    }
                }
            }
        }
        if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            if (keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT) {
                switch (playerNumber) {
                    case 1:
                        batController.stopBatMovement();
                        break;
                    case 2:
                        encoder.sendClientBatMovementDirection(playerNumber, BatController.STOP);
                        break;
                    case 3:
                        encoder.sendClientBatMovementDirection(playerNumber, BatController.STOP);
                        break;
                }
            }
            keyAlreadyPressed = false;
        }
    }
}
