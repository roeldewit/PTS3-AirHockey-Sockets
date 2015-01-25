package Airhockey.Elements;

import Airhockey.Renderer.Constants;
import Airhockey.User.Player;
import Airhockey.Utils.Utils;
import javafx.scene.Node;
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
 * Class that holds all the information about the game's bat item.
 *
 * @author Sam
 */
public class Bat {

    public static final int CENTER_BAT = 0;
    public static final int LEFT_BAT = 1;
    public static final int RIGHT_BAT = 2;

    private Player player;
    protected final int type;

    private float positionX;
    private float positionY;

    public final float diameter;
    protected final float radius;

    public Node node;
    public Image imageNode;

    private final BodyType bodyType;
    private Body body;

    /**
     * Constructor
     *
     * @param enginePositionX Starting x-axis position in JBox2D engine coordinates.
     * @param enginePositionY Starting y-axis position in JBox2D engine coordinates.
     * @param type Defines if this bat is a center, left or right bat.
     */
    public Bat(float enginePositionX, float enginePositionY, int type) {
        this.type = type;
        this.positionX = Utils.toPixelPosX(enginePositionX);
        this.positionY = Utils.toPixelPosY(enginePositionY);
        this.bodyType = BodyType.KINEMATIC;
        this.radius = Constants.BAT_RADIUS;
        this.diameter = radius * 2.0f;
        this.node = create(enginePositionX, enginePositionY);
        this.imageNode = createImageNode();
    }

    /**
     * Creates a node used by the Jbox engine for collision detection.
     *
     * @param enginePositionX
     * @param enginePositionY
     * @return
     */
    private Node create(float enginePositionX, float enginePositionY) {
        Circle bat = new Circle();
        bat.setRadius(radius);
        bat.setFill(Color.TRANSPARENT);

        bat.setLayoutX(positionX);
        bat.setLayoutY(positionY);
        bat.setCache(true);

        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(enginePositionX, enginePositionY);

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

    /**
     * Retrieves the image that will be displayed on screen.
     *
     * @return This bats image.
     */
    private Image createImageNode() {
        Image image;
        switch (type) {
            case CENTER_BAT:
                image = new Image(getClass().getResourceAsStream("Images/RedBat.png"), diameter, diameter, false, false);
                break;
            case LEFT_BAT:
                image = new Image(getClass().getResourceAsStream("Images/LightBlueBat.png"), diameter, diameter, false, false);
                break;
            default:
                image = new Image(getClass().getResourceAsStream("Images/GreenBat.png"), diameter, diameter, false, false);
                break;
        }
        return image;
    }

    /**
     * Sets this bats normal pixel positions.
     *
     * @param xPosition This bats x-axis pixel position.
     * @param yPosition This bats y-axis pixel position.
     */
    public void setPosition(float xPosition, float yPosition) {
        node.setLayoutX(xPosition);
        node.setLayoutY(yPosition);

        positionX = xPosition;
        positionY = yPosition;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public double getImagePositionX() {
        return positionX - radius - 28f;
    }

    public float getImagePositionY() {
        return positionY - radius + 7f;
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
