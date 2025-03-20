package su.panfilov.piramida.components;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import su.panfilov.piramida.R;
import su.panfilov.piramida.models.PyramidsDataSource;
import su.panfilov.piramida.models.SaveTranslation;
import su.panfilov.piramida.models.Diary;

public class PyramidView extends RelativeLayout {
    private static final String TAG = "PyramidView";
    private Context context;

    // Coefficients and margins for pyramid size calculations
    private float coeficentOfHead = 0.185f;
    private float horizontalMarginPercent = 8;
    private float verticalMarginPercent = 8;

    // Pyramid dimensions and positions
    private float topOfPyramid;
    private float bottomOfPyramid;
    private float leftOfPyramid;
    private float rightOfPyramid;
    private float heightOfPyramid;
    private float widthOfPyramid;
    private float headOfPyramid;
    private float widthOfHead;
    private float heightOfLayer;
    private float deltaWidthOfLayer;

    // State flags
    private boolean layersCreated = false;
    public PyramidsDataSource piramidaDataSource = new PyramidsDataSource();
    public boolean pyramidIsLocked = false;
    private boolean isRectangle = false;
    private boolean isTriangle = true;

    // State saving
    public SaveTranslation saveTranslation;
    public boolean savingOn = false;

    // List of layers and views
    public ArrayList<SwipeView> layerViews = new ArrayList<>(0);
    public TriangleView triangleView;
    public LockView lockView;

    // Initial states
    public boolean lockViewInitialVisibility = false;
    public String[] initialLabelsText = {};
    public boolean userInteractive = true;

    // Constructors
    public PyramidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PyramidView(Context context) {
        super(context);
        initView(context);
    }

    // View initialization
    private void initView(Context context) {
        this.context = context;
        saveTranslation = new SaveTranslation(context);
        playSound();
    }

