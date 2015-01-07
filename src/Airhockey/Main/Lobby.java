package Airhockey.Main;

import Airhockey.User.Player;
import Airhockey.Utils.ScoreCalculator;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

    //IMainLobby mainLobby;
    private Registry register;
    private String ipMainServer = "localhost";
    private int portMainServer = 1099;
    Database database;

    HashMap<String, User> hashMapUsernameToUser;

    Stage primaryStage;

    public Lobby(Stage primaryStage) throws RemoteException, NotBoundException, IOException, SQLException {
        LobbySetUp(primaryStage);
        this.primaryStage = primaryStage;
//        database = new Database();
        hashMapUsernameToUser = new HashMap();

        users = new ArrayList<>();

        users.add(new User("Jan"));
        users.add(new User("Piet"));
        users.add(new User("Henk"));
//        users = database.getUsers();
//
        for (User dbuser : users) {
            hashMapUsernameToUser.put(dbuser.getUsername(), dbuser);
        }

        connectToMainServer();

        getInitialChatbox();
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

    private void connectToMainServer() throws RemoteException, NotBoundException {
        //this.mainLobby = null;

        register = LocateRegistry.getRegistry(ipMainServer, portMainServer);

        //this.mainLobby = ((IMainLobby) register.lookup("MainLobby"));
    }

    private void getInitialChatbox() throws RemoteException {
//        SerializableChatBox1 serializableChatBox = mainLobby.getChatBox();
//        chatbox = new Chatbox();
//
//        for (SerializableChatBoxLine serializableChatBoxLine : serializableChatBox.lines) {
//            chatbox.writeLine(new ChatboxLine(serializableChatBoxLine.text, hashMapUsernameToUser.get(serializableChatBoxLine.player)));
//        }
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
