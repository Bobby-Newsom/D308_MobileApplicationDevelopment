package com.example.bobbynewsom_001265608.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacation_table")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "vacation_title")
    private String title;

    @ColumnInfo(name = "vacation_start_date")
    private String startDate;

    @ColumnInfo(name = "vacation_end_date")
    private String endDate;

    public Vacation(int id, String title, String startDate, String endDate) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
