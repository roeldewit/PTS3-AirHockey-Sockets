package Airhockey.Elements;

import Airhockey.Utils.Utils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author Sam
 */
public class TriangleLine {

    private final float positionXL;
    private final float positionYL;
    private final float positionXR;
    private final float positionYR;
    private final float positionXC;
    private final float positionYC;

    public Node node;

    public TriangleLine(int screenHeight, float positionXL, float positionYL, float positionXR, float positionYR, float positionXC, float positionYC) {
        this.positionXL = positionXL;
        this.positionYL = positionYL;
        this.positionXR = positionXR;
        this.positionYR = positionYR;
        this.positionXC = positionXC;
        this.positionYC = positionYC;

        node = createLinePieceAB();
    }

    private Node createLinePieceAB() {
        Vec2 VecL = new Vec2(positionXL, positionYL);
        Vec2 VecR = new Vec2(positionXR, positionYR);
        Vec2 VecC = new Vec2(positionXC, positionYC);
        Vec2[] vecAbAB = new Vec2[]{VecL, VecR, VecC};

        Line bottom = new Line(Utils.toPixelPosX(positionXL) + 35, Utils.toPixelPosY(positionYL) - 36, Utils.toPixelPosX(positionXR) + 21, Utils.toPixelPosY(positionYR) - 36);
        bottom.setStroke(Color.BLACK);
        bottom.setStrokeWidth(3.0);

        Line right = new Line(Utils.toPixelPosX(positionXC) + 28, Utils.toPixelPosY(positionYC) - 32, Utils.toPixelPosX(positionXR) + 22, Utils.toPixelPosY(positionYR) - 37);
        right.setStroke(Color.BLACK);
        right.setStrokeWidth(3.0);

        Group lineGroup = new Group();
        lineGroup.getChildren().addAll(bottom, right);

        createJboxLinePiece(vecAbAB, 3);

        return lineGroup;
    }

    private void createJboxLinePiece(Vec2[] vertices, int verticesSize) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position.set(vertices[0].x, vertices[0].y);

        ChainShape s = new ChainShape();
        s.createChain(vertices, verticesSize);

        FixtureDef fd = new FixtureDef();
        fd.shape = s;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;

        Body body = Utils.world.createBody(bd);
        body.createFixture(fd);
    }

    public int getCenterTopY() {
        return (int) Utils.toPixelPosY(positionYC) - 32;
    }

    public int getBottomLeftY() {
        return (int) Utils.toPixelPosY(positionYL) - 36;
    }

    public int getBottomLeftX() {
        return (int) Utils.toPixelPosX(positionXL) + 35;
    }

    public int getBottomRightX() {
        return (int) Utils.toPixelPosX(positionXR) + 22;
    }
}
