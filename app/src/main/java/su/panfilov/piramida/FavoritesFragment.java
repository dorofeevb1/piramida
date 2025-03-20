package su.panfilov.piramida;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import su.panfilov.piramida.components.ContentsAdapter;
import su.panfilov.piramida.components.ContentsItemAdapter;
import su.panfilov.piramida.models.PyramidsDataSource;

public class FavoritesFragment extends Fragment {

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contents, container, false);

        PyramidsDataSource pyramidsDataSource = new PyramidsDataSource();
        ContentsAdapter adapter = new ContentsAdapter(rootView.getContext());
        applyFavoritesFilter(adapter, pyramidsDataSource);

        ListView contentsListView = rootView.findViewById(R.id.contentsListView);
        contentsListView.setAdapter(adapter);

        ImageView filterImageView = rootView.findViewById(R.id.filterImageView);
        filterImageView.setImageResource(R.drawable.lock_all_kutalog);
        filterImageView.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.rootLayout, ContentsFragment.newInstance());
            ft.addToBackStack(null);
            ft.commit();
        });

        return rootView;
    }

    private void applyFavoritesFilter(ContentsAdapter adapter, PyramidsDataSource pyramidsDataSource) {
        adapter.clearSections();
        ArrayList<String> favoriteItems = new ArrayList<>();
        ArrayList<Boolean> favoriteStates = new ArrayList<>();
        SharedPreferences prefs = requireContext().getSharedPreferences("FavoritesPrefs", Context.MODE_PRIVATE);

        // Цикл от 0 до length-1, так как getTitleForHead использует индексы с 0
        for (int i = 0; i < pyramidsDataSource.getSetOfTitles().length; i++) {
            String title = pyramidsDataSource.getTitleForHead(i);
            boolean isFavorite = prefs.getBoolean("favorite_" + title, false);
            if (isFavorite) {
                favoriteItems.add(title);
                favoriteStates.add(true);
            }
        }

        if (!favoriteItems.isEmpty()) {
            ContentsItemAdapter itemAdapter = new ContentsItemAdapter(getContext(), favoriteItems, favoriteStates);
            adapter.addSection("Избранное", itemAdapter);
        } else {
            ArrayList<String> emptyList = new ArrayList<>();
            emptyList.add("Нет избранных граней");
            ContentsItemAdapter emptyAdapter = new ContentsItemAdapter(getContext(), emptyList);
            adapter.addSection("Избранное", emptyAdapter);
        }
        adapter.notifyDataSetChanged();
    }
}