package persistence;

import model.StickyNote;
import model.Book;
import model.Bookshelf;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonReaderTest extends JsonTest{
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Bookshelf bookshelf = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBookshelf.json");
        try {
            Bookshelf bookshelf = reader.read();
            assertEquals(0, bookshelf.getNumberOfObjects());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBookshelf.json");
        try {
            Bookshelf bookshelf = reader.read();
            List<Book> books = bookshelf.getBooks();
            assertEquals(3, books.size());
            List<StickyNote> notes0= new ArrayList<>();
            checkBook(books.get(0), 150, "A flower for algernon", "Keyes", 1959, 72, notes0);
            List<StickyNote> notes1= new ArrayList<>();
            notes1.add(new StickyNote(3, "My mom's favorite!"));
            notes1.add(new StickyNote(7, "This is an interesting story."));
            checkBook(books.get(1), 137, "A tiny princess", "Burnett", 1888, 50, notes1);
            List<StickyNote> notes2= new ArrayList<>();
            notes2.add(new StickyNote(90, "My childhood favourite!"));
            checkBook(books.get(2), 140, "The Rabbit Revenge Plan", "Spinnen", 2001, 0, notes2);

            List<String> decorations = bookshelf.getDecorations();
            assertEquals(2, decorations.size());
            assertEquals("Teddy bear", decorations.get(0));
            assertEquals("Flowers", decorations.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
