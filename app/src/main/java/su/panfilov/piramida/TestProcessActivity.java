package su.panfilov.piramida;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class TestProcessActivity extends AppCompatActivity {
    private int facetsCount;
    private int currentFacet = 0;
    private ProgressBar progressBar;
    private TextView textFacet;
    private ArrayList<String[]> facetsList; // Список граней

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_process);

        facetsCount = getIntent().getIntExtra("FACETS_COUNT", 4);
        progressBar = findViewById(R.id.progressBar);
        textFacet = findViewById(R.id.textFacet);
        Button btnAbort = findViewById(R.id.btnAbort);

        facetsList = loadFacets(); // Загрузка граней

        updateFacet();

        btnAbort.setOnClickListener(v -> showAbortDialog());
    }

    private ArrayList<String[]> loadFacets() {
        ArrayList<String[]> facets = new ArrayList<>();
        facets.add(new String[]{"Герой", "Атлет", "Боец", "Тактик", "Воин", "Вождь", "Мистик"});
        facets.add(new String[]{"Влияние", "Тело", "Закон", "Финансы", "Технология", "Коммуникация", "Информация"});
        return facets;
    }

    private void updateFacet() {
        if (currentFacet >= facetsCount) {
            Intent intent = new Intent(this, TestResultsActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        String[] facetWords = facetsList.get(new Random().nextInt(facetsList.size()));
        textFacet.setText(String.join(", ", facetWords));

        progressBar.setProgress((currentFacet + 1) * 100 / facetsCount);
        currentFacet++;
    }

    private void showAbortDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Прервать тест?")
                .setMessage("Результаты прохождения теста будут утеряны.")
                .setPositiveButton("Прервать", (dialog, which) -> finish())
                .setNegativeButton("Отмена", null)
                .show();
    }
}
