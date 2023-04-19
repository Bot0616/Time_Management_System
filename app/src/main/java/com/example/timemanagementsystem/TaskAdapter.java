package com.example.timemanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<com.example.timemanagementsystem.Task> taskList;
//    private OnItemClickListener mListener;
    private OnTaskClickListener taskClickListener;
    private OnTaskCheckboxClickListener taskCheckboxClickListener;
    private OnTaskDeleteClickListener taskDeleteClickListener;

    public interface OnTaskClickListener{
        void onTaskClick(com.example.timemanagementsystem.Task task);
    }

    public interface OnTaskCheckboxClickListener {
        void onTaskCheckboxClick(com.example.timemanagementsystem.Task task, boolean isChecked);
    }

    public interface OnTaskDeleteClickListener {
        void onTaskDeleteClick(com.example.timemanagementsystem.Task task);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        taskClickListener = listener;
    }

    public void setOnTaskCheckboxClickListener(OnTaskCheckboxClickListener listener) {
        taskCheckboxClickListener = listener;
    }

    public void setOnTaskDeleteClickListener(OnTaskDeleteClickListener listener) {
        taskDeleteClickListener = listener;
    }

    public TaskAdapter(OnTaskClickListener taskClickListener, OnTaskCheckboxClickListener taskCheckboxClickListener, OnTaskDeleteClickListener taskDeleteClickListener) {
        this.taskClickListener = taskClickListener;
        this.taskCheckboxClickListener = taskCheckboxClickListener;
        this.taskDeleteClickListener = taskDeleteClickListener;
    }

    public void updateTaskList(ArrayList<com.example.timemanagementsystem.Task> taskList) {
        if(taskList == null || taskList.isEmpty()){
            this.taskList = new ArrayList<>();
        }
        else{
            this.taskList = taskList;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public CheckBox mCheckBox;
        public Button mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.taskTitleTextView);
            mCheckBox = itemView.findViewById(R.id.taskCompleteCheckBox);
            mButton = itemView.findViewById(R.id.taskDeleteButton);

            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        com.example.timemanagementsystem.Task task = taskList.get(position);
                        taskClickListener.onTaskClick(task);
                    }
                }
            });

            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        com.example.timemanagementsystem.Task task = taskList.get(position);
                        boolean isChecked = mCheckBox.isChecked();
                        taskCheckboxClickListener.onTaskCheckboxClick(task, isChecked);
                    }
                }
            });

            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        com.example.timemanagementsystem.Task task = taskList.get(position);
                        taskDeleteClickListener.onTaskDeleteClick(task);
                    }
                }
            });
        }
    }

    public TaskAdapter(ArrayList<com.example.timemanagementsystem.Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        com.example.timemanagementsystem.Task currentItem = taskList.get(position);

        holder.mTextView.setText(currentItem.getName());
        holder.mCheckBox.setChecked(currentItem.isCompleted());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}



