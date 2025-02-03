package su.panfilov.piramida.models;

import android.content.Context;
import android.support.annotation.NonNull;
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
//        Map<String, Diary> diariesMap = readDiariesFromCache(context);
//        diariesMap.put(getId(), this);
//        saveDiariesToCache(context, diariesMap);

        Map<String, String> diariesTitles = readDiariesTitlesFromCache(context);
        diariesTitles.put(getId(), title);
        saveDiariesTitlesToCache(context, diariesTitles);
        saveDiaryToCache(context,this);
    }

    public void delete(Context context) {
//        Map<String, Diary> diariesMap = readDiariesFromCache(context);
//        diariesMap.remove(getId());
//        saveDiariesToCache(context, diariesMap);

        //new FileCache(context).clear();

        Map<String, String> diariesTitles = readDiariesTitlesFromCache(context);
        diariesTitles.remove(getId());
        saveDiariesTitlesToCache(context, diariesTitles);
        deleteDiaryFromCache(context, getId());
    }

//    public static Map<String, Diary> readDiariesFromCache(Context context) {
//        Map<String, Diary> diariesMap = new HashMap<String, Diary>();
//
//        FileCache fileCache = new FileCache(context);
//        String diariesListJson = fileCache.getStringFromFile("diaries");
//        if (diariesListJson.equals("")) {
//            diariesListJson = "{}";
//        }
//        Gson gson = new Gson();
//        diariesMap = gson.fromJson(diariesListJson, new TypeToken<HashMap<String, Diary>>() {}.getType());
//
//        return diariesMap;
//    }
//
//    private void saveDiariesToCache(Context context, Map<String, Diary> diariesMap) {
//        FileCache fileCache = new FileCache(context);
//        Gson gson = new Gson();
//        String diariesListJson = gson.toJson(diariesMap, new TypeToken<HashMap<String, Diary>>() {}.getType());
//        fileCache.putStringToFile("diaries", diariesListJson);
//    }


    public static Map<String, String> readDiariesTitlesFromCache(Context context) {
        Map<String, String> diariesTitles = new TreeMap<String, String>();

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
        String diaryDataJson = fileCache.getStringFromFile("diariy_data_" + id);
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
        fileCache.putStringToFile("diariy_data_" + diary.getId(), diaryDataJson);
    }

    private void deleteDiaryFromCache(Context context, String id) {
        FileCache fileCache = new FileCache(context);
        fileCache.deleteFile("diariy_data_" + id);
    }

    public String getId() {
        return DateFormat.format("yyyyMMddHHmmss", timeStamp).toString();
    }
}
