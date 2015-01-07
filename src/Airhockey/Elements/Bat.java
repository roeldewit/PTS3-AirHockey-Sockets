package Airhockey.Elements;

import Airhockey.Renderer.Constants;
import Airhockey.User.Player;
import Airhockey.Utils.Utils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class Bat {

    private Player player;
    private final String type;

    protected final float positionX;
    protected final float positionY;

    protected final float diameter;
    protected final float radius;

    public Node node;
    public Node imageNode;

    private final BodyType bodyType;
    private Body body;

    public Bat(float positionX, float positionY, String type) {
        this.type = type;
        this.positionX = positionX;
        this.positionY = positionY;
        this.bodyType = BodyType.KINEMATIC;
        this.radius = 40;
        this.diameter = radius * 2.0f;
        this.node = create();
        this.imageNode = createImageNode();
    }

    private Node create() {
        Circle bat = new Circle();
        bat.setRadius(radius);
        bat.setFill(Color.TRANSPARENT);

        bat.setLayoutX(Utils.toPixelPosX(positionX));
        bat.setLayoutY(Utils.toPixelPosY(positionY));
        bat.setCache(true);

        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(positionX, positionY);

        CircleShape cs = new CircleShape();
        cs.m_radius = radius * 0.1f;

        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.1f;
        fd.friction = 0.0f;
        fd.restitution = 0.1f;

        body = Utils.world.createBody(bd);
        body.createFixture(fd);
        bat.setUserData(body);
        return bat;
    }

    private Node createImageNode() {
        Image image;
        switch (type) {
            case Constants.COLOR_RED:
                image = new Image(getClass().getResourceAsStream("Images/RedBatSmaller.png"), diameter, diameter, false, false);
                break;
            case Constants.COLOR_BLUE:
                image = new Image(getClass().getResourceAsStream("Images/LightBlueBatSmaller.png"), diameter, diameter, false, false);
                break;
            default:
                image = new Image(getClass().getResourceAsStream("Images/GreenBatSmaller.png"), diameter, diameter, false, false);
                break;
        }
        ImageView imageView = new ImageView(image);
        imageView.relocate(Utils.toPixelPosX(positionX) - radius, Utils.toPixelPosY(positionY) - radius);
        return imageView;
    }

    public void setPosition(float xPosition, float yPosition) {
        node.setLayoutX(xPosition);
        node.setLayoutY(yPosition);
        imageNode.setLayoutX(xPosition - radius);
        imageNode.setLayoutY(yPosition - radius);
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public Fixture getFixture() {
        return body.getFixtureList();
    }

    public Body getBody() {
        return body;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
