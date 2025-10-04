public class Mail {
    private int id;
    private String sender;
    private String receiver;
    private String subject;
    private String body;
    private String date;
    private boolean isRead;

    public Mail(int id, String sender, String receiver, String subject, String body, String date) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.date = date;
        this.isRead = false;
    }
    
    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Mail ID: " + id + "\n" +
               "From: " + sender + "\n" +
               "To: " + receiver + "\n" +
               "Subject: " + subject + "\n" +
               "Date: " + date + "\n" +
               "Status: " + (isRead ? "Read" : "Unread");
    }
}
