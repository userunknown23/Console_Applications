import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventScheduler {
    // Constants for time constraints
    private static final LocalTime START_TIME = LocalTime.of(9, 0);        // Day starts at 9:00 AM
    private static final LocalTime LUNCH_START = LocalTime.of(12, 0);      // Lunch starts at 12:00 PM
    private static final LocalTime LUNCH_END = LocalTime.of(13, 0);        // Lunch ends at 1:00 PM
    private static final LocalTime NETWORKING_EARLIEST = LocalTime.of(16, 0); // Networking can't start before 4:00 PM
    private static final LocalTime NETWORKING_LATEST = LocalTime.of(17, 0);   // Networking can't start after 5:00 PM
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a"); // Format time as "09:00 AM"

    // Lists to store events for each day
    private List<Event> day1Events;    // Stores events scheduled for day 1
    private List<Event> day2Events;    // Stores events scheduled for day 2

    // Constructor initializes empty lists for both days
    public EventScheduler() {
        day1Events = new ArrayList<>();
        day2Events = new ArrayList<>();
    }

    // Main method to schedule all events across days
    public void scheduleEvents(List<Event> events) {
        LocalTime currentTime = START_TIME;                    // Start scheduling from 9:00 AM
        boolean isDay1 = true;                                // Flag to track which day we're scheduling
        List<Event> remainingEvents = new ArrayList<>(events); // Create a copy of events to modify
        Event networkingEvent = new Event("Networking Hands-on", 60); // Create networking event

        // Continue scheduling until all events are assigned
        while (!remainingEvents.isEmpty()) {
            // Set end time based on current day (4 PM for day 1, 5 PM for day 2)
            LocalTime dayEndTime = isDay1 ? NETWORKING_EARLIEST : NETWORKING_LATEST;
            // Select which day's list to add events to
            List<Event> currentDayEvents = isDay1 ? day1Events : day2Events;
            currentTime = START_TIME; // Reset to start of day

            // Schedule events for the current day
            while (currentTime.isBefore(dayEndTime) && !remainingEvents.isEmpty()) {
                // Skip lunch hour
                if (currentTime.equals(LUNCH_START)) {
                    currentTime = LUNCH_END;
                    continue;
                }

                // Find next event that fits in current time slot
                Event nextEvent = findNextSuitableEvent(remainingEvents, currentTime, dayEndTime);
                if (nextEvent == null) break; // No suitable event found

                // Set the scheduled time for the event and add it to current day
                nextEvent.setScheduledTime(currentTime.format(TIME_FORMATTER));
                currentDayEvents.add(nextEvent);
                remainingEvents.remove(nextEvent);

                // Move current time forward by event duration
                currentTime = currentTime.plusMinutes(nextEvent.getDuration());
            }

            // Add networking event at the end of each day
            if (isDay1) {
                networkingEvent.setScheduledTime(currentTime.format(TIME_FORMATTER));
                day1Events.add(networkingEvent);
            } else {
                networkingEvent.setScheduledTime("04:00 PM");
                day2Events.add(networkingEvent);
            }

            isDay1 = false; // Move to day 2
        }
    }

    // Helper method to find the next event that fits in the current time slot
    private Event findNextSuitableEvent(List<Event> events, LocalTime currentTime, LocalTime endTime) {
        for (Event event : events) {
            // Calculate when this event would end if scheduled now
            LocalTime eventEndTime = currentTime.plusMinutes(event.getDuration());
            // Check if event fits before lunch or between lunch and end of day
            if (eventEndTime.isBefore(LUNCH_START) || 
                (eventEndTime.isAfter(LUNCH_END) && eventEndTime.isBefore(endTime))) {
                return event;
            }
        }
        return null; // No suitable event found
    }

    // Method to print the final schedule
    public void printSchedule() {
        // Print Day 1 schedule
        System.out.println("Schedule for Day 1");
        for (Event event : day1Events) {
            System.out.println(event);
        }

        // Print Day 2 schedule
        System.out.println("\nSchedule for Day 2");
        for (Event event : day2Events) {
            System.out.println(event);
        }
    }
} 