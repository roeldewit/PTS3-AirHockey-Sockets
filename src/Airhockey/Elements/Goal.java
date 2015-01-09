package Airhockey.Elements;

import Airhockey.Renderer.Constants;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Sam
 */
public class Goal {

    public static final String GOAL_BOTTOM = "bottom";
    public static final String GOAL_LEFT = "left";
    public static final String GOAL_RIGHT = "right";

    private final int topLeftX;
    private final int topLeftY;
    private final int width = 340;
    private final int height = 40;
    private final double rotation;

    private final Color color;

    public Node node;
    public Node collisionNode;

    public Goal(String position, String colorCode) {

        switch (position) {
            case GOAL_BOTTOM:
                topLeftX = 340;
                topLeftY = 670;
                rotation = 0;
                break;
            case GOAL_LEFT:
                topLeftX = 124;
                topLeftY = 330;
                rotation = -57;
                break;
            default:
                topLeftX = 548;
                topLeftY = 330;
                rotation = 60;
                break;
        }

        color = Color.web(colorCode);

        this.node = createRect();
        this.collisionNode = createCollisionNode();
    }

    /**
     * Creates the visible part of the goal
     *
     * @return
     */
    private Node createRect() {
        Rectangle r = new Rectangle();

        r.setWidth(width);
        r.setHeight(height);
        r.setFill(color);
        r.setLayoutX(topLeftX);
        r.setLayoutY(topLeftY);
        r.setArcWidth(10);
        r.setArcHeight(10);

        RotateTransition t = new RotateTransition(Duration.millis(1), r);
        t.setByAngle(rotation);
        t.setAutoReverse(false);
        t.play();

        return r;
    }

    private Node createCollisionNode() {
        Vec2 TopLeft = new Vec2(topLeftX + 20, topLeftY + 20);
        Vec2 TopRight = new Vec2((float) (topLeftX + width) - 20, topLeftY + 20);

        Line line = new Line(TopLeft.x, TopLeft.y, TopRight.x, TopRight.y);
        line.setStroke(Color.WHITE);

        RotateTransition t = new RotateTransition(Duration.millis(1), line);
        t.setByAngle(rotation);
        t.setAutoReverse(false);
        t.play();

        return line;
    }
}
