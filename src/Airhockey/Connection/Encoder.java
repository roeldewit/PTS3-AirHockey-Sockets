package Airhockey.Connection;

import java.util.ArrayList;

/**
 *
 * @author Sam
 */
public class Encoder {

    private ArrayList<IConnectionManager> connectionMangerList;

    public Encoder() {
        this.connectionMangerList = new ArrayList<>();
    }

    public void addManager(IConnectionManager connectionManager) {
        connectionMangerList.add(connectionManager);
    }

    public synchronized void sendPuckLocation(int x, int y) {
        String command = Protocol.PUCK_LOCATION
                + Protocol.SEPERATOR
                + x
                + Protocol.SEPERATOR
                + y;

        sendCommand(command);
    }

    public synchronized void sendLeftBatLocation(int x, int y) {
        sendBatLocation(Protocol.LEFT_BAT_LOCATION, x, y);
    }

    public synchronized void sendRightBatLocation(int x, int y) {
        sendBatLocation(Protocol.RIGHT_BAT_LOCATION, x, y);
    }

    public synchronized void sendBottomBatLocation(int x, int y) {
        sendBatLocation(Protocol.BOTTOM_BAT_LOCATION, x, y);
    }

    private synchronized void sendBatLocation(String bat, int x, int y) {
        String command = bat
                + Protocol.SEPERATOR
                + x
                + Protocol.SEPERATOR
                + y;

        sendCommand(command);
    }

    public synchronized void sendGoalMade(int newRound, int scorer, int against) {
        String command = Protocol.GOAL_MADE
                + Protocol.SEPERATOR
                + newRound
                + Protocol.SEPERATOR
                + scorer
                + Protocol.SEPERATOR
                + against;

        sendCommand(command);
    }

    public synchronized void sendSetUpGame(String p1Name, String p2Name, String p3Name) {
        String command = Protocol.SET_UP_GAME
                + Protocol.SEPERATOR
                + p1Name
                + Protocol.SEPERATOR
                + p2Name
                + Protocol.SEPERATOR
                + p3Name;

        sendCommand(command);
    }

    public synchronized void sendCommand(String command) {
        for (IConnectionManager manager : connectionMangerList) {
            manager.sendCommand(command);
        }
    }
}
