package com.example.bobbynewsom_001265608.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bobbynewsom_001265608.dao.ExcursionDAO;
import com.example.bobbynewsom_001265608.dao.VacationDAO;
import com.example.bobbynewsom_001265608.entities.Excursion;
import com.example.bobbynewsom_001265608.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 2)
public abstract class VacationDatabaseBuilder extends RoomDatabase {

    private static VacationDatabaseBuilder instance;

    public abstract VacationDAO vacationDAO();

    public abstract ExcursionDAO excursionDAO();

    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VacationDatabaseBuilder.class, "vacation_database.db")
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}

