package Airhockey.Connection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class used to encode any data that needs to be send to other players of the game.
 *
 * @author Sam
 */
public class Encoder {

    private final LinkedHashMap<IConnectionManager, Boolean> connectionMangerMap;

    public Encoder() {
        this.connectionMangerMap = new LinkedHashMap<>();
    }

    /**
     * Adds a connectionmanager to the encoders list.
     *
     * @param connectionManager ConnectionManager to add.
     * @param ready True if other end is ready to receive commands.
     */
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

    /**
     * Called on server side to notify the clients to set up their game.
     *
     * @param type Type for the other side: client player or spectator.
     * @param clientNumber Number assigned to the client on the other end.
     * @param p1Name Player one's name.
     * @param p2Name Player two's name.
     * @param p3Name Player three's name.
     */
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
     * Called on client side. Used to send the client player's data to the host
     *
     * @param name Name of the client player.
     */
    public void sendClientDataToServer(String name) {
        String command = Protocol.CLIENT_SEND_GAME_DATA
                + Protocol.SEPERATOR
                + name;

        sendCommand(command);
    }

    /**
     * Called on client side. Used to send the spectators data to the host
     *
     * @param name Name of the spectator
     */
    public void sendSpectatorDataToServer(String name) {
        String command = Protocol.SPECTATOR_SEND_GAME_DATA
                + Protocol.SEPERATOR
                + name;

        sendCommand(command);
    }

    /**
     * Called on client side. Send to host that player with the given number has quit.
     *
     * @param playerNumber
     */
    public void sendLeavingGame(int playerNumber) {
        String command = Protocol.CLIENT_LEFT_GAME
                + Protocol.SEPERATOR
                + playerNumber;

        sendCommand(command);
    }

    /**
     * Called on the clients side to send a command to move the client player's bat.
     *
     * @param playerNumber Number of the player ordering the command.
     * @param direction Requested direction of the players bat.
     */
    public void sendClientBatMovementDirection(int playerNumber, int direction) {
        String command = Protocol.CLIENT_BAT_MOVEMENT_DIRECTION
                + Protocol.SEPERATOR
                + playerNumber
                + Protocol.SEPERATOR
                + direction;

        sendCommand(command);
    }

    /**
     * Used to send a command to only one client instead of all clients in this encoders list.
     *
     * @param clientNumber Number of the client player to send the command to.
     * @param command The command to send.
     * @param force True if the command has to be send even if the client isn't ready yet.
     */
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

    /**
     * Sends a command to all the clients in this encoder's list.
     *
     * @param command The command to send.
     */
    private synchronized void sendCommand(String command) {
        connectionMangerMap.entrySet().stream().forEach((value) -> {
            if (value.getValue() == true) {
                value.getKey().sendCommand(command);
            }
        });
    }

    /**
     * Stop the connectionManager's-threads.
     */
    public void shutDownManagers() {
        connectionMangerMap.entrySet().stream().forEach((value) -> {
            if (value.getValue() == true) {
                value.getKey().cancel();
            }
        });
    }

    /**
     * Sets the given manager as ready to receive data,
     *
     * @param managerNumber Number of the manager that has to be set.
     */
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
