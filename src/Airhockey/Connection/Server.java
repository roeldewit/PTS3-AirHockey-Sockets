package Airhockey.Connection;

import Airhockey.Renderer.IRenderer;
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
public class Server extends Thread implements IConnectionManager {

    private ObjectOutputStream objectOutputStream;

    private boolean interrupted = false;
    private Socket socket;
    private Decoder decoder;

    public Server(Socket socket, IRenderer renderer) {
        this.socket = socket;
        decoder = new Decoder(renderer);
        renderer.setEncoder(new Encoder(this));
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting server");

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
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
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
