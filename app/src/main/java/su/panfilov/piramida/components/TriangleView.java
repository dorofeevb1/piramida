package su.panfilov.piramida.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class TriangleView extends View {

    private Path path;
    private Paint paint = new Paint();

    public int left;
    public int top;
    public int right;
    public int bottom;

    private int viewWidth = 0;
    private int viewHeight = 0;

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public TriangleView(Context context, int width, int height) {
        super(context);

        viewWidth = width;
        viewHeight = height;

        setup();
    }

    private void setup() {
//        setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
//        setBackgroundColor(getResources().getColor(R.color.colorAccent));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (path == null) {
//            viewWidth = getMeasuredWidth();
//            viewHeight = getMeasuredHeight();
//
//            Log.d("1111","onDraw with viewWidth= " + viewWidth + " and viewHeight= " + viewHeight + " called...");

            path = new Path();
            path.reset();
            path.moveTo(viewWidth / 2.0f, 0);
            path.lineTo(viewWidth, viewHeight);
            path.lineTo(0, viewHeight);
            path.close();
        }

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setColor(PyramidColors.colors()[0]);
        canvas.drawPath(path, paint);
    }
}
