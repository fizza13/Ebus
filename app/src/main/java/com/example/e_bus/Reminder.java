package com.example.e_bus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Reminder extends AppCompatActivity {
    private List<Reminder> reminders;
    private RecyclerView remindersRecyclerView;
    private ReminderAdapter reminderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        reminders = new ArrayList<>();
        remindersRecyclerView = findViewById(R.id.remindersRecyclerView);
        reminderAdapter = new ReminderAdapter(reminders);
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        remindersRecyclerView.setAdapter(reminderAdapter);

        FloatingActionButton addReminderFab = findViewById(R.id.addReminderFab);
        addReminderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddReminderDialog();
            }
        });
    }

    private void showAddReminderDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_reminder);
        dialog.setTitle("Add Reminder");

        final EditText titleEditText = dialog.findViewById(R.id.addReminder);
        final EditText locationEditText = dialog.findViewById(R.id.locationEditText);
        final CheckBox mondayCheckBox = dialog.findViewById(R.id.mondayCheckBox);
        final CheckBox tuesdayCheckBox = dialog.findViewById(R.id.tuesdayCheckBox);
        final CheckBox wednesdayCheckBox = dialog.findViewById(R.id.wednesdayCheckBox);
        final CheckBox thursdayCheckBox = dialog.findViewById(R.id.thursdayCheckBox);
        final CheckBox fridayCheckBox = dialog.findViewById(R.id.fridayCheckBox);
        final CheckBox saturdayCheckBox = dialog.findViewById(R.id.saturdayCheckBox);
        final CheckBox sundayCheckBox = dialog.findViewById(R.id.sundayCheckBox);
        final TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        Button setReminderButton = dialog.findViewById(R.id.setReminderButton);

        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle set reminder button click
                String title = titleEditText.getText().toString();
                String location = locationEditText.getText().toString();
                List<String> days = new ArrayList<>();

                if (mondayCheckBox.isChecked()) days.add("Monday");
                if (tuesdayCheckBox.isChecked()) days.add("Tuesday");
                if (wednesdayCheckBox.isChecked()) days.add("Wednesday");
                if (thursdayCheckBox.isChecked()) days.add("Thursday");
                if (fridayCheckBox.isChecked()) days.add("Friday");
                if (saturdayCheckBox.isChecked()) days.add("Saturday");
                if (sundayCheckBox.isChecked()) days.add("Sunday");

                // Format time as HH:MM
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String time = String.format("%02d:%02d", hour, minute);

                Reminders newReminder = new Reminders(title, time, days, location);
                reminders.add(newReminder);
                reminderAdapter.notifyDataSetChanged();

                // Schedule the reminder (you may need to implement this)
                // Example: scheduleReminder(newReminder);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Implement other necessary methods and logic

    // Sample method for scheduling a reminder (you may need to modify this)
    private void scheduleReminder(Reminder reminder) {
        // Implement code to schedule the reminder, e.g., using AlarmManager
        // You need to decide how you want to handle the scheduling logic
        // and set up the broadcast receiver accordingly
    }
}