package com.example.bobbynewsom_001265608.UI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bobbynewsom_001265608.R;
import com.example.bobbynewsom_001265608.database.Repository;
import com.example.bobbynewsom_001265608.entities.Excursion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExcursionDetails extends AppCompatActivity {

    private EditText titleEditText;
    private EditText dateEditText;
    private Switch alertSwitch;
    private Button saveButton;
    private Button deleteButton;

    private Repository repository;
    private boolean isEditMode = false;
    private int vacationId = -1;
    private int excursionId = -1;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Task Requirement B.5.A  "Display a detailed view of the excursion, including title, and date."
        // Initialize EditTexts, Switch, and Buttons
        titleEditText = findViewById(R.id.editTextExcursionTitle);
        dateEditText = findViewById(R.id.editTextExcursionDate);
        alertSwitch = findViewById(R.id.switchExcursionAlert);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Set onClickListener for date selection
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Get Intent extras to determine if adding or editing
        Intent intent = getIntent();
        if (intent.hasExtra("excursionId")) {
            isEditMode = true;
            excursionId = intent.getIntExtra("excursionId", -1);
            prepopulateFields(excursionId);

            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setText("Delete");
            deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
        } else if (intent.hasExtra("vacationId")) {
            vacationId = intent.getIntExtra("vacationId", -1);
            deleteButton.setVisibility(View.GONE);
        }

        // Save button logic
        saveButton.setOnClickListener(v -> {
            if (isEditMode) {
                updateExcursion();
            } else {
                saveNewExcursion();
            }
        });
    }

    // Task Requirement B.5.A  "Display a detailed view of the excursion, including title, and date."
    // Prepopulate fields if in edit mode ensures that the excursion title and date are fetched properly from the database
    private void prepopulateFields(int excursionId) {
        executor.execute(() -> {
            Excursion excursion = repository.getExcursionById(excursionId);
            runOnUiThread(() -> {
                if (excursion != null) {
                    titleEditText.setText(excursion.getTitle());
                    dateEditText.setText(excursion.getDate());
                    alertSwitch.setChecked(excursion.isAlertEnabled());
                    vacationId = excursion.getVacationId();
                }
            });
        });
    }

    // Task Requirement B.5.C  "Include validation that the input date is formatted correctly"
    // using a datepicker to select the excursion date helps to ensure dates are formatted correctly
    // Show a DatePicker dialog for selecting the excursion date
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = (month1 + 1) + "/" + dayOfMonth + "/" + year1;
                    dateEditText.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    // Task Requirement B.5.B & C  "Enter, Edit, and Delete Excursion information"   &  "Include Validation that the input dates are formatted correctly"
    // Save a new excursion
    private void saveNewExcursion() {
        String title = titleEditText.getText().toString();
        String date = dateEditText.getText().toString();
        boolean alertEnabled = alertSwitch.isChecked();

        // Validate the date input
        if (!isDateValid(date)) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Excursion entity
        Excursion newExcursion = new Excursion(0, title, date, vacationId, alertEnabled);

        // Insert into the database
        repository.insert(newExcursion);

        // Set up a notification if the alert switch is enabled
        if (alertEnabled) {
            scheduleNotification(title, date, "Excursion Alert");
        }

        finish(); // Go back to the previous activity
    }

    // Update an existing excursion
    private void updateExcursion() {
        String title = titleEditText.getText().toString();
        String date = dateEditText.getText().toString();
        boolean alertEnabled = alertSwitch.isChecked();

        // Validate the date input
        if (!isDateValid(date)) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an updated Excursion entity
        Excursion updatedExcursion = new Excursion(excursionId, title, date, vacationId, alertEnabled);

        // Update in the database
        repository.update(updatedExcursion);

        // Set up a notification if the alert switch is enabled
        if (alertEnabled) {
            scheduleNotification(title, date, "Excursion Alert");
        }

        finish(); // Go back to the previous activity
    }

    // Show a confirmation dialog before deleting
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Excursion")
                .setMessage("Are you sure you want to delete this excursion?")
                .setPositiveButton("Yes", (dialog, which) -> deleteExcursion())
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Delete the excursion
    private void deleteExcursion() {
        executor.execute(() -> {
            repository.deleteExcursionById(excursionId);
            runOnUiThread(() -> {
                Toast.makeText(this, "Excursion deleted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Go back to the previous activity
            });
        });
    }

    // Validate the date input
    private boolean isDateValid(String dateStr) {
        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Schedule a notification for the excursion date with the correct alert message
    private void scheduleNotification(String title, String dateString, String notificationTitle) {
        try {
            Date date = dateFormat.parse(dateString);
            long triggerTime = date.getTime();

            // Create an intent for the BroadcastReceiver
            Intent intent = new Intent(this, MyReceiver.class);
            intent.setAction("excursion action");

            // Pass the correct title and message to the receiver
            intent.putExtra("alert_message", notificationTitle + ": " + title);
            intent.putExtra("alert_type", "excursion");  // Add type identifier

            // Create a unique PendingIntent with a different requestCode for excursions
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    (int) (System.currentTimeMillis() / 1000),  // Ensure uniqueness
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Schedule the alarm to trigger the notification
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    // Add options menu for copying or sharing excursion details
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.copy_to_clipboard) {
            copyToClipboard();
        } else if (id == R.id.share_excursion) {
            shareExcursionDetails();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    // Method to get excursion details in a formatted string
    private String getExcursionDetails() {
        String title = titleEditText.getText().toString();
        String date = dateEditText.getText().toString();
        boolean alertEnabled = alertSwitch.isChecked();

        return "Excursion Title: " + title + "\n" +
                "Date: " + date + (alertEnabled ? " (Alert Enabled)" : "");
    }

    // Copy excursion details to clipboard
    private void copyToClipboard() {
        String excursionDetails = getExcursionDetails();
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Excursion Details", excursionDetails);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Excursion details copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    // Share excursion details
    private void shareExcursionDetails() {
        String excursionDetails = getExcursionDetails();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Excursion Details");
        shareIntent.putExtra(Intent.EXTRA_TEXT, excursionDetails);
        startActivity(Intent.createChooser(shareIntent, "Share Excursion Details via"));
    }
}
