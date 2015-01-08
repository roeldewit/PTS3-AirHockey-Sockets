package Airhockey.Connection;

import Airhockey.Main.Game;
import Airhockey.Renderer.IRenderer;
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

    private boolean acceptMore = true;
    private final IRenderer renderer;
    private final Game game;

    public ConnectionListener(Game game, IRenderer renderer) {
        this.renderer = renderer;
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

                Server server = new Server(socket, renderer);
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
