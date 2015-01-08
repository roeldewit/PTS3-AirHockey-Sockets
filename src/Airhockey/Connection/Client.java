package Airhockey.Connection;

import Airhockey.Main.Game;
import Airhockey.Renderer.IRenderer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam
 */
public class Client extends Thread implements IConnectionManager {

    private boolean interrupted;
    private ObjectOutputStream objectOutputStream;
    private final Decoder decoder;
    private final Game game;

    public Client(IRenderer renderer, Game game) {
        this.game = game;

        System.out.println("New Client");
        decoder = new Decoder(renderer, game);
    }

    @Override
    public void run() {
        Socket socket = null;

        try {
            System.out.println("Starting client....");
            socket = new Socket("localhost", 8189);
            socket.setTcpNoDelay(true);

            System.out.println("Client bound");

            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            objectOutputStream.flush();

            InputStream inputStream = socket.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            ObjectInputStream ois = new ObjectInputStream(inputStream);

            game.connectedToServer();

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

    @Override
    public synchronized void sendCommand(String command) {
        try {
            System.out.println("Sending Command: " + command);
            objectOutputStream.writeObject(command);
            objectOutputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancel() {
        interrupted = false;
    }
}
