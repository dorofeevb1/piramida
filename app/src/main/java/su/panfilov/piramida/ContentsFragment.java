package su.panfilov.piramida;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView; // Правильный импорт
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import su.panfilov.piramida.components.ContentsAdapter;
import su.panfilov.piramida.components.ContentsItemAdapter;
import su.panfilov.piramida.models.PyramidsDataSource;

public class ContentsFragment extends Fragment {

    private PyramidsDataSource pyramidsDataSource;
    private ContentsAdapter adapter;

    public static ContentsFragment newInstance() {
        return new ContentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contents, container, false);

        pyramidsDataSource = new PyramidsDataSource();
        adapter = new ContentsAdapter(rootView.getContext());
        resetToCatalogView(adapter, pyramidsDataSource);

        ListView contentsListView = rootView.findViewById(R.id.contentsListView);
        contentsListView.setAdapter(adapter);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.contents);

        ImageView filterImageView = rootView.findViewById(R.id.filterImageView);
        filterImageView.setImageResource(R.drawable.unlock_all_kutalog);
        filterImageView.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.rootLayout, FavoritesFragment.newInstance());
            ft.addToBackStack(null);
            ft.commit();
        });

        SearchView searchView = rootView.findViewById(R.id.searchView); // Строка 57
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterContents(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContents(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            resetToCatalogView(adapter, pyramidsDataSource);
            return false;
        });

        return rootView;
    }

    private void resetToCatalogView(ContentsAdapter adapter, PyramidsDataSource pyramidsDataSource) {
        adapter.clearSections();
        for (int i = 0; i < pyramidsDataSource.getNameOfPiramids().length; i++) {
            ArrayList<String> sectionItems = new ArrayList<>();
            for (int j = i * 6; j < i * 6 + 6 && j < pyramidsDataSource.getSetOfTitles().length; j++) {
                sectionItems.add(pyramidsDataSource.getTitleForHead(j));
            }
            ContentsItemAdapter itemAdapter = new ContentsItemAdapter(getContext(), sectionItems);
            adapter.addSection(pyramidsDataSource.getNameOfPiramids()[i], itemAdapter);
        }
        adapter.notifyDataSetChanged();
    }

    private void filterContents(String query) {
        adapter.clearSections();
        String[][] setOfTitles = pyramidsDataSource.getSetOfTitles();
        query = query.toLowerCase().trim();

        if (query.isEmpty()) {
            resetToCatalogView(adapter, pyramidsDataSource);
            return;
        }

        for (int i = 0; i < pyramidsDataSource.getNameOfPiramids().length; i++) {
            ArrayList<String> sectionItems = new ArrayList<>();
            for (int j = i * 6; j < i * 6 + 6 && j < setOfTitles.length; j++) {
                String title = setOfTitles[j][0];
                if (title.toLowerCase().contains(query)) {
                    sectionItems.add(title);
                }
            }
            if (!sectionItems.isEmpty()) {
                ContentsItemAdapter itemAdapter = new ContentsItemAdapter(getContext(), sectionItems);
                adapter.addSection(pyramidsDataSource.getNameOfPiramids()[i], itemAdapter);
            }
        }

        if (adapter.getSectionCount() == 0) {
            ArrayList<String> emptyList = new ArrayList<>();
            emptyList.add("Ничего не найдено");
            ContentsItemAdapter emptyAdapter = new ContentsItemAdapter(getContext(), emptyList);
            adapter.addSection("Результаты поиска", emptyAdapter);
        }

        adapter.notifyDataSetChanged();
    }
}