package su.panfilov.piramida.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import su.panfilov.piramida.R;
import su.panfilov.piramida.models.PyramidsDataSource;
import su.panfilov.piramida.models.SaveTranslation;

public class PyramidView extends RelativeLayout {

    private static final String TAG = "PyramidView";
    private Context context;

    private float coeficentOfHead = 0.185f;
    private float horizontalMarginInPercent = 8;
    private float topMarginInPercent = 8;
    private float bottomMarginInPercent = 8;

    private float topOfPiramida;
    private float bottomOfPiramida;
    private float leftOfPiramida;
    private float rightOfPiramida;

    private float heightOfPiramida;
    private float widthOfPiramida;

    private float headOfPiramida;
    private float widthOfHead;

    private float heightOfLayer;
    private float deltaWidthOfLayer;

    private boolean layersCreated = false;

    public PyramidsDataSource piramidaDataSource = new PyramidsDataSource();
    public boolean piramidaIsLocked = false;

    public SaveTranslation saveTranslation;
    public boolean savingOn = false;

    public ArrayList<SwipeView> layerViews = new ArrayList<>(0);
    public TriangleView triangleView;
    public LockView lockView;

    public boolean lockViewInitialVisibility = false;
    public String[] initialLabelsText = {};

    public boolean userInteractive = true;

    private boolean isTriangle = true; // Declare the isTriangle variable

