package su.panfilov.piramida.models;

public class Facet {
    private int id;              // Уникальный номер грани
    private String title;        // Название грани
    private String description;  // Описание грани
    private boolean isFavorite;  // Избранная или нет

    // Конструктор - создает новую грань
    public Facet(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isFavorite = false; // По умолчанию не избранная
    }

    // Геттеры - чтобы получать данные
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    // Сеттер - чтобы менять статус избранного
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}