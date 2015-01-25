package Airhockey.Main;

import Airhockey.Connection.Client;
import Airhockey.Connection.ConnectionListener;
import Airhockey.Connection.Decoder;
import Airhockey.Connection.Encoder;
import Airhockey.Connection.IConnectionManager;
import Airhockey.Elements.Bat;
import Airhockey.Renderer.*;
import Airhockey.User.*;
import Airhockey.Utils.Database;
import Airhockey.Utils.ScoreCalculator;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Class that controls the players game and keeps all the data about it.
 *
 * @author Sam
 */
public class Game {

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

    private boolean isHost;
    private boolean isMultiplayer;
    private boolean isSpectator;
    private boolean canSetGoal = true;

    private Database database;

    /**
     * Constructor
     *
     * @param primaryStage Used to create a window.
     */
    public Game(Stage primaryStage) {
        this.primaryStage = primaryStage;
        players = new ArrayList<>();
    }

    /**
     * Sets this game up as a singleplayer game.
     */
    public void startSinglePlayer() {
        this.user = new User("Player1");
        addPlayer("Player1");
        addPlayer("PC1");
        addPlayer("PC2");
        isMultiplayer = false;

        renderer = new Renderer(primaryStage, this, isMultiplayer);
        renderer.start(null, 1);
        renderer.setLabelNames("SAM", "PC1", "PC2");
    }

    /**
     * Sets this game up as a multiplayer game, with the current user as the host.
     *
     * @param user The current user.
     */
    public void startAsHost(User user) {
        this.user = user;
        addPlayer(user.getUsername());
        isHost = true;
        isMultiplayer = true;
        isSpectator = false;

        encoder = new Encoder();
        renderer = new Renderer(primaryStage, this, isMultiplayer);
        connectionListener = new ConnectionListener(this, new Decoder(renderer, this));
        connectionListener.start();
    }

    /**
     * Sets this game up as a multiplayer game, with the current user as a client.
     *
     * @param user The current user.
     * @param ipAddress The ip-adress of the host.
     */
    public void startAsClient(User user, String ipAddress) {
        this.user = user;
        isHost = false;
        isMultiplayer = true;
        isSpectator = false;

        encoder = new Encoder();
        renderer = new ClientRenderer(primaryStage, this, isSpectator);
        client = new Client(new Decoder(renderer, this), this, ipAddress);
        encoder.addManager(client, true);
        client.start();
    }

    /**
     * Sets this game up as a multiplayer game, with the current user as a spectator.
     *
     * @param user The current user.
     * @param ipAddress The ip-adress of the host.
     */
    public void startAsSpectator(User user, String ipAddress) {
        this.user = user;
        isHost = false;
        isMultiplayer = true;
        isSpectator = true;

        encoder = new Encoder();
        renderer = new ClientRenderer(primaryStage, this, isSpectator);
        client = new Client(new Decoder(renderer, this), this, ipAddress);
        encoder.addManager(client, true);
        client.start();
    }

    /**
     * Called on the client side when the client has connected to the host.
     * Sends the client player's username to the host server.
     */
    public void connectedToServer() {
        if (isSpectator) {
            encoder.sendSpectatorDataToServer(user.getUsername());
        } else {
            encoder.sendClientDataToServer(user.getUsername());
        }
    }

    /**
     * Called on the server (host) side after a client has connected to it.
     * Adds the connecion manager to the data encoder.
     *
     * @param manager The connection-manger used to send and receive game data.
     */
    public void clientConnected(IConnectionManager manager) {
        encoder.addManager(manager, false);
    }

    /**
     * Adds a player to the game.
     *
     * @param userName Name of the client player.
     */
    private void addPlayer(String userName) {
        Player player = new Player(players.size() + 1, new User(userName));
        players.add(player);
    }

    /**
     * Called on the server side to add a client to the game.
     * This method keeps track of how many client players have joined the game.
     * When two other players have joined it starts the game.
     *
     * @param userName Name of the client player.
     */
    public synchronized void addClientPlayer(String userName) {
        addPlayer(userName);

        numberOfClientsConnected++;
        if (numberOfClientsConnected == 2) {
            Platform.runLater(() -> {
                String p1Name = players.get(0).user.getUsername();
                String p2Name = players.get(1).user.getUsername();
                String p3Name = players.get(2).user.getUsername();

                for (int i = 2; i < 4; i++) {
                    encoder.sendSetUpGameAsClient(i, p1Name, p2Name, p3Name);
                }
                renderer.start(encoder, 1);
                renderer.setLabelNames(p1Name, p2Name, p3Name);
            });
            //connectionListener.cancel();
        }
    }

