package Airhockey.Main;

import Airhockey.Connection.Client;
import Airhockey.Connection.ConnectionListener;
import Airhockey.Connection.Decoder;
import Airhockey.Connection.Encoder;
import Airhockey.Connection.IConnectionManager;
import Airhockey.Elements.Bat;
import Airhockey.Renderer.*;
import Airhockey.User.*;
import Airhockey.Utils.ScoreCalculator;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 *
 * @author Sam
 */
public class Game {

    private int id;
    private int round = 1;
    private User user;
    private int playerNumber;
    private IRenderer renderer;
    private final ArrayList<Player> players;

    private int numberOfClientsConnected = 0;
    private Encoder encoder;
    private Client client;
    private ConnectionListener connectionListener;

    private final Stage primaryStage;

    //private ArrayList<Spectator> spectators;
    private ScoreCalculator scoreCalculator;
    private boolean isMultiplayer;

    public Game(Stage primaryStage) {
        this.primaryStage = primaryStage;
        players = new ArrayList<>();
    }

    public void startSinglePlayer() {
        addPlayer("Sam");
        addPlayer("PC1");
        addPlayer("PC2");
        isMultiplayer = false;

        renderer = new Renderer(primaryStage, this, isMultiplayer);
        renderer.start(null, 1);
        renderer.setLabelNames("SAM", "PC1", "PC2");
    }

    public void startAsHost(User user) {
        addPlayer(user.getUsername());
        isMultiplayer = true;

        encoder = new Encoder();
        renderer = new Renderer(primaryStage, this, isMultiplayer);
        connectionListener = new ConnectionListener(this, new Decoder(renderer, this));
        connectionListener.start();
    }

    public void startAsClient(User user, String ipAddress) {
        this.user = user;
        isMultiplayer = true;

        encoder = new Encoder();
        renderer = new ClientRenderer(primaryStage, this);
        client = new Client(new Decoder(renderer, this), this, ipAddress);
        encoder.addManager(client);
        client.start();

    }

    public void connectedToServer() {
        encoder.sendGameData(user.getUsername());
    }

    public void clientConnected(IConnectionManager manager) {
        encoder.addManager(manager);
    }

    private void addPlayer(String userName) {
        Player player = new Player(players.size() + 1, new User(userName));
        players.add(player);
    }

    public synchronized void addClientPlayer(String userName) {
        addPlayer(userName);

        numberOfClientsConnected++;
        if (numberOfClientsConnected == 2) {
            Platform.runLater(() -> {
                String p1Name = players.get(0).user.getUsername();
                String p2Name = players.get(1).user.getUsername();
                String p3Name = players.get(2).user.getUsername();

                for (int i = 2; i < 4; i++) {
                    encoder.sendSetUpGame(i, p1Name, p2Name, p3Name);
                }
                renderer.start(encoder, 1);
                renderer.setLabelNames(p1Name, p2Name, p3Name);
            });
            connectionListener.cancel();
        }
    }

    public void startGameAsClient(int playerNumber) {
        this.playerNumber = playerNumber;
        renderer.start(encoder, playerNumber);
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
        Platform.runLater(() -> {
            round++;

            Player playerAgainst = against.getPlayer();
            playerAgainst.downScore();
            renderer.setTextFields(playerAgainst.getId(), playerAgainst.getScore());

            if (scorer != null) {
                Player playerScorer = scorer.getPlayer();
                playerScorer.upScore();
                renderer.setTextFields(playerScorer.getId(), playerScorer.getScore());
                if (isMultiplayer) {
                    encoder.sendGoalMade(round, playerScorer.getId(), playerScorer.getScore(), playerAgainst.getId(), playerAgainst.getScore());
                }
            } else {
                if (isMultiplayer) {
                    encoder.sendGoalMade(round, -1, -1, playerAgainst.getId(), playerAgainst.getScore());
                }
            }

            if (round == 100) {
                if (isMultiplayer) {
                    encoder.sendGameOver();
                }
                gameOver();
            } else {
                renderer.resetRound(round);
            }
        });
    }

    public void gameOver() {
        renderer.stop();
    }

    public String getUsername(int id) {
        return players.get(id - 1).user.getUsername();
    }

}
