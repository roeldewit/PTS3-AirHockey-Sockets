package Airhockey.Elements;

import Airhockey.Utils.Utils;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Class that holds the data for the game's left side of the triangle.
 *
 * @author Sam
 */
public class TriangleLeftLine {

    //public Node node;
    private final float enginePositionXL;
    private final float enginePositionYL;
    private final float enginePositionXR;
    private final float enginePositionYR;

    public final float positionXL;
    public final float positionYL;
    public final float positionXR;
    public final float positionYR;

    /**
     * Constructor
     *
     * @param positionXL x-axis position of the line's left point.
     * @param positionYL y-axis position of the line's left point.
     * @param positionXR x-axis position of the line's right point.
     * @param positionYR y-axis position of the line's right point.
     */
    public TriangleLeftLine(float positionXL, float positionYL, float positionXR, float positionYR) {
        this.enginePositionXL = positionXL;
        this.enginePositionYL = positionYL;
        this.enginePositionXR = positionXR;
        this.enginePositionYR = positionYR;

        this.positionXL = Utils.toPixelPosX(enginePositionXL) + 22;
        this.positionYL = Utils.toPixelPosY(enginePositionYL) - 25;
        this.positionXR = Utils.toPixelPosX(enginePositionXR) + 16;
        this.positionYR = Utils.toPixelPosY(enginePositionYR) - 20;

        create();
    }

    /**
     * Creates the JBox2D engine collision node for this object.
     */
    private void create() {
        Vec2 VecL = new Vec2(enginePositionXL, enginePositionYL);
        Vec2 VecR = new Vec2(enginePositionXR, enginePositionYR);
        Vec2[] vecCombined = new Vec2[]{VecL, VecR, VecL, VecR};

        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position.set(vecCombined[0].x, vecCombined[0].y);

        PolygonShape line = new PolygonShape();
        line.set(vecCombined, 4);

        FixtureDef fd = new FixtureDef();
        fd.shape = line;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;

        Body body = Utils.world.createBody(bd);
        body.createFixture(fd);
    }
}
