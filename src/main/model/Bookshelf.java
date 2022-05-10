package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/IntegerSetLecLab

// This class represents a bookshelf where books and decorations can be stored in appropriate lists
public class Bookshelf implements Writable {
    protected List<Book> books;
    protected List<String> decorations;

    // constructs a Bookshelf object
    // EFFECTS: book and decorations are set to be new empty ArrayLists
    public Bookshelf() {
        books = new ArrayList<>();
        decorations = new ArrayList<>();
    }

    // EFFECTS: Returns true if the given book is in the bookshelf,
    //          and false otherwise
    public boolean containsBook(Book book) {
        return books.contains(book);
    }

    // MODIFIES: this
    // EFFECTS: if the given newBook is not in the bookshelf,
    //          adds the book to the book and returns true.
    //          if the given newBook is already in the bookshelf, returns false
    public boolean insertBook(Book newBook) {
        if (!books.contains(newBook)) {
            books.add(newBook);
            EventLog.getInstance().logEvent(new Event("A book added."));
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: if the given book is in the Bookshelf,
    //          removes the book from the Bookshelf and returns true.
    //          if the given book is not in the Bookshelf, return false
    public boolean removeBook(Book book) {
        if (books.contains(book)) {
            books.remove(book);
            EventLog.getInstance().logEvent(new Event("A book removed."));
            return true;
        }
        return false;
    }

    // EFFECTS: Returns the number of books in the Bookshelf
    public int getNumberOfBooks() {
        return books.size();
    }

    // EFFECTS: Returns true if the given decoration is in the Bookshelf,
    //          and false otherwise
    public boolean containsDecoration(String decoration) {
        return decorations.contains(decoration);
    }

    // MODIFIES: this
    // EFFECTS: newDecoration is added to decorations of the Bookshelf
    public void insertDecoration(String newDecoration) {
        decorations.add(newDecoration);
        EventLog.getInstance().logEvent(new Event("A decoration added."));
    }

    // MODIFIES: this
    // EFFECTS: if the given decoration is in the Bookshelf,
    //          remove the decoration from the Bookshelf.
    //          if the given decoration is not in the Bookshelf, do nothing
    public boolean removeDecoration(String decoration) {
        if (decorations.contains(decoration)) {
            decorations.remove(decoration);
            EventLog.getInstance().logEvent(new Event("A decoration removed."));
            return true;
        }
        return false;
    }

    // EFFECTS: Returns the number of decorations in the Bookshelf
    public int getNumberOfDecorations() {
        return decorations.size();
    }

    // EFFECTS: Returns the number of objects in the Bookshelf
    public int getNumberOfObjects() {
        return books.size() + decorations.size();
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<String> getDecorations() {
        return decorations;
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("books", booksToJson());
        json.put("decorations", decorationsToJson());
        return json;
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: returns notes of this book as a JSON array
    private JSONArray booksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Book b: books) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: returns notes of this book as a JSON array
    private JSONArray decorationsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (String d: decorations) {
            // is it appropriate to add a string object to a JSONArray?
            JSONObject json = new JSONObject();
            json.put("decorationName", d);
            jsonArray.put(json);
        }
        return jsonArray;
    }
}
