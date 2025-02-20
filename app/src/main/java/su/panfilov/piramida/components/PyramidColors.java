package su.panfilov.piramida.components;

import android.graphics.Color;

public class PyramidColors {

    // Predefined color values as constants
    private static final int WHITE = Color.rgb(255, 255, 255);
    private static final int RED = Color.rgb(229, 68, 86);
    private static final int ORANGE = Color.rgb(247, 109, 80);
    private static final int YELLOW = Color.rgb(253, 204, 111);
    private static final int GREEN = Color.rgb(154, 208, 94);
    private static final int BLUE = Color.rgb(74, 194, 231);
    private static final int LIGHT_BLUE = Color.rgb(95, 161, 237);
    private static final int PURPLE = Color.rgb(153, 123, 219);
    private static final int ICON_COLOR = Color.rgb(153, 122, 164);
    private static final int TEXT_COLOR = Color.rgb(108, 108, 108);
    private static final int TEXT_HEADER_COLOR = Color.rgb(90, 106, 122);

    /**
     * Returns an array of predefined colors used in the pyramid views.
     *
     * @return An array of integers representing colors.
     */
    public static int[] colors() {
        return new int[]{
                WHITE, RED, ORANGE, YELLOW, GREEN, BLUE, LIGHT_BLUE, PURPLE
        };
    }

    /**
     * Returns the color used for icons.
     *
     * @return An integer representing the icon color.
     */
    public static int iconColor() {
        return ICON_COLOR;
    }

    /**
     * Returns the color used for text.
     *
     * @return An integer representing the text color.
     */
    public static int textColor() {
        return TEXT_COLOR;
    }

    /**
     * Returns the color used for text headers.
     *
     * @return An integer representing the text header color.
     */
    public static int textHeaderColor() {
        return TEXT_HEADER_COLOR;
    }
}
