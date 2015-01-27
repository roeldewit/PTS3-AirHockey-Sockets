package Airhockey.Renderer;

import org.jbox2d.common.Vec2;

/**
 * Class used to move all the bats that are controlled by a player.
 *
 * @author Sam
 */
public class BatController {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;
    public static final int STOP = 5;

    private final Renderer renderer;

    private boolean batSideMovementLeft = false;
    private boolean batSideMovementRight = false;
    private boolean canStopBatLeft;
    private boolean canStopBatRight;

    private boolean leftBatMovementUp = false;
    private boolean leftBatMovementDown = false;
    private boolean canStopLeftBatTop;
    private boolean canStopLeftBatBottom;

    private boolean rightBatMovementUp = false;
    private boolean rightBatMovementDown = false;
    private boolean canStopRightBatTop;
    private boolean canStopRightBatBottom;

    private final float speed = 30.0f;
    private final float leftSpeedManipulation;
    private final float rightSpeedManipulation;

    public BatController(Renderer renderer) {
        this.renderer = renderer;

        leftSpeedManipulation = speed * 0.55f;
        rightSpeedManipulation = speed * 0.60f;
    }

    /**
     * Deterimines if the center bat is allowed to move in a certain direction.
     * If true moves the center bat in that direction.
     *
     * @param batPositionX x-axis postion of the center bat
     */
    protected void controlCenterBat(float batPositionX) {
        if (batPositionX > Constants.CENTER_BAT_MIN_X) {
            canStopBatLeft = true;
            if (batSideMovementLeft) {
                renderer.batBody.setLinearVelocity(new Vec2(-25.0f, 0.0f));
                batSideMovementLeft = false;
                batSideMovementRight = false;
            }
        } else {
            if (canStopBatLeft) {
                stopBatMovement();
                canStopBatLeft = false;
            }
        }
        if (batPositionX < Constants.CENTER_BAT_MAX_X) {
            canStopBatRight = true;
            if (batSideMovementRight) {
                renderer.batBody.setLinearVelocity(new Vec2(25.0f, 0.0f));
                batSideMovementRight = false;
                batSideMovementLeft = false;
            }
        } else {
            if (canStopBatRight) {
                stopBatMovement();
                canStopBatRight = false;
            }
        }
    }

    /**
     * Allows the center bat to move in the given direction.
     *
     * @param direction The requested direction.
     */
    public void startBatMovement(int direction) {
        if (direction == LEFT) {
            batSideMovementLeft = true;
            batSideMovementRight = false;
        } else if (direction == RIGHT) {
            batSideMovementRight = true;
            batSideMovementLeft = false;
        }
    }

    /**
     * Stops the movement of the center bat and disallows it to move any further.
     */
    public void stopBatMovement() {
        batSideMovementLeft = false;
        batSideMovementRight = false;
        renderer.batBody.setLinearVelocity(new Vec2(0.0f, 0.0f));
    }

    /**
     * Deterimines if the left bat is allowed to move in a certain direction.
     * If true moves the left bat in that direction.
     *
     *
     * @param leftBatPositionY y-axis position of the left bat.
     */
    protected void controlLeftBat(float leftBatPositionY) {
        if (leftBatPositionY > Constants.SIDE_BAT_MAX_Y) {
            canStopLeftBatTop = true;
            if (leftBatMovementUp) {
                renderer.leftBatBody.setLinearVelocity(new Vec2(speed - leftSpeedManipulation, speed));
                leftBatMovementUp = false;
                leftBatMovementDown = false;
            }
        } else {
            if (canStopLeftBatTop) {
                stopLeftBatMovement();
                canStopLeftBatTop = false;
            }
        }
        if (leftBatPositionY < Constants.SIDE_BAT_MIN_Y) {
            canStopLeftBatBottom = true;
            if (leftBatMovementDown) {
                renderer.leftBatBody.setLinearVelocity(new Vec2(-speed + leftSpeedManipulation, -speed));
                leftBatMovementUp = false;
                leftBatMovementDown = false;
            }
        } else {
            if (canStopLeftBatBottom) {
                stopLeftBatMovement();
                canStopLeftBatBottom = false;
            }
        }
    }

    /**
     * Allows the left bat to move in the given direction.
     *
     * @param direction The requested direction.
     */
    public void startLeftBatMovement(int direction) {
        if (direction == UP) {
            leftBatMovementUp = true;
            leftBatMovementDown = false;
        } else if (direction == DOWN) {
            leftBatMovementUp = false;
            leftBatMovementDown = true;
        }
    }

    /**
     * Stops the movement of the right bat and disallows it to move any further.
     */
    public void stopLeftBatMovement() {
        leftBatMovementUp = false;
        leftBatMovementDown = false;
        renderer.leftBatBody.setLinearVelocity(new Vec2(0.0f, 0.0f));
    }

    /**
     * Deterimines if the rigth bat is allowed to move in a certain direction.
     * If true moves the right bat in that direction.
     *
     *
     * @param rightBatPositionY y-axis position of the right bat.
     */
    protected void controlRightBat(float rightBatPositionY) {
        if (rightBatPositionY > Constants.SIDE_BAT_MAX_Y) {
            canStopRightBatTop = true;
            if (rightBatMovementUp) {
                renderer.rightBatBody.setLinearVelocity(new Vec2(-speed + rightSpeedManipulation, speed));
                rightBatMovementUp = false;
                rightBatMovementDown = false;
            }
        } else {
            if (canStopRightBatTop) {
                stopRightBatMovement();
                canStopRightBatTop = false;
            }
        }
        if (rightBatPositionY < Constants.SIDE_BAT_MIN_Y) {
            canStopRightBatBottom = true;
            if (rightBatMovementDown) {
                renderer.rightBatBody.setLinearVelocity(new Vec2(speed - rightSpeedManipulation, -speed));
                rightBatMovementUp = false;
                rightBatMovementDown = false;
            }
        } else {
            if (canStopRightBatBottom) {
                stopRightBatMovement();
                canStopRightBatBottom = false;
            }
        }
    }

    /**
     * Allows the rigth bat to move in the given direction.
     *
     * @param direction The requested direction.
     */
    public void startRightBatMovement(int direction) {
        if (direction == UP) {
            rightBatMovementUp = true;
            rightBatMovementDown = false;
        } else if (direction == DOWN) {
            rightBatMovementUp = false;
            rightBatMovementDown = true;
        }
    }

    /**
     * Stops the movement of the rigth bat and disallows it to move any further.
     */
    public void stopRightBatMovement() {
        rightBatMovementUp = false;
        rightBatMovementDown = false;
        renderer.rightBatBody.setLinearVelocity(new Vec2(0.0f, 0.0f));
    }
}
