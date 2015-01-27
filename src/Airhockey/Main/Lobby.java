package Airhockey.Main;

import Airhockey.Connection.*;
import Airhockey.Serializable.SerializableGame;
import Airhockey.Utils.ScoreCalculator;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.Stage;

/**
 *
 * @author Roel
 */
public class Lobby {

    private final ArrayList<User> users;

    private final Chatbox chatbox;

    private ScoreCalculator scoreCalculator;

    private final Database database;

    private LobbyClient lobbyClient;

    private LobbyEncoder encoder;

    private final HashMap<Integer, SerializableGame> serializableGames;

    private final Stage primaryStage;

    private Thread lobbyClientThread;

    private LobbyController lobbyController;

    private final User user;

    private int gameID;

    public Lobby(Stage primaryStage, User user) {
        LobbySetUp(primaryStage);
        this.primaryStage = primaryStage;

        serializableGames = new HashMap();

        this.user = user;

        users = new ArrayList<>();

        database = new Database();

        initialSetUpLobby();
        chatbox = new Chatbox();

        this.gameID = -1;
    }

    /**
     * sets the game id
     *
     * @param gameID
     */
    public void setGameId(int gameID) {
        if (this.gameID == -1) {
            this.gameID = gameID;
        }
    }

    /**
     * gets the user
     *
     * @return
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * gets the chatbox
     *
     * @return
     */
    public Chatbox getChatbox() {
        return chatbox;
    }

    /**
     *
     * @return the current user.
     */
    public User getCurrentUser() {
        return this.user;
    }

    public HashMap<Integer, SerializableGame> getSerializableGames() {
        return this.serializableGames;
    }

    /**
     * get scoreCalculator
     *
     * @return
     */
    public ScoreCalculator getScoreCalculator() {
        return scoreCalculator;
    }

    /**
     * gets the user
     *
     * @param username
     * @return returns zero if it isn't a known user to server or the client
     */
    public User getUser(String username) {
        User returnvalue = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        if (returnvalue == null) {
            try {
                returnvalue = database.getUser(username);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnvalue;
    }

    /**
     * writes a chatboxLine
     *
     * @param text
     */
    public void writeLine(String text) {
        encoder.writeLine(user.getUsername(), text);
    }

    /**
     * updates the chatbox, by writing one line in the chatbox
     *
     * @param person
     * @param text
     */
    public void remoteChatboxUpdate(String person, String text) {
        lobbyController.updateChatbox(person, text);
    }

    public void remoteUpdateWaitingGame(int id, String description, String portIP, String username) {
        serializableGames.put(id, new SerializableGame(id, description, portIP, username));
        lobbyController.updateGameList(description, id + "");
    }

    public int addWaitingGame(String description) {
        String iphost = "";
        try {
            iphost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }

        encoder.createNewWaitingGame(description, iphost, this.user.getUsername());

        try {
            do {
                Thread.sleep(100);
            } while (gameID == -1);

        } catch (InterruptedException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("gameid has been procesed");

        System.out.println(gameID);
        return gameID;
    }

    public void startGame() {
        try {
            WaitingScreen waitingScreen = new WaitingScreen(primaryStage, true, this.getUser(user.getUsername()), this, "", gameID);
        } catch (Exception ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startGameList(SerializableGame serGame) {
        try {
            String iphost = serGame.hostIP;
            WaitingScreen waitingScreen = new WaitingScreen(primaryStage, false, this.getUser(user.getUsername()), this, iphost, gameID);
            waitingScreen.setSerGame(serGame);
        } catch (Exception ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gameHasStarted(int gameId) {
        encoder.startGame(gameId);
    }

    public void deleteGame(int id) {
        encoder.deleteGame(id);
    }

    private void LobbySetUp(Stage primaryStage) {

        Parent root = null;

        try {
            URL location = getClass().getResource("LobbyLayout.fxml");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

            root = (Parent) fxmlLoader.load(location.openStream());

            lobbyController = fxmlLoader.getController();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initialSetUpLobby() {
        connectToMainServer();
        getInitialChatbox();
        encoder.getCurrentOpenGames();
        lobbyController.setLobby(this);
    }

    private void connectToMainServer() {
        lobbyClient = new LobbyClient(this);

        lobbyClientThread = new Thread(lobbyClient);
        lobbyClientThread.start();

        encoder = getEncoder();
    }

    private void getInitialChatbox() {
        encoder.getLastTenChatBoxLines();
    }

    private LobbyEncoder getEncoder() {
        LobbyEncoder returnvalue = null;

        while (returnvalue == null) {
            returnvalue = lobbyClient.getEncoder();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return returnvalue;
    }
}
