import java.sql.SQLOutput;
import java.util.*;

public class TicketSystem {
    private final List<String> availableBerths = new ArrayList<>(Arrays.asList("L", "U", "M"));
    private final Queue<Passenger> racQueue = new LinkedList<>();
    private final Queue<Passenger> waitingListQueue = new LinkedList<>();
    private final List<Passenger> confirmedPassengers = new ArrayList<>();
    private int ticketCounter = 1;


    public void bookTicket(String name, int age, String gender, String preference) {
        String ticketId = "T" + ticketCounter++;
        Passenger passenger;

        if(!availableBerths.isEmpty()) {
            String allocatedBerth = allocateBerth(age, gender, preference);
            passenger = new Passenger(name, age, gender, preference, allocatedBerth, ticketId);
            confirmedPassengers.add(passenger);
            availableBerths.remove(allocatedBerth);
            System.out.println("Ticket Confirmed : " + passenger);
        } else if(racQueue.size() < 1) {
            passenger = new Passenger(name, age, gender, preference, "RAC", ticketId);
            racQueue.offer(passenger);
            System.out.println("Ticket in RAC : " + passenger);
        } else if(waitingListQueue.size() < 1) {
            passenger = new Passenger(name, age, gender, preference, "Waiting List", ticketId);
            waitingListQueue.offer(passenger);
            System.out.println("Ticket in Waiting List : " + passenger);
        } else {
            System.out.println("No ticket available");
        }
    }

    public void cancelTicket(String ticketId) {
        Optional<Passenger> passengerOpt =confirmedPassengers.stream()
                .filter(p -> p.ticketId.equals(ticketId))
                .findFirst();
        if(passengerOpt.isPresent()){
            Passenger passenger =passengerOpt.get();
            confirmedPassengers.remove(passenger);
            availableBerths.add(passenger.allotedBerth);

            if(!racQueue.isEmpty()){
                Passenger racpassenger =racQueue.poll();
                String allocatedBerth = allocateBerth(racpassenger.age, racpassenger.gender, racpassenger.preferred_berth);
                racpassenger.allotedBerth = allocatedBerth;
                confirmedPassengers.add(racpassenger);
                availableBerths.remove(allocatedBerth);
                System.out.println("RAC ticket moved to confirmed : " + racpassenger);
            }
            if(!waitingListQueue.isEmpty()) {
                Passenger waitinglistpassenger = waitingListQueue.poll();
                racQueue.offer(waitinglistpassenger);
                waitinglistpassenger.allotedBerth = "RAC";
                System.out.println("Waiting List ticket moved to RAC : " + waitinglistpassenger);
            }
            System.out.println("Tciket cancelled successfully for ticketId : " + ticketId);
        }
        else {
            System.out.println("No ticket found with ID : " + ticketId);
        }
    }

    private String allocateBerth(int age, String gender, String preference) {
        if(age > 60 || gender.equalsIgnoreCase("female") && availableBerths.contains("L")){
            return "L";
        }
        if(availableBerths.contains(preference)) {
            return preference;
        }
        return availableBerths.get(0);
    }

    public void viewConfirmedTickets() {
        if(!confirmedPassengers.isEmpty()) {
            System.out.println("Confirmed Tickets : ");
            for(Passenger passenger : confirmedPassengers) {
                System.out.println(passenger);
            }
        }else {
            System.out.println("No Confirmed tickets.");
        }
    }

    public void viewAvailableTickets() {
        System.out.println("Available Berths : " + availableBerths.size());
        System.out.println("Available RAC Tickets :" + (1 - racQueue.size()));
        System.out.println("Available Waiting List Tickets : " + (1 - waitingListQueue.size()));
    }

    public void viewRACTickets() {
        if(!racQueue.isEmpty()) {
            System.out.println("RAC Tickets");
            for(Passenger passenger : racQueue){
                System.out.println(passenger);
            }
        }else {
            System.out.println("No RAC tickets.");
        }
    }

    public void viewWaitingListTickets() {
        if(!waitingListQueue.isEmpty()) {
            System.out.println("Waiting List Tickets");
            for(Passenger passenger : waitingListQueue){
                System.out.println(passenger);
            }
        }else {
            System.out.println("No Waiting List tickets.");
        }
    }
}
