package su.panfilov.piramida.models;

import android.content.Context;

import java.util.Date;

public class SaveTranslation {

    public static enum PiramidaCommands {
        none,
        setLayer, // Установка слоя по индексу
        leftTurn,
        rightTurn,
        allLeftTurn,
        allRightTurn,
        setLockPiramida,
        setOneSide
    }

    public static int numberOfPiramidas = 0;
    public Diary diary;
    private Date timeLastCommand = new Date();

    private Context context;

    public SaveTranslation(Context context) {
        this.context = context;
    }

    private void addCommand(PiramidaCommands command, int layer, int index) {

        HistoryCommands historyCommand = new HistoryCommands();
        historyCommand.command = command.ordinal();
        historyCommand.layer = layer;
        historyCommand.index = index;
        historyCommand.deltaTime = (new Date().getTime() - timeLastCommand.getTime()) / 1000.0;

        diary.historyCommands.add(historyCommand);

        diary.saveState(context);

        timeLastCommand = new Date();
    }

    private void addCommand(PiramidaCommands command, int layer) {
        this.addCommand(command, layer, 1);
    }

    private void addCommand(PiramidaCommands command) {
        this.addCommand(command, 0, 1);
    }

    public void startSaving(int[] sideByLayer, boolean locked) {
        // Обнулить счетчик
        timeLastCommand = new Date();

        for (int index = 0; index < sideByLayer.length; index++) {
            int layer = sideByLayer[index];
            addCommand(PiramidaCommands.setLayer, layer, index);
        }

        setLockPiramida(locked, false);
    }

    public void stopSaving() {
        addCommand(PiramidaCommands.none);
    }

    public void leftTurn(int layer) {
        addCommand(PiramidaCommands.leftTurn, layer);
    }

    public void rightTurn(int layer) {
        addCommand(PiramidaCommands.rightTurn, layer);
    }

    public void allLeftTurn() {
        addCommand(PiramidaCommands.allLeftTurn);
    }

    public void allRightTurn() {
        addCommand(PiramidaCommands.allRightTurn);
    }

    public void setLockPiramida(boolean locked, boolean soundEffect) {
        addCommand(PiramidaCommands.setLockPiramida, (locked ? 1 : 0), (soundEffect ? 1 : 0));
    }

    public void setLockPiramida(boolean locked) {
        setLockPiramida(locked, true);
    }

    public void setOneSide(int layer) {
        addCommand(PiramidaCommands.setOneSide, layer);
    }
}
