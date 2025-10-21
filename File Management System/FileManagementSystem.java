import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File Management Console Application
 * Provides comprehensive file and directory operations through a console interface.
 * Features: Create, Read, Update, Delete, List, Search, Copy, Move files and directories.
 */
public class FileManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static Path currentDirectory = Paths.get(System.getProperty("user.home"));

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   File Management Console Application         ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();

        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1": createFile(); break;
                    case "2": createDirectory(); break;
                    case "3": readFile(); break;
                    case "4": updateFile(); break;
                    case "5": deleteFile(); break;
                    case "6": deleteDirectory(); break;
                    case "7": listContents(); break;
                    case "8": searchFiles(); break;
                    case "9": copyFile(); break;
                    case "10": moveFile(); break;
                    case "11": renameFile(); break;
                    case "12": getFileInfo(); break;
                    case "13": changeDirectory(); break;
                    case "14": showCurrentDirectory(); break;
                    case "0": running = false; System.out.println("\nThank you for using File Management System!"); break;
                    default: System.out.println("❌ Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            
            if (running && !choice.equals("14")) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Current Directory: " + currentDirectory.toAbsolutePath());
        System.out.println("=".repeat(50));
        System.out.println("1.  Create File");
        System.out.println("2.  Create Directory");
        System.out.println("3.  Read File");
        System.out.println("4.  Update File (Append/Overwrite)");
        System.out.println("5.  Delete File");
        System.out.println("6.  Delete Directory");
        System.out.println("7.  List Directory Contents");
        System.out.println("8.  Search Files");
        System.out.println("9.  Copy File");
        System.out.println("10. Move File");
        System.out.println("11. Rename File/Directory");
        System.out.println("12. Get File/Directory Info");
        System.out.println("13. Change Directory");
        System.out.println("14. Show Current Directory");
        System.out.println("0.  Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice: ");
    }

    private static void createFile() throws IOException {
        System.out.print("Enter file name (or path): ");
        String fileName = scanner.nextLine().trim();
        Path filePath = resolvePath(fileName);

        if (Files.exists(filePath)) {
            System.out.println("❌ File already exists: " + filePath);
            return;
        }

        System.out.print("Enter content (or press Enter for empty file): ");
        String content = scanner.nextLine();

        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, content);
        System.out.println("✅ File created successfully: " + filePath);
    }

    private static void createDirectory() throws IOException {
        System.out.print("Enter directory name (or path): ");
        String dirName = scanner.nextLine().trim();
        Path dirPath = resolvePath(dirName);

        if (Files.exists(dirPath)) {
            System.out.println("❌ Directory already exists: " + dirPath);
            return;
        }

        Files.createDirectories(dirPath);
        System.out.println("✅ Directory created successfully: " + dirPath);
    }

    private static void readFile() throws IOException {
        System.out.print("Enter file name (or path): ");
        String fileName = scanner.nextLine().trim();
        Path filePath = resolvePath(fileName);

        if (!Files.exists(filePath)) {
            System.out.println("❌ File not found: " + filePath);
            return;
        }

        if (Files.isDirectory(filePath)) {
            System.out.println("❌ Path is a directory, not a file.");
            return;
        }

        System.out.println("\n" + "─".repeat(50));
        System.out.println("Content of: " + filePath.getFileName());
        System.out.println("─".repeat(50));
        
        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty()) {
            System.out.println("(Empty file)");
        } else {
            for (int i = 0; i < lines.size(); i++) {
                System.out.printf("%4d | %s\n", i + 1, lines.get(i));
            }
        }
        System.out.println("─".repeat(50));
        System.out.println("Total lines: " + lines.size());
    }

    private static void updateFile() throws IOException {
        System.out.print("Enter file name (or path): ");
        String fileName = scanner.nextLine().trim();
        Path filePath = resolvePath(fileName);

        if (!Files.exists(filePath)) {
            System.out.println("❌ File not found: " + filePath);
            return;
        }

        if (Files.isDirectory(filePath)) {
            System.out.println("❌ Path is a directory, not a file.");
            return;
        }

        System.out.println("Choose mode:");
        System.out.println("1. Append content");
        System.out.println("2. Overwrite content");
        System.out.print("Enter choice: ");
        String mode = scanner.nextLine().trim();

        System.out.print("Enter content: ");
        String content = scanner.nextLine();

        if (mode.equals("1")) {
            Files.writeString(filePath, content + System.lineSeparator(), StandardOpenOption.APPEND);
            System.out.println("✅ Content appended successfully.");
        } else if (mode.equals("2")) {
            Files.writeString(filePath, content);
            System.out.println("✅ File overwritten successfully.");
        } else {
            System.out.println("❌ Invalid mode.");
        }
    }

    private static void deleteFile() throws IOException {
        System.out.print("Enter file name (or path): ");
        String fileName = scanner.nextLine().trim();
        Path filePath = resolvePath(fileName);

        if (!Files.exists(filePath)) {
            System.out.println("❌ File not found: " + filePath);
            return;
        }

        if (Files.isDirectory(filePath)) {
            System.out.println("❌ Path is a directory. Use 'Delete Directory' option instead.");
            return;
        }

        System.out.print("Are you sure you want to delete this file? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            Files.delete(filePath);
            System.out.println("✅ File deleted successfully: " + filePath);
        } else {
            System.out.println("❌ Deletion cancelled.");
        }
    }

    private static void deleteDirectory() throws IOException {
        System.out.print("Enter directory name (or path): ");
        String dirName = scanner.nextLine().trim();
        Path dirPath = resolvePath(dirName);

        if (!Files.exists(dirPath)) {
            System.out.println("❌ Directory not found: " + dirPath);
            return;
        }

        if (!Files.isDirectory(dirPath)) {
            System.out.println("❌ Path is a file, not a directory.");
            return;
        }

        System.out.print("Delete recursively? This will delete all contents! (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            deleteDirectoryRecursively(dirPath);
            System.out.println("✅ Directory deleted successfully: " + dirPath);
        } else {
            System.out.println("❌ Deletion cancelled.");
        }
    }

    private static void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                for (Path entry : entries.collect(Collectors.toList())) {
                    deleteDirectoryRecursively(entry);
                }
            }
        }
        Files.delete(path);
    }

    private static void listContents() throws IOException {
        System.out.print("Enter directory path (or press Enter for current): ");
        String dirName = scanner.nextLine().trim();
        Path dirPath = dirName.isEmpty() ? currentDirectory : resolvePath(dirName);

        if (!Files.exists(dirPath)) {
            System.out.println("❌ Directory not found: " + dirPath);
            return;
        }

        if (!Files.isDirectory(dirPath)) {
            System.out.println("❌ Path is not a directory.");
            return;
        }

        System.out.println("\nContents of: " + dirPath.toAbsolutePath());
        System.out.println("─".repeat(80));
        System.out.printf("%-40s %-10s %-15s\n", "Name", "Type", "Size");
        System.out.println("─".repeat(80));

        try (Stream<Path> entries = Files.list(dirPath)) {
            List<Path> sortedEntries = entries.sorted().collect(Collectors.toList());
            
            if (sortedEntries.isEmpty()) {
                System.out.println("(Empty directory)");
            } else {
                for (Path entry : sortedEntries) {
                    String name = entry.getFileName().toString();
                    String type = Files.isDirectory(entry) ? "[DIR]" : "[FILE]";
                    String size = Files.isDirectory(entry) ? "-" : formatFileSize(Files.size(entry));
                    System.out.printf("%-40s %-10s %-15s\n", 
                        name.length() > 40 ? name.substring(0, 37) + "..." : name, 
                        type, 
                        size);
                }
            }
        }
        System.out.println("─".repeat(80));
    }

    private static void searchFiles() throws IOException {
        System.out.print("Enter search pattern (supports wildcards like *.txt): ");
        String pattern = scanner.nextLine().trim();
        
        System.out.print("Search in current directory only? (yes/no): ");
        String shallow = scanner.nextLine().trim().toLowerCase();
        boolean recursive = !shallow.equals("yes") && !shallow.equals("y");

        System.out.println("\nSearching for: " + pattern);
        System.out.println("─".repeat(80));

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        List<Path> results = new ArrayList<>();

        if (recursive) {
            Files.walkFileTree(currentDirectory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (matcher.matches(file.getFileName())) {
                        results.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            try (Stream<Path> entries = Files.list(currentDirectory)) {
                List<Path> shallowResults = entries.filter(p -> matcher.matches(p.getFileName()))
                                .collect(Collectors.toList());
                results.addAll(shallowResults);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No files found matching: " + pattern);
        } else {
            System.out.println("Found " + results.size() + " file(s):");
            for (Path result : results) {
                System.out.println("  • " + result);
            }
        }
        System.out.println("─".repeat(80));
    }

    private static void copyFile() throws IOException {
        System.out.print("Enter source file path: ");
        String source = scanner.nextLine().trim();
        Path sourcePath = resolvePath(source);

        if (!Files.exists(sourcePath)) {
            System.out.println("❌ Source file not found: " + sourcePath);
            return;
        }

        System.out.print("Enter destination path: ");
        String dest = scanner.nextLine().trim();
        Path destPath = resolvePath(dest);

        if (Files.exists(destPath)) {
            System.out.print("Destination exists. Overwrite? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("yes") && !confirm.equals("y")) {
                System.out.println("❌ Copy cancelled.");
                return;
            }
        }

        Files.createDirectories(destPath.getParent());
        Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("✅ File copied successfully from " + sourcePath + " to " + destPath);
    }

    private static void moveFile() throws IOException {
        System.out.print("Enter source file path: ");
        String source = scanner.nextLine().trim();
        Path sourcePath = resolvePath(source);

        if (!Files.exists(sourcePath)) {
            System.out.println("❌ Source file not found: " + sourcePath);
            return;
        }

        System.out.print("Enter destination path: ");
        String dest = scanner.nextLine().trim();
        Path destPath = resolvePath(dest);

        if (Files.exists(destPath)) {
            System.out.print("Destination exists. Overwrite? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("yes") && !confirm.equals("y")) {
                System.out.println("❌ Move cancelled.");
                return;
            }
        }

        Files.createDirectories(destPath.getParent());
        Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("✅ File moved successfully from " + sourcePath + " to " + destPath);
    }

    private static void renameFile() throws IOException {
        System.out.print("Enter file/directory path to rename: ");
        String oldName = scanner.nextLine().trim();
        Path oldPath = resolvePath(oldName);

        if (!Files.exists(oldPath)) {
            System.out.println("❌ File/Directory not found: " + oldPath);
            return;
        }

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine().trim();
        Path newPath = oldPath.getParent().resolve(newName);

        if (Files.exists(newPath)) {
            System.out.println("❌ A file/directory with that name already exists.");
            return;
        }

        Files.move(oldPath, newPath);
        System.out.println("✅ Renamed successfully: " + oldPath.getFileName() + " → " + newName);
    }

    private static void getFileInfo() throws IOException {
        System.out.print("Enter file/directory path: ");
        String name = scanner.nextLine().trim();
        Path path = resolvePath(name);

        if (!Files.exists(path)) {
            System.out.println("❌ File/Directory not found: " + path);
            return;
        }

        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        
        System.out.println("\n" + "─".repeat(50));
        System.out.println("File/Directory Information:");
        System.out.println("─".repeat(50));
        System.out.println("Name:          " + path.getFileName());
        System.out.println("Absolute Path: " + path.toAbsolutePath());
        System.out.println("Type:          " + (attrs.isDirectory() ? "Directory" : "File"));
        System.out.println("Size:          " + formatFileSize(attrs.size()));
        System.out.println("Created:       " + attrs.creationTime());
        System.out.println("Modified:      " + attrs.lastModifiedTime());
        System.out.println("Readable:      " + Files.isReadable(path));
        System.out.println("Writable:      " + Files.isWritable(path));
        System.out.println("Executable:    " + Files.isExecutable(path));
        System.out.println("─".repeat(50));
    }

    private static void changeDirectory() throws IOException {
        System.out.print("Enter directory path (or '..' for parent): ");
        String dirName = scanner.nextLine().trim();
        
        Path newPath;
        if (dirName.equals("..")) {
            newPath = currentDirectory.getParent();
            if (newPath == null) {
                System.out.println("❌ Already at root directory.");
                return;
            }
        } else if (dirName.equals("~") || dirName.isEmpty()) {
            newPath = Paths.get(System.getProperty("user.home"));
        } else {
            newPath = resolvePath(dirName);
        }

        if (!Files.exists(newPath)) {
            System.out.println("❌ Directory not found: " + newPath);
            return;
        }

        if (!Files.isDirectory(newPath)) {
            System.out.println("❌ Path is not a directory.");
            return;
        }

        currentDirectory = newPath.toAbsolutePath().normalize();
        System.out.println("✅ Changed directory to: " + currentDirectory);
    }

    private static void showCurrentDirectory() {
        System.out.println("\n📁 Current Directory: " + currentDirectory.toAbsolutePath());
    }

    private static Path resolvePath(String pathStr) {
        Path path = Paths.get(pathStr);
        return path.isAbsolute() ? path : currentDirectory.resolve(path).normalize();
    }

    private static String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.2f MB", size / (1024.0 * 1024));
        return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
    }
}
