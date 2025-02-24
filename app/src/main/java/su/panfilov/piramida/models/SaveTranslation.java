package su.panfilov.piramida.models;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Date;

public class SaveTranslation {

    // Перечисление команд для управления пирамидой
    public static enum PiramidaCommands {
        none,
        setLayer, // Установка слоя по индексу
        leftTurn, // Поворот слоя влево
        rightTurn, // Поворот слоя вправо
        allLeftTurn, // Поворот всех слоев влево
        allRightTurn, // Поворот всех слоев вправо
        setLockPiramida, // Установка блокировки пирамиды
        setOneSide // Установка одной стороны
    }

    // Количество пирамид
    public static int numberOfPiramidas = 0;

    // Дневник для сохранения истории команд
    public Diary diary;

    // Время последней выполненной команды
    private Date timeLastCommand = new Date();

    // Контекст приложения
    private Context context;

    // Конструктор для инициализации контекста
    public SaveTranslation(Context context) {
        this.context = context;
    }

    // Метод для добавления команды в историю
    private void addCommand(PiramidaCommands command, int layer, int index) {
        // Создание новой записи команды
        HistoryCommands historyCommand = new HistoryCommands();
        historyCommand.command = command.ordinal();
        historyCommand.layer = layer;
        historyCommand.index = index;
        historyCommand.deltaTime = (new Date().getTime() - timeLastCommand.getTime()) / 1000.0;

        // Добавление команды в историю и сохранение состояния
        diary.historyCommands.add(historyCommand);
        diary.saveState(context);

        // Обновление времени последней команды
        timeLastCommand = new Date();
    }

    // Перегруженный метод для добавления команды без индекса
    private void addCommand(PiramidaCommands command, int layer) {
        this.addCommand(command, layer, 1);
    }

    // Перегруженный метод для добавления команды без слоя и индекса
    private void addCommand(PiramidaCommands command) {
        this.addCommand(command, 0, 1);
    }

    // Начало сохранения состояния пирамиды
    public void startSaving(int[] sideByLayer, boolean locked) {
        // Обнуление времени последней команды
        timeLastCommand = new Date();

        // Добавление команд установки слоев
        for (int index = 0; index < sideByLayer.length; index++) {
            int layer = sideByLayer[index];
            addCommand(PiramidaCommands.setLayer, layer, index);
        }

        // Установка блокировки пирамиды
        setLockPiramida(locked, false);
    }

    // Установка состояния блокировки пирамиды
    public void setLockPyramid(boolean isLocked) {
        SharedPreferences preferences = context.getSharedPreferences("PyramidPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("pyramidLocked", isLocked);
        editor.apply();
    }

    // Получение состояния блокировки пирамиды
    public boolean getLockPyramid() {
        SharedPreferences preferences = context.getSharedPreferences("PyramidPrefs", Context.MODE_PRIVATE);
        return preferences.getBoolean("pyramidLocked", false);
    }

    // Остановка сохранения состояния
    public void stopSaving() {
        addCommand(PiramidaCommands.none);
    }

    // Поворот слоя влево
    public void leftTurn(int layer) {
        addCommand(PiramidaCommands.leftTurn, layer);
    }

    // Поворот слоя вправо
    public void rightTurn(int layer) {
        addCommand(PiramidaCommands.rightTurn, layer);
    }

    // Поворот всех слоев влево
    public void allLeftTurn() {
        addCommand(PiramidaCommands.allLeftTurn);
    }

    // Поворот всех слоев вправо
    public void allRightTurn() {
        addCommand(PiramidaCommands.allRightTurn);
    }

    // Установка блокировки пирамиды с учетом звукового эффекта
    public void setLockPiramida(boolean locked, boolean soundEffect) {
        addCommand(PiramidaCommands.setLockPiramida, (locked ? 1 : 0), (soundEffect ? 1 : 0));
    }

    // Установка блокировки пирамиды без звукового эффекта
    public void setLockPiramida(boolean locked) {
        setLockPiramida(locked, true);
    }

    // Установка одной стороны пирамиды
    public void setOneSide(int layer) {
        addCommand(PiramidaCommands.setOneSide, layer);
    }
}
