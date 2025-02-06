package su.panfilov.piramida;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;

import su.panfilov.piramida.components.PyramidView;
import su.panfilov.piramida.models.Diary;
import su.panfilov.piramida.models.SaveTranslation;

public class PyramidFragment extends Fragment {

    private static final String TAG = "PyramidFragment";

    View rootView;

    public boolean newRecord = true;
    private boolean savingOn = false; // Флаг записи

    public PyramidView piramidaView;
    public ImageButton recordButton;
    public Button doneButton;

    private Diary diary;

    public static PyramidFragment newInstance() {
        PyramidFragment fragment = new PyramidFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pyramid, container, false);

        piramidaView = rootView.findViewById(R.id.pyramid);
        recordButton = rootView.findViewById(R.id.recordButton);
        doneButton = rootView.findViewById(R.id.doneButton);
        setSavingOn(false);
        doneButton.setVisibility(View.INVISIBLE);

        return rootView;
    }

    public void setSavingOn(boolean savingOn) {
        this.savingOn = savingOn;
        piramidaView.savingOn = savingOn;
        if (savingOn) {
            // Нажата кнопка записи
            recordButton.setImageDrawable(requireContext().getDrawable(R.drawable.stop));
            doneButton.setVisibility(View.INVISIBLE);
        } else {
            // Нажата кнопка стоп
            recordButton.setImageDrawable(requireContext().getDrawable(R.drawable.done));
            doneButton.setVisibility(View.INVISIBLE);
        }
    }

    public boolean getSavingOn() {
        return savingOn;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
        piramidaView.saveTranslation.diary = diary;
    }

    public Diary getDiary() {
        return diary;
    }

    public void recordTapped(View view) {
        setSavingOn(!savingOn);

        Log.d(TAG, "recordTapped: " + getSavingOn());

        if (savingOn) {
            if (newRecord) {
                // Добавить новую запись
                setDiary(new Diary());
                diary.title = "Новая запись";
                diary.timeStamp = new Date();
                diary.numberOfPiramida = SaveTranslation.numberOfPiramidas;

                diary.saveState(requireContext().getApplicationContext());

                newRecord = false;
            }

            piramidaView.saveTranslation.startSaving(piramidaView.piramidaDataSource.sideByLayer, piramidaView.piramidaIsLocked);
        } else {
            piramidaView.saveTranslation.stopSaving();
        }
    }

    public void doneTapped(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.saving));

        final EditText input = new EditText(requireContext());
        input.setText(getString(R.string.new_record));
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newRecord = true;
                diary.title = input.getText().toString();
                diary.saveState(requireContext().getApplicationContext());
                diary = null;
            }
        });
        builder.setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newRecord = true;
                diary.delete(requireContext().getApplicationContext());
                setDiary(null);
                dialog.cancel();
            }
        });

        builder.show();
        doneButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (diary != null) {
            diary.delete(requireContext().getApplicationContext());
        }
    }
}
