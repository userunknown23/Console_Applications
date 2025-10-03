import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Event> events = new ArrayList<>();
        
        System.out.println("Enter events (one per line). Enter 'done' when finished:");
        
        while (true) {
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("done")) {
                System.out.println();  // Add a blank line after 'done'
                break;
            }
            
            Event event = parseEvent(input);
            if (event != null) {
                events.add(event);
            }
        }
        
        EventScheduler scheduler = new EventScheduler();
        scheduler.scheduleEvents(events);
        scheduler.printSchedule();
        
        scanner.close();
    }
    
    private static Event parseEvent(String input) {
        try {
            if (input.toLowerCase().contains("lightning")) {
                String name = input.replace("lightning", "").trim();
                return new Event(name, 5);
            }
            //"Database Design Fundamentals 60 mins"
            //→ ["Database Design Fundamentals", "60 mins"]
            String[] parts = input.split("\\s+(?=\\d+\\s*mins)");
            if (parts.length == 2) {
                String name = parts[0].trim();
                //[^0-9] means "match any character that is NOT a digit
                int duration = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
                return new Event(name, duration);
            }
        } catch (Exception e) {
            System.out.println("Invalid event format: " + input);
        }
        return null;
    }
} 