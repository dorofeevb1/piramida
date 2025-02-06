package su.panfilov.piramida.models;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import su.panfilov.piramida.components.FileCache;
import su.panfilov.piramida.components.Utils;

public class Diary {
    public String title;
    public String note;
    public int numberOfPiramida;
    public Date timeStamp;
    public ArrayList<HistoryCommands> historyCommands = new ArrayList<>(0);

    public void saveState(Context context) {
        Map<String, String> diariesTitles = readDiariesTitlesFromCache(context);
        diariesTitles.put(getId(), title);
        saveDiariesTitlesToCache(context, diariesTitles);
        saveDiaryToCache(context, this);
    }

    public void delete(Context context) {
        Map<String, String> diariesTitles = readDiariesTitlesFromCache(context);
        diariesTitles.remove(getId());
        saveDiariesTitlesToCache(context, diariesTitles);
        deleteDiaryFromCache(context, getId());
    }

    public static Map<String, String> readDiariesTitlesFromCache(Context context) {
        Map<String, String> diariesTitles = new TreeMap<>();

        FileCache fileCache = new FileCache(context);
        String diariesTitlesJson = fileCache.getStringFromFile("diaries_titles");
        if (diariesTitlesJson.equals("")) {
            diariesTitlesJson = "{}";
        }
        Gson gson = new Gson();
        diariesTitles = gson.fromJson(diariesTitlesJson, new TypeToken<TreeMap<String, String>>() {}.getType());

        return diariesTitles;
    }

    private void saveDiariesTitlesToCache(Context context, Map<String, String> diariesTitles) {
        FileCache fileCache = new FileCache(context);
        Gson gson = new Gson();
        String diariesTitlesJson = gson.toJson(diariesTitles, new TypeToken<TreeMap<String, String>>() {}.getType());
        fileCache.putStringToFile("diaries_titles", diariesTitlesJson);
    }

    public static Diary readDiaryFromCache(Context context, String id) {
        FileCache fileCache = new FileCache(context);
        String diaryDataJson = fileCache.getStringFromFile("diary_data_" + id);
        if (diaryDataJson.equals("")) {
            diaryDataJson = "{}";
        }
        Gson gson = new Gson();
        Diary diary = gson.fromJson(diaryDataJson, Diary.class);

        return diary;
    }

    private void saveDiaryToCache(Context context, Diary diary) {
        FileCache fileCache = new FileCache(context);
        Gson gson = new Gson();
        String diaryDataJson = gson.toJson(diary, Diary.class);
        fileCache.putStringToFile("diary_data_" + diary.getId(), diaryDataJson);
    }

    private void deleteDiaryFromCache(Context context, String id) {
        FileCache fileCache = new FileCache(context);
        fileCache.deleteFile("diary_data_" + id);
    }

    public String getId() {
        return DateFormat.format("yyyyMMddHHmmss", timeStamp).toString();
    }
}
