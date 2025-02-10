package su.panfilov.piramida;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import su.panfilov.piramida.components.HelpAdapter;
import su.panfilov.piramida.models.HelpItem;

public class HelpFragment extends Fragment {

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        initListView(rootView);

        return rootView;
    }

    private void initListView(View rootView) {
        ArrayList<HelpItem> helpItems = new ArrayList<>();

        // Заголовок "Справка" остается фиксированным
        helpItems.add(new HelpItem("Пирамида", ""));
        helpItems.add(new HelpItem("Нажатие на ★", "Добавление грани в «Избранное»"));
        helpItems.add(new HelpItem("Нажатие на ★ (повторно)", "Удаление грани из «Избранного»"));
        helpItems.add(new HelpItem("Нажатие на «Избранное»", "Переключение в режим «Избранное» для отображения только избранных граней"));
        helpItems.add(new HelpItem("Нажатие на «Все грани»", "Переключение обратно в обычный режим"));
        helpItems.add(new HelpItem("Свайп", "Вращение слоя"));
        helpItems.add(new HelpItem("Долгое нажатие на слово уровня", "Построение грани"));
        helpItems.add(new HelpItem("Нажатие на верхушку", "Блокировка грани для прокрутки всех уровней"));
        helpItems.add(new HelpItem("Нажатие на АРх10", "Включение дополнительной грани с численными показателями для определения порядков богатства"));
        helpItems.add(new HelpItem("Повторное нажатие на АРх10", "Включение дополнительной грани с численными показателями для определения времени"));
        helpItems.add(new HelpItem("Нажатие на квадрат", "Переключение на прямоугольный вид"));
        helpItems.add(new HelpItem("Нажатие на треугольник", "Переключение на треугольный вид"));
        helpItems.add(new HelpItem("Нажатие на инфо", "Открытие информации о грани"));

        HelpAdapter helpAdapter = new HelpAdapter(getContext(), helpItems);

        ListView helpListView = rootView.findViewById(R.id.helpListView);
        helpListView.setAdapter(helpAdapter);
    }
}
