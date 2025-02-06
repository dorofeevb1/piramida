package su.panfilov.piramida;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import su.panfilov.piramida.components.DiariesAdapter;
import su.panfilov.piramida.components.HelpAdapter;
import su.panfilov.piramida.models.Diary;
import su.panfilov.piramida.models.DiaryShort;
import su.panfilov.piramida.models.HelpItem;

public class DiaryFragment extends Fragment {

    public PlayFragment playFragment;
    public boolean playFragmentPresented = false;
    private View rootView;

    public static DiaryFragment newInstance() {
        DiaryFragment fragment = new DiaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_diary, container, false);

        initListView(rootView);

        return rootView;
    }

    private void initListView(View rootView) {
        Map<String, String> diaryTitlesMap = Diary.readDiariesTitlesFromCache(requireContext().getApplicationContext());

        ArrayList<DiaryShort> diaryItems = new ArrayList<>(0);
        for (Map.Entry<String, String> entry : diaryTitlesMap.entrySet()) {
            DiaryShort diaryShort = new DiaryShort();
            diaryShort.id = entry.getKey();
            diaryShort.title = entry.getValue();
            diaryItems.add(diaryShort);
        }

        DiariesAdapter diariesAdapter = new DiariesAdapter(this, diaryItems);

        ListView diaryListView = rootView.findViewById(R.id.diaryListView);
        diaryListView.setAdapter(diariesAdapter);
    }

    public void playTapped(View view) {
        if (playFragment == null) {
            return;
        }

        playFragment.playTapped(view);
    }

    public void backTapped(View view) {
        playFragmentPresented = false;
        try {
            if (playFragment != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                FrameLayout playLayout = getActivity().findViewById(R.id.playLayout);
                playLayout.setVisibility(View.INVISIBLE);
                ft.remove(playFragment);
                ft.commit();
                playFragment = null;
            }
        } catch (NullPointerException e) {
            // Handle exception
        }

        if (playFragment == null) {
            return;
        }

        playFragment.backTapped(view);
    }
}
