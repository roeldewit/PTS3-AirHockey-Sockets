package Airhockey.Elements;

import Airhockey.Utils.Utils;
import Airhockey.Properties.PropertiesManager;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Roel
 */
public class LeftBat extends Bat {

    private float speed;
    private final float speedManipulation;

    public LeftBat(float postionX, float postionY, String type) {
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
        speedManipulation = speed * 0.50f;
    }

    public void moveUp(Body puckBody) {
        Body body = (Body) node.getUserData();

        Vec2 linearVelocity = puckBody.getLinearVelocity();
        float verticalVelocity = Utils.toPixelPosY(linearVelocity.y);

        int vv = (int) Math.abs(verticalVelocity);
        if (vv > speed) {
            body.setLinearVelocity(new Vec2(speed - speedManipulation, speed));
        } else {
            body.setLinearVelocity(new Vec2(Utils.toPosX((float) vv), Utils.toPosY((float) vv)));
        }

        float xpos = Utils.toPixelPosX(body.getPosition().x);
        float ypos = Utils.toPixelPosY(body.getPosition().y);
        setPosition(xpos, ypos);
    }

    public void moveDown(Body puckBody) {
        Body body = (Body) node.getUserData();

        Vec2 linearVelocity = puckBody.getLinearVelocity();
        float verticalVelocity = Utils.toPixelPosY(linearVelocity.y);

        int vv = (int) Math.abs(verticalVelocity);
        if (vv > speed) {
            body.setLinearVelocity(new Vec2(-speed + speedManipulation, -speed));
        } else {
            body.setLinearVelocity(new Vec2(Utils.toPosX((float) -vv), Utils.toPosY((float) -vv)));
        }
        float xpos = Utils.toPixelPosX(body.getPosition().x);
        float ypos = Utils.toPixelPosY(body.getPosition().y);
        setPosition(xpos, ypos);
    }

    public void stop() {
        Body body = (Body) node.getUserData();
        body.setLinearVelocity(new Vec2(0.0f, 0.0f));
    }
}
