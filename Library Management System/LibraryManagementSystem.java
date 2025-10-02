import java.util.*;

class Book {
    String title;
    String author;
    String genre;
    boolean isAvailable;

    public Book(String title, String author, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
    }
}

class Member{
    String username;
    List<Book> borrowedbooks;

    public Member(String username){
        this.username = username;
        this.borrowedbooks = new ArrayList<>();
    }

    boolean borrowBook(Book book){
        if(borrowedbooks.size() < 5 && book.isAvailable){
            borrowedbooks.add(book);
            book.isAvailable = false;
            return true;
        }
        return false;
    }

    void returnBook(Book book){
        borrowedbooks.remove(book);
        book.isAvailable = true;
    }
}

public class LibraryManagementSystem{
    static Scanner sc = new Scanner(System.in);
    static List<Book> books = new ArrayList<>();
    static List<Member> members = new ArrayList<>();
    static Member loggedInMember = null;
    static final String adminUsername = "admin";
    static final String adminPassword = "password";

    public static void main(String[] args) {
        books.add(new Book("Science", "Joseph", "Educational"));
        books.add(new Book("Maths", "Robert", "Educational"));
        books.add(new Book("English", "William", "Educational"));

        while( true){
            System.out.println("\nLibrary Management System");
            System.out.println("Enter role (admin/user) : ");
            String role = sc.nextLine();
            if(role.equals("admin")){
                loginAsAdmin();
            }else if(role.equals("user")){
                loginAsUser();
            }else {
                System.out.println("Invalid role. Try again.");
            }
        }
    }

    private static void loginAsAdmin(){
        System.out.println("Enter username : ");
        String username = sc.nextLine();
        System.out.println("Enter password : ");
        String password = sc.nextLine();

        if(username.equalsIgnoreCase(adminUsername) && password.equalsIgnoreCase(adminPassword)){
            System.out.println("LoggedIn as Admin.");
            adminMenu();
        }else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void loginAsUser(){
        System.out.println("Enter username : ");
        String username = sc.nextLine();

        loggedInMember = findOrCreateMember(username);
        System.out.println("LoggedIn as User.");
        userMenu();
    }

    private static Member findOrCreateMember(String username){
        for(Member member : members){
            if(member.username.equalsIgnoreCase(username)){
                return member;
            }
        }
        Member newMember = new Member(username);
        members.add(newMember);
        return newMember;
    }

    private static void adminMenu(){
        while(true){
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Remove Book");
            System.out.println("4. Add Member");
            System.out.println("5. Display All Books");
            System.out.println("6. Display All Members");
            System.out.println("7. Exit");
            System.out.println("Enter choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice){
                case 1:
                    addBook();
                    break;
                case 2:
                    updateBook();
                    break;
                case 3:
                    removeBook();
                    break;
                case 4:
                    addMember();
                    break;
                case 5:
                    displayAllBooks();
                    break;
                case 6:
                    displayAllMembers();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid Choice. Try again !");
            }
        }
    }

    private static void userMenu(){
        while(true){
            System.out.println("\nLibrary Management System");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Display All Books");
            System.out.println("4. Display All Members");
            System.out.println("5. Exit");
            System.out.println("Enter the choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice){
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    displayAllBooks();
                    break;
                case 4:
                    displayAllMembers();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid Choice. Try again !");
            }
        }
    }

    private static void addBook(){
        System.out.println("Enter book title : ");
        String title = sc.nextLine();
        System.out.println("Enter book author : ");
        String author = sc.nextLine();
        System.out.println("Enetr book genre : ");
        String genre = sc.nextLine();
        books.add(new Book(title, author, genre));
        System.out.println("Book added successfully.");
    }

    private static void updateBook(){
        System.out.println("Enter book title to update : ");
        String title = sc.nextLine();

        Book book = findBookByTitle(title);
        if(book != null){
            System.out.println("Enter book author : ");
            book.author = sc.nextLine();
            System.out.println("Enter book genre :");
            book.genre = sc.nextLine();
            System.out.println("Book updated successfully.");
        }else{
            System.out.println("Book not found !");
        }
    }

    private static void removeBook(){
        System.out.println("Enter book title to remove : ");
        String title = sc.nextLine();

        Book book = findBookByTitle(title);
        if(book != null){
            books.remove(title);
            System.out.println("Book removed successfully.");
        }else{
            System.out.println("Book not found !");
        }
    }

    private static Book findBookByTitle(String title){
        for(Book book : books){
            if(book.title.equalsIgnoreCase(title)){
                return book;
            }
        }
        return null;
    }

    private static void addMember(){
        System.out.println("Enter member : ");
        String user = sc.nextLine();
        members.add(new Member(user));
        System.out.println("Member added successfully.");
    }

    private static void borrowBook(){
        System.out.println("Enter book title : ");
        String title = sc.nextLine();

        Book book = findBookByTitle(title);
        if(book != null &&(loggedInMember.borrowBook(book))){
            System.out.println("Book borrowed successfully.");
        }else{
            System.out.println("Book not Available or Borrowing limit reached.");
        }
    }

    private static void returnBook(){
        System.out.println("Enter book title : ");
        String title = sc.nextLine();

        Book book = findBookByTitle(title);
        if (book != null && !book.isAvailable){
            loggedInMember.returnBook(book);
            System.out.println("Book returned successfully.");
        }else{
            System.out.println("Book not found or it wasn't borrowed !");
        }
    }

    private static void displayAllBooks(){
        if(books.isEmpty()){
            System.out.println("No books available !");
        }else{
            System.out.println("List of Books : ");
            for(Book book : books){
                System.out.println(
                        " Title : " + book.title +
                        " Author : " + book.author +
                        " Genre : " + book.genre +
                        " Available : " + book.isAvailable);
            }
        }
    }

    private static void displayAllMembers(){
        if(members.isEmpty()){
            System.out.println("No user exist !");
        }else{
            System.out.println("\n List of Users : ");
            for(Member member : members){
                System.out.println(
                        "Username : " + member.username +
                        " Borrowed Books : " + member.borrowedbooks.size());
            }
        }
    }
}