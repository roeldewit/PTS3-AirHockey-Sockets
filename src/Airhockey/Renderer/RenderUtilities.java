package Airhockey.Renderer;

import Airhockey.Elements.TriangleLine;

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
     * Lists to save X coordinates of the corresponding Y Coordinate on the triangle side angles. Input Y gives X.
     */
    private final int[] movingLineXLeft;
    private final int[] movingLineXRight;

    public RenderUtilities(TriangleLine triangleLine) {
        centerTopY = triangleLine.getCenterTopY();
        bottomY = triangleLine.getBottomLeftY();

        bottomLeftX = triangleLine.getBottomLeftX();
        bottomRightX = triangleLine.getBottomRightX();

        centerLineHeight = bottomY - centerTopY;

        System.out.println("CENTER_TOP_Y: " + centerTopY);
        System.out.println("CENTER LINE HEIGHT: " + centerLineHeight);
        baseLineWidth = bottomRightX - bottomLeftX;

        //Create MovinglineLists
        int triangleHeight = centerLineHeight;
        int triangleHalfWidth = baseLineWidth / 2;
        double calc1 = ((double) triangleHalfWidth / (double) centerLineHeight);

        movingLineXLeft = new int[centerLineHeight];
        movingLineXRight = new int[centerLineHeight];

        for (int i = 0; i < triangleHeight; i++) {
            double calc2 = calc1 * (double) i;
            movingLineXLeft[i] = (int) Math.floor(bottomLeftX + triangleHalfWidth - calc2);
            movingLineXRight[i] = (int) Math.floor(bottomLeftX + triangleHalfWidth + calc2);
        }
    }

    /**
     * Transfroms the given Y coordinate of a side-bat to an X coordinate of the center-bat
     *
     * @param positionY Y coordiate of a given side-bat
     * @return Y coordinate for the center-bat
     */
    protected int batPositionSideToBottom(int positionY) {
        System.out.println("POS: " + positionY);

        double heightPercentage = (100.0 / (double) centerLineHeight) * (double) (positionY - centerTopY + 60);
        double positionX = (((double) baseLineWidth / 100.0) * heightPercentage) + bottomLeftX - (Constants.BAT_RADIUS * 2);

        System.out.println("I: " + heightPercentage);
        System.out.println("L: " + positionX);
        return (int) Math.floor(positionX);
    }

    /**
     * Transfroms the given X coordinate of the center-bat to the X and Y coordinates of left side-bat
     *
     * @param bottomPositionX X coordinate of the center-bat
     * @return a new Position object with X and Y cooridnates
     */
    protected Position batPositionBottomToLeft(int bottomPositionX) {
        int positionY = xPositionBottomToYPositionSide(bottomPositionX - 40);
        int positionX = movingLineXLeft[(int) Math.floor(positionY)];

        return new Position(positionX + 95, (int) Math.floor(positionY));
    }

    /**
     * Transfroms the given X coordinate of the center-bat to the X and Y coordinates of right side-bat
     *
     * @param bottomPositionX X coordinate of the center-bat
     * @return a new Position object with X and Y cooridnates
     */
    protected Position batPositionBottomToRight(int bottomPositionX) {
        int positionY = xPositionBottomToYPositionSide(bottomPositionX - 40);
        int positionX = movingLineXRight[positionY];

        return new Position(positionX - 80, (int) Math.floor(positionY));
    }

    /**
     * Transfroms the given X coordinate of the center-bat to an Y coordinate of a side-bat
     *
     * @param positionX X coordiate of a given center-bat
     * @return Y coordinate for the side-bat
     */
    private int xPositionBottomToYPositionSide(int positionX) {
        double widthPercentage = (100.0 / (double) baseLineWidth) * (double) (positionX - bottomLeftX);
        double positionY = (((double) baseLineWidth / 100.0) * widthPercentage) + centerTopY;
        return (int) Math.floor(positionY);
    }
}
