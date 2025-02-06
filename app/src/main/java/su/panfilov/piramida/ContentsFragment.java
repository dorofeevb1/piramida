package su.panfilov.piramida;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import su.panfilov.piramida.components.ContentsAdapter;
import su.panfilov.piramida.components.ContentsItemAdapter;
import su.panfilov.piramida.models.PyramidsDataSource;

public class ContentsFragment extends Fragment {

    private boolean isFirstImage = true; // Track the current image state

    public static ContentsFragment newInstance() {
        return new ContentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contents, container, false);

        PyramidsDataSource pyramidsDataSource = new PyramidsDataSource();

        // Create the ListView Adapter
        ContentsAdapter adapter = new ContentsAdapter(rootView.getContext());

        // Add Sections
        for (int i = 0; i < pyramidsDataSource.getNameOfPiramids().length; i++) {
            ArrayList<String> sectionItems = new ArrayList<>();
            for (int j = i * 6; j < i * 6 + 6 && j < pyramidsDataSource.getSetOfTitles().length; j++) {
                sectionItems.add(pyramidsDataSource.getTitleForHead(j + 1));
            }
            ContentsItemAdapter itemAdapter = new ContentsItemAdapter(rootView.getContext(), sectionItems);
            adapter.addSection(pyramidsDataSource.getNameOfPiramids()[i], itemAdapter);
        }

        // Get a reference to the ListView holder
        ListView contentsListView = rootView.findViewById(R.id.contentsListView);

        // Set the adapter on the ListView holder
        contentsListView.setAdapter(adapter);

        // Find the ImageView by its ID
        ImageView filterImageView = rootView.findViewById(R.id.filterImageView);

        // Set an OnClickListener on the ImageView
        filterImageView.setOnClickListener(v -> {
            // Toggle the image
            if (isFirstImage) {
                filterImageView.setImageResource(R.drawable.lock_all_kutalog); // Change to the second image

                // Reset the ListView to show the full catalog
                resetToCatalogView(contentsListView, adapter, pyramidsDataSource);
            } else {
                filterImageView.setImageResource(R.drawable.unlock_all_kutalog); // Change back to the first image

                // Apply filter functionality
                applyFilter(contentsListView, adapter, pyramidsDataSource);
            }
            isFirstImage = !isFirstImage; // Toggle the state
        });

        return rootView;
    }

    private void resetToCatalogView(ListView contentsListView, ContentsAdapter adapter, PyramidsDataSource pyramidsDataSource) {
        // Clear existing sections
        adapter.clearSections();

        // Re-add all sections
        for (int i = 0; i < pyramidsDataSource.getNameOfPiramids().length; i++) {
            ArrayList<String> sectionItems = new ArrayList<>();
            for (int j = i * 6; j < i * 6 + 6 && j < pyramidsDataSource.getSetOfTitles().length; j++) {
                sectionItems.add(pyramidsDataSource.getTitleForHead(j + 1));
            }
            ContentsItemAdapter itemAdapter = new ContentsItemAdapter(getContext(), sectionItems);
            adapter.addSection(pyramidsDataSource.getNameOfPiramids()[i], itemAdapter);
        }

        // Notify the ListView that the data has changed
        adapter.notifyDataSetChanged();
    }

    private void applyFilter(ListView contentsListView, ContentsAdapter adapter, PyramidsDataSource pyramidsDataSource) {
        // Clear existing sections
        adapter.clearSections();

        // Apply filter logic
        List<String> filteredItems = new ArrayList<>();
        List<Boolean> filteredCheckBoxStates = new ArrayList<>();

        for (int i = 0; i < pyramidsDataSource.getSetOfTitles().length; i++) {
            // Ensure the adapter is not null
            ContentsItemAdapter itemAdapter = (ContentsItemAdapter) adapter.sections.get(pyramidsDataSource.getNameOfPiramids()[0]);
            if (itemAdapter != null) {
                List<Boolean> checkBoxStates = itemAdapter.getCheckBoxStates();
                if (i < checkBoxStates.size() && checkBoxStates.get(i)) {
                    filteredItems.add(pyramidsDataSource.getTitleForHead(i + 1));
                    filteredCheckBoxStates.add(true); // Set the checkbox state to "checked"
                }
            }
        }

        // Create a new adapter with filtered items
        ContentsItemAdapter filteredAdapter = new ContentsItemAdapter(getContext(), filteredItems, filteredCheckBoxStates);
        contentsListView.setAdapter(filteredAdapter);
    }
}
