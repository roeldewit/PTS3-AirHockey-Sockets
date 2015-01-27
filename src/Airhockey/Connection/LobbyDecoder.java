package Airhockey.Connection;

import Airhockey.Main.Lobby;
import javafx.application.Platform;

/**
 *
 * @author pieper126
 */
public class LobbyDecoder {

    private final Lobby lobby;

    public LobbyDecoder(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * decodes the the recieved messages
     *
     * @param command
     */
    protected void receiveCommand(String command) {
        String[] splitter = command.split(Protocol.SEPERATOR);

        System.out.println(splitter[0]);

        switch (splitter[0]) {
            case Protocol.CHAT_LINE:
                Platform.runLater(() -> {
                    if (splitter.length < 3) {
                        return;
                    }
                    System.out.println(splitter[1] + ":" + splitter[2]);
                    lobby.remoteChatboxUpdate(splitter[1], splitter[2]);
                });
                break;

            case Protocol.GAME_ID:
                System.out.println("decoder is starting to set GAME ID");

                // game_id can be used for retrieving the game on mainServer
                lobby.setGameId(Integer.parseInt(splitter[1]));

                break;

            case Protocol.CHATBOX_LINES:
                Platform.runLater(() -> {
                    if (splitter.length < 4) {
                        return;
                    }

                    int i = 1;
                    while (!splitter[i].equals(Protocol.PROTOCOL_ENDER)) {
                        System.out.println("lobbyDecoder finds:" + splitter[i] + splitter[i + 1] + splitter[i + 2]);
                        lobby.remoteChatboxUpdate(splitter[i], splitter[++i]);
                        i++;

                    }
                });
                break;

            case Protocol.START_GAME:
                Platform.runLater(() -> {

                });
                break;

            case Protocol.CURRENT_OPENGAMES:
                Platform.runLater(() -> {
                    if (splitter.length < 5) {
                        return;
                    }

                    int i = 1;
                    while (!splitter[i].equals(Protocol.PROTOCOL_ENDER)) {
                        int ID = i;
                        int description = ++i;
                        int amoundOfPlayers = ++i;
                        int hostIP = ++i;
                        lobby.remoteUpdateWaitingGame(Integer.parseInt(splitter[ID]), splitter[description], splitter[hostIP], splitter[amoundOfPlayers]);
                        i++;
                    }
                });
                break;
            case Protocol.OPEN_GAME:
                Platform.runLater(() -> {
                    if (splitter.length < 4) {
                        return;
                    }

                    int i = 1;

                    int ID = i;
                    int description = ++i;
                    int amoundOfPlayers = ++i;
                    int hostIP = ++i;
                    System.out.println("SPLITTER:::::  " + splitter[hostIP]);
                    lobby.remoteUpdateWaitingGame(Integer.parseInt(splitter[ID]), splitter[description], splitter[hostIP], splitter[amoundOfPlayers]);

                });
                break;
            case Protocol.TO_BUSY_GAME:
                Platform.runLater(() -> {
                    if (splitter.length < 4) {
                        return;
                    }

                    int ID = 1;

                    lobby.remoteToBusyGame(Integer.parseInt(splitter[ID]));

                });
                break;

        }

    }
}
