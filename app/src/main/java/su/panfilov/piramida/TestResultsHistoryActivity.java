package su.panfilov.piramida;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class TestResultsHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results_history);

        ListView resultsListView = findViewById(R.id.resultsListView);
        Button btnBack = findViewById(R.id.btnBack);

        // Example data
        List<String> testResults = new ArrayList<>();
        testResults.add("1. 17.07.2024 Деньги");
        testResults.add("2. 18.07.2024 Здоровье");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, testResults);
        resultsListView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
    }
}
