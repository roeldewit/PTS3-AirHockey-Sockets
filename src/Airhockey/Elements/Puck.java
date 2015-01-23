package Airhockey.Elements;

import Airhockey.Utils.Utils;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author Roel
 */
public class Puck {

    private static final int DEFAULT_ENGINE_X_POS = 48;
    private static final int DEFAULT_ENGINE_Y_POS = 40;

    public Node node;
    public Image imageNode;

    private float positionX;
    private float positionY;

    private final float radius = 20;

    private final BodyType bodyType;
    public Body body;
    public FixtureDef fd;

    public Puck() {
        this.bodyType = BodyType.DYNAMIC;

        this.positionX = Utils.toPixelPosX(DEFAULT_ENGINE_X_POS);
        this.positionY = Utils.toPixelPosY(DEFAULT_ENGINE_Y_POS);

        node = create();
        imageNode = createImageNode();
    }

    private Node create() {
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(1.0);
        shadow.setOffsetX(1.0);
        shadow.setColor(Color.TRANSPARENT);

        Circle puck = new Circle();
        puck.setRadius(radius);
        puck.setFill(Color.TRANSPARENT);

        puck.setLayoutX(positionX);
        puck.setLayoutY(positionY);
        puck.setCache(true);
        puck.setEffect(shadow);

        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(DEFAULT_ENGINE_X_POS, DEFAULT_ENGINE_Y_POS);

        CircleShape cs = new CircleShape();
        cs.m_radius = radius * 0.1f;

        fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.5f;
        fd.friction = 10.0f;
        fd.restitution = 0.95f;

        body = Utils.world.createBody(bd);
        body.createFixture(fd);
        puck.setUserData(body);
        return puck;
    }

    private Image createImageNode() {
        Image image = new Image(getClass().getResourceAsStream("Images/Puck.png"), radius * 2f, radius * 2f, false, false);
        return image;
    }

    public void setPosition(float xPosition, float yPosition) {
        node.setLayoutX(xPosition);
        node.setLayoutY(yPosition);

        positionX = xPosition;
        positionY = yPosition;
    }

    public Fixture getFixture() {
        return body.getFixtureList();
    }

    public float getImagePositionX() {
        return positionX - (radius * 2.0f);
    }

    public float getImagePositionY() {
        return positionY - radius + 10f;
    }

}
