package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/IntegerSetLecLab

// This class represents a book with the number of page, title, author, year published,
// marked page and a list of notes.
public class Book implements Writable {
    private int page;
    private String title;
    private String author;
    private int year;
    private int markedPage;
    private List<StickyNote> notes;

    // constructs a Book object
    // REQUIRES:    page and year are a positive integer
    // EFFECTS: page, title, author and year are set to the given values,
    //          markedPage is set to be zero by default,
    //          and notes and quotes are set to empty lists
    public Book(int page, String title, String author, int year) {
        this.page = page;
        this.title = title;
        this.author = author;
        this.year = year;
        markedPage = 0;
        notes = new ArrayList<>();
    }

    // EFFECTS: returns number of pages in the Book
    public int getPage() {
        return page;
    }

    // EFFECTS: returns the title of the Book
    public String getTitle() {
        return title;
    }

    // EFFECTS: returns the author of the Book
    public String getAuthor() {
        return author;
    }

    // EFFECTS: returns the year that the book is published
    public int getYear() {
        return year;
    }

    // EFFECTS: returns the marked page in the Book
    public int getMarkedPage() {
        return markedPage;
    }

    // EFFECTS: returns the number of notes in the Book
    public int getNumberOfNotes() {
        return notes.size();
    }

    // MODIFIES:    this
    // EFFECTS: if the page number is a valid number,
    //          sets markedPage to the given page and returns true, and
    //          returns false otherwise
    public boolean mark(int pageToMark) {
        if (0 <= pageToMark && pageToMark <= page) {
            markedPage = pageToMark;
            return true;
        }
        return false;
    }

    // MODIFIES:    this
    // EFFECTS: if the given sticky note is not in the book,
    //          adds the decoration from the book and returns true.
    //          if the given sticky note is already in the book, returns false
    public boolean addNote(StickyNote note) {
        if (!notes.contains(note)) {
            notes.add(note);
            EventLog.getInstance().logEvent(new Event("A sticky note added."));
            return true;
        }
        return false;
    }

    // EFFECTS: returns true if note has already been added to the book,
    //          returns false otherwise
    public boolean containNote(StickyNote note) {
        return notes.contains(note);
    }

    // MODIFIES:    this
    // EFFECTS: if the given sticky note is in the book,
    //          removes the decoration from the book and returns true.
    //          if the given sticky note is not in the book, returns false
    public boolean removeNote(StickyNote note) {
        if (notes.contains(note)) {
            notes.remove(note);
            EventLog.getInstance().logEvent(new Event("A sticky note removed."));
            return true;
        }
        return false;
    }

    public List<StickyNote> getNotes() {
        return notes;
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("title", title);
        json.put("author", author);
        json.put("year", year);
        json.put("markedPage", markedPage);
        json.put("notes", notesToJson());
        return json;
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: returns notes of this book as a JSON array
    private JSONArray notesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (StickyNote n: notes) {
            jsonArray.put(n.toJson());
        }

        return jsonArray;
    }


}
