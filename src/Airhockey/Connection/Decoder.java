package Airhockey.Connection;

import Airhockey.Main.Game;
import Airhockey.Renderer.IRenderer;
import javafx.application.Platform;

/**
 * Class used to decode any received messages by the connectionManagers.
 *
 * @author Sam
 */
public class Decoder {

    private final IRenderer renderer;
    private final Game game;

    /**
     * Constructor
     *
     * @param renderer The renderer used for this game.
     * @param game The game.
     */
    public Decoder(IRenderer renderer, Game game) {
        this.renderer = renderer;
        this.game = game;
    }

    /**
     * Decodes any received command and forwards the information to the renderer or game classes.
     *
     * @param command Received command by the connectionManager.
     */
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

            case Protocol.CLIENT_BAT_MOVEMENT_DIRECTION:
                Platform.runLater(() -> {
                    renderer.moveClientBat(Integer.parseInt(splitter[1]),
                            Integer.parseInt(splitter[2]));
                });
                break;

            case Protocol.GOAL_MADE:
                Platform.runLater(() -> {
                    renderer.setGoalMade(Integer.parseInt(splitter[1]),
                            Integer.parseInt(splitter[2]),
                            Integer.parseInt(splitter[3]),
                            Integer.parseInt(splitter[4]),
                            Integer.parseInt(splitter[5]));
                });
                break;
            case Protocol.CLIENT_SET_UP_GAME:
                Platform.runLater(() -> {
                    game.startGameAsClient(Integer.parseInt(splitter[1]));
                    renderer.setUpGame(splitter[2],
                            splitter[3],
                            splitter[4]
                    );
                });
                break;

            case Protocol.SPECTATOR_SET_UP_GAME:
                Platform.runLater(() -> {
                    game.startGameAsSpectator(Integer.parseInt(splitter[1]));
                    renderer.setUpGame(splitter[2],
                            splitter[3],
                            splitter[4]
                    );
                });
                break;

            case Protocol.CLIENT_SEND_GAME_DATA:
                Platform.runLater(() -> {
                    game.addClientPlayer(splitter[1]);
                });
                break;

            case Protocol.SPECTATOR_SEND_GAME_DATA:
                Platform.runLater(() -> {
                    game.addSpectator(splitter[1]);
                });
                break;

            case Protocol.CHAT_LINE:
                Platform.runLater(() -> {
                    renderer.addChatBoxLine(splitter[1]);
                });
                break;

            case Protocol.GAME_OVER:
                Platform.runLater(() -> {
                    game.gameOver(splitter[1], -1);
                });
                break;
            case Protocol.GAME_CANCELLED:
                Platform.runLater(() -> {
                    game.gameCancelled();
                });
                break;
            case Protocol.CLIENT_LEFT_GAME:
                Platform.runLater(() -> {
                    game.clientLeftGame(Integer.parseInt(splitter[1]));
                });
                break;
        }
    }
}
