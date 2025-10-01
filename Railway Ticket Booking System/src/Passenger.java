public class Passenger {
    String name;
    int age;
    String gender;
    String preferred_berth;
    String allotedBerth;
    String ticketId;

    public Passenger(String name, int age, String gender, String preferred_berth, String allotedBerth, String ticketId) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.preferred_berth = preferred_berth;
        this.allotedBerth = allotedBerth;
        this.ticketId = ticketId;
    }

    @Override
    public String toString() {
        return "Ticket ID : " + ticketId + ",Name : " + name + ",Age : " + age +
                ",Gender : " + gender + ",Alloted Berth : " + allotedBerth;
    }
}