package Airhockey.Elements;

import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
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
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(3.0);
        shadow.setOffsetX(2.0);
        shadow.setColor(Color.BLACK);
        
        InnerShadow is = new InnerShadow();
        is.setOffsetX(0.5f);
        is.setOffsetY(0.5f);

        Rectangle goal = new Rectangle();
        goal.setWidth(width);
        goal.setHeight(height);
        goal.setFill(color);
        goal.setLayoutX(topLeftX);
        goal.setLayoutY(topLeftY);
        goal.setArcWidth(10);
        goal.setArcHeight(10);
        //goal.setEffect(shadow);
        goal.setEffect(is);

        RotateTransition t = new RotateTransition(Duration.millis(1), goal);
        t.setByAngle(rotation);
        t.setAutoReverse(false);
        t.play();

        return goal;
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
