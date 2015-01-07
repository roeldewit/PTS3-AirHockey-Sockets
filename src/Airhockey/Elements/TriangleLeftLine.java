package Airhockey.Elements;

import Airhockey.Utils.Utils;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author Sam
 */
public class TriangleLeftLine {

    public Node node;

    private final float positionXL;
    private final float positionYL;
    private final float positionXR;
    private final float positionYR;

    public TriangleLeftLine(int screenHeight, float positionXL, float positionYL, float positionXR, float positionYR) {
        this.positionXL = positionXL;
        this.positionYL = positionYL;
        this.positionXR = positionXR;
        this.positionYR = positionYR;

        node = create();
    }

    private Node create() {
        Vec2 VecL = new Vec2(positionXL, positionYL);
        Vec2 VecR = new Vec2(positionXR, positionYR);
        Vec2[] vecAbAB = new Vec2[]{VecL, VecR, VecL, VecR};

        Line left = new Line(Utils.toPixelPosX(positionXL) + 34, Utils.toPixelPosY(positionYL) - 37, Utils.toPixelPosX(positionXR) + 28, Utils.toPixelPosY(positionYR) - 32);
        left.setStroke(Color.BLACK);
        left.setStrokeWidth(3.0);

        createJboxLinePiece(vecAbAB, 4);

        return left;
    }

    private void createJboxLinePiece(Vec2[] vertices, int verticesSize) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position.set(vertices[0].x, vertices[0].y);

        PolygonShape line = new PolygonShape();
        line.set(vertices, verticesSize);

        FixtureDef fd = new FixtureDef();
        fd.shape = line;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;

        Body body = Utils.world.createBody(bd);
        body.createFixture(fd);
    }


}
