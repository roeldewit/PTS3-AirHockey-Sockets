package Airhockey.Renderer;

import Airhockey.Connection.Encoder;
import Airhockey.Connection.IConnectionManager;

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

    public void setBottomBatLocation(int x, int y);

    public void setLeftBatLocation(int x, int y);

    public void setRightBatLocation(int x, int y);

    public void setGoalMade(int newRound, int scorer, int against);

    public void setUpGame(String p1Name, String p2Name, String p3Name);

    public void start(Encoder encoder);
}
