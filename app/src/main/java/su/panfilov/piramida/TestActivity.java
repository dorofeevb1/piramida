package su.panfilov.piramida;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button btnDay = findViewById(R.id.btnDay);
        Button btnMonth = findViewById(R.id.btnMonth);
        Button btnYear = findViewById(R.id.btnYear);
        Button btnDecade = findViewById(R.id.btnDecade);
        Button btnResults = findViewById(R.id.btnResults);
        Button btnAboutTest = findViewById(R.id.btnAboutTest);

        btnDay.setOnClickListener(v -> startTest(4));
        btnMonth.setOnClickListener(v -> startTest(12));
        btnYear.setOnClickListener(v -> startTest(20));
        btnDecade.setOnClickListener(v -> startTest(54));
        btnResults.setOnClickListener(v -> openResults());
        btnAboutTest.setOnClickListener(v -> openAboutTest());
    }

    private void startTest(int facets) {
        Intent intent = new Intent(this, TestProcessActivity.class);
        intent.putExtra("FACETS_COUNT", facets);
        startActivity(intent);
    }

    private void openResults() {
        startActivity(new Intent(this, TestResultsHistoryActivity.class));
    }

    private void openAboutTest() {
        startActivity(new Intent(this, AboutTestActivity.class));
    }
}