    /**
     * Called on the server side to add a spectator to the game.
     *
     * @param userName Name of the spectator.
     */
    public void addSpectator(String userName) {
        addPlayer(userName);
        encoder.sendSetUpGameAsSpectator(players.size(),
                players.get(0).user.getUsername(),
                players.get(1).user.getUsername(),
                players.get(2).user.getUsername());
    }

    /**
     * Called on the client side to start the game.
     *
     * @param playerNumber Number the server assigned to the client player.
     */
    public void startGameAsClient(int playerNumber) {
        this.playerNumber = playerNumber;
        renderer.start(encoder, playerNumber);
    }

    /**
     * Called on the spectator side to start the game.
     *
     * @param playerNumber Number the server assigned to the spectator.
     */
    public void startGameAsSpectator(int playerNumber) {
        this.playerNumber = playerNumber;
        renderer.start(encoder, playerNumber);
    }

    /**
     * Called on the server to add a bat to a specific player.
     *
     * @param playerNumber Number of the player.
     * @param bat The bat object.
     */
    public void addPlayerToBat(int playerNumber, Bat bat) {
        for (Player player : players) {
            if (player.getId() == playerNumber) {
                bat.setPlayer(player);
            }
        }
    }

    /**
     * Called on the server side when a goal has been made.
     * Informs all clients that a goal has been made and changes the players scores accordingly.
     * If 10 rounds have been played this method cancels the game.
     *
     * @param scorer Bat of the player that has scored a goal.
     * @param against Bat of the player who had a goal made against him.
     */
    public synchronized void setGoal(Bat scorer, Bat against) {
        if (canSetGoal) {
            canSetGoal = false;

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

                if (round == 11) {
                    gameOver("Game Over", -1);
                } else {
                    renderer.resetRound(round);
                }
                canSetGoal = true;
            });
        }
    }

    /**
     * Gets called on the server side after a client player has disconnected.
     *
     * @param playerNumber Number of the client player who had disconnected.
     */
    public void clientLeftGame(int playerNumber) {
        gameOver("Player" + playerNumber + " left", playerNumber);
    }

    /**
     * Sets the scores of all the players who participated in this game.
     *
     * @param playerNumberWhoDisconnected Number of the player who disconnected. -1 if nobody disconnected.
     */
    private void setScores(int playerNumberWhoDisconnected) {
        try {
            for (int i = 0; i < 3; i++) {
                int playerScore = players.get(playerNumber).getScore();

                if ((playerNumberWhoDisconnected - 1) == i) {
                    database.updateRating(players.get(i).user, ScoreCalculator.calculateAdjustedGameScore(playerScore, round - 1, true));
                } else {
                    database.updateRating(players.get(i).user, ScoreCalculator.calculateAdjustedGameScore(playerScore, round - 1, false));
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Disconnects the game from the other players.
     */
    public void leaveGame() {
        encoder.shutDownManagers();
    }

    /**
     * Gets called when this players game has disconnected.
     */
    public void connectionLost() {
        renderer.stop("Connection Lost");
    }

    /**
     * Called on the client side when the server (host) has cancelled the game.
     */
    public void gameCancelled() {
        encoder.shutDownManagers();
        renderer.stop("Server Disconnected");
    }

    /**
     * Stops the renderer and the connection manager, and if this game is the host notifies the clients that the game has finished and updates the players scores in the database.
     *
     * @param reason Reason the game has stopped.
     * @param playerNumberWhoDisconnected Number of the player who disconnected. -1 if nobody disconnected.
     */
    public void gameOver(String reason, int playerNumberWhoDisconnected) {
        if (isMultiplayer) {
            if (isHost) {
                setScores(playerNumberWhoDisconnected);
                connectionListener.cancel();
                encoder.sendGameOver(reason);
            }
            encoder.shutDownManagers();
        }
        renderer.stop(reason);
    }

    /**
     * @return The username of this game's player.
     */
    public String getUsername() {
        return user.getUsername();
    }

}
