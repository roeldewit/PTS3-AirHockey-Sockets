package Airhockey.Main;

import Airhockey.Connection.Encoder;
import Airhockey.Connection.LobbyClient;
import Airhockey.Connection.LobbyEncoder;
import Airhockey.Rmi.SerializableGame;
import Airhockey.Utils.ScoreCalculator;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private ArrayList<SerializableGame> games;

    //
    private LobbyClient lobbyClient;
    private LobbyEncoder encoder;

    private HashMap<String, User> hashMapUsernameToUser;

    private Stage primaryStage;

    private Thread lobbyClientThread;

    public Lobby(Stage primaryStage) throws NotBoundException, IOException, SQLException {
        LobbySetUp(primaryStage);
        this.primaryStage = primaryStage;
//        database = new Database();
        hashMapUsernameToUser = new HashMap();

        games = new ArrayList<>();
        users = new ArrayList<>();      
     
        users.add(new User("Jan"));
        users.add(new User("Piet"));
        users.add(new User("Henk"));
//        users = database.getUsers();
//
//        for (User dbuser : users) {
//            hashMapUsernameToUser.put(dbuser.getUsername(), dbuser);
//        }

        initialSetUpLobby();
        chatbox = new Chatbox();
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

    public void writeLine(String text, User user) {
        encoder.writeLine(user.getUsername(), text);
    }

    public void addWaitingGame(int id, String description, String portIP, String username) {
        SerializableGame serializableGame = new SerializableGame(id, description, portIP, username);

        games.add(serializableGame);
    }

    private void LobbySetUp(Stage primaryStage) {

        Parent root = null;

        try {
            root = FXMLLoader.load(Lobby.class.getResource("LobbyLayout.fxml"));
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
    
    private LobbyEncoder getEncoder(){
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
