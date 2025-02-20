package su.panfilov.piramida.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import su.panfilov.piramida.R;
import su.panfilov.piramida.components.PyramidColors;

public class SwipeViewBackground extends View {
    private static final String TAG = "SwipeViewBackground";

    private final Paint paint;
    private final Path path = new Path();

    private float deltaWidth;
    private int numberOfLayer;

    public int left;
    public int top;
    public int right;
    public int bottom;

    private int viewWidth = 0;
    private int viewHeight = 0;

    public SwipeViewBackground(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
        this.paint = createPaint();
    }

    public SwipeViewBackground(Context context, float deltaWidth, int layer, int width, int height) {
        super(context);
        this.deltaWidth = deltaWidth;
        this.numberOfLayer = layer;
        this.viewWidth = width;
        this.viewHeight = height;
        this.paint = createPaint();
    }

    private void initAttributes(Context context, @NonNull AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeViewBackground, 0, 0);
        try {
            deltaWidth = a.getFloat(R.styleable.SwipeViewBackground_bgDeltaWidth, 1);
            numberOfLayer = a.getInteger(R.styleable.SwipeViewBackground_bgNumberOfLayer, 1);
        } finally {
            a.recycle();
        }
    }

    private Paint createPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setupPath();
        drawBackground(canvas);
        drawBorder(canvas);
    }

    private void setupPath() {
        path.reset();
        path.moveTo(deltaWidth / 2.0f, 0);
        path.lineTo(viewWidth - deltaWidth / 2.0f, 0);
        path.lineTo(viewWidth, viewHeight);
        path.lineTo(0, viewHeight);
        path.close();
    }

    private void drawBackground(Canvas canvas) {
        paint.setColor(PyramidColors.colors()[numberOfLayer]);
        canvas.drawPath(path, paint);
    }

    private void drawBorder(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(PyramidColors.colors()[0]);
        canvas.drawPath(path, paint);
    }
}
