package com.example.e_bus;

import java.util.List;
public class Reminders extends Reminder {
    private String title;
    private String time;
    private List<String> days;
    private String location;

    public Reminders(String title, String time, List<String> days, String location) {
        this.title = title;
        this.time = time;
        this.days = days;
        this.location = location;
    }

    // Getter methods for retrieving information
    public String getReminderTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public List<String> getDays() {
        return days;
    }

    public String getLocation() {
        return location;
    }

    // Method to get days as a formatted string
    public String getDaysAsString() {
        StringBuilder daysString = new StringBuilder();

        for (String day : days) {
            daysString.append(day).append(", ");
        }

        if (daysString.length() > 0) {
            // Remove the trailing comma and space
            daysString.setLength(daysString.length() - 2);
        }

        return daysString.toString();
    }
}


