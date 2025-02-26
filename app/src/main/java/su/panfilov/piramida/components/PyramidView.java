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

    // Коэффициенты и отступы для расчета размеров пирамиды
    private float coeficentOfHead = 0.185f;
    private float horizontalMarginPercent = 8;
    private float verticalMarginPercent = 8;

    // Размеры и позиции пирамиды
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

    // Флаги состояния
    private boolean layersCreated = false;
    public PyramidsDataSource piramidaDataSource = new PyramidsDataSource();
    public boolean pyramidIsLocked = false;
    private boolean isRectangle = false;
    private boolean isTriangle = true;

    // Сохранение состояния
    public SaveTranslation saveTranslation;
    public boolean savingOn = false;

    // Список слоев и представлений
    public ArrayList<SwipeView> layerViews = new ArrayList<>(0);
    public TriangleView triangleView;
    public LockView lockView;

    // Начальные состояния
    public boolean lockViewInitialVisibility = false;
    public String[] initialLabelsText = {};
    public boolean userInteractive = true;

    // Конструкторы
    public PyramidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PyramidView(Context context) {
        super(context);
        initView(context);
    }

    // Инициализация представления
    private void initView(Context context) {
        this.context = context;
        saveTranslation = new SaveTranslation(context);
        playSound();
    }

    // Воспроизведение звука при инициализации
    private void playSound() {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.sound_shelk);
        mp.start();
        mp.setOnCompletionListener(MediaPlayer::release);
    }

    // Переключение формы пирамиды
    public void toggleShape() {
        Log.d(TAG, "toggleShape called. Current isTriangle: " + isTriangle);

        // Получаем контекст из текущего представления
        Context context = getContext();

        // Находим контейнер с кнопками
        LinearLayout buttonsContainer = ((Activity) context).findViewById(R.id.buttonsContainer);

        if (buttonsContainer == null) {
            Log.e(TAG, "buttonsContainer not found!");
            return;
        }

        if (isTriangle) {
            // Скрываем треугольник и его слои
            if (triangleView != null) {
                triangleView.setVisibility(View.GONE);
                Log.d(TAG, "Triangle view set to GONE");
            }
            // Устанавливаем горизонтальную ориентацию для контейнера с кнопками
            buttonsContainer.setOrientation(LinearLayout.HORIZONTAL);
            isTriangle = false;
            isRectangle = true;

            // Добавляем отступы между кнопками
            addMarginsToButtons(buttonsContainer);

            hideAllLayers();
        } else {
            // Скрываем прямоугольник и его слои
            if (lockView != null) {
                lockView.setVisibility(View.GONE);
                Log.d(TAG, "Rectangle view set to GONE");
            }

            // Устанавливаем вертикальную ориентацию для контейнера с кнопками
            buttonsContainer.setOrientation(LinearLayout.VERTICAL);
            isRectangle = false;
            isTriangle = true;

            // Убираем отступы между кнопками
            removeMarginsFromButtons(buttonsContainer);

            hideAllLayers();
        }

        // Обновляем слои и запрашиваем перерисовку
        layersCreated = false;
        requestLayout();
        invalidate();
        Log.d(TAG, "Shape toggled, requesting layout and invalidate. New isTriangle: " + isTriangle);
    }
    // Helper class to store the state of a SwipeView
    private static class SwipeViewState {
        String text;
        int visibility;
        // Add other properties as needed

        SwipeViewState(SwipeView view) {
            this.text = view.getText();
            this.visibility = view.getVisibility();
            // Capture other properties if necessary
        }
    }

    // Метод для добавления отступов между кнопками
    private void addMarginsToButtons(LinearLayout buttonsContainer) {
        for (int i = 0; i < buttonsContainer.getChildCount(); i++) {
            View button = buttonsContainer.getChildAt(i);
            if (button instanceof ImageButton) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
                params.setMargins(0, 0, 16, 0); // Добавляем отступ справа
                button.setLayoutParams(params);
            }
        }
    }

    // Метод для удаления отступов между кнопками
    private void removeMarginsFromButtons(LinearLayout buttonsContainer) {
        for (int i = 0; i < buttonsContainer.getChildCount(); i++) {
            View button = buttonsContainer.getChildAt(i);
            if (button instanceof ImageButton) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
                params.setMargins(0, 0, 0, 0); // Убираем все отступы
                button.setLayoutParams(params);
            }
        }
    }



    // Метод для скрытия всех слоев
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

    // Инициализация слоев пирамиды
    private void initializeLayers() {
        layersCreated = true;
        calculateDimensions();
        createLayers();
        if (isTriangle) {
            createTriangleView();
        } else {
            createLockView();
        }
        updateAll();
    }

    // Проверка, является ли текущая форма треугольником
    public boolean isTriangle() {
        return isTriangle;
    }

    // Расчет размеров пирамиды
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

    // Расчет размеров треугольника
    private void calculateTriangleDimensions() {
        headOfPyramid = topOfPyramid + heightOfPyramid * coeficentOfHead;
        widthOfHead = widthOfPyramid * coeficentOfHead;
        heightOfLayer = (bottomOfPyramid - headOfPyramid) / 8;
        deltaWidthOfLayer = (widthOfPyramid - widthOfHead) / 8;
        Log.d(TAG, "Triangle Dimensions - heightOfLayer: " + heightOfLayer + ", deltaWidthOfLayer: " + deltaWidthOfLayer);
    }

    // Расчет размеров прямоугольника
    private void calculateRectangleDimensions() {
        heightOfLayer = (heightOfPyramid / 8) / 1.23f;
        deltaWidthOfLayer = 0;
        Log.d(TAG, "Rectangle Dimensions - heightOfLayer: " + heightOfLayer);
    }

    // Создание слоев пирамиды
    private void createLayers() {
        for (int layer = 0; layer < 8; layer++) {
            SwipeView swipeView = createSwipeView(layer);
            layerViews.add(swipeView);
            addView(swipeView, createLayoutParams(layer));
        }
    }

    // Создание представления слоя
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

    // Создание параметров макета для слоя
    private LayoutParams createLayoutParams(int layer) {
        return new LayoutParams(
                Math.round(widthOfPyramid - deltaWidthOfLayer * layer),
                Math.round(heightOfLayer));
    }

    // Создание представления треугольника
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

    // Создание текстового представления для отображения фамилии
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

    // Создание параметров макета для треугольника
    private LayoutParams createTriangleLayoutParams() {
        return new LayoutParams(
                Math.round(widthOfHead),
                Math.round(heightOfPyramid * coeficentOfHead));
    }

    // Создание представления блокировки
    private void createLockView() {
        lockView = new LockView(context, Math.round(widthOfPyramid + 24), Math.round(heightOfPyramid + 24));
        lockView.left = Math.round(leftOfPyramid - 12);
        lockView.top = Math.round(topOfPyramid - 12);
        lockView.right = Math.round(rightOfPyramid + 12);
        lockView.bottom = Math.round(bottomOfPyramid + 12);
        lockView.setVisibility(lockViewInitialVisibility ? VISIBLE : INVISIBLE);
        addView(lockView, createLockLayoutParams());
    }

    // Создание параметров макета для блокировки
    private LayoutParams createLockLayoutParams() {
        return new LayoutParams(
                Math.round(widthOfPyramid + 24),
                Math.round(heightOfPyramid + 24));
    }

    // Размещение дочерних представлений
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

    // Обновление всех слоев
    public void updateAll() {
        String[] oneSide = piramidaDataSource.getFrontSide();
        for (int layer = 0; layer < 8; layer++) {
            layerViews.get(layer).setText(oneSide[layer]);
        }
    }

    // Включение/выключение сохранения состояния
    public void setSavingOn(boolean savingOn) {
        this.savingOn = savingOn;
    }

    // Установка дневника
    public void setDiary(Diary diary) {
        if (this.saveTranslation != null) {
            this.saveTranslation.diary = diary;
        }
    }

    // Переключение состояния блокировки
    private void toggleLockState() {
        if (!userInteractive) {
            return;
        }
        setPyramidIsLocked(!pyramidIsLocked);
        if (savingOn) {
            saveTranslation.setLockPyramid(pyramidIsLocked);
        }
    }

    // Установка состояния блокировки пирамиды
    protected void setPyramidIsLocked(boolean locked) {
        pyramidIsLocked = locked;
        lockView.setVisibility(locked ? VISIBLE : INVISIBLE);
    }

    // Внутренний класс для обработки событий свайпа
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

        // Обработка поворота влево при заблокированной пирамиде
        private void handleLockedLeftTurn() {
            String[] frontSide = piramidaDataSource.allLeftTurn();
            for (int tag = 1000; tag <= 1007; tag++) {
                layerViews.get(tag - 1000).turnLeft(piramidaDataSource.getTitle(tag - 1000));
            }
            if (savingOn) {
                saveTranslation.allLeftTurn();
            }
        }

        // Обработка поворота влево при разблокированной пирамиде
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

        // Обработка поворота вправо при заблокированной пирамиде
        private void handleLockedRightTurn() {
            String[] frontSide = piramidaDataSource.allRightTurn();
            for (int tag = 1000; tag <= 1007; tag++) {
                layerViews.get(tag - 1000).turnRight(piramidaDataSource.getTitle(tag - 1000));
            }
            if (savingOn) {
                saveTranslation.allRightTurn();
            }
        }

        // Обработка поворота вправо при разблокированной пирамиде
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

        // Обработка установки одной стороны
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
