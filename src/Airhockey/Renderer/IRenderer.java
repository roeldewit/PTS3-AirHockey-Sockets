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
    public void setTextFields(String field, String value);

    public void resetRound(int round);

    public void setPuckLocation(int x, int y);

    public void setRedBatLocation(int x, int y);

    public void setBlueBatLocation(int x, int y);

    public void setGreenBatLocation(int x, int y);

    public void setGoalMade(int newRound, int scorer, int against);

    public void setUpGame(int playerNumber, String p1Name, String p2Name, String p3Name);

    public void start(Encoder encoder);

    public void stop();

    public void setLabelNames(String p1Name, String p2Name, String p3Name);
}
