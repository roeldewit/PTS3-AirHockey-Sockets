/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Mainserver;

import Airhockey.Serializable.SerializableGame;
import Airhockey.Serializable.SerializableChatBox1;
import Airhockey.Serializable.SerializableChatBoxLine;
import java.util.ArrayList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author pieper126
 */
public class MainLobby {

    private final ArrayList<SerializableGame> busyGames;

    private final ArrayList<SerializableGame> waitingGames;

    private final SerializableChatBox1 chatbox;

    private final Encoder encoder;

    private int nextGameID;

    private final ArrayList<IConnectionManager> connectionManagers;

    private final ConnectionListener connectionListener;

    private final Thread connectionListenerThread;

    /**
     * Default constructor
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

    /**
     * Get encoder
     * @return Encoder
     */
    public Encoder getEncoder() {
        return this.encoder;
    }

    /**
     * Send busy games
     * @param connectionManager Connection manager to use
     */
    public void sendBusyGames(IConnectionManager connectionManager) {
        throw new NotImplementedException();
    }

    /**
     * Send waiting games
     * @param connectionManager Connection manager to use
     */
    public void sendWaitingGames(IConnectionManager connectionManager) {
        ArrayList<ArrayList<String>> sWaitingGames = new ArrayList<>();

        for (SerializableGame waitingGame : waitingGames) {
            sWaitingGames.add(ExtraArrayListFunctions.createsNodeArrayListWithEnetries(waitingGame.id + "", waitingGame.description, waitingGame.usernames.size() + "", waitingGame.hostIP));
        }

        encoder.sendWaitingGame(sWaitingGames, connectionManager);
    }

    /**
     * Send chat box
     * @param connectionManager Connection manager to use 
     */
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

    /**
     * Add new waiting game
     * @param description Description
     * @param ipHost IP of the host
     * @param username Username
     * @param connectionManager Connection manager to use 
     */
    public void addNewWaitingGame(String description, String ipHost, String username, IConnectionManager connectionManager) {
        SerializableGame serializableGame = new SerializableGame(nextGameID, description, ipHost, username);

        nextGameID++;

        waitingGames.add(serializableGame);
    }

    /**
     * Start game
     * @param id Game id
     */
    public void startGame(int id) {
        for (SerializableGame waitingGame : waitingGames) {
            if (waitingGame.id == id) {
                waitingGames.remove(waitingGame);
                busyGames.add(waitingGame);
                break;
            }
        }
    }

    /**
     * Delete game
     * @param id Game id
     */
    public void deleteGame(int id) {
        for (SerializableGame waitingGame : waitingGames) {
            if (waitingGame.id == id) {
                waitingGames.remove(waitingGame);
                break;
            }
        }

        for (SerializableGame busyGame : busyGames) {
            if (busyGame.id == id) {
                busyGames.remove(busyGame);
                break;
            }
        }
    }

    /**
     * Write a line in the chat box
     * @param username Username
     * @param text Text
     */
    public void writeline(String username, String text) {
        chatbox.writeline(username, text);

        encoder.sendChatBoxLine(username, text);
    }

    /**
     * Add connection manager
     * @param connectionManager Connection manager to use 
     */
    public void addConnectionManager(IConnectionManager connectionManager) {
        encoder.addManager(connectionManager);
    }
}
