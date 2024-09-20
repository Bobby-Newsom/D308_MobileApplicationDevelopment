package com.example.bobbynewsom_001265608.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bobbynewsom_001265608.R;
import com.example.bobbynewsom_001265608.database.Repository;
import com.example.bobbynewsom_001265608.entities.Vacation;

import java.util.Calendar;

public class VacationDetails extends AppCompatActivity {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private EditText titleEditText;
    private EditText accommodationEditText;
    private Button saveButton;

    private Repository repository;
    private boolean isEditMode = false;
    private int vacationId = -1; // Track if we are editing an existing vacation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Initialize EditTexts
        titleEditText = findViewById(R.id.editTextVacationTitle);
        accommodationEditText = findViewById(R.id.editTextAccommodation);
        startDateEditText = findViewById(R.id.editTextStartDate);
        endDateEditText = findViewById(R.id.editTextEndDate);

        // Initialize Save Button
        saveButton = findViewById(R.id.saveButton);

        // Get the intent and check if it's for editing an existing vacation
        Intent intent = getIntent();
        if (intent.hasExtra("vacationId")) {
            isEditMode = true;
            vacationId = intent.getIntExtra("vacationId", -1);
            prepopulateFields(intent); // Populate fields for editing
        }

        // Set onClickListeners for date selection
        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));

        // Save the vacation on button click
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

    private void prepopulateFields(Intent intent) {
        // Set the fields with the existing vacation data
        titleEditText.setText(intent.getStringExtra("title"));
        accommodationEditText.setText(intent.getStringExtra("accommodation"));
        startDateEditText.setText(intent.getStringExtra("startDate"));
        endDateEditText.setText(intent.getStringExtra("endDate"));
    }

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

    private void saveNewVacation() {
        String title = titleEditText.getText().toString();
        String accommodation = accommodationEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();

        // Create a new Vacation entity
        Vacation newVacation = new Vacation(0, title, startDate, endDate, accommodation);

        // Insert into database
        repository.insert(newVacation);

        // Go back to vacation list
        finish();
    }

    private void updateVacation() {
        String title = titleEditText.getText().toString();
        String accommodation = accommodationEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();

        // Create updated Vacation entity
        Vacation updatedVacation = new Vacation(vacationId, title, startDate, endDate, accommodation);

        // Update in the database
        repository.update(updatedVacation);

        // Go back to vacation list
        finish();
    }
}
