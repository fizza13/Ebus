package com.example.e_bus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        RecyclerView recyclerView =findViewById(R.id.recyclerview);

        List<item> items =new ArrayList<item>();
        items.add(new item("RIJ 11","Islamabad",R.drawable.download));
        items.add(new item("RRT 4473","Chandni Chowk, Faizabad",R.drawable.download));
        items.add(new item("RIJ 1017","Khana pul,Taramri Chowk",R.drawable.download));
        items.add(new item("RIJ 1109","Muree Road",R.drawable.download));
        items.add(new item("RIJ 2651","Sachem Kaak pul",R.drawable.download));
        items.add(new item("RIJ 12","Khawaj Corporation ,local",R.drawable.download));
        items.add(new item("RIJ 345","Rawat ,local",R.drawable.download));
        items.add(new item("RIJ 1980","Kamra",R.drawable.download));
        items.add(new item("RIJ 6754","New City",R.drawable.download));
        items.add(new item("RIJ 1104","Nawababad",R.drawable.download));
        items.add(new item("RIJ 376","Wah Cantt",R.drawable.download));
        items.add(new item("RIJ 176","PMO",R.drawable.download));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdaptor(getApplicationContext(),items));

    }
}