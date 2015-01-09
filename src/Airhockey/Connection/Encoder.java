package Airhockey.Connection;

import java.util.HashMap;

/**
 *
 * @author Sam
 */
public class Encoder {

    private final HashMap<Integer, IConnectionManager> connectionMangerMap;

    public Encoder() {
        this.connectionMangerMap = new HashMap<>();
    }

    public void addManager(IConnectionManager connectionManager) {
        connectionMangerMap.put(connectionMangerMap.size(), connectionManager);
    }

    public synchronized void sendPuckLocation(int x, int y) {
        String command = Protocol.PUCK_LOCATION
                + Protocol.SEPERATOR
                + x
                + Protocol.SEPERATOR
                + y;

        sendCommand(command);
    }

    public synchronized void sendBlueBatLocation(int x, int y) {
        sendBatLocation(Protocol.BLUE_BAT_LOCATION, x, y);
    }

    public synchronized void sendLeftBatLocation(int x, int y) {
        sendBatLocation(Protocol.GREEN_BAT_LOCATION, x, y);
    }

    public synchronized void sendRedBatLocation(int x, int y) {
        sendBatLocation(Protocol.RED_BAT_LOCATION, x, y);
    }

    private synchronized void sendBatLocation(String bat, int x, int y) {
        String command = bat
                + Protocol.SEPERATOR
                + x
                + Protocol.SEPERATOR
                + y;

        sendCommand(command);
    }

    public synchronized void sendGoalMade(int newRound, int scorer, int scorerScore, int against, int againstScore) {
        String command = Protocol.GOAL_MADE
                + Protocol.SEPERATOR
                + newRound
                + Protocol.SEPERATOR
                + scorer
                + Protocol.SEPERATOR
                + scorerScore
                + Protocol.SEPERATOR
                + against
                + Protocol.SEPERATOR
                + againstScore;

        sendCommand(command);
    }

    public synchronized void sendSetUpGame(int clientNumber, String p1Name, String p2Name, String p3Name) {
        String command = Protocol.SET_UP_GAME
                + Protocol.SEPERATOR
                + clientNumber
                + Protocol.SEPERATOR
                + p1Name
                + Protocol.SEPERATOR
                + p2Name
                + Protocol.SEPERATOR
                + p3Name;

        sendCommandToOneClient(clientNumber - 2, command);
    }

    public void sendGameOver() {
        String command = Protocol.GAME_OVER;
        sendCommand(command);
    }

    /**
     * CLIENT
     */
    public void sendGameData(String name) {
        String command = Protocol.CLIENT_SEND_GAME_DATA
                + Protocol.SEPERATOR
                + name;

        sendCommand(command);
    }

    private synchronized void sendCommandToOneClient(int clientNumber, String command) {
        connectionMangerMap.get(clientNumber).sendCommand(command);
    }

    private synchronized void sendCommand(String command) {
        connectionMangerMap.entrySet().stream().forEach((value) -> {
            value.getValue().sendCommand(command);
        });
    }
}
