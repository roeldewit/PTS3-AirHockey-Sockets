package Airhockey.Renderer;

import Airhockey.Connection.Encoder;

/**
 * Interface providing a way to comminucate with the renderer classes.
 *
 * @author Sam
 */
public interface IRenderer {

    /**
     * Sets the score fields of a game at the start or after every new round.
     *
     * @param field Field that needs to be adjusted.
     * @param score Value for the new field.
     */
    public void setTextFields(int field, int score);

    /**
     * Resets the game for the start of a new round
     *
     * @param round Number of the new round.
     */
    public void resetRound(int round);

    /**
     * Sets the location of the puck for a client.
     *
     * @param x Puck's x-axis location
     * @param y Puck's y-axis location
     */
    public void setPuckLocation(int x, int y);

    /**
     * Sets the location of the puck for a client.
     *
     * @param x Red bat's x-axis location
     * @param y Red bat's y-axis location
     */
    public void setRedBatLocation(int x, int y);

    /**
     * Sets the location of the blue bat for a client.
     *
     * @param x blue bat's x-axis location
     * @param y blue bat's y-axis location
     */
    public void setBlueBatLocation(int x, int y);

    /**
     * Sets the location of the green bat for a client.
     *
     * @param x green bat's x-axis location
     * @param y green bat's y-axis location
     */
    public void setGreenBatLocation(int x, int y);

    /**
     * Called on the server side after a command from a client to move it's bat.
     *
     * @param playerNumber Number of the player who requested the move.
     * @param direction Requested direction for the bat.
     */
    public void moveClientBat(int playerNumber, int direction);

    /**
     * Called on the client side after a goal has been made.
     *
     * @param newRound Number of the next round.
     * @param scorer Number of the player who scored the goal.
     * @param scorerScore Score of the player who scored the goal.
     * @param against Number of the player who had a goal made against him.
     * @param againstScore Score of the player who had a goal made against him.
     */
    public void setGoalMade(int newRound, int scorer, int scorerScore, int against, int againstScore);

    /**
     * Called on the client side to set the name text field's in the game and start the game's countdown timer.
     *
     * @param p1Name Player one's name.
     * @param p2Name Player two's name.
     * @param p3Name Player three's name.
     */
    public void setUpGame(String p1Name, String p2Name, String p3Name);

    /**
     * Sets up the renderer class.
     *
     * @param encoder The encoder used to send data to the other players.
     * @param playerNumber Number of it's player.
     */
    public void start(Encoder encoder, int playerNumber);

    /**
     * Adds a line in the chatbox of the game.
     *
     * @param line The string of text to be set.
     */
    public void addChatBoxLine(String line);

    /**
     * Stops the renderer class.
     *
     * @param reason The reason why the game was stopped.
     */
    public void stop(String reason);

    /**
     * Called on the client side to set the name text field's.
     *
     * @param p1Name Player one's name.
     * @param p2Name Player two's name.
     * @param p3Name Player three's name.
     */
    public void setLabelNames(String p1Name, String p2Name, String p3Name);
}
