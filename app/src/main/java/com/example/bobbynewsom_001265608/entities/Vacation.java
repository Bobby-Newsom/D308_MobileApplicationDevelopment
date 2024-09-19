package com.example.bobbynewsom_001265608.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacation_table")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationId;

    @ColumnInfo(name = "vacation_title")
    private String title;

    @ColumnInfo(name = "vacation_start_date")
    private String startDate;

    @ColumnInfo(name = "vacation_end_date")
    private String endDate;

    @ColumnInfo(name = "vacation_accommodation")
    private String accommodation;

    public Vacation(int vacationId, String title, String startDate, String endDate, String accommodation) {
        this.vacationId = vacationId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accommodation = accommodation;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
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

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }
}
