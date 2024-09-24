package com.example.bobbynewsom_001265608.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bobbynewsom_001265608.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    //query for all excursions
    @Query("SELECT * FROM excursion_table ORDER BY excursionId ASC")
    List<Excursion> getAllExcursions();

    //query for all excursions associated with a vacation
    @Query("SELECT * FROM excursion_table WHERE vacation_id=:vacationId ORDER BY excursion_date ASC")
    List<Excursion> getAssociatedExcursions(int vacationId);

    @Query("SELECT COUNT(*) FROM excursion_table WHERE vacation_id = :vacationId")
    int countExcursionsByVacationId(int vacationId);
}
