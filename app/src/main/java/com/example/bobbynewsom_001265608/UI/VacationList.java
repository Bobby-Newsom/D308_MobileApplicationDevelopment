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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addVacationButton), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the repository
        repository = new Repository(getApplication());

        // Set up RecyclerView
        recyclerView = findViewById(R.id.vacationListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display vacations
        List<Vacation> vacations = repository.getAllVacations();
        adapter = new VacationAdapter(vacations);
        recyclerView.setAdapter(adapter);
    }
}
