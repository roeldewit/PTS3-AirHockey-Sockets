package Airhockey.Connection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Sam
 */
public class Encoder {

    private final LinkedHashMap<IConnectionManager, Boolean> connectionMangerMap;

    public Encoder() {
        this.connectionMangerMap = new LinkedHashMap<>();
    }

    public void addManager(IConnectionManager connectionManager, boolean ready) {
        connectionMangerMap.put(connectionManager, ready);
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

    public synchronized void sendSetUpGameAsClient(int clientNumber, String p1Name, String p2Name, String p3Name) {
        sendSetUpGame(Protocol.CLIENT_SET_UP_GAME, clientNumber, p1Name, p2Name, p3Name);
    }

    public synchronized void sendSetUpGameAsSpectator(int clientNumber, String p1Name, String p2Name, String p3Name) {
        sendSetUpGame(Protocol.SPECTATOR_SET_UP_GAME, clientNumber, p1Name, p2Name, p3Name);
    }

    private synchronized void sendSetUpGame(String type, int clientNumber, String p1Name, String p2Name, String p3Name) {
        String command = type
                + Protocol.SEPERATOR
                + clientNumber
                + Protocol.SEPERATOR
                + p1Name
                + Protocol.SEPERATOR
                + p2Name
                + Protocol.SEPERATOR
                + p3Name;

        sendCommandToOneClient(clientNumber - 2, command, true);
        setManagerReady(clientNumber - 2);
    }

    public void sendGameOver(String reason) {
        String command = Protocol.GAME_OVER
                + Protocol.SEPERATOR
                + reason;
        sendCommand(command);
    }

    public void sendGameCancelledByServer() {
        String command = Protocol.GAME_CANCELLED;
        sendCommand(command);
    }

    public void sendChatBoxLine(String line) {
        String command = Protocol.CHAT_LINE
                + Protocol.SEPERATOR
                + line;

        sendCommand(command);
    }

    /**
     * CLIENT
     */
    public void sendClientDataToServer(String name) {
        String command = Protocol.CLIENT_SEND_GAME_DATA
                + Protocol.SEPERATOR
                + name;

        sendCommand(command);
    }

    public void sendSpectatorDataToServer(String name) {
        String command = Protocol.SPECTATOR_SEND_GAME_DATA
                + Protocol.SEPERATOR
                + name;

        sendCommand(command);
    }

    public void sendLeavingGame(int playerNumber) {
        String command = Protocol.CLIENT_LEFT_GAME
                + Protocol.SEPERATOR
                + playerNumber;

        sendCommand(command);
    }

    public void sendClientBatMovementDirection(int playerNumber, int direction) {
        String command = Protocol.CLIENT_BAT_MOVEMENT_DIRECTION
                + Protocol.SEPERATOR
                + playerNumber
                + Protocol.SEPERATOR
                + direction;

        sendCommand(command);
    }

    private synchronized void sendCommandToOneClient(int clientNumber, String command, boolean force) {
        int i = 0;
        for (Map.Entry<IConnectionManager, Boolean> value : connectionMangerMap.entrySet()) {
            if (i == clientNumber) {
                if (force || value.getValue() == true) {
                    value.getKey().sendCommand(command);
                }
            }
            i++;
        }
    }

    private synchronized void sendCommand(String command) {
        connectionMangerMap.entrySet().stream().forEach((value) -> {
            if (value.getValue() == true) {
                value.getKey().sendCommand(command);
            }
        });
    }

    public void shutDownManagers() {
        connectionMangerMap.entrySet().stream().forEach((value) -> {
            if (value.getValue() == true) {
                value.getKey().cancel();
            }
        });
    }

    private void setManagerReady(int managerNumber) {
        int i = 0;
        for (Map.Entry<IConnectionManager, Boolean> value : connectionMangerMap.entrySet()) {
            if (i == managerNumber) {
                value.setValue(true);
            }
            i++;
        }
    }
}
