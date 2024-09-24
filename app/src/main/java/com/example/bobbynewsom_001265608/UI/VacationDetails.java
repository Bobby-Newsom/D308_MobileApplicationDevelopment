package com.example.bobbynewsom_001265608.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bobbynewsom_001265608.R;
import com.example.bobbynewsom_001265608.database.Repository;
import com.example.bobbynewsom_001265608.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VacationDetails extends AppCompatActivity {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private EditText titleEditText;
    private EditText accommodationEditText;
    private Button saveButton;
    private Button deleteButton;

    private Repository repository;
    private boolean isEditMode = false;
    private int vacationId = -1; // Track if editing an existing vacation

    private final Executor executor = Executors.newSingleThreadExecutor(); // Executor for background tasks
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // Ensure consistent date format

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Initialize EditTexts and Buttons
        titleEditText = findViewById(R.id.editTextVacationTitle);
        accommodationEditText = findViewById(R.id.editTextAccommodation);
        startDateEditText = findViewById(R.id.editTextStartDate);
        endDateEditText = findViewById(R.id.editTextEndDate);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Check if editing or creating new vacation
        Intent intent = getIntent();
        if (intent.hasExtra("vacationId")) {
            isEditMode = true;
            vacationId = intent.getIntExtra("vacationId", -1);
            prepopulateFields(intent);

            // Show Delete Button for edit mode
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setText("Delete");

            deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
        } else {
            // New vacation mode, show the delete button as "Cancel"
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setText("Cancel");
            deleteButton.setOnClickListener(v -> finish());  // Simply cancel and close activity
        }

        // Set onClickListeners for date selection
        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));

        // Save button logic
        saveButton.setOnClickListener(v -> {
            if (isEditMode) {
                updateVacation();
            } else {
                saveNewVacation();
            }
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addVacationButton), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Prepopulate fields if in edit mode
    private void prepopulateFields(Intent intent) {
        titleEditText.setText(intent.getStringExtra("title"));
        accommodationEditText.setText(intent.getStringExtra("accommodation"));
        startDateEditText.setText(intent.getStringExtra("startDate"));
        endDateEditText.setText(intent.getStringExtra("endDate"));
    }

    // Show a confirmation dialog before deleting
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Vacation")
                .setMessage("Are you sure you want to delete this vacation?")
                .setPositiveButton("Yes", (dialog, which) -> attemptDeleteVacation())
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Attempt to delete a vacation after checking for associated excursions
    private void attemptDeleteVacation() {
        executor.execute(() -> {
            boolean hasExcursions = repository.hasExcursions(vacationId);
            runOnUiThread(() -> {
                if (hasExcursions) {
                    // If there are excursions, show an alert
                    new AlertDialog.Builder(this)
                            .setTitle("Deletion Blocked")
                            .setMessage("This vacation has associated excursions. Please delete them first.")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    deleteVacation();  // Proceed with deletion if no excursions
                }
            });
        });
    }

    // Actual deletion process
    private void deleteVacation() {
        executor.execute(() -> {
            repository.deleteVacationById(vacationId);
            runOnUiThread(() -> {
                Toast.makeText(this, "Vacation deleted successfully", Toast.LENGTH_SHORT).show();
                finish();  // Return to the vacation list activity
            });
        });
    }

    // DatePicker for date selection
    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = (month1 + 1) + "/" + dayOfMonth + "/" + year1;
                    editText.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Save a new vacation with date validation
    private void saveNewVacation() {
        String title = titleEditText.getText().toString();
        String accommodation = accommodationEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();

        // Validate the dates
        if (!isDateValid(startDate, endDate)) {
            Toast.makeText(this, "Start date cannot be after the end date", Toast.LENGTH_SHORT).show();
            return; // Stop execution if validation fails
        }

        // Create new Vacation entity
        Vacation newVacation = new Vacation(0, title, startDate, endDate, accommodation);

        // Insert into database
        repository.insert(newVacation);
        finish(); // Go back to the vacation list
    }

    // Update an existing vacation with date validation
    private void updateVacation() {
        String title = titleEditText.getText().toString();
        String accommodation = accommodationEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();

        // Validate the dates
        if (!isDateValid(startDate, endDate)) {
            Toast.makeText(this, "Start date cannot be after the end date", Toast.LENGTH_SHORT).show();
            return; // Stop execution if validation fails
        }

        // Create updated Vacation entity
        Vacation updatedVacation = new Vacation(vacationId, title, startDate, endDate, accommodation);

        // Update in the database
        repository.update(updatedVacation);
        finish(); // Go back to the vacation list
    }

    // Method to validate that the start date is not after the end date
    private boolean isDateValid(String startDateStr, String endDateStr) {
        try {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);
            return !startDate.after(endDate); // Return false if start date is after end date
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
