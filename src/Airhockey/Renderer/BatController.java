/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Renderer;

import org.jbox2d.common.Vec2;

/**
 *
 * @author Sam
 */
public class BatController {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;

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

    private final float speed = 11.0f;
    private final float leftSpeedManipulation;
    private final float rightSpeedManipulation;

    public BatController(Renderer renderer) {
        this.renderer = renderer;

        leftSpeedManipulation = speed * 0.50f;
        rightSpeedManipulation = speed * 0.55f;
    }

    /**
     * CenterBat
     *
     * @param batPositionX Xpostion of the center bat
     */
    protected void controlCenterBat(float batPositionX) {
        if (batPositionX > 380) {
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
        if (batPositionX < 640) {
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

    public void startBatMovement(int direction) {
        if (direction == LEFT) {
            batSideMovementLeft = true;
            batSideMovementRight = false;
        } else if (direction == RIGHT) {
            batSideMovementRight = true;
            batSideMovementLeft = false;
        }
    }

    public void stopBatMovement() {
        batSideMovementLeft = false;
        batSideMovementRight = false;
        renderer.batBody.setLinearVelocity(new Vec2(0.0f, 0.0f));
    }

    /**
     * Left Bat
     *
     * @param leftBatPositionY
     */
    protected void controlLeftBat(float leftBatPositionY) {
        if (leftBatPositionY > Constants.BAT_MAX_Y) {
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
        if (leftBatPositionY < Constants.BAT_MIN_Y) {
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

    public void startLeftBatMovement(int direction) {
        if (direction == UP) {
            leftBatMovementUp = true;
            leftBatMovementDown = false;
        } else if (direction == DOWN) {
            leftBatMovementUp = false;
            leftBatMovementDown = true;
        }
    }

    public void stopLeftBatMovement() {
        leftBatMovementUp = false;
        leftBatMovementDown = false;
    }

    /**
     * Right Bat
     *
     * @param rightBatPositionY
     */
    protected void controlRightBat(float rightBatPositionY) {
        if (rightBatPositionY > Constants.BAT_MAX_Y) {
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
        if (rightBatPositionY < Constants.BAT_MIN_Y) {
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

    public void startRightBatMovement(int direction) {
        if (direction == UP) {
            rightBatMovementUp = true;
            rightBatMovementDown = false;
        } else if (direction == DOWN) {
            rightBatMovementUp = false;
            rightBatMovementDown = true;
        }
    }

    public void stopRightBatMovement() {
        rightBatMovementUp = false;
        rightBatMovementDown = false;
    }
}
