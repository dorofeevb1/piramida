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
import android.widget.TextView;

import java.util.ArrayList;

import su.panfilov.piramida.R;
import su.panfilov.piramida.models.PyramidsDataSource;
import su.panfilov.piramida.models.SaveTranslation;
import su.panfilov.piramida.models.Diary; // Import the Diary class

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

    private boolean isTriangle = true;

    public PyramidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PyramidView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        saveTranslation = new SaveTranslation(context);
        playSound();
    }

    private void playSound() {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.sound_shelk);
        mp.start();
        mp.setOnCompletionListener(MediaPlayer::release);
    }

    public void toggleShape() {
        isTriangle = !isTriangle;
        triangleView.setIsTriangle(isTriangle);
        requestLayout();
        invalidate();
    }

    public boolean isTriangle() {
        return isTriangle;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!layersCreated) {
            initializeLayers();
        }
        layoutChildren();
    }

    private void initializeLayers() {
        layersCreated = true;
        calculateDimensions();
        createLayers();
        createTriangleView();
        createLockView();
        updateAll();
    }

    private void calculateDimensions() {
        float widthMeasureSpec = getMeasuredWidth();
        float heightMeasureSpec = getMeasuredHeight();

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
    }

    private void createLayers() {
        for (int layer = 0; layer < 8; layer++) {
            SwipeView swipeView = createSwipeView(layer);
            layerViews.add(swipeView);
            addView(swipeView, createLayoutParams(layer));
        }
    }

    private SwipeView createSwipeView(int layer) {
        SwipeView swipeView = new SwipeView(context, deltaWidthOfLayer, layer,
                Math.round(widthOfPiramida - deltaWidthOfLayer * layer),
                Math.round(heightOfLayer));
        swipeView.left = Math.round(leftOfPiramida + deltaWidthOfLayer / 2 * layer);
        swipeView.top = Math.round(bottomOfPiramida - heightOfLayer * (layer + 1));
        swipeView.right = swipeView.left + Math.round(widthOfPiramida - deltaWidthOfLayer * layer);
        swipeView.bottom = swipeView.top + Math.round(heightOfLayer);
        swipeView.tag = layer + 1000;
        swipeView.delegate = new PyramidSwipeViewDelegate();
        swipeView.userInteractive = userInteractive;

        return swipeView;
    }

    private LayoutParams createLayoutParams(int layer) {
        return new LayoutParams(
                Math.round(widthOfPiramida - deltaWidthOfLayer * layer),
                Math.round(heightOfLayer));
    }

    private void createTriangleView() {
        int triangleWidth = Math.round(widthOfHead);
        int triangleHeight = Math.round(heightOfPiramida * coeficentOfHead);
        triangleView = new TriangleView(context, triangleWidth, triangleHeight);
        triangleView.left = Math.round(leftOfPiramida + widthOfPiramida / 2 - widthOfHead / 2);
        triangleView.top = Math.round(topOfPiramida);
        triangleView.right = Math.round(triangleView.left + triangleWidth);
        triangleView.bottom = Math.round(triangleView.top + triangleHeight);
        triangleView.setOnClickListener(v -> toggleLockState());
        addView(triangleView, createTriangleLayoutParams());

        // Создаем TextView для отображения текста "Фамилия"
        TextView familyNameTextView = new TextView(context);
        familyNameTextView.setText("Фамилия");
        familyNameTextView.setTextColor(Color.BLACK); // Установите нужный цвет текста
        familyNameTextView.setTextSize(16); // Установите нужный размер текста

        // Размещаем TextView под нижней частью пирамиды
        LayoutParams textViewParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        textViewParams.topMargin = Math.round(bottomOfPiramida - 80 ); // 10 - отступ от нижней части пирамиды
        textViewParams.leftMargin = Math.round(leftOfPiramida * 5 );
        addView(familyNameTextView, textViewParams);
    }


    private LayoutParams createTriangleLayoutParams() {
        return new LayoutParams(
                Math.round(widthOfHead),
                Math.round(heightOfPiramida * coeficentOfHead));
    }

    private void createLockView() {
        lockView = new LockView(context, Math.round(widthOfPiramida + 24), Math.round(heightOfPiramida + 24));
        lockView.left = Math.round(leftOfPiramida - 12);
        lockView.top = Math.round(topOfPiramida - 12);
        lockView.right = Math.round(rightOfPiramida + 12);
        lockView.bottom = Math.round(bottomOfPiramida + 12);
        lockView.setVisibility(lockViewInitialVisibility ? VISIBLE : INVISIBLE);
        addView(lockView, createLockLayoutParams());
    }

    private LayoutParams createLockLayoutParams() {
        return new LayoutParams(
                Math.round(widthOfPiramida + 24),
                Math.round(heightOfPiramida + 24));
    }

    private void layoutChildren() {
        for (SwipeView swipeView : layerViews) {
            swipeView.layout(swipeView.left, swipeView.top, swipeView.right, swipeView.bottom);
        }
        triangleView.layout(triangleView.left, triangleView.top, triangleView.right, triangleView.bottom);
        lockView.layout(lockView.left, lockView.top, lockView.right, lockView.bottom);
    }

    public void updateAll() {
        String[] oneSide = piramidaDataSource.getFrontSide();
        for (int layer = 0; layer < 8; layer++) {
            layerViews.get(layer).setText(oneSide[layer]);
        }
    }

    public void setSavingOn(boolean savingOn) {
        this.savingOn = savingOn;
        // Additional logic if needed when saving state changes
    }

    public void setDiary(Diary diary) {
        if (this.saveTranslation != null) {
            this.saveTranslation.diary = diary;
        }
        // Additional logic if needed when diary is set
    }

    private void toggleLockState() {
        if (!userInteractive) {
            return;
        }
        setPiramidaIsLocked(!piramidaIsLocked);
        if (savingOn) {
            saveTranslation.setLockPiramida(piramidaIsLocked);
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
                handleLockedLeftTurn();
                return piramidaDataSource.getFrontSide()[layer];
            }
            handleUnlockedLeftTurn(layer);
            return piramidaDataSource.leftTurn(layer);
        }

        private void handleLockedLeftTurn() {
            String[] frontSide = piramidaDataSource.allLeftTurn();
            for (int tag = 1000; tag <= 1007; tag++) {
                layerViews.get(tag - 1000).turnLeft(piramidaDataSource.getTitle(tag - 1000));
            }
            if (savingOn) {
                saveTranslation.allLeftTurn();
            }
        }

        private void handleUnlockedLeftTurn(int layer) {
            if (savingOn) {
                saveTranslation.leftTurn(layer);
            }
        }

        @Override
        public String rightTurn(int layer) {
            if (piramidaIsLocked) {
                handleLockedRightTurn();
                return piramidaDataSource.getFrontSide()[layer];
            }
            handleUnlockedRightTurn(layer);
            return piramidaDataSource.rightTurn(layer);
        }

        private void handleLockedRightTurn() {
            String[] frontSide = piramidaDataSource.allRightTurn();
            for (int tag = 1000; tag <= 1007; tag++) {
                layerViews.get(tag - 1000).turnRight(piramidaDataSource.getTitle(tag - 1000));
            }
            if (savingOn) {
                saveTranslation.allRightTurn();
            }
        }

        private void handleUnlockedRightTurn(int layer) {
            if (savingOn) {
                saveTranslation.rightTurn(layer);
            }
        }

        @Override
        public String getTitle(int layer) {
            return piramidaDataSource.getTitle(layer);
        }

        @Override
        public void setOneSide(int byLayer) {
            piramidaDataSource.setOneSide(byLayer);
            handleSetOneSide(byLayer);
        }

        private void handleSetOneSide(int byLayer) {
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
