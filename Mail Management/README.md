# Mail Management System

A console-based mail management application built in Java.

## Features

- **Inbox Management**: View and read incoming mails
- **Compose Mail**: Send new mails to recipients
- **Sent Mails**: Track all sent messages
- **Trash**: Delete and manage unwanted mails
- **Search**: Find mails by subject or sender
- **Read Status**: Track read/unread status of mails

## How to Run

1. Navigate to the `src` directory
2. Compile the Java files:
   ```
   javac Mail.java MailManagementSystem.java
   ```
3. Run the application:
   ```
   java MailManagementSystem
   ```

## Usage

- Enter your email address when prompted
- Use the menu options to manage your mails
- Navigate through inbox, sent mails, and trash
- Compose new mails and search existing ones

## Classes

- **Mail.java**: Represents a mail object with sender, receiver, subject, body, and metadata
- **MailManagementSystem.java**: Main application with menu and mail operations
