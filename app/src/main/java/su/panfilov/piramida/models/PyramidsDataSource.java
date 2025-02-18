package su.panfilov.piramida.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class PyramidsDataSource {

    private String[][] setOfTitles = {
            {"ТОЧКА СБОРКИ", "", "", "", "", "", "", ""},
            // Пирамида Бытия
            {"ГЕРОЙ", "Атлет", "Боец", "Тактик", "Воин", "Вождь", "Стратег", "Мистик"},
            {"ВЛИЯНИЕ", "Тело", "Закон", "Финансы", "Технология", "Коммуникация", "Информация", "Состояние"},
            {"КОММУНИКАЦИЯ", "Одиночество", "Контакт", "Знакомство", "Товарищество", "Дружба", "Партнерство", "Уединение"},
            {"СОЗНАНИЕ", "Актуальное", "Оперативное", "Подсознательное", "Бессознательное", "Эгрегоры", "Архетипы", "Элементы"},
            {"ТВОРЧЕСТВО", "Материал", "Форма", "Стиль", "Вдохновение", "Утверждение", "Воображение", "Откровение"},
            {"ДЕЯТЕЛЬНОСТЬ", "Ремесло", "Технология", "Наука", "Методология", "Философия", "Искусство", "Индивидуация"},
            // Пирамида Коммуникации
            {"РЕПУТАЦИЯ", "Узнавание", "Воспоминание", "Знакомство", "Расположенность", "Эксперимент", "Утверждение", "Рекомендация"},
            {"ОТНОШЕНИЕ", "Созерцание", "Узнавание", "Инициация", "Постижение", "Служение", "Творение", "Единение"},
            {"ИНТЕГРАЦИЯ", "Отдаленность", "Включение", "Роль", "Статус", "Отношения", "Интеграция", "Представительство"},
            {"ХОЛОС", "Физический", "Эмоциональный", "Личностный", "Межличностный", "Социальный", "Принципиальный", "Универсальный"},
            {"ЦЕННОСТИ", "Выживание", "Инициатива", "Гибкость", "Искренность", "Равностность", "Результат", "Бытие"},
            {"СОСТОЯНИЕ", "Равнодушие", "Любопытство", "Интерес", "Принятие", "Понимание", "Доверие", "Любовь"},
            // Пирамида Социум
            {"ОБЩЕСТВО", "Личность", "Семья", "Клан", "Род", "Народ", "Нация", "Вид"},
            {"НЕЙРОЛОГИКА", "Окружение", "Поведение", "Способности", "Убеждения", "Ценности", "Идентичность", "Миссия"},
            {"ПОТРЕБНОСТИ", "Физиология", "Безопасность", "Принадлежность", "Признание", "Познание", "Эстетика", "Реализация"},
            {"КЛИЕНТЫ", "Холодный рынок", "Потенциальные потребители", "Посетители", "Переговорщики", "Повторные клиенты", "Постоянные клиенты", "Приверженцы"},
            {"ИНФОРМАЦИЯ", "Носитель", "Восприятие", "Язык", "Преобразование", "Смысл", "Цель", "Поле"},
            //{"КУЛЬТУРА","Религия","Миф","Искусство","Наука","Техника","Право","Язык"}, // version 1.0
            {"КУЛЬТУРА", "Язык", "Право", "Техника", "Наука", "Миф", "Искусство", "Религия"}, // version 1.1
            // Пирамида Деятельность
            {"ЛИДЕР", "Оппортунист", "Дипломат", "Эксперт", "Достиженец", "Индивидуалист", "Стратег", "Алхимик"},
            {"УПРАВЛЕНИЕ", "Персона", "Задачи", "Процессы", "Качество", "Цели", "Культура", "Организация"},
            {"ПРОЦЕССЫ", "Этика", "Планирование", "Администрирование", "Реализация", "Развитие", "Изменение", "Видение"},
            //{"БРЕНД","Образ","Динамика","Коммуникация","Метафора","Территория","Миф","Резонанс"}, // version 1.0
            {"БРЕНД", "Образ", "Динамика", "Коммуникация", "Метафора", "Территория", "Миф", "Резонанс"}, // version 1.1
            {"ЖИЗНЕННЫЙ ЦИКЛ", "Предпринимательство", "Партнерство", "Команда", "Структура", "Процессы", "Производительность", "Капитализация"},
            {"ЦЕЛИ", "Поступок", "Ситуация", "Операция", "Тактика", "Проект", "Стратегия", "Амбиция"}
    };

    private String[][] setOfTitlesDe = {
            {"Montagepunkt", "", "", "", "", "", "", ""},
            // Пирамида Бытия
            {"HELD", "Athlet", "Kämpfer", "Taktiker", "Krieger", "Führer", "Stratege", "Mystiker"},
            {"EINFLUSS", "Körper", "Gesetz", "Finanzen", "Technologie", "Kommunikation", "Information", "Zustand"},
            {"KOMMUNIKATION", "Einsamkeit", "Kontakt", "Bekanntschaft", "Kameradschaft", "Freundschaft", "Partnerschaft", "Abgeschiedenheit"},
            {"BEWUSSTSEIN", "Akutelles", "Operatives", "Unterbewusste", "Unbewusste", "Egregors", "Archetypen", "Elemente"},
            {"SCHAFFEN", "Material", "Form", "Stil", "Inspiration", "Aussage", "Vorstellungsvermögen", "Offenbarung"},
            {"TÄTIGKEIT", "Handwerk", "Technologie", "Wissenschaft", "Methodologie", "Philosophie", "Kunst", "Individuation"},
            // Пирамида Коммуникации
            {"REPUTATION", "Erkennung", "Erinnerung", "Bekannschaft", "Befindlichkeit", "Experiment", "Aussage", "Empfehlung"},
            {"EINSTELLUNG", "Betrachten", "Erkennen", "Initiation", "Begreifen", "Mission", "Schöpfung", "Einigkeit"},
            {"INTEGRATION", "Abstand", "Aufnahme", "Rolle", "Status", "Beziehung", "Integration", "Repräsentation"},
            {"HOLOS", "Physisches", "Emotionales", "Persönliches", "Zwischenmenschlich", "Soziales", "Prinzipielles", "Universelles"},
            {"WERTE", "Überleben", "Initiative", "Flexibilität", "Aufrichtigkeit", "Gleichheit", "Resultat", "Dasein"},
            {"ZUSTÄNDE", "Gleichgültigkeit", "Neugier", "Interesse", "Annahme", "Verständnis", "Vertrauen", "Liebe"},
            // Пирамида Социум
            {"GESELLSCHAFT", "Persönlichkeit", "Familie", "Sippe", "Stamm", "Volk", "Nation", "Gattung"},
            {"HEUROLOGIK", "Umgebung", "Verhalten", "Fägikeiten", "Überzeugungen", "Werte", "Identität", "Mission"},
            {"BEDÜRFNISSE", "Physiologie", "Sicherheit", "Zugehörigkeit", "Anerkennung", "Erkenntnis", "Ästhetik", "Realisierung"},
            {"KUNDEN", "Kalter Markt", "Potenzielle Kundschaft", "Besucher", "Vermittler", "Wiederholungskundschaft", "Stmmkundschaft", "Anhängerschaft"},
            {"INFORMATION", "Träger", "Wahrnehmung", "Sprache", "Modifikation", "Sinn", "Ziel", "Feld"},
            //{"КУЛЬТУРА","Религия","Миф","Искусство","Наука","Техника","Право","Язык"}, // version 1.0
            {"KULTUR", "Sprache", "Recht", "Technik", "Wissenschaft", "Mythos", "Kunst", "Religion"}, // version 1.1
            // Пирамида Деятельность
            {"FÜHRER", "Opportunist", "Diplomat", "Experte", "Zielstrebig", "Individualist", "Stratege", "Alchimist"},
            {"MANAGEMENT", "Peson", "Aufgaben", "Prozesse", "Qualität", "Ziele", "Kultur", "Organisation"},
            {"PROZESSE", "Ethik", "Planung", "Administration", "Realisierung", "Entwicklung", "Veränderung", "Vision"},
            //{"БРЕНД","Образ","Динамика","Коммуникация","Метафора","Территория","Миф","Резонанс"}, // version 1.0
            {"MARKE", "Image", "Dynamik", "Kommunikation", "Metapher", "Gebiet", "Mythos", "Resonanz"}, // version 1.1
            {"LEBENSZYKLUS", "Unternehmen", "Partnerschaft", "Team", "Struktur", "Prozesse", "(Betriebs)Leistung", "Kapitalisierung"},
            {"ZIELE", "Tat", "Situation", "Operation", "Taktik", "Projekt", "Strategie", "Ambition"}
    };

    public String[] nameOfPiramids = {"Пирамида Бытия", "Пирамида Коммуникации", "Пирамида Социум", "Пирамида Деятельность"};

    public String[] nameOfPiramidsDe = {"Dasein", "Kommunikation", "Soz.Umfeld", "Tätigkeit"};

    public int[] sideByLayer = {0, 0, 0, 0, 0, 0, 0, 0};

    // Получить текущий заголовок для слоя layer
    public String getTitle(int layer) {
        int side = sideByLayer[layer];
        String[][] setOfTitles = getSetOfTitles();
        return setOfTitles[side][layer];
    }

    // Получить название грани (нижний слой)
    public String getTitleForHead(int side) {
        String[][] setOfTitles = getSetOfTitles();
        return setOfTitles[side][0];
    }

    // Получить массив заголовков грани
    public String[] getFrontSide() {
        ArrayList<String> frontSide = new ArrayList<>(0);
        String[][] setOfTitles = getSetOfTitles();
        for (int layer = 0; layer < sideByLayer.length; layer++) {
            int side = sideByLayer[layer];
            frontSide.add(setOfTitles[side][layer]);
        }
        String[] s = {};
        s = frontSide.toArray(s);

        return s;
    }

    // Получить массив заголовков грани
    public ArrayList<String> getSideTitles(int side) {
        String[][] setOfTitles = getSetOfTitles();
        ArrayList<String> sideTitles = new ArrayList<>(0);
        sideTitles.addAll(Arrays.asList(setOfTitles[side]));

        return sideTitles;
    }

    // Повернуть влево слой layer
    public String leftTurn(int layer) {
        String[][] setOfTitles = getSetOfTitles();
        int side = sideByLayer[layer] + 1;
        if (side >= setOfTitles.length) {
            side = 0;
        }
        sideByLayer[layer] = side;

        return setOfTitles[side][layer];
    }

    // Повернуть вправо слой layer
    public String rightTurn(int layer) {
        String[][] setOfTitles = getSetOfTitles();
        int side = sideByLayer[layer] - 1;
        if (side < 0) {
            side = setOfTitles.length - 1;
        }
        sideByLayer[layer] = side;

        return setOfTitles[side][layer];
    }

    // Повернуть влево грань
    public String[] allLeftTurn() {
        for (int layer = 0; layer < sideByLayer.length; layer++) {
            leftTurn(layer);
        }

        return getFrontSide();
    }

    // Повернуть вправо грань
    public String[] allRightTurn() {
        for (int layer = 0; layer < sideByLayer.length; layer++) {
            rightTurn(layer);
        }

        return getFrontSide();
    }

    // Установить грань по слою layer
    public void setOneSide(int layer) {
        int side = sideByLayer[layer];
        for (int i = 0; i < 8; i++) {
            sideByLayer[i] = side;
        }
    }

    // Установить слой layer на грань
    public void setLayer(int layer, int index) {
        sideByLayer[layer] = index;
    }

    // Установить все слои по грани index
    public void setSide(int[] layers) {
        for (int index = 0; index < layers.length; index++) {
            int layer = layers[index];
            sideByLayer[layer] = index;
        }
    }

    public String[] getNameOfPiramids() {
        return Locale.getDefault().getLanguage().equals("de") ?
                this.nameOfPiramidsDe : this.nameOfPiramids;
    }

    public String[][] getSetOfTitles() {
        return Locale.getDefault().getLanguage().equals("de") ?
                this.setOfTitlesDe : this.setOfTitles;
    }
}
