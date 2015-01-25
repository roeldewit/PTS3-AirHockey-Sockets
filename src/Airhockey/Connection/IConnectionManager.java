package Airhockey.Connection;

/**
 * Interface for classes initiating a connection to another player of the game.
 *
 * @author Sam
 */
public interface IConnectionManager {

    /**
     * Used for sending a specific command to the other side of the connection.
     *
     * @param command A string of text with all the data that needs to be send.
     */
    public void sendCommand(String command);

    /**
     * Cancels the connectionmanager's connection.
     */
    public void cancel();
}
