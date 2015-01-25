package Airhockey.Elements;

import Airhockey.Renderer.Constants;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;

/**
 * Class that holds all the information about the game's goal item.
 *
 * @author Sam
 */
public class Goal {

    public final int topLeftX;
    public final int topLeftY;
    private final int width = 340;
    private final int height = 40;
    private final double rotation;

    public Node collisionNode;
    public Image imageNode;

    /**
     * Constructor
     *
     * @param colorCode Determines the color and position of this goal.
     */
    public Goal(String colorCode) {
        switch (colorCode) {
            case Constants.COLOR_RED:
                topLeftX = 320;
                topLeftY = 670;
                rotation = 0;
                imageNode = new Image(getClass().getResourceAsStream("Images/RedGoal.png"), width, height, false, false);
                break;
            case Constants.COLOR_BLUE:
                topLeftX = 125;
                topLeftY = 330;
                rotation = -59;
                imageNode = new Image(getClass().getResourceAsStream("Images/BlueGoal.png"), 208, 310, false, false);
                break;
            default:
                topLeftX = 512;
                topLeftY = 330;
                rotation = 62;
                imageNode = new Image(getClass().getResourceAsStream("Images/GreenGoal.png"), 192, 316, false, false);
                break;
        }

        this.collisionNode = createCollisionNode();
    }

    /**
     * Creates collision detection part of this goal.
     *
     * @return The collision node;
     */
    private Node createCollisionNode() {
        Vec2 TopLeft = new Vec2(topLeftX + 20, topLeftY + 20);
        Vec2 TopRight = new Vec2((float) (topLeftX + width) - 20, topLeftY + 20);

        Line line = new Line(TopLeft.x, TopLeft.y, TopRight.x, TopRight.y);
        line.setStroke(Color.TRANSPARENT);

        RotateTransition t = new RotateTransition(Duration.millis(1), line);
        t.setByAngle(rotation);
        t.setAutoReverse(false);
        t.play();

        return line;
    }
}
