package su.panfilov.piramida;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AboutTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_test);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnMainScreen = findViewById(R.id.btnMainScreen);

        btnBack.setOnClickListener(v -> finish());
        btnMainScreen.setOnClickListener(v -> startActivity(new Intent(this, TestActivity.class)));
    }
}
