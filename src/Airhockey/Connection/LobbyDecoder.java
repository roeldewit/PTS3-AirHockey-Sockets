/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Connection;

import Airhockey.Main.ChatboxLine;
import Airhockey.Main.Lobby;
import javafx.application.Platform;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author pieper126
 */
public class LobbyDecoder {

    private final Lobby lobby;

    public LobbyDecoder(Lobby lobby) {
        this.lobby = lobby;
    }

    protected void receiveCommand(String command) {
        String[] splitter = command.split(Protocol.SEPERATOR);

        switch (splitter[0]) {
            case Protocol.CHAT_LINE:
                Platform.runLater(() -> {
                    lobby.getChatbox().writeLine(new ChatboxLine(splitter[2], lobby.getUser(splitter[1])));
                });
                break;

            case Protocol.GAME_ID:
                Platform.runLater(() -> {
                    // game_id can be used for retrieving the game on mainServer
                    int gameId = Integer.parseInt(splitter[1]);
                });
                break;

            case Protocol.CHATBOX_LINES:
                Platform.runLater(() -> {
                    int i = 1;
                    while (splitter[i] != Protocol.PROTOCOL_ENDER) {
                        lobby.getChatbox().writeLine(new ChatboxLine(splitter[i], lobby.getUser(splitter[++i])));
                        i++;
                    }
                });
                break;

            case Protocol.START_GAME:
                Platform.runLater(() -> {
                    throw new NotImplementedException();
                });
                break;

            case Protocol.CURRENT_OPENGAMES:
                Platform.runLater(() -> {
                    int i = 1;
                    while (splitter[i] != Protocol.PROTOCOL_ENDER) {
                        int ID = i;
                        int description = ++i;
                        int amoundOfPlayers = ++i;
                        int hostIP = ++i;
                        lobby.addWaitingGame(Integer.parseInt(splitter[ID]), splitter[description], splitter[amoundOfPlayers], splitter[hostIP]);
                        i++;                        
                    }
                });
                break;

        }

    }
}
