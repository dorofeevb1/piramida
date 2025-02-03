package su.panfilov.piramida.components;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import su.panfilov.piramida.HelpFragment;
import su.panfilov.piramida.R;
import su.panfilov.piramida.models.HelpItem;


public class HelpAdapter extends BaseAdapter {

    private Fragment fragment;
    public ArrayList<HelpItem> data;


    public HelpAdapter(HelpFragment f, ArrayList<HelpItem> d) {

        fragment = f;
        data = d;

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        HelpItemHolder holder = null;

        if(convertView == null)
        {
            LayoutInflater inflater = fragment.getLayoutInflater();
            convertView = inflater.inflate(R.layout.help_list_item, parent, false);

            holder = new HelpItemHolder();
            holder.title = convertView.findViewById(R.id.helpItemTitle);
            holder.subtitle = convertView.findViewById(R.id.helpItemSubtitle);

            convertView.setTag(holder);
        }
        else
        {
            holder = (HelpItemHolder)convertView.getTag();
        }

        HelpItem helpItem = data.get(position);

        holder.title.setText(helpItem.title);
        holder.subtitle.setText(helpItem.subtitle);

        return convertView;
    }

    static class HelpItemHolder
    {
        public TextView title;
        public TextView subtitle;
    }
}