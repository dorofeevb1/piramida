package su.panfilov.piramida.components;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import su.panfilov.piramida.DiaryFragment;
import su.panfilov.piramida.HelpFragment;
import su.panfilov.piramida.MainActivity;
import su.panfilov.piramida.PlayFragment;
import su.panfilov.piramida.R;
import su.panfilov.piramida.models.Diary;
import su.panfilov.piramida.models.DiaryShort;
import su.panfilov.piramida.models.HelpItem;

public class DiariesAdapter extends BaseAdapter {

    private static final String TAG = "DiariesAdapter";

    private DiaryFragment fragment;
    public ArrayList<DiaryShort> data;

    public DiariesAdapter(DiaryFragment f, ArrayList<DiaryShort> d) {
        fragment = f;
        data = d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        DiaryItemHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = fragment.getLayoutInflater();
            convertView = inflater.inflate(R.layout.diary_list_item, parent, false);

            holder = new DiaryItemHolder();
            holder.title = convertView.findViewById(R.id.diaryItemTitle);
            holder.viewButton = convertView.findViewById(R.id.viewDiaryButton);
            holder.deleteButton = convertView.findViewById(R.id.removeDiaryButton);

            convertView.setTag(holder);
        } else {
            holder = (DiaryItemHolder) convertView.getTag();
        }

        String diaryId = data.get(position).id;
        final Diary diary = Diary.readDiaryFromCache(fragment.requireContext().getApplicationContext(), diaryId);

        Log.d(TAG, "getView: " + holder.title);

        holder.title.setText(diary.title);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment.playFragmentPresented) {
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
                builder.setTitle("Изменение записи");

                final EditText input = new EditText(fragment.getContext());
                input.setText(diary.title);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        diary.title = input.getText().toString();
                        diary.saveState(fragment.requireContext().getApplicationContext());
                        data.get(position).title = diary.title;
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment.playFragmentPresented) {
                    return;
                }

                try {
                    fragment.playFragment = PlayFragment.newInstance();
                    if (fragment.playFragment != null) {
                        fragment.playFragmentPresented = true;
                        fragment.playFragment.diary = diary;
                        FragmentTransaction ft = fragment.getActivity().getSupportFragmentManager().beginTransaction();
                        FrameLayout playLayout = fragment.getActivity().findViewById(R.id.playLayout);
                        playLayout.setVisibility(View.VISIBLE);
                        ft.add(R.id.playLayout, fragment.playFragment, fragment.playFragment.getTag());
                        ft.commit();
                    }
                } catch (NullPointerException e) {
                    // Handle exception
                }
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment.playFragmentPresented) {
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
                builder.setTitle("Удалить запись?");

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        diary.delete(fragment.requireContext().getApplicationContext());
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return convertView;
    }

    static class DiaryItemHolder {
        public TextView title;
        public ImageButton viewButton;
        public ImageButton deleteButton;
    }
}
