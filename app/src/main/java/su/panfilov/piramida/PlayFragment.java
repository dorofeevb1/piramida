package su.panfilov.piramida;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import su.panfilov.piramida.components.PyramidView;
import su.panfilov.piramida.models.Diary;
import su.panfilov.piramida.models.HistoryCommands;
import su.panfilov.piramida.models.SaveTranslation;


public class PlayFragment extends Fragment {

    private static final String TAG = "PlayFragment";

    View rootView;

    private boolean playingOn = false; // Флаг записи

    public PyramidView piramidaView;
    public ImageButton playButton;
    public ImageButton backButton;

    public SaveTranslation.PiramidaCommands command = SaveTranslation.PiramidaCommands.none;

    public Diary diary;
    int currentCommand = 0;

    public static PlayFragment newInstance() {
        PlayFragment fragment = new PlayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_play, container, false);

        piramidaView = rootView.findViewById(R.id.pyramidPlay);
        playButton = rootView.findViewById(R.id.playButton);
        setPlayingOn(false);

        piramidaView.userInteractive = false;
        //addObservers();

        initStartState();
        return rootView;
    }

    private void initStartState() {
        currentCommand = 0;
        int countOfCommands = 0;

        if (diary.historyCommands.size() > 0) {
            countOfCommands = diary.historyCommands.size();
        }

        if (countOfCommands >= 9) {
            for (int layer = 0; layer <= 7; layer++) {
                HistoryCommands command = diary.historyCommands.get(layer);
                piramidaView.piramidaDataSource.setLayer(command.layer, command.index);
            }
            HistoryCommands command = diary.historyCommands.get(8);
            piramidaView.lockViewInitialVisibility = command.layer != 0;
            currentCommand = 9;
        }

        if (piramidaView.layerViews.size() > 0) {
            piramidaView.updateAll();
        }
    }

    public void setPlayingOn(boolean playingOn) {
        this.playingOn = playingOn;
        if (playingOn) {
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        } else {
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.play));
        }
    }

    public boolean getPlayingOn() {
        return playingOn;
    }

    public Diary getDiary() {
        return diary;
    }

    public void playTapped(View view) {
        setPlayingOn(!playingOn);
        if (playingOn) {
            initStartState();
            nextCommand(currentCommand);
        }
    }

    public void backTapped(View view) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        setPlayingOn(false);
    }

    public void nextCommand(int indexOfCommand) {
        nextCommand(indexOfCommand, false);
    }

    public void nextCommand(int indexOfCommand, boolean animated) {
        if (!playingOn) {
            return;
        }

        int countOfCommands = diary.historyCommands.size();

        if (countOfCommands <= indexOfCommand) {
            setPlayingOn(false);
            return;
        }

        HistoryCommands command;
        try {
            command = diary.historyCommands.get(indexOfCommand);
        } catch (Exception e) {
            setPlayingOn(false);
            return;
        }

        if (command == null) {
            setPlayingOn(false);
            return;
        }

        int layer = command.layer;
        int index = command.index;
        TextView label = piramidaView.layerViews.get(layer).label;
        String newTitle;

        switch (SaveTranslation.PiramidaCommands.values()[command.command]) {
            case setLayer:
                piramidaView.piramidaDataSource.setLayer(layer, index);
                newTitle = piramidaView.piramidaDataSource.getTitle(layer);
                label.setText(newTitle);
                break;
            case leftTurn:
                piramidaView.layerViews.get(layer).playSwipe();
                newTitle = piramidaView.piramidaDataSource.leftTurn(layer);
                piramidaView.layerViews.get(layer).turnLeft(newTitle);
                break;
            case rightTurn:
                piramidaView.layerViews.get(layer).playSwipe();
                newTitle = piramidaView.piramidaDataSource.rightTurn(layer);
                piramidaView.layerViews.get(layer).turnRight(newTitle);
                break;
            case allLeftTurn:
                String[] newTitles = piramidaView.piramidaDataSource.allLeftTurn();

                for (int layerNum = 0; layerNum <= 7; layerNum++) {
                    if (layerNum == 0) {
                        piramidaView.layerViews.get(layerNum).playSwipe();
                    }

                    piramidaView.layerViews.get(layerNum).turnLeft(newTitles[layerNum]);
                }
                break;

            case allRightTurn:
                String[] newTitles2 = piramidaView.piramidaDataSource.allRightTurn();

                for (int layerNum = 0; layerNum <= 7; layerNum++) {
                    if (layerNum == 0) {
                        piramidaView.layerViews.get(layerNum).playSwipe();
                    }

                    piramidaView.layerViews.get(layerNum).turnRight(newTitles2[layerNum]);
                }
                break;

            case setLockPiramida:
                if (index == 1) {
//                    audioPlayer.stop();
//                    audioPlayer.play();
                }
                piramidaView.lockView.setVisibility(layer == 0 ? View.INVISIBLE : View.VISIBLE);
                break;
            case setOneSide:
                piramidaView.piramidaDataSource.setOneSide(layer);

                for (int layerIn = 0; layerIn <= 7; layerIn++) {
                    if (layerIn == layer) {
                        // Можно сделать анимацию увеличения-уменьшения масштабирования
                        piramidaView.layerViews.get(layerIn).playSwipe();
                        continue;
                    }

                    if (layerIn % 2 == 0) {
                        piramidaView.layerViews.get(layerIn).turnRight(piramidaView.piramidaDataSource.getTitle(layerIn));
                    } else {
                        piramidaView.layerViews.get(layerIn).turnLeft(piramidaView.piramidaDataSource.getTitle(layerIn));
                    }
                }
                break;
            default:
                break;
        }

        if (!(indexOfCommand + 1 < countOfCommands && diary.historyCommands.size() >= currentCommand + 1)) {
            setPlayingOn(false);
            return;
        }

        currentCommand = indexOfCommand + 1;
        HistoryCommands nextCommandObject = diary.historyCommands.get(currentCommand);

        // schedule next command
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nextCommand(currentCommand, true);
                        }
                    });
                } catch (NullPointerException e) {
                    //
                }
            }
        }, Math.round(nextCommandObject.deltaTime * 1000));

    }

}
