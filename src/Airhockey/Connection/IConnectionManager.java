package Airhockey.Connection;

/**
 *
 * @author Sam
 */
public interface IConnectionManager {

    public void sendCommand(String command);

    public void cancel();
}
