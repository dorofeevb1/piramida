package su.panfilov.piramida;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import su.panfilov.piramida.components.HelpAdapter;
import su.panfilov.piramida.models.HelpItem;

public class HelpFragment extends Fragment {

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        initListView(rootView);

        return rootView;
    }

    private void initListView(View rootView) {
        String[] titles = getResources().getStringArray(R.array.help_titles);
        String[] subtitles = getResources().getStringArray(R.array.help_subtitles);

        ArrayList<HelpItem> helpItems = new ArrayList<>(0);

        for (int i = 0; i < titles.length; i++) {
            if (!(titles.length > i || subtitles.length > i)) {
                continue;
            }

            HelpItem helpItem = new HelpItem("", "");

            if (titles.length > i) {
                helpItem.title = titles[i];
            }
            if (subtitles.length > i) {
                helpItem.subtitle = subtitles[i];
            }

            helpItems.add(helpItem);
        }

        HelpAdapter helpAdapter = new HelpAdapter(this, helpItems);

        ListView helpListView = rootView.findViewById(R.id.helpListView);
        helpListView.setAdapter(helpAdapter);
    }
}
