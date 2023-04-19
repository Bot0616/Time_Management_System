package com.example.timemanagementsystem;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ViewAdapter extends ArrayAdapter<String> {

    ArrayList<String> list;
    Context a;

    public ViewAdapter(Context context, ArrayList<String>item) {
        super(context, R.layout.list_rows,item);
        this.a = context;
        list = item;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater q = (LayoutInflater) a.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = q.inflate(R.layout.list_rows,null);

            TextView count = convertView.findViewById(R.id.count);
            count.setText(position + 1 +".");

            TextView heading = convertView.findViewById(R.id.heading);
            heading.setText(list.get(position));

            ImageView delete =convertView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shoppinglist.deleteItem(position);
                }
            });
        }
        return convertView;

    }
}

