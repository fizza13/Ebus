package com.example.e_bus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private List<BusModel> busList;
    private Context context;

    public BusAdapter(List<BusModel> busList, Context context) {
        this.busList = busList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusModel bus = busList.get(position);
        holder.busNumberTextView.setText(bus.getBusNumber());

        // Set click listener to open a new activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the clicked bus number to the new activity
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    String clickedBusNumber = busList.get(clickedPosition).getBusNumber();
                    openNewActivity(clickedBusNumber);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return busList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView busNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busNumberTextView = itemView.findViewById(R.id.busNumberTextView);
        }
    }

    private void openNewActivity(String busNumber) {
        Intent intent = new Intent(context, NewActivity.class);
        intent.putExtra("BUS_NUMBER", busNumber);
        context.startActivity(intent);
    }
}
