package Airhockey.Connection;

import Airhockey.Main.Game;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam
 */
public class Server extends Thread implements IConnectionManager {

    private ObjectOutputStream objectOutputStream;

    private final int clientNumber;
    private boolean interrupted = false;
    private final Socket socket;
    private final Decoder decoder;
    private final Game game;

    public Server(Socket socket, Decoder decoder, Game game, int clientNumber) {
        this.socket = socket;
        this.decoder = decoder;
        this.game = game;
        this.clientNumber = clientNumber;
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting server");
            socket.setTcpNoDelay(true);

            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            objectOutputStream.flush();

            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

            while (!interrupted) {
                String command = (String) ois.readObject();
                //System.out.println("Received command: " + command);
                decoder.receiveCommand(command);
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);

            System.out.println("Connection lost to client: " + clientNumber);
            interrupted = true;
            game.clientLeftGame(clientNumber);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void sendCommand(String command) {
        if (!interrupted) {
            try {
                //System.out.println("Sending Command: " + command);
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
