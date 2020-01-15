package com.igor.langugecards.database.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.igor.langugecards.R;
import com.igor.langugecards.model.TranslateSettings;

import static com.igor.langugecards.constants.Constants.EMPTY_STRING;

public class TranslateSettingInteractor {

    private final Context mContext;

    public static final String FROM = "from";
    public static final String TO = "to";

    private static final String TRANSLATE_PREFERENCES = "TRANSLATE ";
    private static final String LANGUAGE = " LANGUAGE";
    private static final String LANGUAGE_CODE = " LANGUAGE CODE";

    public TranslateSettingInteractor(@NonNull Context context) {
        mContext = context;
    }

    public void writeTranslateSettings(@NonNull String tag,
                                              @Nullable String language,
                                              @Nullable String languageCode) {
        mContext.getSharedPreferences(TRANSLATE_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .putString(TRANSLATE_PREFERENCES + tag + LANGUAGE, language)
                .putString(TRANSLATE_PREFERENCES + tag + LANGUAGE_CODE, languageCode)
                .apply();
    }

    public TranslateSettings readTranslateSettings() {
        SharedPreferences translatePrefs = mContext.getSharedPreferences(TRANSLATE_PREFERENCES, Context.MODE_PRIVATE);
        String fromLang = translatePrefs.getString(TRANSLATE_PREFERENCES + FROM + LANGUAGE, mContext.getString(R.string.auto_detecting));
        String fromLangCode = translatePrefs.getString(TRANSLATE_PREFERENCES + FROM + LANGUAGE_CODE, EMPTY_STRING);
        String toLang = translatePrefs.getString(TRANSLATE_PREFERENCES + TO + LANGUAGE, mContext.getString(R.string.not_defined));
        String toLangCode = translatePrefs.getString(TRANSLATE_PREFERENCES + TO + LANGUAGE_CODE, EMPTY_STRING);
        return new TranslateSettings(fromLang, fromLangCode, toLang, toLangCode);
    }
}
