package Airhockey.Elements;

import Airhockey.Utils.Utils;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Class that holds the data for the game's center and right side of the triangle.
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

    /**
     * Constructor
     *
     * @param positionXL x-axis position of the line's left point.
     * @param positionYL y-axis position of the line's left point.
     * @param positionXR x-axis position of the line's right point.
     * @param positionYR y-axis position of the line's right point.
     * @param positionXC x-axis position of the line's center point.
     * @param positionYC y-axis position of the line's center point.
     */
    public TriangleLine(float positionXL, float positionYL, float positionXR, float positionYR, float positionXC, float positionYC) {
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

    /**
     * Creates the JBox2D engine collision node for this object.
     */
    private void createLinePieceAB() {
        Vec2 VecL = new Vec2(enginePositionXL, enginePositionYL);
        Vec2 VecR = new Vec2(enginePositionXR, enginePositionYR);
        Vec2 VecC = new Vec2(enginePositionXC, enginePositionYC);
        Vec2[] vecCombined = new Vec2[]{VecL, VecR, VecC};

        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position.set(vecCombined[0].x, vecCombined[0].y);

        ChainShape s = new ChainShape();
        s.createChain(vecCombined, 3);

        FixtureDef fd = new FixtureDef();
        fd.shape = s;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;

        Body body = Utils.world.createBody(bd);
        body.createFixture(fd);
    }
}
