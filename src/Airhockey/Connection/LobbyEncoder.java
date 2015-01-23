package Airhockey.Connection;

/**
 *
 * @author pieper126
 */
public class LobbyEncoder {

    private final IConnectionManager connectionManager;

    public LobbyEncoder(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public synchronized void getCurrentRunningGames() {

    }

    public synchronized void getCurrentOpenGames() {
        String command = Protocol.GET_CURRENT_OPENGAMES;

        sendCommand(command);
    }

    public synchronized void createNewWaitingGame(String description, String IPhost, String username) {
        String command = Protocol.ADD_NEW_GAME
                + Protocol.SEPERATOR
                + description
                + Protocol.SEPERATOR
                + IPhost
                + Protocol.SEPERATOR
                + username;

        sendCommand(command);
    }

    public synchronized void deleteGame(int id) {
        String command = Protocol.CHAT_LINE
                + Protocol.SEPERATOR
                + id;

        sendCommand(command);
    }

    public synchronized void writeLine(String username, String text) {
        String command = Protocol.CHAT_LINE
                + Protocol.SEPERATOR
                + username
                + Protocol.SEPERATOR
                + text;

        sendCommand(command);
    }

    public synchronized void getLastTenChatBoxLines() {
        String command = Protocol.GET_CHATBOX_LINES;

        sendCommand(command);
    }

    public synchronized void sendCommand(String command) {
        connectionManager.sendCommand(command);
    }
}
