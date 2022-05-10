package persistence;

import model.StickyNote;
import model.Book;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonTest {
    protected void checkBook(Book book, int page, String title, String author, int year, int markedPage, List<StickyNote> notes) {
        assertEquals(page, book.getPage());
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(year, book.getYear());
        assertEquals(markedPage, book.getMarkedPage());
        int i = 0;
        for (StickyNote n: book.getNotes()) {
            assertEquals(notes.get(i).getPage(), n.getPage());
            i++;
        }
    }
}
