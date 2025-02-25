package su.panfilov.piramida;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;

public class TestResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results);

        TextView resultsTextView = findViewById(R.id.resultsTextView);
        TextView btnClose = findViewById(R.id.btnClose);

        ArrayList<String> selectedFacets = getIntent().getStringArrayListExtra("SELECTED_FACETS");

        // Static data to display when no results are available
        ArrayList<String> staticData = new ArrayList<>(Arrays.asList(
                "Финансовая стабильность",
                "Инвестиции",
                "Доходы",
                "Расходы",
                "Бюджетирование",
                "Сбережения",
                "Мистик"
        ));


        // Build the result string with HTML formatting
        StringBuilder results = new StringBuilder();
        results.append("<b>Результаты теста</b><br/>");
        results.append("Тема: Деньги<br/><br/>");
        results.append("Вы уровень: <b>51%</b><br/><br/>");
        results.append("<b>Индивидуализация</b><br/>");

        // Use selectedFacets if available, otherwise use staticData
        ArrayList<String> facetsToDisplay = (selectedFacets != null && !selectedFacets.isEmpty()) ? selectedFacets : staticData;

        for (String facet : facetsToDisplay) {
            results.append(facet).append("<br/>");
        }

        // Set the formatted text to the TextView
        resultsTextView.setText(Html.fromHtml(results.toString()));

        btnClose.setOnClickListener(v -> finish());
    }
}
