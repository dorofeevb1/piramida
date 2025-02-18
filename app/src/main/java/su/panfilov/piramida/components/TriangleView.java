package su.panfilov.piramida.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Color;

public class TriangleView extends View {

    private Path path;
    private Paint paint = new Paint();
    private Paint layerPaint = new Paint(); // Кисть для слоев

    public int left;
    public int top;
    public int right;
    public int bottom;

    private int viewWidth = 0;
    private int viewHeight = 0;

    private int numberOfLayers = 8; // Количество слоев
    private float layerHeight; // Высота одного слоя

    private boolean isTriangle = true; // Flag to determine if the shape is a triangle or square

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public TriangleView(Context context, int width, int height) {
        super(context);

        viewWidth = width;
        viewHeight = height;
        layerHeight = viewHeight / numberOfLayers; // Высота одного слоя

        setup();
    }

    private void setup() {
        // Настройка кисти для треугольника
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setColor(PyramidColors.colors()[0]); // Используем цвет из PyramidColors

        // Настройка кисти для слоев
        layerPaint.setStyle(Paint.Style.FILL);
        layerPaint.setAntiAlias(true);
        layerPaint.setColor(Color.parseColor("#80000000")); // Полупрозрачный черный цвет
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (path == null) {
            path = new Path();
        }

        path.reset();

        if (isTriangle) {
            // Draw triangle
            path.moveTo(viewWidth / 2.0f, 0); // Верхняя точка
            path.lineTo(viewWidth, viewHeight); // Правая нижняя точка
            path.lineTo(0, viewHeight); // Левая нижняя точка
            path.close();
        } else {
            // Draw square
            path.moveTo(0, 0); // Верхняя левая точка
            path.lineTo(viewWidth, 0); // Верхняя правая точка
            path.lineTo(viewWidth, viewHeight); // Нижняя правая точка
            path.lineTo(0, viewHeight); // Нижняя левая точка
            path.close();
        }

        // Отрисовываем треугольник или квадрат
        canvas.drawPath(path, paint);

        // Отрисовываем слои
        for (int i = 0; i < numberOfLayers; i++) {
            float layerTop = i * layerHeight;
            float layerBottom = (i + 1) * layerHeight;

            // Создаем путь для слоя
            Path layerPath = new Path();
            if (isTriangle) {
                layerPath.moveTo(viewWidth / 2.0f - (viewWidth / 2.0f) * (layerTop / viewHeight), layerTop);
                layerPath.lineTo(viewWidth / 2.0f + (viewWidth / 2.0f) * (layerTop / viewHeight), layerTop);
                layerPath.lineTo(viewWidth / 2.0f + (viewWidth / 2.0f) * (layerBottom / viewHeight), layerBottom);
                layerPath.lineTo(viewWidth / 2.0f - (viewWidth / 2.0f) * (layerBottom / viewHeight), layerBottom);
            } else {
                layerPath.moveTo(0, layerTop);
                layerPath.lineTo(viewWidth, layerTop);
                layerPath.lineTo(viewWidth, layerBottom);
                layerPath.lineTo(0, layerBottom);
            }
            layerPath.close();

            // Отрисовываем слой
            canvas.drawPath(layerPath, layerPaint);
        }
    }

    public void setIsTriangle(boolean isTriangle) {
        this.isTriangle = isTriangle;
        invalidate(); // Redraw the view with the new shape
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        layerHeight = viewHeight / numberOfLayers;
        setMeasuredDimension(viewWidth, viewHeight);
    }
}
