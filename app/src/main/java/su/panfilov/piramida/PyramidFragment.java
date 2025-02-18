package su.panfilov.piramida;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Arrays;

import su.panfilov.piramida.components.PyramidView;
import su.panfilov.piramida.models.Diary;

public class PyramidFragment extends Fragment {

    private static final String TAG = "PyramidFragment";

    View rootView;

    public boolean newRecord = true;
    private boolean savingOn = false;

    public PyramidView pyramidView;
    public ImageButton toggleShapeButton;
    public ImageButton toggleFavoritesButton;
    public ImageButton likeButton;
    public ImageButton linkButton;
    public ImageButton apButton;
    public LinearLayout wealthOrdersContainer;

    private Diary diary;

    private boolean isWealthOrdersVisible = true; // Track the current mode

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

        pyramidView = rootView.findViewById(R.id.pyramid);
        toggleShapeButton = rootView.findViewById(R.id.toggleShapeButton);
        toggleFavoritesButton = rootView.findViewById(R.id.toggleFavoritesButton);
        likeButton = rootView.findViewById(R.id.likeButton);
        linkButton = rootView.findViewById(R.id.linkButton);
        apButton = rootView.findViewById(R.id.apButton);
        wealthOrdersContainer = rootView.findViewById(R.id.wealthOrdersContainer);

        setSavingOn(false);

        toggleShapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShape(v);
            }
        });

        apButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWealthOrders();
            }
        });

        return rootView;
    }

    public void setSavingOn(boolean savingOn) {
        this.savingOn = savingOn;
        pyramidView.savingOn = savingOn;
    }

    public boolean getSavingOn() {
        return savingOn;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
        pyramidView.saveTranslation.diary = diary;
    }

    public Diary getDiary() {
        return diary;
    }

    public void toggleShape(View view) {
        if (pyramidView != null) {
            pyramidView.toggleShape();
            if (pyramidView.isTriangle()) {
                toggleShapeButton.setImageResource(R.drawable.square);
            } else {
                toggleShapeButton.setImageResource(R.drawable.triangle);
            }
        }
    }

    public void toggleFavorites(View view) {
        // Logic for toggling favorites
    }

    public void likeTapped(View view) {
        // Logic for like button
    }

    public void toggleWealthOrders() {
        Log.d(TAG, "toggleWealthOrders called");
        if (isWealthOrdersVisible) {
            Log.d(TAG, "Switching to time units");
            wealthOrdersContainer.setVisibility(View.VISIBLE);
            updateLabelsForTimeUnits();
        } else {
            Log.d(TAG, "Switching to wealth orders");
            wealthOrdersContainer.setVisibility(View.VISIBLE);
            updateLabelsForWealthOrders();
        }
        isWealthOrdersVisible = !isWealthOrdersVisible;
        Log.d(TAG, "wealthOrdersContainer visibility: " + (wealthOrdersContainer.getVisibility() == View.VISIBLE ? "VISIBLE" : "GONE"));
    }

    private void updateLabelsForWealthOrders() {
        String[] wealthOrders = {
                "1000 000 000",
                "1 000 000",
                "100 000",
                "10 000",
                "1000",
                "100",
                "10"
        };
        updateLabels(wealthOrders);
    }

    private void updateLabelsForTimeUnits() {
        String[] timeUnits = {
                "10 лет",
                "Год",
                "Месяц",
                "День",
                "Час",
                "Минута",
                "Секунда" // Added 7th element
        };
        updateLabels(timeUnits);
    }

    private void updateLabels(String[] labels) {
        Log.d(TAG, "Updating labels with: " + Arrays.toString(labels));
        TextView wealthOrder1 = rootView.findViewById(R.id.wealthOrder1);
        TextView wealthOrder2 = rootView.findViewById(R.id.wealthOrder2);
        TextView wealthOrder3 = rootView.findViewById(R.id.wealthOrder3);
        TextView wealthOrder4 = rootView.findViewById(R.id.wealthOrder4);
        TextView wealthOrder5 = rootView.findViewById(R.id.wealthOrder5);
        TextView wealthOrder6 = rootView.findViewById(R.id.wealthOrder6);
        TextView wealthOrder7 = rootView.findViewById(R.id.wealthOrder7);

        wealthOrder1.setText(labels[0]);
        wealthOrder2.setText(labels[1]);
        wealthOrder3.setText(labels[2]);
        wealthOrder4.setText(labels[3]);
        wealthOrder5.setText(labels[4]);
        wealthOrder6.setText(labels[5]);
        wealthOrder7.setText(labels[6]);

        Log.d(TAG, "Labels updated successfully");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (diary != null) {
            diary.delete(requireContext().getApplicationContext());
        }
    }
}
