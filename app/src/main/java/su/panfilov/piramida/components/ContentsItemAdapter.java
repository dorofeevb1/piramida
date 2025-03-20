package su.panfilov.piramida.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import su.panfilov.piramida.R;

public class ContentsItemAdapter extends ArrayAdapter<String> {

    private List<Boolean> checkBoxStates;
    private SharedPreferences prefs;

    public ContentsItemAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
        checkBoxStates = new ArrayList<>(Collections.nCopies(items.size(), false));
        prefs = context.getSharedPreferences("FavoritesPrefs", Context.MODE_PRIVATE);
        loadFavorites();
    }

    public ContentsItemAdapter(Context context, ArrayList<String> items, ArrayList<Boolean> states) {
        super(context, 0, items);
        checkBoxStates = new ArrayList<>(states);
        prefs = context.getSharedPreferences("FavoritesPrefs", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contents_list_item, parent, false);
        }

        ImageView checkBox = convertView.findViewById(R.id.checkboxImageView);
        checkBox.setImageResource(checkBoxStates.get(position) ? R.drawable.ic_checkbox_checked : R.drawable.ic_checkbox_unchecked);
        checkBox.setOnClickListener(v -> {
            boolean newState = !checkBoxStates.get(position);
            checkBoxStates.set(position, newState);
            checkBox.setImageResource(newState ? R.drawable.ic_checkbox_checked : R.drawable.ic_checkbox_unchecked);
            saveFavorite(position, newState);
            notifyDataSetChanged();
        });

        TextView title = convertView.findViewById(R.id.textView);
        title.setText(getItem(position));

        return convertView;
    }

    public List<Boolean> getCheckBoxStates() {
        return checkBoxStates;
    }

    private void saveFavorite(int position, boolean state) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("favorite_" + getItem(position), state);
        editor.apply();
    }

    private void loadFavorites() {
        for (int i = 0; i < getCount(); i++) {
            boolean state = prefs.getBoolean("favorite_" + getItem(i), false);
            checkBoxStates.set(i, state);
        }
    }
}