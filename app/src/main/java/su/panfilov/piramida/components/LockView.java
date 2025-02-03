package su.panfilov.piramida.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import su.panfilov.piramida.R;

public class LockView extends View {
    private Path path;
    private Paint paint = new Paint();

    public int left;
    public int top;
    public int right;
    public int bottom;

    private int viewWidth = 0;
    private int viewHeight = 0;

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public LockView(Context context, int width, int height) {
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

            float gipotenusa = (float)Math.sqrt(viewHeight * viewHeight + viewWidth * viewWidth / 4) / (viewWidth / 4);
            float offset = (gipotenusa - 1);

            float centerXOfPiramida = viewWidth / 2;
            float centerYOfPiramida = viewHeight / 2 - offset;

            float scaleLockLayer = 1;
            float point1X = centerXOfPiramida;
            float point1Y = centerYOfPiramida - viewHeight / 2 * scaleLockLayer + 6;
            float point2X = centerXOfPiramida - viewWidth / 2 * scaleLockLayer + 3;
            float point2Y = centerYOfPiramida + viewHeight / 2 * scaleLockLayer;
            float point3X = centerXOfPiramida + viewWidth / 2 * scaleLockLayer - 3;
            float point3Y = centerYOfPiramida + viewHeight / 2 * scaleLockLayer;

            path = new Path();
            path.reset();
            path.moveTo(point1X, point1Y);
            path.lineTo(point2X, point2Y);
            path.lineTo(point3X, point3Y);
            path.close();
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6);
        paint.setColor(getResources().getColor(R.color.colorTabActive));
        canvas.drawPath(path, paint);
    }
}
