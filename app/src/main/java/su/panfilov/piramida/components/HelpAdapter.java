package su.panfilov.piramida.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import su.panfilov.piramida.R;
import su.panfilov.piramida.models.HelpItem;

public class HelpAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HelpItem> data;

    public HelpAdapter(Context context, ArrayList<HelpItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.help_list_item, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.helpItemTitle);
            holder.subtitle = convertView.findViewById(R.id.helpItemSubtitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HelpItem helpItem = data.get(position);

        holder.title.setText(helpItem.title);
        holder.subtitle.setText(helpItem.subtitle);

        // Если нет подзаголовка, оставляем цветной заголовок
        if (helpItem.subtitle.isEmpty()) {
            holder.title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.title.setTextSize(18);
            holder.subtitle.setVisibility(View.GONE);
        } else {
            // Подзаголовки (описания действий) делаем черными
            holder.title.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.title.setTextSize(16);
            holder.subtitle.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.subtitle.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView subtitle;
    }
}
