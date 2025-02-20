package su.panfilov.piramida;

public class InformationManager {

    private String infoText;  // Строка для хранения текста

    public InformationManager() {
        // Инициализируем начальный текст
        this.infoText = "Here is the information text";
    }

    // Получить текущий текст
    public String getInfoText() {
        return infoText;
    }

    // Обновить текст
    public void updateInfoText(String newText) {
        this.infoText = newText;
    }
}
