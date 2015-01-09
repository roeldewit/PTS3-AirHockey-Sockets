package Airhockey.Renderer;

import Airhockey.Connection.Encoder;

/**
 *
 * @author Sam
 */
public interface IRenderer {

    /**
     * Used by clients.
     */
    public void setTextFields(int field, int score);

    public void resetRound(int round);

    public void setPuckLocation(int x, int y);

    public void setRedBatLocation(int x, int y);

    public void setBlueBatLocation(int x, int y);

    public void setGreenBatLocation(int x, int y);

    public void setGoalMade(int newRound, int scorer, int scorerScore, int against, int againstScore);

    public void setUpGame(String p1Name, String p2Name, String p3Name);

    public void start(Encoder encoder, int playerNumber);

    public void stop();

    public void setLabelNames(String p1Name, String p2Name, String p3Name);
}
