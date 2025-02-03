package su.panfilov.piramida.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import su.panfilov.piramida.R;

public class ContentsItemAdapter extends ArrayAdapter<String> {

    public ContentsItemAdapter(Context context, List<String> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contents_list_item, parent, false);
        }

        String item = getItem(position);

        TextView textView = convertView.findViewById(R.id.contentsItemTitle);
        CheckBox checkBox = convertView.findViewById(R.id.contentsItemCheckBox);

        if (textView != null) {
            textView.setText(item);
        }

        if (checkBox != null) {
            // Set any initial state for the checkbox if needed
            checkBox.setChecked(false);
        }

        return convertView;
    }
}