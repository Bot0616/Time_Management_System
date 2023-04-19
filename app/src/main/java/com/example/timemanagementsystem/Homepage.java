package com.example.timemanagementsystem;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_main);
    }

    public void onButton1Clicked(View view) {
        Intent intent = new Intent(this, Todolist.class);
        startActivity(intent);
    }

    public void onButton2Clicked(View view) {
        Intent intent = new Intent(this, Calendar_Main.class);
        startActivity(intent);
    }

    public void onButton3Clicked(View view) {
        Intent intent = new Intent(this, Pomodoro.class);
        startActivity(intent);
    }

    public void onButton4Clicked(View view) {
        Intent intent = new Intent(this, shoppinglist.class);
        startActivity(intent);
    }
}
