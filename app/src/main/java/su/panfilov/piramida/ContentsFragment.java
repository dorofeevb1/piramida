package su.panfilov.piramida;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import su.panfilov.piramida.components.ContentsAdapter;
import su.panfilov.piramida.components.ContentsItemAdapter;
import su.panfilov.piramida.models.PyramidsDataSource;

public class ContentsFragment extends Fragment {

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
            for (int j = i * 6; j < i * 6 + 6; j++) {
                sectionItems.add(pyramidsDataSource.getTitleForHead(j + 1));
            }
            ContentsItemAdapter itemAdapter = new ContentsItemAdapter(rootView.getContext(), sectionItems);
            adapter.addSection(pyramidsDataSource.getNameOfPiramids()[i], itemAdapter);
        }

        // Get a reference to the ListView holder
        ListView contentsListView = rootView.findViewById(R.id.contentsListView);

        // Set the adapter on the ListView holder
        contentsListView.setAdapter(adapter);

        return rootView;
    }
}
