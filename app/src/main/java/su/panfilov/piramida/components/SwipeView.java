package su.panfilov.piramida.components;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import su.panfilov.piramida.R;
import su.panfilov.piramida.components.PyramidColors;

import static android.graphics.Typeface.DEFAULT_BOLD;

public class SwipeView extends ViewGroup {
    private static final String TAG = "SwipeView";

    private final MediaPlayer mp;
    private final Context context;
    private boolean isTriangle = true;
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
        initAttributes(attrs);
        mp = createMediaPlayer(R.raw.swipe_short);
        setup();
    }

    public SwipeView(Context context, float deltaWidth, int layer, int width, int height) {
        super(context);
        this.context = context;
        this.deltaWidth = deltaWidth;
        this.numberOfLayer = layer;
        this.width = width;
        this.height = height;
        mp = createMediaPlayer(R.raw.swipe_short);
        setup();
    }

    private void initAttributes(@NonNull AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeView, 0, 0);
        try {
            deltaWidth = a.getFloat(R.styleable.SwipeView_deltaWidth, 1);
            numberOfLayer = a.getInteger(R.styleable.SwipeView_layer, 1);
        } finally {
            a.recycle();
        }
    }

    private MediaPlayer createMediaPlayer(int resourceId) {
        MediaPlayer player = MediaPlayer.create(context, resourceId);
        player.setOnPreparedListener(MediaPlayer::start);
        player.setOnCompletionListener(MediaPlayer::release);
        player.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "MediaPlayer error: " + what + ", " + extra);
            mp.reset();
            return true;
        });
        return player;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutBackground();
        layoutPolygon();
        layoutLabel();
    }

    private void layoutBackground() {
        backgroundLayer.layout(0, 0, width, height);
    }

    private void layoutPolygon() {
        if (numberOfLayer > 0) {
            polygon.layout(Math.round(width / 2.0f - height / 4.0f), Math.round(height / 4.0f),
                    Math.round(width / 2.0f + height / 4.0f), Math.round(height / 4.0f * 3));
        }
    }

    private void layoutLabel() {
        label.measure(0, 0);
        int labelWidth = label.getMeasuredWidth();
        int labelHeight = label.getMeasuredHeight();
        if (labelWidth > width - deltaWidth / 1.5) {
            String text = label.getText().toString();
            text = text.substring(0, text.length() / 2) + "\n" + text.substring(text.length() / 2);
            label.setText(text);
            label.setTextSize(height / 8);
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
            setupPolygon();
        }

        setupLabel();
        setListeners();
    }

    private void setupPolygon() {
        polygon = new ImageView(context);
        int polygonIcon = getResources().getIdentifier("ic_polygon_" + numberOfLayer, "drawable", context.getPackageName());
        polygon.setImageDrawable(getResources().getDrawable(polygonIcon));
        addView(polygon);
    }

    private void setupLabel() {
        label = new TextView(context);
        label.setTextColor(numberOfLayer == 0 ? PyramidColors.textColor() : Color.WHITE);
        label.setText("");
        label.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        label.setTextSize(height / 8);
        label.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        label.setTypeface(Typeface.create(DEFAULT_BOLD, Typeface.BOLD));
        addView(label);
    }

    private void setListeners() {
        setOnLongClickListener(v -> {
            if (!userInteractive) {
                return true;
            }
            didLongTap();
            return true;
        });

        setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeRight() {
                if (!userInteractive) {
                    return;
                }
                didRightSwipe();
            }

            @Override
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
            polygon.setVisibility(text.isEmpty() ? VISIBLE : INVISIBLE);
        }
        label.setText(text);
        label.setTextSize(height / 8);
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
        if (mp != null && isMediaPlayerPrepared()) {
            mp.start();
        }
    }

    private boolean isMediaPlayerPrepared() {
        try {
            return mp.isPlaying() || mp.getCurrentPosition() > 0;
        } catch (IllegalStateException e) {
            Log.e(TAG, "MediaPlayer is not prepared or has been released.");
            return false;
        }
    }

    public void turnLeft(String title) {
        cubeTransition(title, false);
    }

    public void turnRight(String title) {
        cubeTransition(title, true);
    }

    private void cubeTransition(final String text, final boolean toRight) {
        float realWidth = width;
        if (label.getText().toString().isEmpty()) {
            if (polygon != null) {
                realWidth = polygon.getWidth();
            }
        } else {
            realWidth = label.getWidth() == 0 ? width : label.getWidth();
        }
        float coeff = width / realWidth;
        Log.d(TAG, "cubeTransition: hide " + coeff);

        final View targetView = label.getText().toString().isEmpty() ? polygon : label;

        targetView.animate()
                .scaleX(0.0f)
                .translationX((toRight ? 0.5f + 0.2f * coeff : -0.2f * coeff) * (width - deltaWidth))
                .setDuration(300)
                .withEndAction(() -> {
                    setText(text);
                    final View newTargetView = text.isEmpty() ? polygon : label;
                    newTargetView.animate()
                            .scaleX(1.0f)
                            .translationX(0)
                            .setDuration(300)
                            .start();
                })
                .start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mp != null) {
            mp.release();
        }
    }
}
