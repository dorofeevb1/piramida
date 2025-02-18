package su.panfilov.piramida;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize the ListView with settings options
        initListView(rootView);

        return rootView;
    }

    private void initListView(View rootView) {
        // Create a list of settings options
        List<String> settingsOptions = new ArrayList<>();
        settingsOptions.add("Включение/Выключение звука");

        // Get the ListView from the layout
        ListView settingsListView = rootView.findViewById(R.id.settingsTab);

        if (settingsListView != null) {
            // Create a custom adapter to populate the ListView
            SettingsAdapter adapter = new SettingsAdapter(requireContext(), settingsOptions);

            // Set the adapter to the ListView
            settingsListView.setAdapter(adapter);

            // Set item click listener for the ListView
            settingsListView.setOnItemClickListener((parent, view, position, id) -> {
                String selectedItem = settingsOptions.get(position);
                // Handle item click based on the selected item
                switch (selectedItem) {
                    case "Включение/Выключение звука":
                        // Handle the sound toggle option
                        break;
                    // Add more cases as needed
                }
            });
        } else {
            // Log an error message if the ListView is null
            Log.e("SettingsFragment", "ListView is null");
        }
    }

}
