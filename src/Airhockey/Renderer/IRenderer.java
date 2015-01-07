package Airhockey.Renderer;

import Airhockey.Connection.Encoder;

/**
 *
 * @author Sam
 */
public interface IRenderer {

    public void setTextFields(String field, String value);

    public void resetRound(int round);

    public void setPuckLocation(float x, float y);

    public void setBottomBatLocation(float x, float y);

    public void setLeftBatLocation(float x, float y);

    public void setRightBatLocation(float x, float y);

    public void setEncoder(Encoder encoder);

    public void connectionMade();
}
