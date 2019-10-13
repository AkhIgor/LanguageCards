package com.igor.langugecards.database.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.igor.langugecards.model.Card;

@Database(entities = {Card.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
}
