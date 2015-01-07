package Airhockey.Connection;

/**
 *
 * @author Sam
 */
public class Encoder {

    private final IConnectionManager connectionManger;

    public Encoder(IConnectionManager connectionManger) {
        this.connectionManger = connectionManger;
    }

    public synchronized void sendPuckLocation(float x, float y) {
        String command = Protocol.PUCK_LOCATION
                + Protocol.SEPERATOR
                + x
                + Protocol.SEPERATOR
                + y;

        connectionManger.sendCommand(command);
    }

    public synchronized void sendLeftBatLocation(float x, float y) {
        sendBatLocation(Protocol.LEFT_BAT_LOCATION, x, y);
    }

    public synchronized void sendRightBatLocation(float x, float y) {
        sendBatLocation(Protocol.RIGHT_BAT_LOCATION, x, y);
    }

    public synchronized void sendBottomBatLocation(float x, float y) {
        sendBatLocation(Protocol.BOTTOM_BAT_LOCATION, x, y);
    }

    private synchronized void sendBatLocation(String bat, float x, float y) {
        String command = bat
                + Protocol.SEPERATOR
                + x
                + Protocol.SEPERATOR
                + y;

        connectionManger.sendCommand(command);
    }
}
