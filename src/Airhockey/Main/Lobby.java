package Airhockey.Main;

import Airhockey.Connection.*;
import Airhockey.Serializable.SerializableGame;
import Airhockey.Utils.ScoreCalculator;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.Stage;

/**
 *
 * @author Roel
 */
public class Lobby {

    private ArrayList<User> users;

    private Chatbox chatbox;

    private ScoreCalculator scoreCalculator;

    private Database database;

    private LobbyClient lobbyClient;

    private LobbyEncoder encoder;

    private HashMap<Integer, SerializableGame> serializableGames;

    private Stage primaryStage;

    private Thread lobbyClientThread;

    private LobbyController lobbyController;

    private User user;

    private int gameID;

    public final ReentrantLock lock;

    public Lobby(Stage primaryStage, User user) throws NotBoundException, IOException, SQLException {
        LobbySetUp(primaryStage);
        this.primaryStage = primaryStage;

        serializableGames = new HashMap();

        this.user = user;

        users = new ArrayList<>();
        this.lock = new ReentrantLock();

//        games = new ArrayList<>();
        users = new ArrayList<>();

        users.add(new User("Jan"));
        users.add(new User("Piet"));
        users.add(new User("Henk"));

        initialSetUpLobby();
        chatbox = new Chatbox();

        this.gameID = -1;
    }

    public void setGameId(int gameID) {
        if (this.gameID == -1) {
            this.gameID = gameID;
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public Chatbox getChatbox() {
        return chatbox;
    }

    public ScoreCalculator getScoreCalculator() {
        return scoreCalculator;
    }

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
            } catch (SQLException ex) {
                Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public void writeLine(String text) {
        encoder.writeLine(user.getUsername(), text);
    }

    public void remoteChatboxUpdate(String person, String text) {
        lobbyController.updateChatbox(person, text);
    }

    public void remoteUpdateWaitingGame(int id, String description, String portIP, String username) {
        serializableGames.put(id, new SerializableGame(id, description, portIP, username));
        lobbyController.updateGameList(description, id + "");
    }

    public int addWaitingGame(String description) {
        String iphost = "localhost";

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

    public void startGame() {
        try {
            WaitingScreen waitingScreen = new WaitingScreen(primaryStage, true, this.getUser(user.getUsername()));
        } catch (Exception ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startGameList() {
        try {
            WaitingScreen waitingScreen = new WaitingScreen(primaryStage, false, this.getUser(user.getUsername()));
        } catch (Exception ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private Game joinGame(int id, String usern) throws RemoteException {
//        SerializableGame serializableGame = mainLobby.getWaitingGames().get(id);
//        mainLobby.joinGame(id, usern);
//        ArrayList<Player> players = new ArrayList<>();
//
//        int i = 0;
//        for (String username : serializableGame.usernames) {
//            User user = hashMapUsernameToUser.get(username);
//
//            if (i < 3) {
//                players.add(new Player(i, user));
//            }
//        }
//
//        // to do invullen  eigen speler
//        return new Game(primaryStage, serializableGame.hostIP, 1099, players, null);
//    }
//    private ArrayList<SerializableGame> getRunningGames() throws RemoteException {
//        return mainLobby.getBusyGames();
//    }
//
//    private ArrayList<SerializableGame> getWaitingGames() throws RemoteException {
//        return mainLobby.getWaitingGames();
//    }
//
//    private void startGame(SerializableGame serializableGame) throws RemoteException {
//        mainLobby.startGame(serializableGame);
//    }

    /*
     public LobbyController(String ipMainServer, int portMainServer) {
     db = new Database();

     this.ipMainServer = ipMainServer;
     this.portMainServer = portMainServer;

     connectToMainServer();

     try {
     for (User dbuser : db.getUsers()) {
     hashMapUsernameToUser.put(dbuser.getUsername(), dbuser);
     }
     } catch (Exception e) {
     throw new RuntimeException(e.getMessage());
     }

     SerializableChatBox serChatBox = mainLobby.getChatBox();

     chatItems = FXCollections.observableArrayList();
     ratingItems = FXCollections.observableArrayList("TestUser1 : 21", "TestUser2 : 19");
     gameItems = FXCollections.observableArrayList();
     lvRatingTable = new ListView();
     lvRatingTable.setItems(ratingItems);
     }
     */
}
