package Airhockey.Connection;

import Airhockey.Main.Game;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Thread runs on the hosts computer and listens to incoming client who want to join as a player or spectator.
 *
 * @author Sam
 */
public class ConnectionListener extends Thread {

    private static int clientNumber = 2;

    private boolean acceptMore = true;
    private final Decoder decoder;
    private final Game game;

    /**
     * Constructor
     *
     * @param game The host's game.
     * @param decoder Decoder used to decode the incoming commands.
     */
    public ConnectionListener(Game game, Decoder decoder) {
        this.decoder = decoder;
        this.game = game;
    }

    /**
     * Listen for client connections.
     * When aforementioned is found starts a new connectionManager to communicate to the connected client.
     */
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

    /**
     * Cancels the listener and shuts down this thread.
     */
    public void cancel() {
        acceptMore = false;
        System.out.println("ConnectionListener cancelled");
    }
}
