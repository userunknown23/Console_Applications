import java.util.*;
import java.util.stream.Collectors;

public class MailManagementSystem {
    private List<Mail> mails;
    private Set<String> spamWords;
    private Scanner scanner;

    public MailManagementSystem() {
        mails = new ArrayList<>();
        spamWords = new HashSet<>(Arrays.asList("lottery", "winner", "prize", "free", "urgent", "congratulations"));
        scanner = new Scanner(System.in);
    }

    public void storeMail() {
        System.out.println("Enter sender email:");
        String sender = scanner.nextLine();
        System.out.println("Enter receiver email:");
        String receiver = scanner.nextLine();
        System.out.println("Enter subject:");
        String subject = scanner.nextLine();
        System.out.println("Enter content:");
        String content = scanner.nextLine();

        Mail mail = new Mail(sender, receiver, subject, content);
        checkSpam(mail);
        mails.add(mail);
        System.out.println("Mail stored successfully!");
    }

    public void deleteMail() {
        System.out.println("Enter mail index to delete:");
        int index = scanner.nextInt();
        scanner.nextLine(); // consume newline
        if (index >= 0 && index < mails.size()) {
            mails.remove(index);
            System.out.println("Mail deleted successfully!");
        } else {
            System.out.println("Invalid mail index!");
        }
    }

    public void addTag() {
        System.out.println("Enter mail index:");
        int index = scanner.nextInt();
        scanner.nextLine(); // consume newline
        if (index >= 0 && index < mails.size()) {
            System.out.println("Enter tag:");
            String tag = scanner.nextLine();
            mails.get(index).addTag(tag);
            System.out.println("Tag added successfully!");
        } else {
            System.out.println("Invalid mail index!");
        }
    }

    public void showStats() {
        System.out.println("\nMail Statistics:");
        System.out.println("Total mails: " + mails.size());
        
        // Group tags
        // Map<String, Long> tagGroups = mails.stream()
        //     .flatMap(mail -> mail.getTags().stream())
        //     .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));
        
        // System.out.println("\nTag Groups:");
        // tagGroups.forEach((tag, count) -> System.out.println(tag + ": " + count));

        System.out.println("\nEnter number of recent mails to display:");
        int n = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        System.out.println("\nRecent " + n + " mails:");
        mails.stream()
            .skip(Math.max(0, mails.size() - n))
            .forEach(System.out::println);
    }

    private void checkSpam(Mail mail) {
        String content = mail.getContent().toLowerCase();
        for (String word : spamWords) {
            if (content.contains(word)) {
                mail.setSpam(true);
                break;
            }
        }
    }

    public void search() {
        System.out.println("Enter search query:");
        String query = scanner.nextLine().toLowerCase();
        
        List<Mail> results = mails.stream()
            .filter(mail -> 
                mail.getSender().toLowerCase().contains(query) ||
                mail.getReceiver().toLowerCase().contains(query) ||
                mail.getSubject().toLowerCase().contains(query) ||
                mail.getContent().toLowerCase().contains(query) ||
                mail.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(query)))
            .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No mails found matching the query.");
        } else {
            System.out.println("\nSearch Results:");
            results.forEach(System.out::println);
        }
    }

    public void wildcardSearch() {
        System.out.println("Enter wildcard pattern (use * for any characters):");
        final String pattern = scanner.nextLine().toLowerCase().replace("*", ".*");
        
        List<Mail> results = mails.stream()
            .filter(mail -> 
                mail.getSender().toLowerCase().matches(pattern) ||
                mail.getReceiver().toLowerCase().matches(pattern) ||
                mail.getSubject().toLowerCase().matches(pattern) ||
                mail.getContent().toLowerCase().matches(pattern) ||
                mail.getTags().stream().anyMatch(tag -> tag.toLowerCase().matches(pattern)))
            .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No mails found matching the pattern.");
        } else {
            System.out.println("\nWildcard Search Results:");
            results.forEach(System.out::println);
        }
    }

    public void displayMenu() {
        System.out.println("\nMail Management System");
        System.out.println("1. Store Mail");
        System.out.println("2. Delete Mail");
        System.out.println("3. Add Tag");
        System.out.println("4. Show Statistics");
        System.out.println("5. Search");
        System.out.println("6. Wildcard Search");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void main(String[] args) {
        MailManagementSystem system = new MailManagementSystem();
        int choice;
        
        do {
            system.displayMenu();
            choice = system.scanner.nextInt();
            system.scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    system.storeMail();
                    break;
                case 2:
                    system.deleteMail();
                    break;
                case 3:
                    system.addTag();
                    break;
                case 4:
                    system.showStats();
                    break;
                case 5:
                    system.search();
                    break;
                case 6:
                    system.wildcardSearch();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 7);
        
        system.scanner.close();
    }
} 