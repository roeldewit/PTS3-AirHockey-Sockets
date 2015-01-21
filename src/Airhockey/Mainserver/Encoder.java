package Airhockey.Mainserver;

import Airhockey.Serializable.SerializableGame;
import java.util.ArrayList;

/**
 *
 * @author Sam
 */
public class Encoder {

    private ArrayList<IConnectionManager> connectionMangerList;

    /**
     * Encoder encodes all out going messages
     */
    public Encoder() {
        this.connectionMangerList = new ArrayList<>();
    }

    public synchronized void addManager(IConnectionManager connectionManager) {
        connectionMangerList.add(connectionManager);
    }

    public synchronized void sendChatBoxLine(String username, String text) {
        String command = Protocol.CHAT_LINE
                + Protocol.SEPERATOR
                + username
                + Protocol.SEPERATOR
                + text;

        sendCommand(command);
    }

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

    public synchronized void sendGameID(int gameID, IConnectionManager connectionManager) {
        String command = Protocol.GAME_ID
                + Protocol.SEPERATOR
                + gameID;

        connectionManager.sendCommand(command);
    }

    public synchronized void sendWaitingGames(ArrayList<ArrayList<String>> waitingGames, IConnectionManager connectionManager) {
        String command = Protocol.CURRENT_OPENGAMES
                + Protocol.SEPERATOR;

        for (ArrayList<String> waitingGame : waitingGames) {
            command = waitingGame.get(0)
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
    
    public synchronized void sendBusyGames(ArrayList<ArrayList<String>> busyGames, IConnectionManager connectionManager) {
        String command = Protocol.CURRENT_BUSYGAMES
                + Protocol.SEPERATOR;

        for (ArrayList<String> busyGame : busyGames) {
            command = busyGame.get(0)
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

    public synchronized void sendCommand(String command) {
        for (IConnectionManager manager : connectionMangerList) {
            manager.sendCommand(command);
        }
    }
}
