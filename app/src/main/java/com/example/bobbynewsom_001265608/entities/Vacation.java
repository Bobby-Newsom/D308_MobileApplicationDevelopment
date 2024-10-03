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

    @ColumnInfo(name = "start_alert_enabled") // New field to store the state of start date alert
    private boolean startAlertEnabled;

    @ColumnInfo(name = "end_alert_enabled") // New field to store the state of end date alert
    private boolean endAlertEnabled;

    // Updated constructor to include the new alert fields
    public Vacation(int vacationId, String title, String startDate, String endDate, String accommodation, boolean startAlertEnabled, boolean endAlertEnabled) {
        this.vacationId = vacationId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accommodation = accommodation;
        this.startAlertEnabled = startAlertEnabled;
        this.endAlertEnabled = endAlertEnabled;
    }

    // Getters and Setters for all fields

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

    public boolean isStartAlertEnabled() {
        return startAlertEnabled;
    }

    public void setStartAlertEnabled(boolean startAlertEnabled) {
        this.startAlertEnabled = startAlertEnabled;
    }

    public boolean isEndAlertEnabled() {
        return endAlertEnabled;
    }

    public void setEndAlertEnabled(boolean endAlertEnabled) {
        this.endAlertEnabled = endAlertEnabled;
    }
}
