package Airhockey.Mainserver;

import com.sun.corba.se.spi.activation.Server;
import java.io.*;
import java.net.Socket;
import java.util.logging.*;

/**
 * 
 * @author stijn
 */
public class MainLobbyServer extends Thread implements IConnectionManager {

    private final Socket socket;

    private ObjectOutputStream objectOutputStream;

    private Decoder decoder;

    private boolean interrupted = false;

    private final MainLobby lobby;

    /**
     * Sends and recieves information to a specific client that is connected to
     * it
     *
     * @param socket
     * @param lobby
     */
    public MainLobbyServer(Socket socket, MainLobby lobby) {
        this.socket = socket;
        this.lobby = lobby;

        decoder = new Decoder(lobby, this);
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting MainLobby");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            objectOutputStream = new ObjectOutputStream(outputStream);
            ObjectInputStream ois = new ObjectInputStream(inputStream);

            while (!interrupted) {
                String command = (String) ois.readObject();
                System.out.println("Received command: " + command);
                decoder.receiveCommand(command);
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(MainLobbyServer.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                lobby.getEncoder().deleteManager(this);
                objectOutputStream.close();
                socket.close();                
            } catch (IOException ex) {
                Logger.getLogger(MainLobbyServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void sendCommand(String command) {
        try {
            System.out.println("Sending Command: " + command);
            objectOutputStream.writeObject(command);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
