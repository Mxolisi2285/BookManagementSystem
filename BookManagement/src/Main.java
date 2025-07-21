import java.io.*;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "books.ser";
    private static ArrayList<Book> books = new ArrayList<>();
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        if (!login()) return;

        loadBooks();
        int choice;
        do {
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewBooks();
                case 3 -> searchBook();
                case 4 -> deleteBook();
                case 5 -> updateBook();
                case 6 -> sortBooks();
                case 7 -> saveBooks();
                case 0 -> {
                    saveBooks();
                    System.out.println("👋 Goodbye!");
                }
                default -> System.out.println("❌ Invalid option.");
            }
        } while (choice != 0);
    }

    private static boolean login() {
        System.out.println("🔐 Admin Login");
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();
        if (user.equals(ADMIN_USERNAME) && pass.equals(ADMIN_PASSWORD)) {
            System.out.println("✅ Login successful.\n");
            return true;
        } else {
            System.out.println("❌ Access Denied.");
            return false;
        }
    }

    private static void showMenu() {
        System.out.println("\n📚 Book Management System");
        System.out.println("1. ➕ Add Book");
        System.out.println("2. 📖 View All Books");
        System.out.println("3. 🔍 Search Book by Title");
        System.out.println("4. 🗑️ Delete Book by ID");
        System.out.println("5. ✏️ Update Book by ID");
        System.out.println("6. 📊 Sort Books");
        System.out.println("7. 💾 Save to File");
        System.out.println("0. ❌ Exit");
        System.out.print("Choose option: ");
    }

    private static void addBook() {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        books.add(new Book(id, title, author, year));
        System.out.println("✅ Book added successfully.");
    }

    private static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("📭 No books found.");
            return;
        }
        System.out.println("📚 List of Books:");
        books.forEach(System.out::println);
    }

    private static void searchBook() {
        System.out.print("Enter Title to search: ");
        String title = scanner.nextLine();
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                System.out.println("🔍 Found: " + book);
                found = true;
            }
        }
        if (!found) System.out.println("❌ No book with that title.");
    }

    private static void deleteBook() {
        System.out.print("Enter ID of book to delete: ");
        String id = scanner.nextLine();
        boolean removed = books.removeIf(book -> book.getId().equals(id));
        System.out.println(removed ? "🗑️ Book deleted." : "❌ Book not found.");
    }

    private static void updateBook() {
        System.out.print("Enter ID of book to update: ");
        String id = scanner.nextLine();
        for (Book book : books) {
            if (book.getId().equals(id)) {
                System.out.print("New Title: ");
                book.setTitle(scanner.nextLine());
                System.out.print("New Author: ");
                book.setAuthor(scanner.nextLine());
                System.out.print("New Year: ");
                book.setYear(scanner.nextInt());
                scanner.nextLine();
                System.out.println("✅ Book updated.");
                return;
            }
        }
        System.out.println("❌ Book not found.");
    }

    private static void sortBooks() {
        System.out.println("Sort by: 1. Title  2. Author  3. Year");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> books.sort(Comparator.comparing(Book::getTitle));
            case 2 -> books.sort(Comparator.comparing(Book::getAuthor));
            case 3 -> books.sort(Comparator.comparingInt(Book::getYear));
            default -> System.out.println("❌ Invalid choice.");
        }
        System.out.println("✅ Sorted successfully.");
    }

    private static void saveBooks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(books);
            System.out.println("💾 Books saved to file.");
        } catch (IOException e) {
            System.out.println("❌ Error saving file: " + e.getMessage());
        }
    }

    private static void loadBooks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            books = (ArrayList<Book>) in.readObject();
            System.out.println("📂 Books loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Failed to load books: " + e.getMessage());
        }
    }
}
