package su.panfilov.piramida.components;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import su.panfilov.piramida.R;
import su.panfilov.piramida.TestActivity;

public class AboutTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_test);


        // Найти TextView
        TextView aboutTestText = findViewById(R.id.aboutTestText);

        // Установить текст
        aboutTestText.setText("Пройдите тест, чтобы определить на каком уровне вы находитесь, " +
                "относительно актуального для вас запроса. В качестве запроса можно взять: " +
                "цель в будущем, ситуацию в прошлом, текущее состояние в проекте, " +
                "состояние жизни в целом, настрой на день и т.д. Выбор запроса ничем не ограничен. " +
                "Всё в ваших руках.\n\n" +
                "Пройдя тест, вы сможете обрести дополнительные точки опоры, " +
                "структурировать представления о запросе, принять новые решения " +
                "для достижения желаемых изменений в настоящем и будущем.\n\n" +
                "Тест включает в себя 4 режима тестирования:\n" +
                "• День (4 грани, 2-4 минуты)\n" +
                "• Месяц (12 граней, 6-12 минут)\n" +
                "• Год (20 граней, 10-20 минут)\n" +
                "• Десятилетие (54 грани, 26-54 минуты)\n\n" +
                "Количество граней равно количеству вопросов, в каждом из которых вам " +
                "нужно будет выбрать 1 слово, лучше других отражающее состояние дел на данный момент. " +
                "Слова внутри каждой грани будут специально перемешаны, чтобы вам было легче " +
                "сфокусироваться именно на самих словах, а не стараться выбрать ваш «любимый» номер уровня грани.");

        // Найти кнопки
        Button btnBack = findViewById(R.id.btnBack);
        Button btnMainScreen = findViewById(R.id.btnMainScreen);

        // Кнопка "Назад" - просто закрывает текущую активность
        btnBack.setOnClickListener(v -> finish());

        // Кнопка "На основной экран" - открывает `TestActivity` и очищает стек активности
        btnMainScreen.setOnClickListener(v -> {
            Intent intent = new Intent(AboutTestActivity.this, TestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}