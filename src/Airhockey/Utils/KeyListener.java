package Airhockey.Utils;

import Airhockey.Renderer.BatController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Class containing the key listener
 *
 * @author Roel
 */
public class KeyListener implements EventHandler<KeyEvent> {

    private final BatController batController;
    private boolean keyAlreadyPressed;

    /**
     * Constructor
     *
     * @param batController BatController
     */
    public KeyListener(BatController batController) {
        this.batController = batController;
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
                    batController.startBatMovement(BatController.LEFT);
                } else if (keyCode == KeyCode.RIGHT) {
                    batController.startBatMovement(BatController.RIGHT);
                }
            }
        }
        if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            if (keyCode == KeyCode.LEFT) {
                batController.stopBatMovement();
            } else if (keyCode == KeyCode.RIGHT) {
                batController.stopBatMovement();
            }
            keyAlreadyPressed = false;
        }
    }
}
