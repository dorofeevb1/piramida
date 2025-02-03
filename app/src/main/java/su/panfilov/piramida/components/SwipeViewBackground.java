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
import android.widget.RelativeLayout;
import android.widget.TextView;

import su.panfilov.piramida.R;

public class SwipeViewBackground extends View {

    private Context context;
    private Paint paint;

    private float deltaWidth;
    private int numberOfLayer;

    private Path path = new Path();

    public int left;
    public int top;
    public int right;
    public int bottom;

    private int viewWidth = 0;
    private int viewHeight = 0;

    public SwipeViewBackground(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SwipeViewBackground, 0, 0);
        try {
            deltaWidth = a.getFloat(R.styleable.SwipeViewBackground_bgDeltaWidth, 1);
            numberOfLayer = a.getInteger(R.styleable.SwipeViewBackground_bgNumberOfLayer, 1);
        } finally {
            a.recycle();
        }
    }

    public SwipeViewBackground(Context context, float deltaWidth, int layer, int width, int height) {
        super(context);

        this.context = context;

        this.deltaWidth = deltaWidth;
        this.numberOfLayer = layer;
        viewWidth = width;
        viewHeight = height;
    }

    private void setup() {

        path.reset();
        path.moveTo(deltaWidth / 2.0f, 0);
        path.lineTo(viewWidth - deltaWidth / 2.0f, 0);
        path.lineTo(viewWidth, viewHeight);
        path.lineTo(0, viewHeight);
        path.close();

        paint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        setup();

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(PyramidColors.colors()[numberOfLayer]);
        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(PyramidColors.colors()[0]);
        canvas.drawPath(path, paint);
    }
}
