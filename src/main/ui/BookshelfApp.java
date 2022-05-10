package ui;

import model.Book;
import model.Bookshelf;
import model.StickyNote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import persistence.JsonReader;
import persistence.JsonWriter;

// This class references code from these repos
// Link: https://github.students.cs.ubc.ca/CPSC210/TellerApp
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Bookshelf Application
public class BookshelfApp {
    private Bookshelf myShelf;
    private Scanner in;
    private static final int QUIT_COMMAND = 7;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/bookshelf.json";


    // EFFECTS: runs the bookshelf application
    public BookshelfApp() {
        runBookshelf();
    }

    // MODIFIES:    this
    // EFFECTS: processes user input to operate the bookshelf
    private void runBookshelf() {
        boolean proceed = true;
        int command;

        init();
        System.out.println("Welcome! This is your bookshelf.");

        while (proceed) {
            mainMenu();
            command = in.nextInt();
            if (command == QUIT_COMMAND) {
                System.out.println("Bye!");
                proceed = false;
            } else if (command > QUIT_COMMAND || command < 1) {
                System.out.println("Invalid command.");
            } else {
                runCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a bookshelf and sets up a scanner
    private void init() {
        myShelf = new Bookshelf();
        Book book1 = new Book(100, "A flower for algernon", "Keyes", 1959);
        Book book2 = new Book(50, "A tiny princess", "Burnett", 1888);
        Book book3 = new Book(140, "The Rabbit Revenge Plan", "Spinnen", 2001);
        book1.mark(72);
        book2.addNote(new StickyNote(3, "My mom's favorite!"));
        book2.addNote(new StickyNote(7, "This is an interesting story."));
        book3.mark(137);
        book3.addNote(new StickyNote(90, "My childhood favourite!"));
        myShelf.insertBook(book1);
        myShelf.insertBook(book2);
        myShelf.insertBook(book3);
        myShelf.insertDecoration("A teddy bear");
        in = new Scanner(System.in);
        in.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: display the main menu for the user
    private void mainMenu() {
        System.out.println("--- --- --- --- --- MAIN MENU --- --- --- --- ---");
        System.out.println("    How can I help you? Pick a command!");
        System.out.println("\t1. Add something");
        System.out.println("\t2. Remove something");
        System.out.println("\t3. View books");
        System.out.println("\t4. View decorations");
        System.out.println("\t5. Save the bookshelf to file");
        System.out.println("\t6. Load a bookshelf from file");
        System.out.println("\t7. Leave for now... *remember to save your bookshelf!*");
    }

    // MODIFIES:    this
    // EFFECTS: process the user's command response to the main menu
    private void runCommand(int command) {
        switch (command) {
            case 1:
                addObjects();
                break;
            case 2:
                removeObjects();
                break;
            case 3:
                viewBooks();
                break;
            case 4:
                viewDecorations();
                break;
            case 5:
                saveBookshelf();
                break;
            case 6:
                loadBookshelf();
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    // MODIFIES:    this
    // EFFECTS: process the user's request to add an object to the bookshelf
    private void addObjects() {
        int command;
        addMenu();
        command = in.nextInt();
        switch (command) {
            case 1:
                addBook();
                break;
            case 2:
                addDecoration();
                break;
            default:
                System.out.println("Invalid command.");
        }
        System.out.println("We'll go back to the main menu!\n");
    }

    // EFFECTS: display a menu to add objects for the user
    private void addMenu() {
        System.out.println("--- What do you want to add? Pick a command! ---");
        System.out.println("\t1. Add a book.");
        System.out.println("\t2. Add a decoration.");
    }

    // MODIFIES:    this
    // EFFECTS: add a book to the bookshelf
    private void addBook() {
        Book book = getBookInfo();
        myShelf.insertBook(book);
        System.out.println("The book is successfully added!");
    }

    // MODIFIES:    this
    // EFFECTS: gets information about a book from user input
    //          and returns a book object with the information
    private Book getBookInfo() {
        int page;
        String title;
        String author;
        int year;
        System.out.println("--- Could you give me the information about the book? ---");
        System.out.print("Title: ");
        title = in.next();
        System.out.print("Author: ");
        author = in.next();
        System.out.print("Publication year: ");
        year = in.nextInt();
        System.out.print("Number of pages: ");
        page = in.nextInt();

        return new Book(page, title, author, year);
    }

    // MODIFIES:    this
    // EFFECTS: adds a decoration to the bookshelf
    private void addDecoration() {
        String name;
        System.out.println("--- Could you give me the name of the decoration? ---");
        System.out.print("Name of the decoration: ");
        name = in.next();
        myShelf.insertDecoration(name);
        System.out.println("The decoration is successfully added!");
    }

    // MODIFIES:    this
    // EFFECTS: allows the user to remove an object from the bookshelf
    private void removeObjects() {
        int command;
        removeMenu();
        command = in.nextInt();
        switch (command) {
            case 1:
                if (removeBook()) {
                    System.out.println("The book is successfully removed.");
                } else {
                    System.out.println("The book is not in the shelf.");
                }
                break;
            case 2:
                if (removeDecoration()) {
                    System.out.println("The decoration is successfully removed.");
                } else {
                    System.out.println("The decoration is not in the shelf.");
                }
                break;
            default:
                System.out.println("Invalid input.");
        }
        System.out.println("We'll go back to the main menu!\n");
    }

    // EFFECTS: displays a menu to remove objects from the bookshelf
    private void removeMenu() {
        System.out.println("--- What do you want to remove? Pick a command! ---");
        System.out.println("\t1. Remove a book.");
        System.out.println("\t2. Remove a decoration.");
    }

    // MODIFIES:    this
    // EFFECTS: if a book specified by the user is found in the bookshelf,
    //          removes the book from the bookshelf and returns true.
    //          if the book is not found, returns false
    private boolean removeBook() {
        String title = getTitleFromUser();
        for (Book b: myShelf.getBooks()) {
            if (b.getTitle().equals(title)) {
                myShelf.removeBook(b);
                return true;
            }
        }
        return false;
    }

    // EFFECTS: obtains and returns a title of a book from the user input
    private String getTitleFromUser() {
        String title;
        System.out.println("--- What's the title of the book? ---");
        System.out.print("Title of the book: ");
        title = in.next();
        return title;
    }

    // MODIFIES:    this
    // EFFECTS: if a decoration specified by the user is found in the bookshelf,
    //          removes the decoration from the bookshelf and returns true.
    //          if the decoration is not found, returns false
    private boolean removeDecoration() {
        String name;
        System.out.println("--- What's the name of the decoration you want to remove? ---");
        System.out.print("Name of the decoration: ");
        name = in.next();
        for (String d: myShelf.getDecorations()) {
            if (d.equals(name)) {
                myShelf.removeDecoration(d);
                return true;
            }
        }
        return false;
    }

    // MODIFIES:    this
    // EFFECTS: proceeds the user's request to view contents in the bookshelf
    private void viewBooks() {
        int command;
        viewMenu();
        command = in.nextInt();
        switch (command) {
            case 1:
                viewTitles();
                break;
            case 2:
                viewAuthors();
                break;
            case 3:
                if (investigateBook()) {
                    break;
                } else {
                    System.out.println("The book is not in the shelf.");
                }
                break;
            default:
                System.out.println("Invalid input.");
        }
        System.out.println("--- --- --- --- --- --- --- ---");
        System.out.println("We'll go back to the main menu!\n");
    }

    // EFFECTS: displays a menu to view books in the bookshelf
    private void viewMenu() {
        System.out.println("--- What do you want to view? Pick a command! ---");
        System.out.println("\t1. Titles of the books.");
        System.out.println("\t2. Authors of the books.");
        System.out.println("\t3. Investigate a book.");
    }

    // EFFECTS: displays the titles of books in the bookshelf
    private void viewTitles() {
        System.out.println("--- List of books in the shelf ---");
        for (Book b: myShelf.getBooks()) {
            System.out.println(b.getTitle());
        }
    }

    // EFFECTS: displays the authors of books in the bookshelf
    private void viewAuthors() {
        System.out.println("--- List of authors in the shelf ---");
        for (Book b: myShelf.getBooks()) {
            System.out.println(b.getAuthor());
        }
    }

    // MODIFIES:   this
    // EFFECTS: if a book that the user specify the title is found,
    //          display information of the book and returns true.
    //          if the book is not found, returns false
    private boolean investigateBook() {
        String title = getTitleFromUser();
        for (Book b: myShelf.getBooks()) {
            if (b.getTitle().equals(title)) {
                System.out.println("Title: " + b.getTitle());
                System.out.println("Author: " + b.getAuthor());
                System.out.println("Publication year: " + b.getYear());
                System.out.println("Number of pages: " + b.getPage());
                System.out.println("Book mark: " + b.getMarkedPage());
                displayStickyNotes(b);
                modifyBook(b);
                return true;
            }
        }
        return false;
    }

    // REQUIRES:    the parameter book is not null
    // MODIFIES:    this
    // EFFECTS: proceeds the user's request to modify information of a book
    private void modifyBook(Book book) {
        modifyMenu();
        int command;
        command = in.nextInt();
        switch (command) {
            case 1:
                markPage(book);
                break;
            case 2:
                organizeStickyNote(book);
                break;
            case 3:
                System.out.println("Alright!");
                break;
            default:
                System.out.println("Invalid input.");
        }
    }

    // EFFECTS: displays a menu to modify a book in the bookshelf
    private void modifyMenu() {
        System.out.println("--- Do you want to modify this book? Pick a command! ---");
        System.out.println("\t1. Move the bookmark.");
        System.out.println("\t2. Organize sticky notes.");
        System.out.println("\t3. No, put the book back to the shelf.");
    }

    // REQUIRES:    the parameter book is not null
    // MODIFIES:    this
    // EFFECTS: moves the bookmark in the book according to user input
    private void markPage(Book book) {
        int pageToMark;
        System.out.print("Page to put the bookmark: ");
        pageToMark = in.nextInt();
        if (book.mark(pageToMark)) {
            System.out.println("The bookmark is at page " + pageToMark + " now. ");
        } else {
            System.out.println("The page doesn't exist. The bookmark is at the original position.");
        }
    }

    // REQUIRES:    the parameter book is not null
    // MODIFIES:    this
    // EFFECTS: proceeds the user's request to organize sticky notes in the book
    private void organizeStickyNote(Book book) {
        displayStickyNotes(book);
        stickyNoteMenu();
        int command;
        command = in.nextInt();
        switch (command) {
            case 1:
                addStickyNote(book);
                break;
            case 2:
                removeStickyNote(book);
                break;
            case 3:
                rewriteStickyNote(book);
                break;
            case 4:
                relocateStickyNote(book);
                break;
            default:
                System.out.println("Invalid input.");
        }
    }

    // EFFECTS: displays contents of all sticky notes in the book
    private void displayStickyNotes(Book book) {
        for (StickyNote s: book.getNotes()) {
            System.out.println("Note - in page " + s.getPage());
            System.out.println("\t" + s.getText());
        }
    }

    // EFFECTS: display menu for the user to choose actions regarding sticky notes
    private void stickyNoteMenu() {
        System.out.println("--- What do you want to do with the sticky notes? Pick a command! ---");
        System.out.println("\t1. Add.");
        System.out.println("\t2. Remove.");
        System.out.println("\t3. Rewrite.");
        System.out.println("\t3. Relocate.");
    }

    // REQUIRES:    the parameter book is not null
    // MODIFIES:    this
    // EFFECTS: adds a new sticky note to the book according to the user input
    private void addStickyNote(Book book) {
        System.out.println("Which page do you want to add the note to?: ");
        int page = in.nextInt();
        System.out.println("Write your note (in one line!)");
        String note = in.next();
        book.addNote(new StickyNote(page, note));
    }

    // REQUIRES:    the parameter book is not null
    // MODIFIES:    this
    // EFFECTS: removes sticky notes from the page specified by the user
    private void removeStickyNote(Book book) {
        System.out.println("Which page do you want to remove notes from?: ");
        int page = in.nextInt();
        for (StickyNote s: book.getNotes()) {
            if (s.getPage() == page) {
                book.removeNote(s);
            }
        }
        System.out.println("The page now have no sticky note.");
    }

    // REQUIRES:    the parameter book is not null
    // MODIFIES:    this
    // EFFECTS: allows the user to rewrite sticky notes on the page specified by the user
    private void rewriteStickyNote(Book book) {
        System.out.println("Where is the note that you want to rewrite?: page ");
        int page = in.nextInt();
        for (StickyNote s: book.getNotes()) {
            if (s.getPage() == page) {
                System.out.println("Note - in page " + s.getPage());
                System.out.println("\t" + s.getText());
                System.out.println("Overwrite this note to (in one line): ");
                String newNote = in.next();
                s.rewrite(newNote);
            }
        }
        System.out.println("You overwrote the sticky note(s).");
    }

    // REQUIRES:    the parameter book is not null
    // MODIFIES:    this
    // EFFECTS: allows the user to relocate sticky notes from and to the pages specified by the user
    private void relocateStickyNote(Book book) {
        System.out.println("Where is the note that you want to relocate?: page ");
        int page = in.nextInt();
        for (StickyNote s: book.getNotes()) {
            if (s.getPage() == page) {
                System.out.println("Note - in page " + s.getPage());
                System.out.println("\t" + s.getText());
                System.out.println("Relocate this note to : page ");
                int newPage = in.nextInt();
                s.relocate(newPage);
            }
        }
        System.out.println("You relocated the sticky note(s).");
    }

    // EFFECTS: displays names of all decorations in the bookshelf
    private void viewDecorations() {
        System.out.println("***Your decorations***");
        for (String n: myShelf.getDecorations()) {
            System.out.println(n);
        }
    }

    // EFFECTS: saves the bookshelf to file
    private void saveBookshelf() {
        try {
            jsonWriter.open();
            jsonWriter.write(myShelf);
            jsonWriter.close();
            System.out.println("Saved the bookshelf to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads a bookshelf from file
    private void loadBookshelf() {
        try {
            myShelf = jsonReader.read();
            System.out.println("Loaded a bookshelf from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
