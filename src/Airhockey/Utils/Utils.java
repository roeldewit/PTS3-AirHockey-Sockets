package Airhockey.Utils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 *
 * @author Sam
 */
public class Utils {

    public static final World world = new World(new Vec2(0.0f, 0.0f));

    //Screen width and height
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;

    /**
     * Convert a JBox2D x coordinate to a JavaFX pixel x coordinate.
     *
     * @param posX
     * @return
     */
    public static float toPixelPosX(float posX) {
        float x = WIDTH * posX / 100.0f;
        return x;
    }

    /**
     * Convert a JavaFX pixel x coordinate to a JBox2D x coordinate
     *
     * @param posX
     * @return
     */
    public static float toPosX(float posX) {
        float x = (posX * 100.0f * 1.0f) / WIDTH;
        return x;
    }

    /**
     * Convert a JBox2D y coordinate to a JavaFX pixel y coordinate
     *
     * @param posY
     * @return
     */
    public static float toPixelPosY(float posY) {
        float y = HEIGHT - (1.0f * HEIGHT) * posY / 100.0f;
        return y;
    }

    /**
     * Convert a JavaFX pixel y coordinate to a JBox2D y coordinate.
     *
     * @param posY
     * @return
     */
    public static float toPosY(float posY) {
        float y = 100.0f - ((posY * 100 * 1.0f) / HEIGHT);
        return y;
    }

    /**
     * Convert a JBox2D width to pixel width
     *
     * @param width Width in JBox2D dimentions
     * @return Widht in pixel dimentions
     */
    public static float toPixelWidth(float width) {
        return WIDTH * width / 100.0f;
    }

    /**
     * Convert a JBox2D height pixel width
     *
     * @param height Height in JBox2D dimentions
     * @return height in pixel dimentions
     */
    public static float toPixelHeight(float height) {
        return HEIGHT * height / 100.0f;
    }
}
