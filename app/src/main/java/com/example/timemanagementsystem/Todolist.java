package com.example.timemanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Paint;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Todolist extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TodoListAdapter adapter;
    private List<Task> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_main);

        // Load tasks from file
        TaskManager taskManager = new TaskManager();
        todoList = taskManager.loadTasks(this);

        // If tasks are null, initialize an empty list
        if (todoList == null) {
            todoList = new ArrayList<>();
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TodoListAdapter(todoList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton addTaskButton = findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(v -> addTask());
    }




    private void addTask() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add task");

        View view = LayoutInflater.from(this).inflate(R.layout.add_task_dialog, null);
        EditText input = view.findViewById(R.id.task_title_input);
        DatePicker datePicker = view.findViewById(R.id.date_picker);

        builder.setView(view);


        Spinner prioritySpinner = view.findViewById(R.id.task_priority_spinner);


        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this, R.array.priority_levels, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);





        builder.setPositiveButton("Add", (dialog, which) -> {
            int priority = prioritySpinner.getSelectedItemPosition();


            String taskTitle = input.getText().toString().trim();
            if (!taskTitle.isEmpty()) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                Date dueDate = calendar.getTime();
                int taskPriority = prioritySpinner.getSelectedItemPosition();


                todoList.add(new Task(taskTitle, false, dueDate, taskPriority));
                TaskManager taskManager = new TaskManager();
                taskManager.saveTasks(todoList, this);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Please enter a task name.", Toast.LENGTH_SHORT).show();
            }

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void editTask(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(todoList.get(position).getTitle());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String updatedTask = input.getText().toString().trim();
            if (!updatedTask.isEmpty()) {
                todoList.get(position).setTitle(updatedTask);
                TaskManager taskManager = new TaskManager();
                taskManager.saveTasks(todoList, this);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Please enter a task name.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    public class TodoItemViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle;
        public CheckBox checkBox;
        public TextView taskDueDate; // Add the due date field
        public ImageButton deleteButton;
        public ImageButton editButton;
        public TextView taskPriority;

        public TodoItemViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.task_title);
            checkBox = itemView.findViewById(R.id.checkbox);
            taskDueDate = itemView.findViewById(R.id.task_due_date); // Initialize the due date field
            deleteButton = itemView.findViewById(R.id.delete_button);
            editButton = itemView.findViewById(R.id.edit_button);
            taskPriority = itemView.findViewById(R.id.task_priority_spinner);

        }
    }



    public class TodoListAdapter extends RecyclerView.Adapter<TodoItemViewHolder> {
        private List<Task> todoList;

        public TodoListAdapter(List<Task> todoList) {
            this.todoList = todoList;
        }

        @NonNull
        @Override
        public TodoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
            return new TodoItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TodoItemViewHolder holder, int position) {
            Task task = todoList.get(position);
            holder.taskTitle.setText(task.getTitle());
            holder.checkBox.setChecked(task.isCompleted());
            holder.taskPriority.setText(getResources().getStringArray(R.array.priority_levels)[task.getPriority()]);


            if (task.getDueDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                holder.taskDueDate.setText(sdf.format(task.getDueDate().getTime()));
                holder.taskDueDate.setVisibility(View.VISIBLE);
            } else {
                holder.taskDueDate.setVisibility(View.GONE);
            }

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setCompleted(isChecked);
                holder.taskTitle.setPaintFlags(isChecked ? holder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : holder.taskTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            });

            holder.deleteButton.setOnClickListener(v -> {
                todoList.remove(position);
                TaskManager taskManager = new TaskManager();
                Context context = holder.itemView.getContext();
                taskManager.saveTasks(todoList, context);
                notifyDataSetChanged();
            });

            holder.editButton.setOnClickListener(v -> {
                ((Todolist) v.getContext()).editTask(position);
            });
        }



        @Override
        public int getItemCount() {
            return todoList.size();
        }
    }
    public class Task {
        private String title;
        private boolean completed;
        private Date dueDate;
        private int priority;


        public Task(String title, boolean completed, Date dueDate, int priority) {
            this.title = title;
            this.completed = completed;
            this.dueDate = dueDate;
            this.priority = priority;

        }

        // Getters and setters
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }

        public Date getDueDate() {
            return dueDate;
        }

        public void setDueDate(Date dueDate) {
            this.dueDate = dueDate;
        }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
    }

    public class TaskManager {

        private static final String FILE_NAME = "tasks.txt";

        public void saveTasks(List<Task> tasks, Context context) {
            try {
                FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(tasks);
                oos.close();
                fos.close();

                Log.d("TaskManager", "Tasks saved successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public List<Task> loadTasks(Context context) {
            List<Task> tasks = null;
            try {
                FileInputStream fis = context.openFileInput(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                tasks = (List<Task>) ois.readObject();
                ois.close();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return tasks;
        }


        public void deleteTasks(Context context) {
            context.deleteFile(FILE_NAME);
        }
    }


}
