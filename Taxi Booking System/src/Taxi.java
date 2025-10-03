import java.util.*;

class Taxi{
    int id; //unique taxi id
    char currentPoint = 'A'; //intial location of taxis
    int totalEarnings = 0;
    List<Booking> bookings = new ArrayList<>(); //all bookings

    public Taxi(int id){
        this.id = id;
    }

    public boolean isAvailable(int requestTime){
        if (bookings.isEmpty()) return true; //checks if bookings is empty or not
        Booking lastBooking = bookings.get(bookings.size()-1); //get lastbooking time
        return lastBooking.dropTime <= requestTime;
    }

    public int calculateEarnings(char from, char to){
        int distance = Math.abs(to-from) * 15;
        return 100 + Math.max(0, (distance - 5) * 10); //earnings for from & to
    }

    public void addBooking(Booking booking){
        bookings.add(booking);
        totalEarnings += booking.amount;
        currentPoint = booking.to;
    }
}