    public PyramidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        saveTranslation = new SaveTranslation(context);
        final MediaPlayer mp = MediaPlayer.create(this.context, R.raw.sound_shelk);
        mp.start();
    }

    public PyramidView(Context context) {
        super(context);
        this.context = context;
        saveTranslation = new SaveTranslation(context);
        final MediaPlayer mp = MediaPlayer.create(this.context, R.raw.sound_shelk);
        mp.start();
    }

    public void toggleShape() {
        isTriangle = !isTriangle; // Toggle the shape state
        triangleView.setIsTriangle(isTriangle); // Update the TriangleView
        requestLayout(); // Request layout update
        invalidate(); // Redraw the view with the new shape
    }

    public boolean isTriangle() {
        return isTriangle; // Return the current shape state
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        super.onLayout(b, i, i1, i2, i3);
        init();

        for (SwipeView swipeView : layerViews) {
            swipeView.layout(swipeView.left, swipeView.top, swipeView.right, swipeView.bottom);
        }

        triangleView.layout(triangleView.left, triangleView.top, triangleView.right, triangleView.bottom);
        lockView.layout(lockView.left, lockView.top, lockView.right, lockView.bottom);
    }

    protected void init() {
        if (layersCreated) {
            return;
        }

        layersCreated = true;

        float widthMeasureSpec = this.getMeasuredWidth();
        float heightMeasureSpec = this.getMeasuredHeight();

        topOfPiramida = widthMeasureSpec * topMarginInPercent / 100;
        bottomOfPiramida = heightMeasureSpec * (100 - bottomMarginInPercent) / 100;
        leftOfPiramida = widthMeasureSpec * horizontalMarginInPercent / 100;
        rightOfPiramida = widthMeasureSpec - leftOfPiramida;

        heightOfPiramida = bottomOfPiramida - topOfPiramida;
        widthOfPiramida = rightOfPiramida - leftOfPiramida;

        headOfPiramida = topOfPiramida + heightOfPiramida * coeficentOfHead;
        widthOfHead = widthOfPiramida * coeficentOfHead;

        heightOfLayer = (bottomOfPiramida - headOfPiramida) / 8;
        deltaWidthOfLayer = (widthOfPiramida - widthOfHead) / 8;

        // Создать 8 слоев
        for (int layer = 0; layer < 8; layer++) {
            SwipeView swipeView = new SwipeView(context, deltaWidthOfLayer, layer,
                    Math.round(widthOfPiramida - deltaWidthOfLayer * (float)layer),
                    Math.round(heightOfLayer));
            swipeView.left = Math.round(leftOfPiramida + deltaWidthOfLayer / 2 * (float)layer);
            swipeView.top = Math.round(bottomOfPiramida - heightOfLayer * (float)(layer + 1));
            swipeView.right = swipeView.left + Math.round(widthOfPiramida - deltaWidthOfLayer * (float)layer);
            swipeView.bottom = swipeView.top + Math.round(heightOfLayer);
            swipeView.tag = layer + 1000;
            swipeView.delegate = new PyramidSwipeViewDelegate();
            swipeView.userInteractive = userInteractive;

            LayoutParams layoutParams = new LayoutParams(
                    Math.round(widthOfPiramida - deltaWidthOfLayer * (float)layer),
                    Math.round(heightOfLayer));
            addView(swipeView, layoutParams);
            layerViews.add(swipeView);
        }

        int triangleWidth = Math.round(widthOfHead);
        int triangleHeight = Math.round(heightOfPiramida * coeficentOfHead);
        triangleView = new TriangleView(context, triangleWidth, triangleHeight);
        triangleView.left = Math.round(leftOfPiramida + widthOfPiramida / 2 - widthOfHead / 2);
        triangleView.top = Math.round(topOfPiramida);
        triangleView.right = Math.round(triangleView.left + triangleWidth);
        triangleView.bottom = Math.round(triangleView.top + triangleHeight);
        triangleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userInteractive) {
                    return;
                }

                setPiramidaIsLocked(!piramidaIsLocked);

                if (savingOn) {
                    saveTranslation.setLockPiramida(piramidaIsLocked);
                }
            }
        });

        LayoutParams layoutParams = new LayoutParams(
                Math.round(widthOfHead),
                Math.round(heightOfPiramida * coeficentOfHead));
        addView(triangleView, layoutParams);

        lockView = new LockView(context, Math.round(widthOfPiramida + 24), Math.round(heightOfPiramida + 24));
        lockView.left = Math.round(leftOfPiramida - 12);
        lockView.top = Math.round(topOfPiramida - 12);
        lockView.right = Math.round(rightOfPiramida + 12);
        lockView.bottom = Math.round(bottomOfPiramida + 12);
        lockView.setVisibility(lockViewInitialVisibility ? VISIBLE : INVISIBLE);
        LayoutParams lvLayoutParams = new LayoutParams(
                Math.round(widthOfPiramida + 24),
                Math.round(heightOfPiramida + 24));
        addView(lockView, lvLayoutParams);

        updateAll();
    }

    public void updateAll() {
        String[] oneSide = piramidaDataSource.getFrontSide();
        for (int layer = 0; layer < 8; layer++) {
            layerViews.get(layer).setText(oneSide[layer]);
        }
    }

    protected void setPiramidaIsLocked(boolean locked) {
        piramidaIsLocked = locked;
        lockView.setVisibility(locked ? VISIBLE : INVISIBLE);
    }

    private class PyramidSwipeViewDelegate implements SwipeViewDelegate {
        @Override
        public String leftTurn(int layer) {
            if (piramidaIsLocked) {
                String[] frontSide = piramidaDataSource.allLeftTurn();

                for (int tag = 1000; tag <= 1007; tag++) {
                    layerViews.get(tag - 1000).turnLeft(piramidaDataSource.getTitle(tag - 1000));
                }

                if (savingOn) {
                    saveTranslation.allLeftTurn();
                }
                return frontSide[layer];
            }

            if (savingOn) {
                saveTranslation.leftTurn(layer);
            }
            return piramidaDataSource.leftTurn(layer);
        }

        @Override
        public String rightTurn(int layer) {
            if (piramidaIsLocked) {
                String[] frontSide = piramidaDataSource.allRightTurn();

                for (int tag = 1000; tag <= 1007; tag++) {
                    layerViews.get(tag - 1000).turnRight(piramidaDataSource.getTitle(tag - 1000));
                }

                if (savingOn) {
                    saveTranslation.allRightTurn();
                }
                return frontSide[layer];
            }

            if (savingOn) {
                saveTranslation.rightTurn(layer);
            }
            return piramidaDataSource.rightTurn(layer);
        }

        @Override
        public String getTitle(int layer) {
            return piramidaDataSource.getTitle(layer);
        }

        @Override
        public void setOneSide(int byLayer) {
            piramidaDataSource.setOneSide(byLayer);

            for (int tag = 1000; tag <= 1007; tag++) {
                if (tag == byLayer + 1000) {
                    layerViews.get(tag - 1000).playSwipe();
                    continue;
                }

                if (tag % 2 == 0) {
                    layerViews.get(tag - 1000).turnRight(piramidaDataSource.getTitle(tag - 1000));
                } else {
                    layerViews.get(tag - 1000).turnLeft(piramidaDataSource.getTitle(tag - 1000));
                }
            }
            if (savingOn) {
                saveTranslation.setOneSide(byLayer);
            }
        }
    }
}
