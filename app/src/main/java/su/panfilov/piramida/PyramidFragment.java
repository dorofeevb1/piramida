package su.panfilov.piramida;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import su.panfilov.piramida.components.PyramidView;
import su.panfilov.piramida.models.Diary;

public class PyramidFragment extends Fragment {

    private static final String TAG = "PyramidFragment";

    View rootView;

    public boolean newRecord = true;
    private boolean savingOn = false; // Флаг записи

    public PyramidView piramidaView;
    public ImageButton toggleShapeButton; // Изменено на ImageButton
    public ImageButton toggleFavoritesButton; // Изменено на ImageButton
    public ImageButton likeButton;
    public ImageButton linkButton;

    private Diary diary;

    public static PyramidFragment newInstance() {
        PyramidFragment fragment = new PyramidFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pyramid, container, false);

        piramidaView = rootView.findViewById(R.id.pyramid);
        toggleShapeButton = rootView.findViewById(R.id.toggleShapeButton);
        toggleFavoritesButton = rootView.findViewById(R.id.toggleFavoritesButton);
        likeButton = rootView.findViewById(R.id.likeButton);
        linkButton = rootView.findViewById(R.id.linkButton);

        setSavingOn(false);

        // Устанавливаем обработчик нажатия на кнопку переключения формы
        toggleShapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShape(v);
            }
        });

        return rootView;
    }

    public void setSavingOn(boolean savingOn) {
        this.savingOn = savingOn;
        piramidaView.savingOn = savingOn;
    }

    public boolean getSavingOn() {
        return savingOn;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
        piramidaView.saveTranslation.diary = diary;
    }

    public Diary getDiary() {
        return diary;
    }

    public void toggleShape(View view) {
//        // Логика переключения между Прямоугольником и Треугольником
//        if (piramidaView != null) {
//            piramidaView.toggleShape();
//
//            // Обновление иконки кнопки
//            if (piramidaView.isTriangle()) {
//                toggleShapeButton.setImageResource(R.drawable.square); // Иконка для квадрата
//            } else {
//                toggleShapeButton.setImageResource(R.drawable.triangle); // Иконка для треугольника
//            }
//        }
    }

    public void toggleFavorites(View view) {
        // Логика включения/выключения режима Избранное/Все грани
    }

    public void likeTapped(View view) {
        // Логика для кнопки "Лайк"
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (diary != null) {
            diary.delete(requireContext().getApplicationContext());
        }
    }
}