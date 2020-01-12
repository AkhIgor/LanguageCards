package com.igor.langugecards.database.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.igor.langugecards.database.room.DAO.CardInteractor;
import com.igor.langugecards.model.Card;

@Database(entities = {Card.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase getInstance(@NonNull Context context) {
        return Room
                .databaseBuilder(context, AppDatabase.class, "AppDatabase")
                .build();
    }

    public abstract CardInteractor getCardInteractor();
}
