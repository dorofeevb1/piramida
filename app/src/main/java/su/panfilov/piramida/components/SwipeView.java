package su.panfilov.piramida.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import androidx.annotation.Dimension;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import su.panfilov.piramida.R;

import static android.graphics.Typeface.DEFAULT_BOLD;

public class SwipeView extends RelativeLayout {
    private static final String TAG = "SwipeView";

    final MediaPlayer mp;

    private Context context;
    private Paint paint;

    private float deltaWidth;
    private int numberOfLayer;

    private SwipeViewBackground backgroundLayer;
    public TextView label;
    private ImageView polygon;

    public SwipeViewDelegate delegate;

    public int tag;

    public int width;
    public int height;
    public int left;
    public int top;
    public int right;
    public int bottom;

    public boolean userInteractive = true;

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SwipeView, 0, 0);
        try {
            deltaWidth = a.getFloat(R.styleable.SwipeView_deltaWidth, 1);
            numberOfLayer = a.getInteger(R.styleable.SwipeView_layer, 1);
        } finally {
            a.recycle();
        }

        mp = MediaPlayer.create(context, R.raw.swipe_short);

        setup();
    }

    public SwipeView(Context context, float deltaWidth, int layer, int width, int height) {
        super(context);

        this.context = context;

        this.deltaWidth = deltaWidth;
        this.numberOfLayer = layer;
        this.width = width;
        this.height = height;

        mp = MediaPlayer.create(context, R.raw.swipe_short);

        setup();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        backgroundLayer.layout(0, 0, width, height);
        if (numberOfLayer > 0) {
            polygon.layout(Math.round(width / 2.0f - height / 4.0f), Math.round(height / 4.0f),
                    Math.round(width / 2.0f + height / 4.0f), Math.round(height / 4.0f * 3));
        }
        label.measure(0, 0);
        int labelWidth = label.getMeasuredWidth();
        int labelHeight = label.getMeasuredHeight();
        if (labelWidth > width - deltaWidth / 1.5) {
            String text = label.getText().toString();
            text = text.substring(0, text.length() / 2) + "\n" + text.substring(text.length() / 2);
            label.setText(text);
            label.setTextSize(Dimension.SP, height / 8);
            label.measure(0, 0);
            labelWidth = label.getMeasuredWidth();
            labelHeight = label.getMeasuredHeight();
        }
        label.layout((width - labelWidth) / 2, (height - labelHeight) / 2, (width + labelWidth) / 2, (height + labelHeight) / 2);
    }

    private void setup() {
        backgroundLayer = new SwipeViewBackground(context, deltaWidth, numberOfLayer, width, height);
        addView(backgroundLayer);

        if (numberOfLayer > 0) {
            polygon = new ImageView(context);
            int polygonIcon = getResources()
                    .getIdentifier("ic_polygon_" + String.valueOf(numberOfLayer),
                            "drawable", context.getPackageName());
            polygon.setImageDrawable(getResources().getDrawable(polygonIcon));
            addView(polygon);
        }

        label = new TextView(context);
        label.setTextColor(numberOfLayer == 0 ? PyramidColors.textColor() : Color.WHITE);
        label.setText("");
        label.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        label.setTextSize(Dimension.SP, Math.round(height / 6.0));
        label.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        Typeface fontTypeFace = Typeface.create(DEFAULT_BOLD, Typeface.BOLD);
        label.setTypeface(fontTypeFace);
        addView(label);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!userInteractive) {
                    return true;
                }

                didLongTap();

                return true;
            }
        });

        setOnTouchListener(new OnSwipeTouchListener(context) {

            public void onSwipeRight() {
                if (!userInteractive) {
                    return;
                }

                didRightSwipe();
            }

            public void onSwipeLeft() {
                if (!userInteractive) {
                    return;
                }

                didLeftSwipe();
            }

        });
    }

    public void setText(String text) {
        if (polygon != null) {
            polygon.setVisibility(text.equals("") ? VISIBLE : INVISIBLE);
        }
        label.setText(text);
        label.setTextSize(Dimension.SP, Math.round(height / 6.0));
    }

    public void update() {
        if (delegate == null) {
            return;
        }

        label.setText(delegate.getTitle(tag - 1000));
    }

    public void didLeftSwipe() {
        if (delegate == null) {
            return;
        }

        String title = delegate.leftTurn(tag - 1000);
        playSwipe();
        turnLeft(title);
    }

    public void didRightSwipe() {
        if (delegate == null) {
            return;
        }

        String title = delegate.rightTurn(tag - 1000);
        playSwipe();
        turnRight(title);
    }

    public void didLongTap() {
        if (delegate == null) {
            return;
        }

        delegate.setOneSide(tag - 1000);
    }

    public void playSwipe() {
        mp.start();
    }

    public void turnLeft(String title) {
        cubeTransition(title, false);
    }

    public void turnRight(String title) {
        cubeTransition(title, true);
    }

    private void cubeTransition(final String text, final boolean toRight) {
        float realWidth = width;
        if (label.getText().equals("")) {
            if (polygon != null) {
                realWidth = polygon.getWidth();
            }
        } else {
            realWidth = label.getWidth() == 0 ? width : label.getWidth();
        }
        float coeff = width / realWidth;
        Log.d(TAG, "cubeTransition: hide " + coeff);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f);
        scaleAnimation.setDuration(200);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, (toRight ? 0.5f + 0.2f * coeff : -0.2f * coeff) * (width - deltaWidth), 0, 0);
        translateAnimation.setDuration(300);

        AnimationSet animationHide = new AnimationSet(true);
        animationHide.addAnimation(scaleAnimation);
        animationHide.addAnimation(translateAnimation);

        animationHide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setText(text);
                float realWidth = width;
                if (text.equals("")) {
                    if (polygon != null) {
                        realWidth = polygon.getWidth();
                    }
                } else {
                    realWidth = label.getWidth() == 0 ? width : label.getWidth();
                }
                float coeff = width / realWidth;
                Log.d(TAG, "cubeTransition: show " + coeff);

                ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f);
                scaleAnimation.setDuration(300);

                TranslateAnimation translateAnimation = new TranslateAnimation((toRight ? -0.2f * coeff : 0.5f + 0.2f * coeff) * (width - deltaWidth), 0, 0, 0);
                translateAnimation.setDuration(200);

                AnimationSet animationShow = new AnimationSet(true);
                animationShow.addAnimation(scaleAnimation);
                animationShow.addAnimation(translateAnimation);
                if (text.equals("")) {
                    if (polygon != null) {
                        polygon.startAnimation(animationShow);
                    }
                } else {
                    label.startAnimation(animationShow);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (label.getText().equals("")) {
            if (polygon != null) {
                polygon.startAnimation(animationHide);
            }
        } else {
            label.startAnimation(animationHide);
        }
    }
}
