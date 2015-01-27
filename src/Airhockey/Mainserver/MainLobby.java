package Airhockey.Mainserver;

import Airhockey.Serializable.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Class containing the Main Lobby
 *
 * @author pieper126
 */
public class MainLobby {

    private final ArrayList<SerializableGame> busyGames;

    private ArrayList<SerializableGame> waitingGames;

    private final SerializableChatBox1 chatbox;

    private final Encoder encoder;

    private int nextGameID;

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

        connectionListener = new ConnectionListener(this);

        connectionListenerThread = new Thread(connectionListener);
        connectionListenerThread.start();

        File file = new File("games.bin");

        if (file.exists() && !file.isDirectory()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                waitingGames = (ArrayList<SerializableGame>) objectInputStream.readObject();
                objectInputStream.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Get encoder
     *
     * @return Encoder
     */
    public Encoder getEncoder() {
        return this.encoder;
    }

    /**
     * Send busy games
     *
     * @param connectionManager Connection manager to use
     */
    public void sendBusyGames(IConnectionManager connectionManager) {
        ArrayList<ArrayList<String>> sBusyGames = new ArrayList<>();

        for (SerializableGame busyGame : busyGames) {
            sBusyGames.add(ExtraArrayListFunctions.createNodeArrayListWithEntries(busyGame.id + "", busyGame.description, busyGame.usernames.size() + "", busyGame.hostIP));
        }

        encoder.sendBusyGames(sBusyGames, connectionManager);
    }

    /**
     * Send waiting games
     *
     * @param connectionManager Connection manager to use
     */
    public void sendWaitingGames(IConnectionManager connectionManager) {
        ArrayList<ArrayList<String>> sWaitingGames = new ArrayList<>();

        for (SerializableGame waitingGame : waitingGames) {
            sWaitingGames.add(ExtraArrayListFunctions.createNodeArrayListWithEntries(waitingGame.id + "", waitingGame.description, waitingGame.usernames.size() + "", waitingGame.hostIP));
        }

        encoder.sendWaitingGames(sWaitingGames, connectionManager);
    }

    /**
     * Send chat box
     *
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
     *
     * @param description Description
     * @param ipHost IP of the host
     * @param username Username
     * @param connectionManager Connection manager to use
     */
    public void addNewWaitingGame(String description, String ipHost, String username, IConnectionManager connectionManager) {
        SerializableGame serializableGame = new SerializableGame(nextGameID, description, ipHost, username);

        encoder.sendGameID(nextGameID, connectionManager);

        nextGameID++;

        waitingGames.add(serializableGame);

        encoder.sendWaitingGame(ExtraArrayListFunctions.createNodeArrayListWithEntries(serializableGame.id + "", serializableGame.description, serializableGame.usernames.size() + "", serializableGame.hostIP));

        writeGamesToFile();
    }

    /**
     * Start game
     *
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

        encoder.setToBusyGame(id);

        writeGamesToFile();
    }

    /**
     * Delete game
     *
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

        writeGamesToFile();
    }

    /**
     * Write a line in the chat box
     *
     * @param username Username
     * @param text Text
     */
    public void writeline(String username, String text) {
        chatbox.writeline(username, text);

        encoder.sendChatBoxLine(username, text);
    }

    /**
     * Add connection manager
     *
     * @param connectionManager Connection manager to use
     */
    public void addConnectionManager(IConnectionManager connectionManager) {
        encoder.addManager(connectionManager);
    }

    /**
     * Write games to file
     */
    private void writeGamesToFile() {
        ArrayList<SerializableGame> games = new ArrayList<>();
        games.addAll(waitingGames);
        games.addAll(busyGames);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("games.bin");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(games);
            objectOutputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
