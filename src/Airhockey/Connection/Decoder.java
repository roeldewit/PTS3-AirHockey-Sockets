package Airhockey.Connection;

import Airhockey.Main.Game;
import Airhockey.Renderer.IRenderer;
import javafx.application.Platform;

/**
 *
 * @author Sam
 */
public class Decoder {

    private final IRenderer renderer;
    private final Game game;

    public Decoder(IRenderer renderer, Game game) {
        this.renderer = renderer;
        this.game = game;
    }

    protected void receiveCommand(String command) {
        String[] splitter = command.split(Protocol.SEPERATOR);

        switch (splitter[0]) {
            case Protocol.PUCK_LOCATION:
                Platform.runLater(() -> {
                    renderer.setPuckLocation(Integer.parseInt(splitter[1]),
                            Integer.parseInt(splitter[2]));
                });
                break;

            case Protocol.RED_BAT_LOCATION:
                Platform.runLater(() -> {
                    renderer.setRedBatLocation(Integer.parseInt(splitter[1]),
                            Integer.parseInt(splitter[2]));
                });
                break;

            case Protocol.BLUE_BAT_LOCATION:
                Platform.runLater(() -> {
                    renderer.setBlueBatLocation(Integer.parseInt(splitter[1]),
                            Integer.parseInt(splitter[2]));
                });
                break;

            case Protocol.GREEN_BAT_LOCATION:
                Platform.runLater(() -> {
                    renderer.setGreenBatLocation(Integer.parseInt(splitter[1]),
                            Integer.parseInt(splitter[2]));
                });
                break;

            case Protocol.GOAL_MADE:
                Platform.runLater(() -> {
                    renderer.setGoalMade(Integer.parseInt(splitter[1]),
                            Integer.parseInt(splitter[2]),
                            Integer.parseInt(splitter[3]));
                });
                break;
            case Protocol.SET_UP_GAME:
                Platform.runLater(() -> {
                    renderer.setUpGame(Integer.parseInt(splitter[1]),
                            splitter[2],
                            splitter[3],
                            splitter[4]);
                });
                break;
            case Protocol.CLIENT_SEND_GAME_DATA:
                game.addClientPlayer(splitter[1]);
                break;
            case Protocol.GAME_OVER:
                Platform.runLater(() -> {
                    game.gameOver();
                });
                break;
        }
    }

}
