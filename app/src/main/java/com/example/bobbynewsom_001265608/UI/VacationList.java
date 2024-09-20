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

        FloatingActionButton fab = findViewById(R.id.backToMainActivityButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, MainActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.vacationListRecyclerView);

        final VacationAdapter adapter = new VacationAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize the repository
        repository = new Repository(getApplication());

        // Optionally check and insert test data
        insertTestData(); // This ensures test data is loaded

        // Fetch and display vacations
        List<Vacation> vacations = repository.getmAllVacations();
        adapter.setVacations(vacations);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> vacations = repository.getmAllVacations();
        recyclerView = findViewById(R.id.vacationListRecyclerView);

        final VacationAdapter adapter = new VacationAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setVacations(vacations);
        adapter.notifyDataSetChanged();
    }

    public void insertTestData(){
        Repository repository = new Repository(getApplication());
        Vacation vacation1 = new Vacation(0, "Bermuda Trip", "2024-10-15", "2024-10-20", "Ocean View Resort");
        repository.insert(vacation1); // Make sure this method actually persists the data
    }
}
