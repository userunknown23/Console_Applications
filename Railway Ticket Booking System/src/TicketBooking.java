import java.util.Scanner;

public class TicketBooking {
    public static void main(String[] args) {
        TicketSystem ticketSystem = new TicketSystem();
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\n Railway Ticket Booking System");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. View Confirmed Tickets");
            System.out.println("4. View Available Tickets");
            System.out.println("5. View RAC Tickets");
            System.out.println("6. View Waiting List Tickets");
            System.out.println("7. Exit");
            System.out.print("Enter your choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Name : ");
                    String name = sc.nextLine();
                    System.out.print("Enter Age : ");
                    int age = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    System.out.print("Enter Gender (Male/Female): ");
                    String gender = sc.nextLine();
                    System.out.print("Enter Berth Preference (L,M,U) : ");
                    String preferredBerth = sc.nextLine();
                    ticketSystem.bookTicket(name, age, gender, preferredBerth);
                    break;
                case 2:
                    System.out.print("Enter your ticketId : ");
                    String ticketId = sc.next();
                    ticketSystem.cancelTicket(ticketId);
                    break;
                case 3:
                    ticketSystem.viewConfirmedTickets();
                    break;
                case 4:
                    ticketSystem.viewAvailableTickets();
                    break;
                case 5:
                    ticketSystem.viewRACTickets();
                    break;
                case 6:
                    ticketSystem.viewWaitingListTickets();
                    break;
                case 7:
                    System.out.println("Exiting ...");
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice. Try again !");
            }
        }

    }
}
