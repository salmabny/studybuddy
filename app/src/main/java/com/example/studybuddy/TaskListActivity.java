package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private TaskAdapter taskAdapter;
    private DatabaseHelper db;
    private FloatingActionButton fabAddTask;
    private LinearLayout tvEmptyState;
    private TextView tvTaskCount;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        // Initialiser les vues
        rvTasks = findViewById(R.id.rvTasks);
        fabAddTask = findViewById(R.id.fabAddTask);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        tvTaskCount = findViewById(R.id.tvTaskCount);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        db = new DatabaseHelper(this);

        // Configurer RecyclerView
        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        loadTasks();

        // Ajouter une nouvelle tâche
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Bottom navigation
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // TODO : aller à l'accueil
                return true;
            } else if (id == R.id.nav_planning) {
                // déjà sur Planning / TaskList
                return true;
            } else if (id == R.id.nav_progress) {
                // TODO : aller à Progression
                return true;
            } else if (id == R.id.nav_profile) {
                // TODO : aller à Profil
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks(); // recharger les tâches si on revient d'ajout/modification
    }

    private void loadTasks() {
        List<Task> taskList = db.getAllTasks(); // méthode getAllTasks() à créer dans DatabaseHelper

        if (taskList.isEmpty()) {
            rvTasks.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvTasks.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);

            taskAdapter = new TaskAdapter(this, taskList);
            rvTasks.setAdapter(taskAdapter);

            tvTaskCount.setText(String.valueOf(taskList.size()));
        }
    }

    // Méthodes pour éditer et supprimer une tâche
    public void editTask(Task task) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra("task_id", task.getId());
        startActivity(intent);
    }

    public void deleteTask(Task task) {
        db.deleteTask(task.getId());
        loadTasks();
    }
}
