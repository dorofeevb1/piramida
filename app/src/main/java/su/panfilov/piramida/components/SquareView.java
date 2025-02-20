package su.panfilov.piramida.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SquareView extends View {
    private static final int DEFAULT_COLOR = Color.BLUE;
    private final Paint paint;
    private int width;
    private int height;

    public SquareView(Context context, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
        this.paint = createPaint(DEFAULT_COLOR);
    }

    public SquareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.paint = createPaint(DEFAULT_COLOR);
        initAttributes(attrs);
    }

    public SquareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.paint = createPaint(DEFAULT_COLOR);
        initAttributes(attrs);
    }

    private void initAttributes(@NonNull AttributeSet attrs) {
        // Initialize attributes from XML if needed
    }

    private Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    public void setSquareColor(int color) {
        paint.setColor(color);
        invalidate(); // Redraw the view with the new color
    }

    public int getSquareColor() {
        return paint.getColor();
    }
}
