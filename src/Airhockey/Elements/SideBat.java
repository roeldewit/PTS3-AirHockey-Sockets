package Airhockey.Elements;

import Airhockey.Utils.Utils;
import Airhockey.Properties.PropertiesManager;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * Class that extends the bat class to move it along a triangle side and to add AI functionality.
 *
 * @author Sam
 */
public class SideBat extends Bat {

    private float speed;
    private final float speedManipulation;

    public SideBat(float postionX, float postionY, int type) {
        super(postionX, postionY, type);

        String difficulty = PropertiesManager.loadProperty("LEB-Difficulty");
        System.out.println("L:" + difficulty);
        difficulty = "EASY";

        switch (difficulty) {
            case "EASY":
                speed = 8.0f;
                break;
            case "MEDIUM":
                speed = 11.0f;
                break;
            case "HARD":
                speed = 14.0f;
                break;
            case "VERY_HARD":
                speed = 17.0f;
                break;
        }
        if (type == LEFT_BAT) {
            speedManipulation = speed * 0.55f;
        } else {
            speedManipulation = speed * 0.60f;
        }
    }

    /**
     * AI method to move the bat up according to the puck's position.
     *
     * @param puckBody JBox body of the puck.
     */
    public void moveUp(Body puckBody) {
        Body body = (Body) node.getUserData();

        Vec2 linearVelocity = puckBody.getLinearVelocity();
        float verticalVelocity = Utils.toPixelPosY(linearVelocity.y);

        int vv = (int) Math.abs(verticalVelocity);
        if (vv > speed) {
            if (type == LEFT_BAT) {
                body.setLinearVelocity(new Vec2(speed - speedManipulation, speed));
            } else if (type == RIGHT_BAT) {
                body.setLinearVelocity(new Vec2(-speed + speedManipulation, speed));
            }
        } else {
            if (type == LEFT_BAT) {
                body.setLinearVelocity(new Vec2(Utils.toPosX((float) vv), Utils.toPosY((float) vv)));
            } else if (type == RIGHT_BAT) {
                body.setLinearVelocity(new Vec2(Utils.toPosX((float) -vv), Utils.toPosY((float) vv)));
            }
        }

        float xpos = Utils.toPixelPosX(body.getPosition().x);
        float ypos = Utils.toPixelPosY(body.getPosition().y);
        setPosition(xpos, ypos);
    }

    /**
     * AI method to move the bat down according to the puck's position.
     *
     * @param puckBody JBox body of the puck.
     */
    public void moveDown(Body puckBody) {
        Body body = (Body) node.getUserData();

        Vec2 linearVelocity = puckBody.getLinearVelocity();
        float verticalVelocity = Utils.toPixelPosY(linearVelocity.y);

        int vv = (int) Math.abs(verticalVelocity);
        if (vv > speed) {
            if (type == LEFT_BAT) {
                body.setLinearVelocity(new Vec2(-speed + speedManipulation, -speed));
            } else if (type == RIGHT_BAT) {
                body.setLinearVelocity(new Vec2(speed - speedManipulation, -speed));
            }
        } else {
            if (type == LEFT_BAT) {
                body.setLinearVelocity(new Vec2(Utils.toPosX((float) -vv), Utils.toPosY((float) -vv)));
            } else if (type == RIGHT_BAT) {
                body.setLinearVelocity(new Vec2(Utils.toPosX((float) vv), Utils.toPosY((float) -vv)));
            }
        }
        float xpos = Utils.toPixelPosX(body.getPosition().x);
        float ypos = Utils.toPixelPosY(body.getPosition().y);
        setPosition(xpos, ypos);
    }

    /**
     * Stops the movement of the bat.
     */
    public void stop() {
        Body body = (Body) node.getUserData();
        body.setLinearVelocity(new Vec2(0.0f, 0.0f));
    }
}
