package Airhockey.Connection;

import Airhockey.Main.Game;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam
 */
public class ConnectionListener extends Thread {

    private static int clientNumber = 2;

    private boolean acceptMore = true;
    private final Decoder decoder;
    private final Game game;

    public ConnectionListener(Game game, Decoder decoder) {
        this.decoder = decoder;
        this.game = game;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            System.out.println("Starting connection listener...");
            serverSocket = new ServerSocket(8189);

            while (acceptMore) {
                Socket socket = serverSocket.accept();
                System.out.println("Server bound");

                Server server = new Server(socket, decoder, game, clientNumber);
                clientNumber++;

                game.clientConnected(server);
                new Thread(server).start();
            }

        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cancel() {
        acceptMore = false;
        System.out.println("ConnectionListener cancelled");
    }
}
