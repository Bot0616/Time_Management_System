package com.example.timemanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class TaskDatabase {
    public static final String MYDATABASE_NAME = "TASK_DATABASE";
    public static final String MYDATABASE_TABLE = "TASK_TABLE";
    public static final int MYDATABASE_VERSION = 1;
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String COMPLETED = "completed";

    private static final String SCRIPT_CREATE_DATABASE = "create table "
            + MYDATABASE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " text not null, "
            + DATE + " int, "
            + COMPLETED + " int);";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public TaskDatabase(Context c){
        context = c;
    }

    public TaskDatabase openToRead() throws android.database.SQLException{
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public TaskDatabase openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public long insertTask(com.example.timemanagementsystem.Task task) {
        ContentValues values = new ContentValues();
        values.put(NAME, task.getName());
        values.put(DATE, task.getDate().getTime());
        values.put(COMPLETED, task.isCompleted() ? 1 : 0);
        return sqLiteDatabase.insert(MYDATABASE_TABLE, null, values);
    }

    public ArrayList<com.example.timemanagementsystem.Task> retrieveTask(Date date){
        long dateInMillis = date.getTime();
        ArrayList<com.example.timemanagementsystem.Task> tasks = new ArrayList<>();
        String[] columns = new String[] { ID, NAME, COMPLETED };
//        String[] columns = new String[] { COMPLETED };
        String[] selectionArgs = { String.valueOf(dateInMillis) };
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns
        , DATE + "=?"
        , selectionArgs, null, null, ID + " ASC");

        String result = "";

        if (cursor.getCount() == 0){
            result = "No task found for this date";
            return null;
        }
        else {
            int index_ID = cursor.getColumnIndex(ID);
            int index_NAME = cursor.getColumnIndex(NAME);
            int index_COMPLETED = cursor.getColumnIndex(COMPLETED);
            if(cursor.moveToFirst()){
                do{
                    int id = cursor.getInt(index_ID);
                    String name = cursor.getString(index_NAME);
                    boolean completed = cursor.getInt(index_COMPLETED) == 1;
                    com.example.timemanagementsystem.Task task = new com.example.timemanagementsystem.Task(id, name, date, completed);
                    tasks.add(task);
                }while(cursor.moveToNext());
            }

        }

        cursor.close();
        return tasks;
    }

    public void updateTask(com.example.timemanagementsystem.Task task) {
        ContentValues values = new ContentValues();
        Log.i("TaskDatabase", task.getName());
        values.put(NAME, task.getName());
        values.put(DATE, task.getDate().getTime());
        values.put(COMPLETED, task.isCompleted() ? 1 : 0);

        sqLiteDatabase.update(MYDATABASE_TABLE, values, ID + "=?", new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(int id) {
        sqLiteDatabase.delete(MYDATABASE_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SCRIPT_CREATE_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SCRIPT_CREATE_DATABASE);
        }
    }
}
