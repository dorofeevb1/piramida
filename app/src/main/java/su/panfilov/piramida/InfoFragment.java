package su.panfilov.piramida;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Пустой конструктор
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Загружаем макет для этого фрагмента
        View rootView = inflater.inflate(R.layout.info_screen, container, false);

        // Находим кнопку "Назад"
        ImageButton backButton = rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Возвращаемся на предыдущий фрагмент
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        // Находим кнопку "Сердечко"
        ImageButton heartButton = rootView.findViewById(R.id.heartButton);
        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Логика для кнопки "Сердечко"
                // Например, добавить в избранное
            }
        });

        return rootView;
    }
}