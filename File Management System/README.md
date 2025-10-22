# File Management Console Application

A comprehensive console-based file management system implemented in Java that provides full CRUD operations for files and directories.

## Features

### File Operations
- ✅ **Create File** - Create new files with optional content
- ✅ **Read File** - Display file contents with line numbers
- ✅ **Update File** - Append or overwrite file content
- ✅ **Delete File** - Remove files with confirmation
- ✅ **Copy File** - Copy files to new locations
- ✅ **Move File** - Move/relocate files
- ✅ **Rename File** - Rename files and directories

### Directory Operations
- ✅ **Create Directory** - Create new directories (with parent path support)
- ✅ **Delete Directory** - Remove directories recursively
- ✅ **List Contents** - View directory contents with details (type, size)
- ✅ **Change Directory** - Navigate the file system
- ✅ **Show Current Directory** - Display working directory

### Advanced Features
- ✅ **Search Files** - Find files using wildcards (e.g., `*.txt`, `report*`)
  - Shallow search (current directory only)
  - Recursive search (all subdirectories)
- ✅ **File Information** - View detailed metadata:
  - Size, creation time, modification time
  - Permissions (readable, writable, executable)
  - Absolute path and type

## How to Run

### Prerequisites
- Java Development Kit (JDK) 11 or higher

### Compile
```bash
javac "File Management System/FileManagementSystem.java"
```

### Run
```bash
java -cp "File Management System" FileManagementSystem
```

Or on Windows PowerShell:
```powershell
javac "File Management System\FileManagementSystem.java"
java -cp "File Management System" FileManagementSystem
```

## Usage Guide

### Main Menu
When you start the application, you'll see a menu with 15 options:

```
==================================================
Current Directory: /home/user
==================================================
1.  Create File
2.  Create Directory
3.  Read File
4.  Update File (Append/Overwrite)
5.  Delete File
6.  Delete Directory
7.  List Directory Contents
8.  Search Files
9.  Copy File
10. Move File
11. Rename File/Directory
12. Get File/Directory Info
13. Change Directory
14. Show Current Directory
0.  Exit
==================================================
```

### Example Workflows

#### Creating and Editing a File
1. Select option `1` (Create File)
2. Enter filename: `notes.txt`
3. Enter content: `This is my first note`
4. File created! ✅

To append more content:
1. Select option `4` (Update File)
2. Enter filename: `notes.txt`
3. Choose mode `1` (Append)
4. Enter new content: `Adding more notes`

#### Organizing Files with Directories
1. Select option `2` (Create Directory)
2. Enter name: `projects/java/myapp`
3. Directory structure created! ✅

#### Searching for Files
1. Select option `8` (Search Files)
2. Enter pattern: `*.java` (finds all Java files)
3. Choose recursive: `no` (current dir only) or `yes` (all subdirs)
4. Results displayed with full paths

#### Copying and Moving Files
Copy a file:
1. Select option `9` (Copy File)
2. Enter source: `report.txt`
3. Enter destination: `backup/report_copy.txt`
4. File copied! ✅

Move a file:
1. Select option `10` (Move File)
2. Enter source: `temp.txt`
3. Enter destination: `archive/temp.txt`
4. File moved! ✅

#### Viewing File Information
1. Select option `12` (Get File Info)
2. Enter path: `document.pdf`
3. See details:
   - Size: 2.45 MB
   - Created: 2025-10-21T10:30:15Z
   - Modified: 2025-10-21T14:22:03Z
   - Permissions: Readable, Writable

### Navigation Tips

- **Relative paths**: Enter just the filename (e.g., `file.txt`) for current directory
- **Absolute paths**: Enter full path (e.g., `/home/user/documents/file.txt`)
- **Parent directory**: Use `..` when changing directory
- **Home directory**: Use `~` or press Enter when changing directory
- **Wildcards**: Use `*` for multiple characters, `?` for single character
  - `*.txt` - all text files
  - `report_*.pdf` - all PDFs starting with "report_"
  - `file?.txt` - file1.txt, fileA.txt, etc.

## Features Breakdown

### Path Resolution
- Supports both relative and absolute paths
- Automatically creates parent directories when needed
- Normalizes paths for consistency

### Safety Features
- Confirmation prompts for destructive operations (delete, overwrite)
- Existence checks before operations
- Clear error messages with ❌ indicator
- Success confirmations with ✅ indicator

### File Size Display
Automatically formats file sizes for readability:
- Bytes (B)
- Kilobytes (KB)
- Megabytes (MB)
- Gigabytes (GB)

### Directory Listing
Formatted table view showing:
- File/directory name (truncated if too long)
- Type indicator: `[FILE]` or `[DIR]`
- Size (for files)

## Technical Details

### Dependencies
- Standard Java library only (no external dependencies)
- Uses `java.nio.file` for modern file I/O
- Compatible with Java 11+

### Error Handling
- Comprehensive exception handling for all file operations
- User-friendly error messages
- Graceful recovery from failures

### Limitations
- File content encoding: Uses system default
- No file permissions modification
- No archive/compression support
- Basic text file reading (may not display binary files properly)

## Security Considerations

⚠️ **Important Safety Notes:**
- This application can delete files and directories permanently
- Always double-check paths before confirming destructive operations
- Recursive directory deletion cannot be undone
- Be cautious when running with elevated privileges

## Future Enhancements

Potential features for future versions:
- File compression/decompression (ZIP support)
- Batch operations (copy/move/delete multiple files)
- File comparison and diff
- Permission/attribute modification
- File encryption/decryption
- Configuration file for default settings
- Command history and favorites
- Color-coded output (ANSI colors)
- Progress bars for large file operations

## Troubleshooting

### Common Issues

**Issue**: "Access Denied" error
- **Solution**: Check file/directory permissions or run with appropriate privileges

**Issue**: File not found
- **Solution**: Verify the path is correct; use option 7 to list contents

**Issue**: Cannot delete directory
- **Solution**: Directory may not be empty; use recursive delete (option 6)

**Issue**: Path too long on Windows
- **Solution**: Use shorter paths or enable long path support in Windows

## Contributing

Feel free to enhance this application with:
- Additional file operations
- Improved UI/UX
- Performance optimizations
- Bug fixes

## License

This project is part of the Console_Applications repository. See repository LICENSE for details.
