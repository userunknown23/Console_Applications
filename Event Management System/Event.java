public class Event {
    private String name;
    private int duration;
    private String scheduledTime;

    public Event(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public String toString() {
        return scheduledTime + " " + name + " " + 
               (duration == 5 ? "lightning" : duration + " mins");
    }
} 