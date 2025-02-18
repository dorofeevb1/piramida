
package su.panfilov.piramida;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SettingsAdapter extends ArrayAdapter<String> {

    public SettingsAdapter(Context context, List<String> settingsOptions) {
        super(context, 0, settingsOptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String settingOption = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_settings, parent, false);
        }

        // Lookup view for data population
        ImageView icon = convertView.findViewById(R.id.icon);
        TextView text = convertView.findViewById(R.id.text);

        // Populate the data into the template view using the data object
        text.setText(settingOption);

        // Set the icon based on the setting option
        if (settingOption.equals("Включение/Выключение звука")) {
            icon.setImageResource(R.drawable.ic_sound); // Replace with your icon drawable
        } else {
            icon.setVisibility(View.GONE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
