package Airhockey.Connection;

import Airhockey.Main.Lobby;
import java.io.*;
import java.net.Socket;
import java.util.logging.*;

/**
 *
 * @author pieper126
 */
public class LobbyClient extends Thread implements IConnectionManager {

    private ObjectOutputStream objectOutputStream;
    private final LobbyDecoder decoder;
    private boolean interrupted;

    private LobbyEncoder encoder;

    /**
     * manages the communication between the MainServer and the lobby
     *
     * @param lobby
     */
    public LobbyClient(Lobby lobby) {
        decoder = new LobbyDecoder(lobby);
    }

    @Override
    public void run() {
        Socket socket = null;

        try {
            System.out.println("Starting client....");
            socket = new Socket("192.168.101", 8190);
            System.out.println(socket.getInetAddress());

            encoder = new LobbyEncoder(this);

            System.out.println("Client bound");

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            objectOutputStream = new ObjectOutputStream(outputStream);
            ObjectInputStream ois = new ObjectInputStream(inputStream);

            while (!interrupted) {
                String command = (String) ois.readObject();
                System.out.println("Command Received: " + command);
                decoder.receiveCommand(command);
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * gets the #Encoder
     *
     * @return the #Encoder
     */
    public synchronized LobbyEncoder getEncoder() {
        return encoder;
    }

    /**
     * send the given command using the connected socket
     *
     * @param command
     */
    @Override
    public synchronized void sendCommand(String command) {
        try {
            System.out.println("Sending Command: " + command);
            objectOutputStream.writeObject(command);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * the client stops listening to the server
     */
    public void cancel() {
        interrupted = false;
    }
}
