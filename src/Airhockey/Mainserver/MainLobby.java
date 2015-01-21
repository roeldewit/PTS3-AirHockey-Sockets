/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Mainserver;

import Airhockey.Rmi.*;
import java.util.ArrayList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author pieper126
 */
public class MainLobby {

    private ArrayList<SerializableGame> busyGames;

    private ArrayList<SerializableGame> waitingGames;

    private final SerializableChatBox1 chatbox;

    private final Encoder encoder;

    private int nextGameID;

    private ArrayList<IConnectionManager> connectionManagers;

    private final ConnectionListener connectionListener;

    private Thread connectionListenerThread;

    /**
     * the Lobby of the MainServer
     */
    public MainLobby() {
        busyGames = new ArrayList<>();
        waitingGames = new ArrayList<>();
        chatbox = new SerializableChatBox1();

        nextGameID = 0;
        this.encoder = new Encoder();
        this.connectionManagers = new ArrayList<>();

        connectionListener = new ConnectionListener(this);

        connectionListenerThread = new Thread(connectionListener);
        connectionListenerThread.start();
    }

    public Encoder getEncoder() {
        return this.encoder;
    }

    public void sendBusyGames(IConnectionManager connectionManager) {
        throw new NotImplementedException();
    }

    public void sendWaitingGames(IConnectionManager connectionManager) {
        ArrayList<ArrayList<String>> sWaitingGames = new ArrayList<>();

        for (SerializableGame waitingGame : waitingGames) {
            sWaitingGames.add(ExtraArrayListFunctions.createsNodeArrayListWithEnetries(waitingGame.id + "", waitingGame.description, waitingGame.usernames.size() + "", waitingGame.hostIP));
        }

        encoder.sendWaitingGame(sWaitingGames, connectionManager);
    }

    public void sendChatbox(IConnectionManager connectionManager) {

        ArrayList<SerializableChatBoxLine> chatboxlines = chatbox.getSerializableChatBoxWithTenLastLines().lines;
        ArrayList<ArrayList<String>> sChatboxLines = new ArrayList<>();

        for (SerializableChatBoxLine chatboxline : chatboxlines) {
            ArrayList<String> sChatboxline = new ArrayList<>();

            sChatboxline.add(chatboxline.player);
            sChatboxline.add(chatboxline.text);

            sChatboxLines.add(sChatboxline);
        }

        encoder.sendInitialChatBox(sChatboxLines, connectionManager);
    }

    public void addNewWaitingGame(String description, String ipHost, String username, IConnectionManager connectionManager) {
        SerializableGame serializableGame = new SerializableGame(nextGameID, description, ipHost, username);

        nextGameID++;

        waitingGames.add(serializableGame);
    }

    public void startGame(int id) {
        for (SerializableGame waitinggame : waitingGames) {
            if (waitinggame.id == id) {
                busyGames.add(waitinggame);
                break;
            }
        }
    }

    public void deleteGame(int id) {
        for (SerializableGame waitinggame : waitingGames) {
            if (waitinggame.id == id) {
                busyGames.add(waitinggame);
                break;
            }
        }

        for (SerializableGame busyGame : busyGames) {
            if (busyGame.id == id) {
                busyGames.add(busyGame);
                break;
            }
        }
    }

    public void writeline(String username, String text) {
        chatbox.writeline(username, text);

        encoder.sendChatBoxLine(username, text);
    }

    public void addConnectionManager(IConnectionManager connectionManager) {
        encoder.addManager(connectionManager);
    }
}
