package com.example.studybuddy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDate, etHour, etSubject, etDescription;
    private RadioGroup rgType;
    private Button btnCreate;
    private ImageView btnBack;

    private DatabaseHelper dbHelper;
    private int taskId = -1;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHelper = new DatabaseHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);
        etHour = findViewById(R.id.etHour);
        etSubject = findViewById(R.id.etSubject);
        etDescription = findViewById(R.id.etDescription);
        rgType = findViewById(R.id.rgType);
        btnCreate = findViewById(R.id.btnCreate);
        btnBack = findViewById(R.id.btnBack);

        // Retour vers TaskListActivity
        btnBack.setOnClickListener(v -> finish());

        // ---------------- DatePicker et TimePicker ----------------
        etDate.setOnClickListener(v -> showDatePicker());
        etHour.setOnClickListener(v -> showTimePicker());

        // ---------------- Chargement des données si modification ----------------
        if (getIntent() != null && getIntent().hasExtra("task_id")) {
            taskId = getIntent().getIntExtra("task_id", -1);
            loadTaskData(taskId);
            btnCreate.setText("MODIFIER LA TÂCHE");
        }

        btnCreate.setOnClickListener(v -> saveOrUpdateTask());
    }

    // Affiche le DatePicker
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(AddTaskActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String dateText = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etDate.setText(dateText);
                }, year, month, day);
        datePicker.show();
    }

    // Affiche le TimePicker
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(AddTaskActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    String timeText = String.format("%02d:%02d", selectedHour, selectedMinute);
                    etHour.setText(timeText);
                }, hour, minute, true);
        timePicker.show();
    }

    // Charge les données de la tâche pour modification
    private void loadTaskData(int id) {
        currentTask = dbHelper.getAllTasks().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);

        if (currentTask != null) {
            etTitle.setText(currentTask.getTitle());
            etDate.setText(currentTask.getDate());
            etHour.setText(currentTask.getHour());
            etSubject.setText(currentTask.getSubject());
            etDescription.setText(currentTask.getDescription());

            switch (currentTask.getType()) {
                case "Cours": rgType.check(R.id.rbCours); break;
                case "Devoir": rgType.check(R.id.rbDevoir); break;
                case "Examen": rgType.check(R.id.rbExamen); break;
            }
        }
    }

    // Sauvegarde ou met à jour la tâche
    private void saveOrUpdateTask() {
        String title = etTitle.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String hour = etHour.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        int selectedTypeId = rgType.getCheckedRadioButtonId();
        String type = "";
        if (selectedTypeId == R.id.rbCours) type = "Cours";
        else if (selectedTypeId == R.id.rbDevoir) type = "Devoir";
        else if (selectedTypeId == R.id.rbExamen) type = "Examen";

        if (title.isEmpty()) { etTitle.setError("Titre requis"); return; }
        if (date.isEmpty()) { etDate.setError("Date requise"); return; }
        if (hour.isEmpty()) { etHour.setError("Heure requise"); return; }

        if (taskId == -1) {
            Task newTask = new Task(title, description, date, hour, subject, type);
            boolean added = dbHelper.addTask(newTask);
            if (added) Toast.makeText(this, "Tâche ajoutée avec succès", Toast.LENGTH_SHORT).show();
        } else {
            currentTask.setTitle(title);
            currentTask.setDescription(description);
            currentTask.setDate(date);
            currentTask.setHour(hour);
            currentTask.setSubject(subject);
            currentTask.setType(type);

            boolean updated = dbHelper.updateTask(currentTask);
            if (updated) Toast.makeText(this, "Tâche modifiée avec succès", Toast.LENGTH_SHORT).show();
        }

        finish(); // Retour à TaskListActivity
    }
}
