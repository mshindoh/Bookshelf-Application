package persistence;

import model.StickyNote;
import model.Book;
import model.Bookshelf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads bookshelf from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads a Bookshelf from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Bookshelf read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBookshelf(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses a Bookshelf from JSON object and returns it
    private Bookshelf parseBookshelf(JSONObject jsonObject) {
        Bookshelf bookshelf = new Bookshelf();
        addBooks(bookshelf, jsonObject);
        addDecorations(bookshelf, jsonObject);
        return bookshelf;
    }

    // MODIFIES: bookshelf
    // EFFECTS: parses Books from JSON object and adds them to the bookshelf
    private void addBooks(Bookshelf bookshelf, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("books");
        for (Object json : jsonArray) {
            JSONObject nextBook = (JSONObject) json;
            addBook(bookshelf, nextBook);
        }
    }

    // MODIFIES: bookshelf
    // EFFECTS: parses a book from JSON object and adds it to bookshelf
    private void addBook(Bookshelf bookshelf, JSONObject jsonObject) {
        int page = jsonObject.getInt("page");
        String title = jsonObject.getString("title");
        String author = jsonObject.getString("author");
        int year = jsonObject.getInt("year");
        Book book = new Book(page, title, author, year);
        book.mark(jsonObject.getInt("markedPage"));
        addStickyNotes(book, jsonObject);
        bookshelf.insertBook(book);
    }

    // MODIFIES: book
    // EFFECTS: parses notes from JSON object and adds them to book
    private void addStickyNotes(Book book, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("notes");
        for (Object json : jsonArray) {
            JSONObject nextNote = (JSONObject) json;
            addStickyNote(book, nextNote);
        }
    }

    // MODIFIES: book
    // EFFECTS: parses note from JSON object and adds it to book
    private void addStickyNote(Book book, JSONObject jsonObject) {
        int page = jsonObject.getInt("page");
        String text = jsonObject.getString("text");

        StickyNote note = new StickyNote(page, text);
        book.addNote(note);
    }

    // MODIFIES: bookshelf
    // EFFECTS: parses decorations from JSON object and adds them to bookshelf
    private void addDecorations(Bookshelf bookshelf, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("decorations");
        for (Object json : jsonArray) {
            JSONObject nextDecoration = (JSONObject) json;
            addDecoration(bookshelf, nextDecoration);
        }
    }

    // MODIFIES: bookshelf
    // EFFECTS: parses decoration from JSON object and adds it to bookshelf
    private void addDecoration(Bookshelf bookshelf, JSONObject jsonObject) {
        String decoration = jsonObject.getString("decorationName");
        bookshelf.insertDecoration(decoration);
    }

}
