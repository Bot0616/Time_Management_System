package com.example.timemanagementsystem;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class shoppinglist extends AppCompatActivity {

    static ListView listView;
    static ArrayList<String> item;
    static ViewAdapter adapters;
    EditText keyin;
    ImageView enter;
    static Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_main);

        listView = findViewById(R.id.listview);
        keyin = findViewById(R.id.keyin);
        enter = findViewById(R.id.enter);

        item = new ArrayList<>();
        c = getApplicationContext();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int x, long y)
            {
                String name = item.get(x);
                makeToast(name);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int x, long y) {

                makeToast("Deleted:" + item.get(x));
                deleteItem(x);
                return false;
            }
        });

        adapters = new ViewAdapter(getApplicationContext(),item);
        listView.setAdapter(adapters);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = keyin.getText().toString();
                if(text.length() == 0)
                {
                    makeToast("Keyin an item.");
                }else
                    {
                    addItem(text);
                    keyin.setText("");
                    makeToast("Added:" + text);
                    }
            }
        });
        readfile();
    }

    static Toast t;
    private static void makeToast(String s){
        if(t!= null) t.cancel();
        t = Toast.makeText(c,s,Toast.LENGTH_SHORT);
        t.show();

    }

    public void readfile() {

        File access = getApplicationContext().getFilesDir();
        File readFile = new File(access, "shoppinglist.txt");
        byte[] data = new byte[(int) readFile.length()];

        FileInputStream newStream = null;
        try {
            newStream = new FileInputStream(readFile);
            newStream.read(data);

            String s = new String(data);
            s = s.substring(1, s.length() - 1);
            String split[] = s.split(",");

            //when it is empty
            if (split.length == 1 && split[0].isEmpty())
                item = new ArrayList<>();
            else item = new ArrayList<>(Arrays.asList(split));

            adapters = new ViewAdapter(this,item);
            listView.setAdapter(adapters);

        } catch (Exception e)
            {
                 e.printStackTrace();
            }
    }

    //after delete item
    public static void deleteItem (int delete){
        makeToast("Removed: " + item.get(delete));
        item.remove(delete);
        listView.setAdapter(adapters);
    }

    //to add item
    public static void addItem(String items){
        item.add(items);
        listView.setAdapter(adapters);
    }

    @Override
    protected void onDestroy() {
        File access = getApplicationContext().getFilesDir();

        try {
            FileOutputStream fileStream = new FileOutputStream(new File(access,"shoppinglist.txt"));
            fileStream.write(item.toString().getBytes());
            fileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}