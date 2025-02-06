package su.panfilov.piramida.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import su.panfilov.piramida.R;

public class ContentsItemAdapter extends ArrayAdapter<String> {

    private List<Boolean> checkBoxStates;

    public ContentsItemAdapter(Context context, List<String> items) {
        super(context, 0, items);
        checkBoxStates = new ArrayList<>(Collections.nCopies(items.size(), false));
    }

    public ContentsItemAdapter(Context context, List<String> items, List<Boolean> initialCheckBoxStates) {
        super(context, 0, items);
        checkBoxStates = new ArrayList<>(initialCheckBoxStates);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contents_list_item, parent, false);
        }

        String item = getItem(position);
        boolean isChecked = checkBoxStates.get(position);

        TextView textView = convertView.findViewById(R.id.contentsItemTitle);
        ImageView checkBox = convertView.findViewById(R.id.contentsItemCheckBox);

        if (textView != null) {
            textView.setText(item);
        }

        if (checkBox != null) {
            // Устанавливаем изображение в зависимости от состояния чекбокса
            checkBox.setImageResource(isChecked ? R.drawable.ic_checkbox_checked : R.drawable.ic_checkbox_unchecked);

            checkBox.setOnClickListener(v -> {
                boolean newState = !checkBoxStates.get(position);
                checkBoxStates.set(position, newState);

                // Обновляем изображение чекбокса после изменения состояния
                checkBox.setImageResource(newState ? R.drawable.ic_checkbox_checked : R.drawable.ic_checkbox_unchecked);

                notifyDataSetChanged();
            });
        }

        return convertView;
    }

    public List<Boolean> getCheckBoxStates() {
        return checkBoxStates;
    }
}
