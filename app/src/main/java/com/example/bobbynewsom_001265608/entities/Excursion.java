package com.example.bobbynewsom_001265608.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursion_table")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionId;

    @ColumnInfo(name = "excursion_title")
    private String title;

    @ColumnInfo(name = "excursion_date")
    private String date;

    @ColumnInfo(name = "vacation_id")
    private int vacationId;

    @ColumnInfo(name = "alert_enabled") // alert support for excursions
    private boolean alertEnabled;

    public Excursion(int excursionId, String title, String date, int vacationId, boolean alertEnabled) {
        this.excursionId = excursionId;
        this.title = title;
        this.date = date;
        this.vacationId = vacationId;
        this.alertEnabled = alertEnabled; // Set alertEnabled in constructor
    }

    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
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

    public boolean isAlertEnabled() {
        return alertEnabled; // Getter for alertEnabled
    }

    public void setAlertEnabled(boolean alertEnabled) {
        this.alertEnabled = alertEnabled; // Setter for alertEnabled
    }
}
