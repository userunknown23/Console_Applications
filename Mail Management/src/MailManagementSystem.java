import java.util.ArrayList;
import java.util.Scanner;

public class MailManagementSystem {
    private ArrayList<Mail> inbox;
    private ArrayList<Mail> sentMails;
    private ArrayList<Mail> trash;
    private Scanner scanner;
    private int mailIdCounter;
    private String currentUser;

    public MailManagementSystem(String currentUser) {
        this.inbox = new ArrayList<>();
        this.sentMails = new ArrayList<>();
        this.trash = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.mailIdCounter = 1;
        this.currentUser = currentUser;
        loadSampleData();
    }

    private void loadSampleData() {
        inbox.add(new Mail(mailIdCounter++, "john@example.com", currentUser, "Meeting Tomorrow", 
                          "Don't forget about the meeting at 10 AM.", "2025-10-03"));
        inbox.add(new Mail(mailIdCounter++, "hr@company.com", currentUser, "Leave Approval", 
                          "Your leave request has been approved.", "2025-10-02"));
        inbox.add(new Mail(mailIdCounter++, "admin@system.com", currentUser, "System Update", 
                          "System maintenance scheduled for this weekend.", "2025-10-01"));
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║     MAIL MANAGEMENT SYSTEM             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("User: " + currentUser);
            System.out.println("\n1. View Inbox (" + inbox.size() + " mails)");
            System.out.println("2. View Sent Mails (" + sentMails.size() + " mails)");
            System.out.println("3. Compose New Mail");
            System.out.println("4. Read Mail");
            System.out.println("5. Delete Mail");
            System.out.println("6. View Trash (" + trash.size() + " mails)");
            System.out.println("7. Search Mails");
            System.out.println("8. Exit");
            System.out.print("\nEnter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    viewInbox();
                    break;
                case 2:
                    viewSentMails();
                    break;
                case 3:
                    composeMail();
                    break;
                case 4:
                    readMail();
                    break;
                case 5:
                    deleteMail();
                    break;
                case 6:
                    viewTrash();
                    break;
                case 7:
                    searchMails();
                    break;
                case 8:
                    System.out.println("\nThank you for using Mail Management System!");
                    return;
                default:
                    System.out.println("\nInvalid choice! Please try again.");
            }
        }
    }

    private void viewInbox() {
        System.out.println("\n═══════════════ INBOX ═══════════════");
        if (inbox.isEmpty()) {
            System.out.println("No mails in inbox.");
            return;
        }
        for (Mail mail : inbox) {
            System.out.println("\n" + mail);
            System.out.println("─────────────────────────────────────");
        }
    }

    private void viewSentMails() {
        System.out.println("\n═══════════════ SENT MAILS ═══════════════");
        if (sentMails.isEmpty()) {
            System.out.println("No sent mails.");
            return;
        }
        for (Mail mail : sentMails) {
            System.out.println("\n" + mail);
            System.out.println("─────────────────────────────────────");
        }
    }

    private void composeMail() {
        System.out.println("\n═══════════════ COMPOSE MAIL ═══════════════");
        System.out.print("To: ");
        String receiver = scanner.nextLine();
        System.out.print("Subject: ");
        String subject = scanner.nextLine();
        System.out.print("Body: ");
        String body = scanner.nextLine();
        
        String date = java.time.LocalDate.now().toString();
        Mail newMail = new Mail(mailIdCounter++, currentUser, receiver, subject, body, date);
        sentMails.add(newMail);
        
        System.out.println("\n✓ Mail sent successfully!");
    }

    private void readMail() {
        System.out.print("\nEnter Mail ID to read: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Mail mail = findMailById(id, inbox);
        if (mail != null) {
            mail.setRead(true);
            System.out.println("\n═══════════════ MAIL DETAILS ═══════════════");
            System.out.println(mail);
            System.out.println("\nBody:\n" + mail.getBody());
            System.out.println("════════════════════════════════════════════");
        } else {
            System.out.println("\nMail not found in inbox!");
        }
    }

    private void deleteMail() {
        System.out.print("\nEnter Mail ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Mail mail = findMailById(id, inbox);
        if (mail != null) {
            inbox.remove(mail);
            trash.add(mail);
            System.out.println("\n✓ Mail moved to trash!");
        } else {
            mail = findMailById(id, sentMails);
            if (mail != null) {
                sentMails.remove(mail);
                trash.add(mail);
                System.out.println("\n✓ Mail moved to trash!");
            } else {
                System.out.println("\nMail not found!");
            }
        }
    }

    private void viewTrash() {
        System.out.println("\n═══════════════ TRASH ═══════════════");
        if (trash.isEmpty()) {
            System.out.println("Trash is empty.");
            return;
        }
        for (Mail mail : trash) {
            System.out.println("\n" + mail);
            System.out.println("─────────────────────────────────────");
        }
    }

    private void searchMails() {
        System.out.print("\nEnter search keyword (subject/sender): ");
        String keyword = scanner.nextLine().toLowerCase();

        System.out.println("\n═══════════════ SEARCH RESULTS ═══════════════");
        boolean found = false;

        for (Mail mail : inbox) {
            if (mail.getSubject().toLowerCase().contains(keyword) || 
                mail.getSender().toLowerCase().contains(keyword)) {
                System.out.println("\n" + mail);
                System.out.println("─────────────────────────────────────");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No mails found matching '" + keyword + "'");
        }
    }

    private Mail findMailById(int id, ArrayList<Mail> mailList) {
        for (Mail mail : mailList) {
            if (mail.getId() == id) {
                return mail;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your email: ");
        String userEmail = scanner.nextLine();
        
        MailManagementSystem system = new MailManagementSystem(userEmail);
        system.displayMenu();
        scanner.close();
    }
}
