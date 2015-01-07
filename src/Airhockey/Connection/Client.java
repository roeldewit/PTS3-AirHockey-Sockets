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
public class Client extends Thread implements IConnectionManager {

    private ObjectOutputStream ous;
    private Decoder decoder;
    private boolean interrupted;

    public Client(IRenderer renderer) {
        System.out.println("New Client");
        decoder = new Decoder(renderer);
        renderer.setEncoder(new Encoder(this));
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting client....");
            Socket socket = new Socket("localhost", 8189);

            System.out.println("Client bound");

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            ous = new ObjectOutputStream(outputStream);
            ObjectInputStream ois = new ObjectInputStream(inputStream);

            while (!interrupted) {
                String command = (String) ois.readObject();
                decoder.receiveCommand(command);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void sendCommand(String command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
