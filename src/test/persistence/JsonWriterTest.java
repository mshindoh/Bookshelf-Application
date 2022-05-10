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

public class JsonWriterTest extends JsonTest{
    @Test
    void testWriterInvalidFile() {
        try {
            Bookshelf bookshelf = new Bookshelf();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Bookshelf bookshelf = new Bookshelf();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyBookshelf.json");
            writer.open();
            writer.write(bookshelf);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyBookshelf.json");
            bookshelf = reader.read();
            assertEquals(0, bookshelf.getNumberOfObjects());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Bookshelf bookshelf = new Bookshelf();
            Book book1 = new Book(150, "A flower for algernon", "Keyes", 1959);
            Book book2 = new Book(50, "A tiny princess", "Burnett", 1888);
            book1.mark(72);
            book2.addNote(new StickyNote(3, "My mom's favorite!"));
            bookshelf.insertBook(book1);
            bookshelf.insertBook(book2);
            bookshelf.insertDecoration("Teddy bear");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBookshelf.json");
            writer.open();
            writer.write(bookshelf);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBookshelf.json");
            bookshelf = reader.read();

            List<Book> books = bookshelf.getBooks();
            assertEquals(2, books.size());
            checkBook(books.get(0), 150, "A flower for algernon", "Keyes", 1959, 72, new ArrayList<>());
            List<StickyNote> notes1= new ArrayList<>();
            notes1.add(new StickyNote(3, "My mom's favorite!"));
            checkBook(books.get(1), 50, "A tiny princess", "Burnett", 1888, 0, notes1);

            List<String> decorations = bookshelf.getDecorations();
            assertEquals(1, decorations.size());
            assertEquals("Teddy bear", decorations.get(0));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
