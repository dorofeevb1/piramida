package su.panfilov.piramida;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Arrays;

import su.panfilov.piramida.components.PyramidView;
import su.panfilov.piramida.models.Diary;

public class PyramidFragment extends Fragment {

    private static final String TAG = "PyramidFragment";

    private View rootView;
    private boolean newRecord = true;
    private boolean savingOn = false;

    private PyramidView pyramidView;
    private ImageButton toggleShapeButton;
    private ImageButton toggleFavoritesButton;
    private ImageButton likeButton;
    private ImageButton linkButton;
    private ImageButton apButton;
    private LinearLayout wealthOrdersContainer;

    private Diary diary;
    private boolean isWealthOrdersVisible = true;

    // Создание нового экземпляра фрагмента
    public static PyramidFragment newInstance() {
        return new PyramidFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инициализация представления фрагмента
        rootView = inflater.inflate(R.layout.fragment_pyramid, container, false);
        initViews();
        setListeners();
        setSavingOn(false);
        return rootView;
    }

    // Инициализация представлений
    private void initViews() {
        pyramidView = rootView.findViewById(R.id.pyramid);
        toggleShapeButton = rootView.findViewById(R.id.toggleShapeButton);
        toggleFavoritesButton = rootView.findViewById(R.id.toggleFavoritesButton);
        likeButton = rootView.findViewById(R.id.likeButton);
        linkButton = rootView.findViewById(R.id.linkButton);
        apButton = rootView.findViewById(R.id.apButton);
        wealthOrdersContainer = rootView.findViewById(R.id.wealthOrdersContainer);
    }

    // Установка слушателей для кнопок
    private void setListeners() {
        linkButton.setOnClickListener(v -> openInfoScreen());
        toggleShapeButton.setOnClickListener(v -> toggleShape());
        apButton.setOnClickListener(v -> toggleWealthOrders());
    }

    // Открытие экрана с информацией
    private void openInfoScreen() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new InfoFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    // Установка состояния сохранения
    public void setSavingOn(boolean savingOn) {
        this.savingOn = savingOn;
        if (pyramidView != null) {
            pyramidView.setSavingOn(savingOn);
        }
    }

    // Получение текущего состояния сохранения
    public boolean getSavingOn() {
        return savingOn;
    }

    // Установка дневника
    public void setDiary(Diary diary) {
        this.diary = diary;
        if (pyramidView != null) {
            pyramidView.setDiary(diary);
        }
    }

    // Получение дневника
    public Diary getDiary() {
        return diary;
    }

    // Переключение формы пирамиды
    private void toggleShape() {
        if (pyramidView != null) {
            pyramidView.toggleShape();
            updateToggleShapeButtonIcon();
        }
    }

    // Обновление иконки кнопки переключения формы
    private void updateToggleShapeButtonIcon() {
        int iconRes = pyramidView.isTriangle() ? R.drawable.square : R.drawable.triangle;
        toggleShapeButton.setImageResource(iconRes);
    }

    // Переключение видимости контейнера "Богатство"
    private void toggleWealthOrders() {
        Log.d(TAG, "toggleWealthOrders called");
        isWealthOrdersVisible = !isWealthOrdersVisible;
        updateWealthOrdersVisibility();
    }

    // Обновление видимости контейнера "Богатство"
    private void updateWealthOrdersVisibility() {
        wealthOrdersContainer.setVisibility(View.VISIBLE);
        String[] labels = isWealthOrdersVisible ? getWealthOrdersLabels() : getTimeUnitsLabels();
        updateLabels(labels);
    }

    // Получение меток для "Богатства"
    private String[] getWealthOrdersLabels() {
        return new String[]{
                "1,000,000,000", "1,000,000", "100,000", "10,000", "1,000", "100", "10"
        };
    }

    // Получение меток для единиц времени
    private String[] getTimeUnitsLabels() {
        return new String[]{
                "10 лет", "Год", "Месяц", "День", "Час", "Мин", "Сек"
        };
    }

    // Обновление меток в контейнере
    private void updateLabels(String[] labels) {
        Log.d(TAG, "Updating labels with: " + Arrays.toString(labels));
        TextView[] wealthOrderTextViews = getWealthOrderTextViews();
        for (int i = 0; i < labels.length; i++) {
            wealthOrderTextViews[i].setText(labels[i]);
        }
        Log.d(TAG, "Labels updated successfully");
    }

    // Получение массива TextView для меток "Богатства"
    private TextView[] getWealthOrderTextViews() {
        return new TextView[]{
                rootView.findViewById(R.id.wealthOrder1),
                rootView.findViewById(R.id.wealthOrder2),
                rootView.findViewById(R.id.wealthOrder3),
                rootView.findViewById(R.id.wealthOrder4),
                rootView.findViewById(R.id.wealthOrder5),
                rootView.findViewById(R.id.wealthOrder6),
                rootView.findViewById(R.id.wealthOrder7)
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (diary != null) {
            diary.delete(requireContext().getApplicationContext());
        }
    }
}
