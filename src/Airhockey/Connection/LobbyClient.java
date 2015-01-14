/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Connection;

import Airhockey.Main.Lobby;
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
 * @author pieper126
 */
public class LobbyClient extends Thread implements IConnectionManager {
    private ObjectOutputStream objectOutputStream;
    private final LobbyDecoder decoder;
    private boolean interrupted;
    
    private LobbyEncoder encoder;

    public LobbyClient(Lobby lobby) {
        decoder = new LobbyDecoder(lobby);
    }

    @Override
    public void run() {
        Socket socket = null;

        try {
            System.out.println("Starting client....");
            socket = new Socket("localhost", 8189);
            
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

    @Override
    public synchronized void sendCommand(String command) {
        try {
            System.out.println("Sending Command: " + command);
            objectOutputStream.writeObject(command);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancel() {
        interrupted = false;
    }
}