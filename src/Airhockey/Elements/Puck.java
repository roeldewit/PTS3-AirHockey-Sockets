package Airhockey.Elements;

import Airhockey.Renderer.Constants;
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
public class Puck {

    public Node node;
    public Node imageNode;

    private final float positionX;
    private final float positionY;

    private float radius = 25f;

    private final BodyType bodyType;
    public Body body;
    public FixtureDef fd;

    public Puck(float positionX, float positionY) {
        this.radius = Utils.BALL_RADIUS;
        this.positionX = positionX;
        this.positionY = positionY;
        this.bodyType = BodyType.DYNAMIC;

        node = create();
        imageNode = createImageNode();
    }

    private Node create() {
        Circle puck = new Circle();
        puck.setRadius(radius);
        puck.setFill(Color.web(Constants.COLOR_ORANGE));

        puck.setLayoutX(Utils.toPixelPosX(positionX));
        puck.setLayoutY(Utils.toPixelPosY(positionY));
        puck.setCache(true);

        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(positionX, positionY);

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

    private Node createImageNode() {
        Image image = new Image(getClass().getResourceAsStream("Images/Puck.png"), radius * 2f, radius * 2f, false, false);

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

    public Fixture getFixture() {
        return body.getFixtureList();
    }

}
