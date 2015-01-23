package Airhockey.Elements;

import Airhockey.Utils.Utils;
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

    private final float enginePositionXL;
    private final float enginePositionYL;
    private final float enginePositionXR;
    private final float enginePositionYR;
    private final float enginePositionXC;
    private final float enginePositionYC;

    public final float positionXL;
    public final float positionYL;
    public final float positionXR;
    public final float positionYR;
    public final float positionXC;
    public final float positionYC;

    public TriangleLine(int screenHeight, float positionXL, float positionYL, float positionXR, float positionYR, float positionXC, float positionYC) {
        this.enginePositionXL = positionXL;
        this.enginePositionYL = positionYL;
        this.enginePositionXR = positionXR;
        this.enginePositionYR = positionYR;
        this.enginePositionXC = positionXC;
        this.enginePositionYC = positionYC;

        this.positionXL = Utils.toPixelPosX(enginePositionXL) + 25;
        this.positionYL = Utils.toPixelPosY(enginePositionYL) - 24;
        this.positionXR = Utils.toPixelPosX(enginePositionXR) + 23;
        this.positionYR = Utils.toPixelPosY(enginePositionYR) - 24;
        this.positionXC = Utils.toPixelPosX(enginePositionXC) + 17;
        this.positionYC = Utils.toPixelPosY(enginePositionYC) - 20;

        createLinePieceAB();
    }

    private void createLinePieceAB() {
        Vec2 VecL = new Vec2(enginePositionXL, enginePositionYL);
        Vec2 VecR = new Vec2(enginePositionXR, enginePositionYR);
        Vec2 VecC = new Vec2(enginePositionXC, enginePositionYC);
        Vec2[] vecAbAB = new Vec2[]{VecL, VecR, VecC};

        createJboxLinePiece(vecAbAB, 3);
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
        return (int) Utils.toPixelPosY(enginePositionYC) - 32;
    }

    public int getBottomLeftY() {
        return (int) Utils.toPixelPosY(enginePositionYL) - 36;
    }

    public int getBottomLeftX() {
        return (int) Utils.toPixelPosX(enginePositionXL) + 35;
    }

    public int getBottomRightX() {
        return (int) Utils.toPixelPosX(enginePositionXR) + 22;
    }
}
