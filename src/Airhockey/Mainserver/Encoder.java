package Airhockey.Mainserver;

import Airhockey.Connection.Protocol;
import java.util.ArrayList;

/**
 *
 * @author Sam
 */
public class Encoder {

    private final ArrayList<IConnectionManager> connectionMangerList;

    /**
     * Encoder encodes all out going messages
     */
    public Encoder() {
        this.connectionMangerList = new ArrayList<>();
    }

    /**
     * adds a #IConnectionManager to the list of connectionManagers
     *
     * @param connectionManager
     */
    public synchronized void addManager(IConnectionManager connectionManager) {
        connectionMangerList.add(connectionManager);
    }

    /**
     * deletes a #IConnectionManager from the list of connectionManagers
     *
     * @param connecitonManager
     */
    public synchronized void deleteManager(IConnectionManager connecitonManager) {
        connectionMangerList.remove(connecitonManager);
    }

    /**
     * sends a chatboxLine
     *
     * @param username
     * @param text
     */
    public synchronized void sendChatBoxLine(String username, String text) {
        String command = Protocol.CHAT_LINE
                + Protocol.SEPERATOR
                + username
                + Protocol.SEPERATOR
                + text;

        sendCommand(command);
    }

    /**
     * sends the initial chatbox
     *
     * @param chatboxlines
     * @param connectionManager
     */
    public synchronized void sendInitialChatBox(ArrayList<ArrayList<String>> chatboxlines, IConnectionManager connectionManager) {
        String command = Protocol.CHATBOX_LINES
                + Protocol.SEPERATOR;

        for (ArrayList<String> chatboxline : chatboxlines) {
            command += chatboxline.get(1)
                    + Protocol.SEPERATOR
                    + chatboxline.get(0)
                    + Protocol.SEPERATOR;
        }

        command += Protocol.PROTOCOL_ENDER;

        connectionManager.sendCommand(command);
    }

    /**
     * send a GameID
     *
     * @param gameID
     * @param connectionManager
     */
    public synchronized void sendGameID(int gameID, IConnectionManager connectionManager) {
        String command = Protocol.GAME_ID
                + Protocol.SEPERATOR
                + gameID;

        connectionManager.sendCommand(command);
    }

    /**
     * sends all waiting games
     *
     * @param waitingGames
     * @param connectionManager
     */
    public synchronized void sendWaitingGames(ArrayList<ArrayList<String>> waitingGames, IConnectionManager connectionManager) {
        System.out.println("sending waiting games");
        String command = Protocol.CURRENT_OPENGAMES
                + Protocol.SEPERATOR;

        for (ArrayList<String> waitingGame : waitingGames) {
            command += waitingGame.get(0)
                    + Protocol.SEPERATOR
                    + waitingGame.get(1)
                    + Protocol.SEPERATOR
                    + waitingGame.get(2)
                    + Protocol.SEPERATOR
                    + waitingGame.get(3)
                    + Protocol.SEPERATOR;
        }

        command += Protocol.PROTOCOL_ENDER;

        connectionManager.sendCommand(command);
    }

    /**
     * sends a waiting game
     *
     * @param waitingGame
     */
    public synchronized void sendWaitingGame(ArrayList<String> waitingGame) {
        String command = Protocol.OPEN_GAME
                + Protocol.SEPERATOR
                + waitingGame.get(0)
                + Protocol.SEPERATOR
                + waitingGame.get(1)
                + Protocol.SEPERATOR
                + waitingGame.get(2)
                + Protocol.SEPERATOR
                + waitingGame.get(3)
                + Protocol.SEPERATOR;

        command += Protocol.PROTOCOL_ENDER;

        sendCommand(command);
    }

    /**
     * sends all busy games
     *
     * @param busyGames
     * @param connectionManager
     */
    public synchronized void sendBusyGames(ArrayList<ArrayList<String>> busyGames, IConnectionManager connectionManager) {
        System.out.println("sending busy games");
        String command = Protocol.CURRENT_BUSYGAMES
                + Protocol.SEPERATOR;

        for (ArrayList<String> busyGame : busyGames) {
            command += busyGame.get(0)
                    + Protocol.SEPERATOR
                    + busyGame.get(1)
                    + Protocol.SEPERATOR
                    + busyGame.get(2)
                    + Protocol.SEPERATOR
                    + busyGame.get(3)
                    + Protocol.SEPERATOR;
        }

        command += Protocol.PROTOCOL_ENDER;

        connectionManager.sendCommand(command);
    }

    /**
     * sends a command
     *
     * @param command
     */
    public synchronized void sendCommand(String command) {
        for (IConnectionManager manager : connectionMangerList) {
            manager.sendCommand(command);
        }
    }
}
