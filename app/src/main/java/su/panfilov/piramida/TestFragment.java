package su.panfilov.piramida;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TestFragment extends Fragment {

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        Button btnDay = view.findViewById(R.id.btnDay);
        Button btnMonth = view.findViewById(R.id.btnMonth);
        Button btnYear = view.findViewById(R.id.btnYear);
        Button btnDecade = view.findViewById(R.id.btnDecade);
        Button btnResults = view.findViewById(R.id.btnResults);
        Button btnAboutTest = view.findViewById(R.id.btnAboutTest);

        btnDay.setOnClickListener(v -> startTest(4));
        btnMonth.setOnClickListener(v -> startTest(12));
        btnYear.setOnClickListener(v -> startTest(20));
        btnDecade.setOnClickListener(v -> startTest(54));

        btnResults.setOnClickListener(v -> openResults());
        btnAboutTest.setOnClickListener(v -> openAboutTest());

        return view;
    }

    private void startTest(int facets) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), TestProcessActivity.class);
            intent.putExtra("FACETS_COUNT", facets);
            startActivity(intent);
        }
    }

    private void openResults() {
        if (getActivity() != null) {
            startActivity(new Intent(getActivity(), TestResultsActivity.class));
        }
    }

    private void openAboutTest() {
        if (getActivity() != null) {
            startActivity(new Intent(getActivity(), AboutTestActivity.class));
        }
    }
}
