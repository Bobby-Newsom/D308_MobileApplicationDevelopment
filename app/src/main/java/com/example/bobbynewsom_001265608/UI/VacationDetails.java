package com.example.bobbynewsom_001265608.UI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Switch;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobbynewsom_001265608.R;
import com.example.bobbynewsom_001265608.database.Repository;
import com.example.bobbynewsom_001265608.entities.Excursion;
import com.example.bobbynewsom_001265608.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VacationDetails extends AppCompatActivity {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private EditText titleEditText;
    private EditText accommodationEditText;
    private Button saveButton;
    private Button deleteButton;
    private Switch startAlertSwitch;
    private Switch endAlertSwitch;

    // Excursion RecyclerView
    private RecyclerView excursionRecyclerView;
    private ExcursionAdapter excursionAdapter;

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

        // Initialize EditTexts, Switches, and Buttons
        titleEditText = findViewById(R.id.editTextVacationTitle);
        accommodationEditText = findViewById(R.id.editTextAccommodation);
        startDateEditText = findViewById(R.id.editTextStartDate);
        endDateEditText = findViewById(R.id.editTextEndDate);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        startAlertSwitch = findViewById(R.id.switchStartDateAlert);
        endAlertSwitch = findViewById(R.id.switchEndDateAlert);

        //Task Requirement B.3.G. : Display a list of excursions associated with each vacation
        // Initialize RecyclerView on the Vacation Details/Edit page for Excursions displays a list of excursions associated with each vacation
        excursionRecyclerView = findViewById(R.id.excursionRecyclerView);
        excursionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        excursionAdapter = new ExcursionAdapter();
        excursionRecyclerView.setAdapter(excursionAdapter);

        // Share button support
        Button shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> shareVacationDetails());

        // Check if editing or creating a new vacation
        Intent intent = getIntent();
        if (intent.hasExtra("vacationId")) {
            isEditMode = true;
            vacationId = intent.getIntExtra("vacationId", -1);
            prepopulateFields(vacationId);  // Load data from database
        }
        //Task Requirement B.3.C. : Include validation that the input dates are formatted correctly
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

        // Load associated excursions
        loadExcursions(vacationId);

        // Set the RecyclerView item click listener for editing an excursion
        excursionAdapter.setOnItemClickListener(excursion -> {
            Intent excursionIntent = new Intent(VacationDetails.this, ExcursionDetails.class);
            excursionIntent.putExtra("excursionId", excursion.getExcursionId());
            startActivity(excursionIntent);
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addVacationButton), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Delete button logic
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    //Task Requirement B.3.G. : Display a list of excursions associated with each vacation
    // Load excursions associated with the current vacation
    private void loadExcursions(int vacationId) {
        executor.execute(() -> {
            List<Excursion> excursions = repository.getExcursionsForVacation(vacationId);
            runOnUiThread(() -> {
                // Update the RecyclerView adapter with the new list
                excursionAdapter.setExcursions(excursions);
                excursionAdapter.notifyDataSetChanged(); // Ensure the UI reflects the updated data
            });
        });
    }

    // Add options menu for adding excursions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    // Handle menu item selection for adding excursions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_excursion) {
            if (vacationId == -1) {
                // If the vacation hasn't been saved yet (vacationId is -1), show an alert
                new AlertDialog.Builder(this)
                        .setTitle("Cannot Add Excursion")
                        .setMessage("Please save the vacation before adding excursions.")
                        .setPositiveButton("OK", null)
                        .show();
                return true;
            } else {
                // If the vacation is saved, proceed to add an excursion
                Intent intent = new Intent(this, ExcursionDetails.class);
                intent.putExtra("vacationId", vacationId); // Pass the vacation ID
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    // Prepopulate fields if in edit mode
    private void prepopulateFields(int vacationId) {
        executor.execute(() -> {
            Vacation vacation = repository.getVacationById(vacationId);  // Fetch from database

            runOnUiThread(() -> {
                if (vacation != null) {
                    titleEditText.setText(vacation.getTitle());
                    accommodationEditText.setText(vacation.getAccommodation());
                    startDateEditText.setText(vacation.getStartDate());
                    endDateEditText.setText(vacation.getEndDate());

                    // Set switch states
                    startAlertSwitch.setChecked(vacation.isStartAlertEnabled());
                    endAlertSwitch.setChecked(vacation.isEndAlertEnabled());
                }
            });
        });
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

    //Task Requirement B.3.D. : Include validation that the vacation start date is not after the end date
    // Save a new vacation with date validation and switch states
    private void saveNewVacation() {
        String title = titleEditText.getText().toString().trim();
        String accommodation = accommodationEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();
        boolean startAlertEnabled = startAlertSwitch.isChecked();
        boolean endAlertEnabled = endAlertSwitch.isChecked();

        // Validate that title and accommodation are not empty
        if (title.isEmpty() || accommodation.isEmpty()) {
            Toast.makeText(this, "Title and Accommodation are required fields", Toast.LENGTH_SHORT).show();
            return; // Stop execution if validation fails
        }

        // Validate the dates
        if (!isDateValid(startDate, endDate)) {
            Toast.makeText(this, "Start date cannot be after the end date", Toast.LENGTH_SHORT).show();
            return; // Stop execution if validation fails
        }

        // Create new Vacation entity
        Vacation newVacation = new Vacation(0, title, startDate, endDate, accommodation, startAlertEnabled, endAlertEnabled);

        // Insert into database
        repository.insert(newVacation);

        // Set up notifications for start and end date if switches are enabled
        if (startAlertEnabled) {
            scheduleNotification(title, startDate, "Vacation Starting", "start_action");
        }
        if (endAlertEnabled) {
            scheduleNotification(title, endDate, "Vacation Ending", "end_action");
        }

        finish(); // Go back to the vacation list
    }

    // Update an existing vacation with date validation and switch states
    private void updateVacation() {
        String title = titleEditText.getText().toString().trim();
        String accommodation = accommodationEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();
        boolean startAlertEnabled = startAlertSwitch.isChecked();
        boolean endAlertEnabled = endAlertSwitch.isChecked();

        // Validate that title and accommodation are not empty
        if (title.isEmpty() || accommodation.isEmpty()) {
            Toast.makeText(this, "Title and Accommodation are required fields", Toast.LENGTH_SHORT).show();
            return; // Stop execution if validation fails
        }

        // Validate the vacation start and end dates
        if (!isDateValid(startDate, endDate)) {
            Toast.makeText(this, "Start date cannot be after the end date", Toast.LENGTH_SHORT).show();
            return; // Stop execution if validation fails
        }

        // Fetch associated excursions and validate their dates
        executor.execute(() -> {
            List<Excursion> excursions = repository.getExcursionsForVacation(vacationId);
            boolean datesValid = true;

            try {
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);

                // Check if any excursion date is outside the vacation date range
                for (Excursion excursion : excursions) {
                    Date excursionDate = dateFormat.parse(excursion.getDate());
                    if (excursionDate.before(start) || excursionDate.after(end)) {
                        datesValid = false;
                        break;
                    }
                }

                final boolean finalDatesValid = datesValid;

                runOnUiThread(() -> {
                    if (!finalDatesValid) {
                        // If dates are invalid, show an alert to the user
                        new AlertDialog.Builder(this)
                                .setTitle("Invalid Dates")
                                .setMessage("One or more excursions have dates outside the updated vacation range. Please adjust excursion dates or modify the vacation dates accordingly.")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        // Proceed with updating the vacation if dates are valid
                        Vacation updatedVacation = new Vacation(vacationId, title, startDate, endDate, accommodation, startAlertEnabled, endAlertEnabled);
                        repository.update(updatedVacation);

                        // Set up notifications for start and end date if switches are enabled
                        if (startAlertEnabled) {
                            scheduleNotification(title, startDate, "Vacation Starting", "start_action");
                        }
                        if (endAlertEnabled) {
                            scheduleNotification(title, endDate, "Vacation Ending", "end_action");
                        }

                        finish(); // Go back to the vacation list
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error parsing dates. Please try again.", Toast.LENGTH_SHORT).show());
            }
        });
    }



    // Method to format vacation details into a string for "Share" support
    private String getVacationDetails() {
        String title = titleEditText.getText().toString();
        String accommodation = accommodationEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();
        boolean startAlertEnabled = startAlertSwitch.isChecked();
        boolean endAlertEnabled = endAlertSwitch.isChecked();

        return "Vacation Title: " + title + "\n" +
                "Accommodation: " + accommodation + "\n" +
                "Start Date: " + startDate + (startAlertEnabled ? " (Alert Enabled)" : "") + "\n" +
                "End Date: " + endDate + (endAlertEnabled ? " (Alert Enabled)" : "");
    }

    // Method to give options for sharing vacations
    private void shareVacationDetails() {
        String vacationDetails = getVacationDetails();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vacation Details");
        shareIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails);

        // Show options to the user for sharing (email, SMS, etc.)
        startActivity(Intent.createChooser(shareIntent, "Share Vacation Details via"));
    }

    // Method to copy vacation details to clipboard
    private void copyToClipboard() {
        String vacationDetails = getVacationDetails();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Vacation Details", vacationDetails);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Vacation details copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    // Method to schedule a notification with the vacation/excursion title included along with debugging
    private void scheduleNotification(String title, String dateString, String notificationTitle, String action) {
        try {
            Date date = dateFormat.parse(dateString);
            long triggerTime = date.getTime();

            // Create an intent for the BroadcastReceiver
            Intent intent = new Intent(this, MyReceiver.class);
            intent.setAction(action);

            // Pass the correct title and message to the receiver
            intent.putExtra("alert_message", notificationTitle + ": " + title);
            intent.putExtra("alert_type", "vacation");  // Add type identifier

            // Create a unique PendingIntent with a different requestCode for vacations
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

    // Task Requirement B.3.D. : Include validation that the vacation start date is not after the end date
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

    // Task Requirement B.3.G. : Display a list of excursions associated with each vacation
    // Reload excursions whenever the user comes back to the VacationDetails activity
    @Override
    protected void onResume() {
        super.onResume();

        // loads excursions associated with the current vacation by its ID
        loadExcursions(vacationId); // refresh the RecyclerView
    }

}
