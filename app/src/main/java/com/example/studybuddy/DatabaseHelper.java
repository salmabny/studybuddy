package com.example.studybuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nom et version de la base
    private static final String DATABASE_NAME = "StudyBuddy.db";
    private static final int DATABASE_VERSION = 1;

    // Table Users
    private static final String TABLE_USERS = "users";
    private static final String USER_ID = "id";
    private static final String USER_FULLNAME = "full_name"; // corrigé
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";
    private static final String USER_CREATED_AT = "created_at";

    // Table Tasks
    private static final String TABLE_TASKS = "tasks";
    private static final String TASK_ID = "id";
    private static final String TASK_TITLE = "title";
    private static final String TASK_DESCRIPTION = "description";
    private static final String TASK_DATE = "date";
    private static final String TASK_HOUR = "hour";
    private static final String TASK_SUBJECT = "subject";
    private static final String TASK_TYPE = "type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création table Users
        String createUsers = "CREATE TABLE " + TABLE_USERS + "("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_FULLNAME + " TEXT,"
                + USER_EMAIL + " TEXT,"
                + USER_PASSWORD + " TEXT,"
                + USER_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(createUsers);

        // Création table Tasks
        String createTasks = "CREATE TABLE " + TABLE_TASKS + "("
                + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASK_TITLE + " TEXT,"
                + TASK_DESCRIPTION + " TEXT,"
                + TASK_DATE + " TEXT,"
                + TASK_HOUR + " TEXT,"
                + TASK_SUBJECT + " TEXT,"
                + TASK_TYPE + " TEXT"
                + ")";
        db.execSQL(createTasks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // -------------------- USER METHODS --------------------

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_FULLNAME, user.getFullName());
        cv.put(USER_EMAIL, user.getEmail());
        cv.put(USER_PASSWORD, user.getPassword());
        cv.put(USER_CREATED_AT, user.getCreatedAt());

        long result = db.insert(TABLE_USERS, null, cv);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{USER_ID},
                USER_EMAIL + "=? AND " + USER_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                null,
                USER_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(USER_FULLNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(USER_PASSWORD)));
            user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(USER_CREATED_AT)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{USER_ID},
                USER_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // -------------------- TASK METHODS --------------------

    public boolean addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK_TITLE, task.getTitle());
        cv.put(TASK_DESCRIPTION, task.getDescription());
        cv.put(TASK_DATE, task.getDate());
        cv.put(TASK_HOUR, task.getHour());
        cv.put(TASK_SUBJECT, task.getSubject());
        cv.put(TASK_TYPE, task.getType());

        long result = db.insert(TABLE_TASKS, null, cv);
        db.close();
        return result != -1;
    }

    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK_TITLE, task.getTitle());
        cv.put(TASK_DESCRIPTION, task.getDescription());
        cv.put(TASK_DATE, task.getDate());
        cv.put(TASK_HOUR, task.getHour());
        cv.put(TASK_SUBJECT, task.getSubject());
        cv.put(TASK_TYPE, task.getType());

        int result = db.update(TABLE_TASKS, cv, TASK_ID + "=?", new String[]{String.valueOf(task.getId())});
        db.close();
        return result > 0;
    }

    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_TASKS, TASK_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS,
                null, null, null, null, null,
                TASK_DATE + " ASC, " + TASK_HOUR + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TASK_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TASK_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TASK_DESCRIPTION)));
                task.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TASK_DATE)));
                task.setHour(cursor.getString(cursor.getColumnIndexOrThrow(TASK_HOUR)));
                task.setSubject(cursor.getString(cursor.getColumnIndexOrThrow(TASK_SUBJECT)));
                task.setType(cursor.getString(cursor.getColumnIndexOrThrow(TASK_TYPE)));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }
}
