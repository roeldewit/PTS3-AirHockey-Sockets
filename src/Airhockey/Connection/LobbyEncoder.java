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

    /**
     * request the current open games from the server
     */
    public synchronized void getCurrentOpenGames() {
        String command = Protocol.GET_CURRENT_OPENGAMES;

        sendCommand(command);
    }

    /**
     * sends the information from a new game to the server
     *
     * @param description
     * @param IPhost
     * @param username
     */
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

    /**
     * deletes a game using the given id
     *
     * @param id
     */
    public synchronized void deleteGame(int id) {
        String command = Protocol.CHAT_LINE
                + Protocol.SEPERATOR
                + id;

        sendCommand(command);
    }

    /**
     * writes a new chatboxline to the server
     *
     * @param username
     * @param text
     */
    public synchronized void writeLine(String username, String text) {
        String command = Protocol.CHAT_LINE
                + Protocol.SEPERATOR
                + username
                + Protocol.SEPERATOR
                + text;

        sendCommand(command);
    }

    /**
     * request the ten last chatboxlines this method is called on start-up of
     * the lobby
     */
    public synchronized void getLastTenChatBoxLines() {
        String command = Protocol.GET_CHATBOX_LINES;

        sendCommand(command);
    }

    /**
     * send the given command to the server
     *
     * @param command
     */
    public synchronized void sendCommand(String command) {
        connectionManager.sendCommand(command);
    }
}
