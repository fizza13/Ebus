package com.example.e_bus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BusAdapter busAdapter;
    private List<BusModel> busList;
    private SearchView searchView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        busList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        // Fetch bus numbers from Firestore
        fetchBusData();


        busAdapter = new BusAdapter(busList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(busAdapter);


    }

    private void fetchBusData() {
        CollectionReference busCollection = db.collection("BusNumber");

        busCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String busNumber = document.getString("Bus Number");
                        if (busNumber != null) {
                            busList.add(new BusModel(busNumber));
                        }
                    }
                    busAdapter.notifyDataSetChanged(); // Notify adapter after data is fetched
                } else {
                    Toast.makeText(Search.this, "Error fetching bus data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
