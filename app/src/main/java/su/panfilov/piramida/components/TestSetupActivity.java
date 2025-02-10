package su.panfilov.piramida;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class TestSetupActivity extends AppCompatActivity {
    private int facetsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_setup);

        facetsCount = getIntent().getIntExtra("FACETS_COUNT", 4);

        EditText inputTheme = findViewById(R.id.inputTheme);
        Button btnStartTest = findViewById(R.id.btnStartTest);

        btnStartTest.setOnClickListener(v -> {
            String theme = inputTheme.getText().toString().trim();
            if (!theme.isEmpty()) {
                Intent intent = new Intent(TestSetupActivity.this, TestProcessActivity.class);
                intent.putExtra("FACETS_COUNT", facetsCount);
                intent.putExtra("THEME", theme);
                startActivity(intent);
            }
        });
    }
}
