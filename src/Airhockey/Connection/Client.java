package Airhockey.Connection;

import Airhockey.Main.Game;
import java.io.*;
import java.net.Socket;
import java.util.logging.*;


/**
 * Thread used by a client computer to start a connection with the game's host.
 *
 * @author Sam
 */
public class Client extends Thread implements IConnectionManager {

    private boolean interrupted = false;
    private ObjectOutputStream objectOutputStream;
    private final Decoder decoder;
    private final Game game;
    private final String ipAddress;

    /**
     * Constructor
     *
     * @param decoder Decoder used to decode the incoming commands.
     * @param game The client's game.
     * @param ipAddress Ip address of the host.
     */
    public Client(Decoder decoder, Game game, String ipAddress) {
        this.game = game;
        this.ipAddress = ipAddress;
        this.decoder = decoder;
    }

    /**
     * Tries to create a connection with the host server.
     * Keeps listening for incoming commands.
     */
    @Override
    public void run() {
        Socket socket = null;

        try {
            System.out.println("Starting client....");
            System.out.println("IP: " + ipAddress);
            socket = new Socket(ipAddress, 8189);
            
            socket.setTcpNoDelay(true);

            System.out.println("Client bound");

            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            objectOutputStream.flush();

            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

            game.connectedToServer();

            while (!interrupted) {
                String command = (String) ois.readObject();
                //System.out.println("Command Received: " + command);
                decoder.receiveCommand(command);
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);

            game.connectionLost();
            interrupted = true;
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

    @Override
    public synchronized void sendCommand(String command) {
        if (!interrupted) {
            try {
                System.out.println("Sending Command: " + command);
                objectOutputStream.writeObject(command);
                objectOutputStream.flush();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void cancel() {
        interrupted = true;
    }
}