    // Play sound on initialization
    private void playSound() {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.sound_shelk);
        mp.start();
        mp.setOnCompletionListener(MediaPlayer::release);
    }

    // Toggle pyramid shape
    public void toggleShape() {
        Log.d(TAG, "toggleShape called. Current isTriangle: " + isTriangle);

        Context context = getContext();
        LinearLayout buttonsContainer = ((Activity) context).findViewById(R.id.buttonsContainer);

        if (buttonsContainer == null) {
            Log.e(TAG, "buttonsContainer not found!");
            return;
        }

        // Сохраняем текущее состояние свайпов
        String[] currentStates = new String[layerViews.size()];
        for (int i = 0; i < layerViews.size(); i++) {
            currentStates[i] = layerViews.get(i).getText(); // Сохраняем текст (или состояние) каждого слоя
        }

        // Удаляем старые слои из view и очищаем список
        for (SwipeView layer : layerViews) {
            removeView(layer);
        }
        layerViews.clear();

        if (isTriangle) {
            toggleToRectangle(buttonsContainer);
        } else {
            toggleToTriangle(buttonsContainer);
        }

        layersCreated = false;
        requestLayout();
        invalidate();

        // Восстанавливаем состояние свайпов после создания новых слоев
        for (int i = 0; i < layerViews.size(); i++) {
            layerViews.get(i).setText(currentStates[i]);
        }

        Log.d(TAG, "Shape toggled, requesting layout and invalidate. New isTriangle: " + isTriangle);
    }

    private void toggleToRectangle(LinearLayout buttonsContainer) {
        if (triangleView != null) {
            triangleView.setVisibility(View.GONE);
            Log.d(TAG, "Triangle view set to GONE");
        }
        buttonsContainer.setOrientation(LinearLayout.HORIZONTAL);
        isTriangle = false;
        isRectangle = true;
        addMarginsToButtons(buttonsContainer);
        hideAllLayers();
    }

    private void toggleToTriangle(LinearLayout buttonsContainer) {
        if (lockView != null) {
            lockView.setVisibility(View.GONE);
            Log.d(TAG, "Rectangle view set to GONE");
        }
        buttonsContainer.setOrientation(LinearLayout.VERTICAL);
        isRectangle = false;
        isTriangle = true;
        removeMarginsFromButtons(buttonsContainer);
        hideAllLayers();
    }

    // Helper class to store the state of a SwipeView
    private static class SwipeViewState {
        String text;
        int visibility;

        SwipeViewState(SwipeView view) {
            this.text = view.getText();
            this.visibility = view.getVisibility();
        }
    }

    // Add margins between buttons
    private void addMarginsToButtons(LinearLayout buttonsContainer) {
        for (int i = 0; i < buttonsContainer.getChildCount(); i++) {
            View button = buttonsContainer.getChildAt(i);
            if (button instanceof ImageButton) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
                params.setMargins(0, 0, 16, 0); // Add right margin
                button.setLayoutParams(params);
            }
        }
    }

    // Remove margins between buttons
    private void removeMarginsFromButtons(LinearLayout buttonsContainer) {
        for (int i = 0; i < buttonsContainer.getChildCount(); i++) {
            View button = buttonsContainer.getChildAt(i);
            if (button instanceof ImageButton) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
                params.setMargins(0, 0, 0, 0); // Remove all margins
                button.setLayoutParams(params);
            }
        }
    }

    // Hide all layers
    private void hideAllLayers() {
        for (SwipeView layer : layerViews) {
            layer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!layersCreated) {
            initializeLayers();
        }
        layoutChildren();
    }

    // Initialize pyramid layers
    private void initializeLayers() {
        layersCreated = true;
        calculateDimensions();
        createLayers();
        if (isTriangle) {
            createTriangleView();
        } else {
            createLockView();
        }
        updateAll(); // Устанавливаем текст слоев на основе текущего состояния в piramidaDataSource
    }

    // Check if the current shape is a triangle
    public boolean isTriangle() {
        return isTriangle;
    }

    // Calculate pyramid dimensions
    private void calculateDimensions() {
        float widthMeasureSpec = getMeasuredWidth();
        float heightMeasureSpec = getMeasuredHeight();

        topOfPyramid = widthMeasureSpec * verticalMarginPercent / 100;
        bottomOfPyramid = heightMeasureSpec * (100 - verticalMarginPercent) / 100;
        leftOfPyramid = widthMeasureSpec * horizontalMarginPercent / 100;
        rightOfPyramid = widthMeasureSpec - leftOfPyramid;

        heightOfPyramid = bottomOfPyramid - topOfPyramid;
        widthOfPyramid = rightOfPyramid - leftOfPyramid;

        if (isTriangle) {
            calculateTriangleDimensions();
        } else {
            calculateRectangleDimensions();
        }
    }

    // Calculate triangle dimensions
    private void calculateTriangleDimensions() {
        headOfPyramid = topOfPyramid + heightOfPyramid * coeficentOfHead;
        widthOfHead = widthOfPyramid * coeficentOfHead;
        heightOfLayer = (bottomOfPyramid - headOfPyramid) / 8;
        deltaWidthOfLayer = (widthOfPyramid - widthOfHead) / 8;
    }

    // Calculate rectangle dimensions
    private void calculateRectangleDimensions() {
        heightOfLayer = (heightOfPyramid / 8) / 1.23f;
        deltaWidthOfLayer = 0;
    }

    // Create pyramid layers
    private void createLayers() {
        for (int layer = 0; layer < 8; layer++) {
            SwipeView swipeView = createSwipeView(layer);
            layerViews.add(swipeView);
            addView(swipeView, createLayoutParams(layer));
        }
    }

    // Create a layer view
    private SwipeView createSwipeView(int layer) {
        SwipeView swipeView = new SwipeView(context, deltaWidthOfLayer, layer,
                Math.round(widthOfPyramid - deltaWidthOfLayer * layer),
                Math.round(heightOfLayer));
        swipeView.left = Math.round(leftOfPyramid + deltaWidthOfLayer / 2 * layer);
        swipeView.top = Math.round(bottomOfPyramid - heightOfLayer * (layer + 1));
        swipeView.right = swipeView.left + Math.round(widthOfPyramid - deltaWidthOfLayer * layer);
        swipeView.bottom = swipeView.top + Math.round(heightOfLayer);
        swipeView.tag = layer + 1000;
        swipeView.delegate = new PyramidSwipeViewDelegate();
        swipeView.userInteractive = userInteractive;
        return swipeView;
    }

    // Create layout parameters for a layer
    private LayoutParams createLayoutParams(int layer) {
        return new LayoutParams(
                Math.round(widthOfPyramid - deltaWidthOfLayer * layer),
                Math.round(heightOfLayer));
    }

    // Create triangle view
    private void createTriangleView() {
        int triangleWidth = Math.round(widthOfHead);
        int triangleHeight = Math.round(heightOfPyramid * coeficentOfHead);
        triangleView = new TriangleView(context, triangleWidth, triangleHeight);
        triangleView.left = Math.round(leftOfPyramid + widthOfPyramid / 2 - widthOfHead / 2);
        triangleView.top = Math.round(topOfPyramid);
        triangleView.right = Math.round(triangleView.left + triangleWidth);
        triangleView.bottom = Math.round(triangleView.top + triangleHeight);
        triangleView.setOnClickListener(v -> toggleLockState());
        addView(triangleView, createTriangleLayoutParams());

        createFamilyNameTextView();
    }

    // Create text view for displaying family name
    private void createFamilyNameTextView() {
        TextView familyNameTextView = new TextView(context);
        familyNameTextView.setText("Фамилия");
        familyNameTextView.setTextColor(Color.BLACK);
        familyNameTextView.setTextSize(16);

        LayoutParams textViewParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        textViewParams.topMargin = Math.round(bottomOfPyramid - 80);
        textViewParams.leftMargin = Math.round(leftOfPyramid * 5);
        addView(familyNameTextView, textViewParams);
    }

    // Create layout parameters for triangle view
    private LayoutParams createTriangleLayoutParams() {
        return new LayoutParams(
                Math.round(widthOfHead),
                Math.round(heightOfPyramid * coeficentOfHead));
    }

    // Create lock view
    private void createLockView() {
        lockView = new LockView(context, Math.round(widthOfPyramid + 24), Math.round(heightOfPyramid + 24));
        lockView.left = Math.round(leftOfPyramid - 12);
        lockView.top = Math.round(topOfPyramid - 12);
        lockView.right = Math.round(rightOfPyramid + 12);
        lockView.bottom = Math.round(bottomOfPyramid + 12);
        lockView.setVisibility(lockViewInitialVisibility ? VISIBLE : INVISIBLE);
        addView(lockView, createLockLayoutParams());
    }

    // Create layout parameters for lock view
    private LayoutParams createLockLayoutParams() {
        return new LayoutParams(
                Math.round(widthOfPyramid + 24),
                Math.round(heightOfPyramid + 24));
    }

    // Layout child views
    private void layoutChildren() {
        for (SwipeView swipeView : layerViews) {
            swipeView.layout(swipeView.left, swipeView.top, swipeView.right, swipeView.bottom);
        }
        if (isTriangle && triangleView != null) {
            triangleView.layout(triangleView.left, triangleView.top, triangleView.right, triangleView.bottom);
        }
        if (lockView != null) {
            lockView.layout(lockView.left, lockView.top, lockView.right, lockView.bottom);
        }
    }

    // Update all layers
    public void updateAll() {
        String[] oneSide = piramidaDataSource.getFrontSide();
        for (int layer = 0; layer < 8; layer++) {
            layerViews.get(layer).setText(oneSide[layer]);
        }
    }

    // Enable/disable saving state
    public void setSavingOn(boolean savingOn) {
        this.savingOn = savingOn;
    }

    // Set diary
    public void setDiary(Diary diary) {
        if (this.saveTranslation != null) {
            this.saveTranslation.diary = diary;
        }
    }

    // Toggle lock state
    private void toggleLockState() {
        if (!userInteractive) {
            return;
        }
        setPyramidIsLocked(!pyramidIsLocked);
        if (savingOn) {
            saveTranslation.setLockPyramid(pyramidIsLocked);
        }
    }

    // Set pyramid lock state
    protected void setPyramidIsLocked(boolean locked) {
        pyramidIsLocked = locked;
        lockView.setVisibility(locked ? VISIBLE : INVISIBLE);
    }

    // Inner class to handle swipe events
    private class PyramidSwipeViewDelegate implements SwipeViewDelegate {
        @Override
        public String leftTurn(int layer) {
            if (pyramidIsLocked) {
                handleLockedLeftTurn();
                return piramidaDataSource.getFrontSide()[layer];
            }
            handleUnlockedLeftTurn(layer);
            return piramidaDataSource.leftTurn(layer);
        }

        // Handle left turn when pyramid is locked
        private void handleLockedLeftTurn() {
            String[] frontSide = piramidaDataSource.allLeftTurn();
            for (int tag = 1000; tag <= 1007; tag++) {
                layerViews.get(tag - 1000).turnLeft(piramidaDataSource.getTitle(tag - 1000));
            }
            if (savingOn) {
                saveTranslation.allLeftTurn();
            }
        }

        // Handle left turn when pyramid is unlocked
        private void handleUnlockedLeftTurn(int layer) {
            if (savingOn) {
                saveTranslation.leftTurn(layer);
            }
        }

        @Override
        public String rightTurn(int layer) {
            if (pyramidIsLocked) {
                handleLockedRightTurn();
                return piramidaDataSource.getFrontSide()[layer];
            }
            handleUnlockedRightTurn(layer);
            return piramidaDataSource.rightTurn(layer);
        }

        // Handle right turn when pyramid is locked
        private void handleLockedRightTurn() {
            String[] frontSide = piramidaDataSource.allRightTurn();
            for (int tag = 1000; tag <= 1007; tag++) {
                layerViews.get(tag - 1000).turnRight(piramidaDataSource.getTitle(tag - 1000));
            }
            if (savingOn) {
                saveTranslation.allRightTurn();
            }
        }

        // Handle right turn when pyramid is unlocked
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

        // Handle setting one side
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
