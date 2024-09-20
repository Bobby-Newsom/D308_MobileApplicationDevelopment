package com.example.bobbynewsom_001265608.UI;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobbynewsom_001265608.R;
import com.example.bobbynewsom_001265608.database.Repository;
import com.example.bobbynewsom_001265608.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {

    private Repository repository;
    private RecyclerView recyclerView;
    private VacationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);

        // Set up floating action button for navigating back to main activity
        FloatingActionButton fab = findViewById(R.id.backToMainActivityButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, MainActivity.class);
            startActivity(intent);
        });

        // Set up floating action button for adding a new vacation
        FloatingActionButton addFab = findViewById(R.id.addVacationButton);
        addFab.setOnClickListener(view -> {
            // Start VacationDetails activity to add a new vacation (no extras passed)
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });

        // Initialize RecyclerView and set up adapter
        recyclerView = findViewById(R.id.vacationListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VacationAdapter(this);
        recyclerView.setAdapter(adapter);

        // Initialize the repository and fetch vacations
        repository = new Repository(getApplication());
        fetchAndDisplayVacations();

        // Optionally insert test data (DELETE THIS IN PRODUCTION)
//        insertTestData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list when returning from add/edit activities
        fetchAndDisplayVacations();
    }

    private void fetchAndDisplayVacations() {
        // Fetch vacations from the repository and update the adapter
        List<Vacation> vacations = repository.getmAllVacations();
        adapter.setVacations(vacations);
    }

    // Method to insert test data (DELETE THIS IN PRODUCTION, REFERENCE IN MAINACTIVITY.JAVA)
//    public void insertTestData() {
//        // Make sure the data is only inserted once for testing purposes
//        Vacation vacation1 = new Vacation(0, "Bermuda Trip", "2024-10-15", "2024-10-20", "Ocean View Resort");
//        repository.insert(vacation1); // Persist the vacation in the database
//    }
}
