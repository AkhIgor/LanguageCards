package com.igor.langugecards.database.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.igor.langugecards.model.TranslateSettings;

public class TranslateSettingInteractor {

    private static final String TRANSLATE_PREFERENCES = "TRANSLATE ";

    private static final String LANGUAGE = " LANGUAGE";
    private static final String LANGUAGE_CODE = " LANGUAGE CODE";

    public static final String FROM = "from";
    public static final String TO = "to";

    public static void writeTranslateSettings(@NonNull String tag,
                                       @NonNull Context context,
                                       @Nullable String language,
                                       @Nullable String languageCode) {
        context.getSharedPreferences(TRANSLATE_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .putString(TRANSLATE_PREFERENCES + tag + LANGUAGE, language)
                .putString(TRANSLATE_PREFERENCES + tag + LANGUAGE_CODE, languageCode)
                .apply();
    }

    public static TranslateSettings readTranslateSettings(@NonNull Context context) {
       SharedPreferences translatePrefs = context.getSharedPreferences(TRANSLATE_PREFERENCES, Context.MODE_PRIVATE);
       String fromLang = translatePrefs.getString(TRANSLATE_PREFERENCES + FROM + LANGUAGE, "");
       String fromLangCode = translatePrefs.getString(TRANSLATE_PREFERENCES + FROM + LANGUAGE_CODE, "");
       String toLang = translatePrefs.getString(TRANSLATE_PREFERENCES + TO + LANGUAGE, "");
       String toLangCode = translatePrefs.getString(TRANSLATE_PREFERENCES + TO + LANGUAGE_CODE, "");
       return new TranslateSettings(fromLang, fromLangCode, toLang, toLangCode);
    }
}
