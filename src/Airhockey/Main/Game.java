package Airhockey.Main;

import Airhockey.Connection.Client;
import Airhockey.Connection.ConnectionListener;
import Airhockey.Connection.Encoder;
import Airhockey.Connection.IConnectionManager;
import Airhockey.Elements.Bat;
import Airhockey.Renderer.*;
import Airhockey.User.*;
import Airhockey.Utils.ScoreCalculator;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 *
 * @author Roel
 */
public class Game {

    private int id;
    private int round = 1;
    private Player owner;
    private IRenderer renderer;
    private ArrayList<Player> players;
    private ArrayList<User> users;
    private Player currentPlayer;

    private int numberOfClientsConnected = 0;
    private Encoder encoder;

    private Client client;

//    private ArrayList<Spectator> spectators;
    private Chatbox chatbox;
    private ScoreCalculator scoreCalculator;

    private boolean isHost;
    private boolean isMultiplayer;

    // data of the host
    private String ipHost;
    private int port;

    private ConnectionListener connectionListener;

    public Game(Stage primaryStage, boolean isHost, boolean isMultiplayer) {
        this.isHost = isHost;
        this.isMultiplayer = isMultiplayer;
        players = new ArrayList<>();

        addPlayer(new User("Sam"));
        addPlayer(new User("Com1"));
        addPlayer(new User("Com2"));

        encoder = new Encoder();

        //renderer = new Renderer(primaryStage, this, isMultiplayer);
        if (isHost) {
            this.renderer = new Renderer(primaryStage, this, true);
            connectionListener = new ConnectionListener(this, renderer);
            connectionListener.start();
        } else {
            this.renderer = new ClientRenderer(primaryStage, this);
            client = new Client(renderer);
            client.start();
        }
    }

    /**
     * Constructor used to start a game as a Host
     *
     * @param primaryStage
     * @param players
     * @param users
     * @throws RemoteException
     */
    public Game(Stage primaryStage, ArrayList<Player> players, ArrayList<User> users) throws RemoteException {
        this.players = players;
        this.owner = players.get(0);
        this.chatbox = new Chatbox();
        this.users = users;
        this.currentPlayer = players.get(0);

        this.renderer = new Renderer(primaryStage, this, true);
        connectionListener = new ConnectionListener(this, renderer);
    }

    /**
     * Constructor used to start a game as a client
     *
     * @param primaryStage
     * @param ipHost
     * @param port
     * @param players
     * @param currentPlayer
     * @throws RemoteException
     */
    public Game(Stage primaryStage, String ipHost, int port, ArrayList<Player> players, Player currentPlayer) throws RemoteException {
        this.ipHost = ipHost;
        this.port = port;
        this.players = players;
        this.owner = players.get(0);
        this.currentPlayer = currentPlayer;

        this.renderer = new ClientRenderer(primaryStage, this);
        client = new Client(renderer);

    }

    public synchronized void clientConnected(IConnectionManager manager) {
        encoder.addManager(manager);

        numberOfClientsConnected++;

        System.out.println("NOCC: " + numberOfClientsConnected);
        if (numberOfClientsConnected == 2) {
            System.out.println("joajoa");
            Platform.runLater(() -> {
                System.out.println("PFRL joajoa");
                renderer.start(encoder);
            });
            connectionListener.cancel();
        }
    }

    public void addPlayer(User user) {
        Player player = new Player(players.size() + 1, user);
        players.add(player);
    }

    public void startGame(Player owner) {
        throw new UnsupportedOperationException();
    }

    public void leaveGame(User user) {
        /*zodra een speler het speelveld van een spel, waarvoor inmiddels ten minste 1 ronde is
         voltooid, vroegtijdig verlaat, bijvoorbeeld vanwege een slechte internetverbinding1
         ,
         wordt het spel als beëindigd verklaard. De eindscore S wordt dan als volgt
         gecorrigeerd:
         S := (S-20)*10/n + 20
         waarbij n het aantal gespeelde ronden voorstelt (1≤n≤10).
         Als de ratingscore voor de weggevallen speler beter is dan zijn huidige rating, wordt
         de ratingscore genegeerd. Als de ratingscore van een nog aanwezige speler slechter is
         dan zijn/haar huidige rating, wordt deze ratingscore ook niet verwerkt*/
    }

    public void addPlayerToBat(int playerId, Bat bat) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                System.out.println("pi:" + playerId);
                bat.setPlayer(player);
                //bat.getPlayer();
                System.out.println("bp: " + bat.getPlayer());
            }
        }
    }

    public synchronized void setGoal(Bat scorer, Bat against, Encoder encoder) {
        round++;

        Player playerAgainst = against.getPlayer();
        playerAgainst.downScore();
        renderer.setTextFields("PLAYER" + playerAgainst.getId() + "_SCORE", String.valueOf(playerAgainst.getScore()));

        if (scorer != null) {
            Player playerScorer = scorer.getPlayer();
            playerScorer.upScore();
            renderer.setTextFields("PLAYER" + playerScorer.getId() + "_SCORE", String.valueOf(playerScorer.getScore()));
            encoder.sendGoalMade(round, playerScorer.getId(), playerAgainst.getId());
        } else {
            encoder.sendGoalMade(round, -1, playerAgainst.getId());
        }

        if (round == 10) {
            stop();
        } else {
            renderer.resetRound(round);
        }

    }

    public String getUsername(int id) {
        return players.get(id - 1).user.getUsername();
    }

    private void stop() {
        //renderer.resetRound();
    }

}
