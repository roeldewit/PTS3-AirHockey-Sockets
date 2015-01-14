package Airhockey.Renderer;

import Airhockey.Elements.TriangleLine;
import javafx.scene.canvas.GraphicsContext;

/**
 * Used for transforming side and center bat cooridinates
 *
 * @author Sam
 */
class RenderUtilities {

    protected static final int TRIANGLE_HEIGHT = 693;

    private final int bottomLeftX;
    private final int bottomRightX;

    private final int centerTopY;
    private final int bottomY;

    private final int centerLineHeight; //height of triangle
    private final int baseLineWidth;    //width of triangle

    /**
     * Lists to save X coordinates of the corresponding Y Coordinate on the
     * triangle side angles. Input Y gives X.
     */
    private final int[] batMovingLineXLeft;
    private final int[] batMovingLineXRight;

    private final int[] puckMovingLineTiltedRight;
    //private final int[] puckMovingLineTiltedLeft;

    public RenderUtilities(TriangleLine triangleLine) {
        centerTopY = triangleLine.getCenterTopY();
        bottomY = triangleLine.getBottomLeftY();

        bottomLeftX = triangleLine.getBottomLeftX();
        bottomRightX = triangleLine.getBottomRightX();

        centerLineHeight = bottomY - centerTopY;

        System.out.println("CENTER_TOP_Y: " + centerTopY);
        System.out.println("CENTER LINE HEIGHT: " + centerLineHeight);
        System.out.println("CENTER_BOTTOM_Y: " + bottomY);
        System.out.println("Bottom Right X: " + bottomRightX);
        System.out.println("Bottom Left X: " + bottomLeftX);
        baseLineWidth = bottomRightX - bottomLeftX;

        //Create MovinglineLists
        int triangleHeight = centerLineHeight;
        int triangleHalfWidth = baseLineWidth / 2;
        double calc1 = ((double) triangleHalfWidth / (double) centerLineHeight);

        batMovingLineXLeft = new int[centerLineHeight];
        batMovingLineXRight = new int[centerLineHeight];

        for (int i = 0; i < triangleHeight; i++) {
            double calc2 = calc1 * (double) i;
            batMovingLineXLeft[i] = (int) Math.floor(bottomLeftX + triangleHalfWidth - calc2);
            batMovingLineXRight[i] = (int) Math.floor(bottomLeftX + triangleHalfWidth + calc2);
        }

        int triangleHalfHeight = triangleHeight / 2;
        double calc3 = ((double) triangleHalfHeight + 100.0) / (double) baseLineWidth;

        puckMovingLineTiltedRight = new int[centerLineHeight];

        for (int i = 0; i < centerLineHeight; i++) {
            double calc4 = calc3 * (double) i;
            puckMovingLineTiltedRight[i] = (int) Math.floor(bottomY - calc4);
            System.out.println("X: " + i + "Y: " + puckMovingLineTiltedRight[i]);
        }
    }

    public void drawLine(GraphicsContext gc) {
        //Position p = serverPuckToBlueClientPuck(6);
        //Position p2 = serverPuckToBlueClientPuck(centerLineHeight + 6);

        //gc.strokeLine(p.x, p.y, p2.x, p2.y);
    }

    private int translate(int y) {
        int swagg = (y - centerTopY);
        System.out.println("SWAGG: " + swagg);

        int value = (y - centerTopY);
        if (swagg != 0) {
            value -= 1;
        }

        System.out.println("Value: " + value);
        return value;
    }

//    public Position serverPuckToBlueClientPuck(int puckX, int puckY) {
//        int tempY = translate(puckY);
//
//        int x = bottomRightX - tempY;
//        int y = puckMovingLineTiltedRight[tempY];
//
//        Position position = new Position(x, y);
//        return position;
//    }
    public Position serverPuckToBlueClientPuck(int puckX, int puckY) {
        int tempX = (puckY - centerTopY);
        //int x = (int) Math.floor((double) (puckY - centerTopY) * 0.80);

        //ConversionTriangle
        double convTrWidth = tempX / 2;
        double convTrHeight = Math.tan(Math.toRadians(58.0)) * convTrWidth;
        double convTrSideLength = convTrWidth / Math.cos(Math.toRadians(58.0));

        int diff = ((baseLineWidth / 2) + bottomLeftX) - puckX;

        int test = (int) (Math.floor(convTrSideLength / 2)) + diff;

        int width = (int) Math.floor(((double) convTrWidth / convTrSideLength) * (double) test);
        int height = (int) Math.floor(((double) convTrHeight / convTrSideLength) * (double) test);

        int aWidth = tempX - (int) (Math.floor(convTrWidth - (double) width));

        int x = bottomRightX - aWidth;
        int y = bottomY - height;

        return new Position(x, y);
    }

    /**
     * Transfroms the given Y coordinate of a side-bat to an X coordinate of the
     * center-bat
     *
     * @param positionY Y coordiate of a given side-bat
     * @return Y coordinate for the center-bat
     */
    protected int batPositionSideToBottom(int positionY) {
        System.out.println("POS: " + positionY);

        double heightPercentage = (100.0 / (double) centerLineHeight) * (double) (positionY - centerTopY + 60);
        double positionX = (((double) baseLineWidth / 100.0) * heightPercentage) + bottomLeftX - (Constants.BAT_RADIUS * 2);

//        System.out.println("I: " + heightPercentage);
//        System.out.println("L: " + positionX);
        return (int) Math.floor(positionX);
    }

    /**
     * Transfroms the given X coordinate of the center-bat to the X and Y
     * coordinates of left side-bat
     *
     * @param bottomPositionX X coordinate of the center-bat
     * @return a new Position object with X and Y cooridnates
     */
    protected Position batPositionBottomToLeft(int bottomPositionX) {
        int positionY = xPositionBottomToYPositionSide(bottomPositionX - (int) Constants.BAT_RADIUS);
        int positionX = batMovingLineXLeft[(int) Math.floor(positionY)];

        return new Position(positionX + (int) (Constants.BAT_RADIUS * 1.5), (int) Math.floor(positionY));
    }

    /**
     * Transfroms the given X coordinate of the center-bat to the X and Y
     * coordinates of right side-bat
     *
     * @param bottomPositionX X coordinate of the center-bat
     * @return a new Position object with X and Y cooridnates
     */
    protected Position batPositionBottomToRight(int bottomPositionX) {
        int positionY = centerLineHeight - xPositionBottomToYPositionSide(bottomPositionX - (int) Constants.BAT_RADIUS) + (int) (Constants.BAT_RADIUS * 2);
        int positionX = batMovingLineXRight[positionY];

        return new Position(positionX - (int) Constants.BAT_RADIUS, (int) Math.floor(positionY));
    }

    /**
     * Transfroms the given X coordinate of the center-bat to an Y coordinate of
     * a side-bat
     *
     * @param positionX X coordiate of a given center-bat
     * @return Y coordinate for the side-bat
     */
    private int xPositionBottomToYPositionSide(int positionX) {
        double widthPercentage = (100.0 / (double) baseLineWidth) * (double) (positionX - bottomLeftX);
        double positionY = (((double) baseLineWidth / 100.0) * (widthPercentage * 0.9)) + centerTopY;
        return (int) Math.floor(positionY);
    }
}
