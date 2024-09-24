package com.example.bobbynewsom_001265608.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bobbynewsom_001265608.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacation_table")
    List<Vacation> getAllVacations();

    @Query("DELETE FROM vacation_table WHERE vacationId = :vacationId")
    void deleteVacationById(int vacationId);

}
