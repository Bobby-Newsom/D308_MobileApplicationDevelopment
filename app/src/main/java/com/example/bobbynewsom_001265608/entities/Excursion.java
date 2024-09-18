package com.example.bobbynewsom_001265608.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursion_table")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "excursion_title")
    private String title;

    @ColumnInfo(name = "excursion_date")
    private String date;

    @ColumnInfo(name = "vacation_id")
    private int vacationId;

    public Excursion(int id, String title, String date, int vacationId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.vacationId = vacationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }
}
