package Airhockey.Mainserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pieper126
 */
public class ConnectionListener extends Thread {

    private boolean acceptMore = true;

    private final MainLobby lobby;

    /**
     * contains the #Socket and acts as the entry point for clients
     *
     * @param lobby
     */
    public ConnectionListener(MainLobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            System.out.println("Starting connection listener...");
            serverSocket = new ServerSocket(8190);

            while (acceptMore) {
                System.out.println("more are being accepted");
                Socket socket = serverSocket.accept();
                System.out.println("Server bound");

                MainLobbyServer server = new MainLobbyServer(socket, lobby);
                
                lobby.getEncoder().addManager(server);
                
                new Thread(server).start();
            }

        } catch (IOException e) {
            Logger.getLogger(MainLobbyServer.class.getName()).log(Level.SEVERE, null, e);
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
