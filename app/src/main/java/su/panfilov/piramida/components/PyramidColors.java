package su.panfilov.piramida.components;

import android.graphics.Color;

public class PyramidColors {

    public static int[] colors() {
        int[] colors = {
            Color.rgb(255, 255, 255),
            Color.rgb(229, 68, 86),
            Color.rgb(247, 109, 80),
            Color.rgb(253, 204, 111),
            Color.rgb(154, 208, 94),
            Color.rgb(74, 194, 231),
            Color.rgb(95, 161, 237),
            Color.rgb(153, 123, 219)
        };

        return colors;
    }

    public static int iconColor() {
        return Color.rgb(153, 122, 164);
    }

    public static int textColor() {
        return Color.rgb(108, 108, 108);
    }

    public static int textHeaderColor() {
        return Color.rgb(90, 106, 122);
    }

}
