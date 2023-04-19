package com.example.timemanagementsystem;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Calendar_Main extends AppCompatActivity implements TaskAdapter.OnTaskClickListener, TaskAdapter.OnTaskCheckboxClickListener, TaskAdapter.OnTaskDeleteClickListener{

    private CalendarView calendarView;
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private Button previousMonthButton;
    private Button nextMonthButton;
    private Button addTaskButton;
    private Date selectedDate;
    private TaskDatabase taskdb;
    private ArrayList<com.example.timemanagementsystem.Task> taskList;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_main);

        calendarView = findViewById(R.id.calendarView);
        taskRecyclerView = findViewById(R.id.taskRecyclerView);

        Calendar currentDate = Calendar.getInstance();
        selectedDate = new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).getTime();
        taskList = getTasksForDate(selectedDate);
        displayTask(taskList);

        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        minDate.set(Calendar.MONTH, currentDate.get(Calendar.MONTH));
        minDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
        calendarView.setMinDate(minDate.getTimeInMillis());

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, maxDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        maxDate.set(Calendar.MONTH, currentDate.get(Calendar.MONTH));
        maxDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
        calendarView.setMaxDate(maxDate.getTimeInMillis());

        previousMonthButton = findViewById(R.id.previousMonthButton);
        nextMonthButton = findViewById(R.id.nextMonthButton);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendarView.getDate());

        previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentYear = calendar.get(Calendar.YEAR);

                int previousMonth = currentMonth - 1;
                int previousYear = currentYear;
                if(previousMonth < 0){
                    previousMonth = 11;
                    previousYear--;
                }

                calendar.set(previousYear, previousMonth, 1);
                Calendar minDate = calendar;
                minDate.set(Calendar.DAY_OF_MONTH, 1);
                minDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                minDate.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                calendarView.setMinDate(minDate.getTimeInMillis());

                Calendar maxDate = calendar;
                maxDate.set(Calendar.DAY_OF_MONTH, maxDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                maxDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                maxDate.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                calendarView.setMaxDate(maxDate.getTimeInMillis());

//                selectedDate = new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), 1).getTime();
//                taskList = getTasksForDate(selectedDate);
//                displayTask(taskList);

                taskRecyclerView.setVisibility(View.INVISIBLE);
                addTaskButton.setVisibility(View.INVISIBLE);
            }
        });

        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentYear = calendar.get(Calendar.YEAR);

                int nextMonth = currentMonth + 1;
                int nextYear = currentYear;
                if (nextMonth > 11) {
                    nextMonth = 0;
                    nextYear++;
                }

                calendar.set(nextYear, nextMonth, 1);
                Calendar minDate = calendar;
                minDate.set(Calendar.DAY_OF_MONTH, 1);
                minDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                minDate.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                calendarView.setMinDate(minDate.getTimeInMillis());

                Calendar maxDate = calendar;
                maxDate.set(Calendar.DAY_OF_MONTH, maxDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                maxDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                maxDate.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                calendarView.setMaxDate(maxDate.getTimeInMillis());

//                selectedDate = new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), 1).getTime();
//                taskList = getTasksForDate(selectedDate);
//                displayTask(taskList);

                taskRecyclerView.setVisibility(View.INVISIBLE);
                addTaskButton.setVisibility(View.INVISIBLE);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                selectedDate = new GregorianCalendar(year, month, day).getTime();
                taskList = getTasksForDate(selectedDate);
                displayTask(taskList);

                taskRecyclerView.setVisibility(View.VISIBLE);
                addTaskButton.setVisibility(View.VISIBLE);
            }
        });

        addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });

    }

   private void showAddTaskDialog() {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle("Add Task");

       View view = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
       final EditText taskNameEditText = view.findViewById(R.id.taskNameEditText);

       builder.setView(view);

       builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               String taskName = taskNameEditText.getText().toString().trim();
               if(!taskName.isEmpty()){
                   addTask(taskName);
               }
           }
       });

       builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.cancel();
           }
       });

       builder.show();
    }

    private void showUpdateTaskTitleDialog(com.example.timemanagementsystem.Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Task");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        final EditText taskNameEditText = view.findViewById(R.id.taskNameEditText);

        builder.setView(view);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String taskName = taskNameEditText.getText().toString().trim();
                if(!taskName.isEmpty()){
                    updateTask(taskName, task.isCompleted(), task.getId());
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void displayTask(ArrayList<com.example.timemanagementsystem.Task> task){
        taskRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(task);
        taskRecyclerView.setAdapter(taskAdapter);
        taskAdapter.setOnTaskClickListener(this);
        taskAdapter.setOnTaskCheckboxClickListener(this);
        taskAdapter.setOnTaskDeleteClickListener(this);
        taskAdapter.updateTaskList(task);
    }

    private void addTask(String taskName) {
        com.example.timemanagementsystem.Task task = new com.example.timemanagementsystem.Task(id, taskName, selectedDate, false);

        taskdb = new TaskDatabase(this);
        taskdb.openToWrite();
        taskdb.insertTask(task);
        taskdb.close();

        ArrayList<com.example.timemanagementsystem.Task> taskRetrieved = new ArrayList<>();
        taskRetrieved = getTasksForDate(selectedDate);
        displayTask(taskRetrieved);
    }

    private ArrayList<com.example.timemanagementsystem.Task> getTasksForDate(Date date) {
        ArrayList<com.example.timemanagementsystem.Task> taskRetrieved = new ArrayList<>();
        taskdb = new TaskDatabase(this);
        taskdb.openToRead();
        taskRetrieved = taskdb.retrieveTask(date);
        taskdb.close();
        return taskRetrieved;
    }

    private void updateTask(String taskName, boolean completed, int id){
        com.example.timemanagementsystem.Task task = new com.example.timemanagementsystem.Task(id, taskName, selectedDate, completed);

        taskdb = new TaskDatabase(this);
        taskdb.openToWrite();
        taskdb.updateTask(task);
        taskdb.close();

        ArrayList<com.example.timemanagementsystem.Task> taskRetrieved = new ArrayList<>();
        taskRetrieved = getTasksForDate(selectedDate);
        displayTask(taskRetrieved);
    }

    private void deleteTask(int id) {
        taskdb = new TaskDatabase(this);
        taskdb.openToWrite();
        taskdb.deleteTask(id);
        taskdb.close();

        ArrayList<com.example.timemanagementsystem.Task> taskRetrieved = new ArrayList<>();
        taskRetrieved = getTasksForDate(selectedDate);
        displayTask(taskRetrieved);
    }

    @Override
    public void onTaskClick(com.example.timemanagementsystem.Task task) {
        showUpdateTaskTitleDialog(task);
    }

    @Override
    public void onTaskCheckboxClick(com.example.timemanagementsystem.Task task, boolean isChecked) {
        updateTask(task.getName(), isChecked, task.getId());
    }

    @Override
    public void onTaskDeleteClick(com.example.timemanagementsystem.Task task) {
        deleteTask(task.getId());
    }
}