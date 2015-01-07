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

    public synchronized void sendBatLocation(String bat, float x, float y) {
        String command = bat
                + Protocol.SEPERATOR
                + x
                + Protocol.SEPERATOR
                + y;

        connectionManger.sendCommand(command);
    }
}
