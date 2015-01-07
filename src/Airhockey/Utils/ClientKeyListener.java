package Airhockey.Utils;

//import Airhockey.Client.ClientController;
import Airhockey.Renderer.ClientRenderer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

//public class ClientKeyListener implements EventHandler<KeyEvent> {
//    private final ClientRenderer renderer;
//    //private GameController gameController;
//    private boolean keyAlreadyPressed;
//    //private ClientController clientcontroller;
//
//    public ClientKeyListener(ClientRenderer renderer, ClientController clientController) {
//        this.renderer = renderer;
//        this.clientcontroller = clientController;
//    }
//
//    @Override
//    public void handle(KeyEvent event) {
//        final KeyCode keyCode = event.getCode();
//
//        if (!keyAlreadyPressed) {
//            if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
//                keyAlreadyPressed = true;
//                if (keyCode == KeyCode.LEFT) {
//                    clientcontroller.moveLeft();
//                } else if (keyCode == KeyCode.RIGHT) {
//                    clientcontroller.moveRight();
////                    renderer.startTimer();
//                }
//            }
//        }
//        if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
//            System.out.println("sfasdf");
//            if (keyCode == KeyCode.LEFT) {
//                clientcontroller.stopMovement();
//            } else if (keyCode == KeyCode.RIGHT) {
//                clientcontroller.stopMovement();
//            }
//            keyAlreadyPressed = false;
//        }
//    }
//}
