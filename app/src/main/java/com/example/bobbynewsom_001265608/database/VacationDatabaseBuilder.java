package com.example.bobbynewsom_001265608.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bobbynewsom_001265608.dao.ExcursionDAO;
import com.example.bobbynewsom_001265608.dao.VacationDAO;
import com.example.bobbynewsom_001265608.entities.Excursion;
import com.example.bobbynewsom_001265608.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 1)
public abstract class VacationDatabaseBuilder extends RoomDatabase {

    private static VacationDatabaseBuilder instance;
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();


}
