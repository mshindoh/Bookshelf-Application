package model;

import model.Book;
import model.Bookshelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/IntegerSetLecLab
public class BookshelfTest {
    private Bookshelf shelf;

    @BeforeEach
    void runBefore() {
        shelf = new Bookshelf();
    }

    @Test
    void testConstructor() {
        assertEquals(0, shelf.getNumberOfBooks());
        assertEquals(0, shelf.getNumberOfDecorations());
    }

    @Test
    void testInsertBook() {
        Book book1 = new Book(15, "The title", "An author", 1997);

        assertTrue(shelf.insertBook(book1));
        assertEquals(1, shelf.getNumberOfBooks());
        assertEquals(1, shelf.getNumberOfObjects());
        assertTrue(shelf.containsBook(book1));
        List<Book> bookList = shelf.getBooks();
        assertEquals(book1, bookList.get(0));
    }

    @Test
    void testInsertBookOverlap() {
        Book book1 = new Book(15, "The title", "An author", 1997);

        assertTrue(shelf.insertBook(book1));
        assertFalse(shelf.insertBook(book1));
        assertEquals(1, shelf.getNumberOfBooks());
        assertEquals(1, shelf.getNumberOfObjects());
        assertTrue(shelf.containsBook(book1));
    }

    @Test
    void testInsertBookMany() {
        Book book1 = new Book(15, "The title", "An author", 1997);
        Book book2 = new Book(15, "The title", "An author", 1997);
        Book book3 = new Book(15, "The title", "An author", 1997);

        assertTrue(shelf.insertBook(book1));
        assertTrue(shelf.insertBook(book2));
        assertTrue(shelf.insertBook(book3));
        assertEquals(3, shelf.getNumberOfBooks());
        assertEquals(3, shelf.getNumberOfObjects());
        assertTrue(shelf.containsBook(book1));
        assertTrue(shelf.containsBook(book2));
        assertTrue(shelf.containsBook(book3));
    }


    @Test
    void testRemoveBook() {
        Book book1 = new Book(15, "The title", "An author", 1997);

        assertTrue(shelf.insertBook(book1));
        assertTrue(shelf.removeBook(book1));
        assertEquals(0, shelf.getNumberOfBooks());
        assertEquals(0, shelf.getNumberOfObjects());
        assertFalse(shelf.containsBook(book1));

    }

    @Test
    void testRemoveNotFound() {
        Book book1 = new Book(15, "The title", "An author", 1997);
        Book book2 = new Book(15, "The title", "An author", 1997);

        assertTrue(shelf.insertBook(book1));
        assertFalse(shelf.removeBook(book2));
        assertEquals(1, shelf.getNumberOfBooks());
        assertEquals(1, shelf.getNumberOfObjects());
        assertTrue(shelf.containsBook(book1));
        assertFalse(shelf.containsBook(book2));
    }

    @Test
    void testRemoveBookMany() {
        Book book1 = new Book(15, "The title", "An author", 1997);
        Book book2 = new Book(15, "The title", "An author", 1997);
        Book book3 = new Book(15, "The title", "An author", 1997);

        assertTrue(shelf.insertBook(book1));
        assertTrue(shelf.insertBook(book2));
        assertTrue(shelf.insertBook(book3));
        assertTrue(shelf.removeBook(book1));
        assertTrue(shelf.removeBook(book3));
        assertEquals(1, shelf.getNumberOfBooks());
        assertEquals(1, shelf.getNumberOfObjects());
        assertFalse(shelf.containsBook(book1));
        assertFalse(shelf.containsBook(book3));
        assertTrue(shelf.containsBook(book2));
    }

    @Test
    void testRemoveBookMiddle() {
        Book book1 = new Book(15, "The title", "An author", 1997);
        Book book2 = new Book(15, "The title", "An author", 1997);
        Book book3 = new Book(15, "The title", "An author", 1997);

        assertTrue(shelf.insertBook(book1));
        assertTrue(shelf.insertBook(book2));
        assertTrue(shelf.insertBook(book3));
        assertTrue(shelf.removeBook(book2));
        assertEquals(2, shelf.getNumberOfBooks());
        assertEquals(2, shelf.getNumberOfObjects());
        assertTrue(shelf.containsBook(book1));
        assertTrue(shelf.containsBook(book3));
        assertFalse(shelf.containsBook(book2));
    }

    @Test
    void testInsertDecoration() {
        String decoration1 = "A doll";

        shelf.insertDecoration(decoration1);
        assertEquals(1, shelf.getNumberOfDecorations());
        assertEquals(1, shelf.getNumberOfObjects());
        assertTrue(shelf.containsDecoration(decoration1));
        List<String> decorationList = shelf.getDecorations();
        assertEquals(decoration1, decorationList.get(0));
    }

    @Test
    void testInsertDecorationMany() {
        String decoration1 = "A doll";
        String decoration2 = "A teddy bear";
        String decoration3 = "A photo frame";

        shelf.insertDecoration(decoration1);
        shelf.insertDecoration(decoration2);
        shelf.insertDecoration(decoration3);
        assertEquals(3, shelf.getNumberOfDecorations());
        assertEquals(3, shelf.getNumberOfObjects());
        assertTrue(shelf.containsDecoration(decoration1));
        assertTrue(shelf.containsDecoration(decoration2));
        assertTrue(shelf.containsDecoration(decoration3));
    }

    @Test
    void testRemoveDecoration() {
        String decoration1 = "A doll";

        shelf.insertDecoration(decoration1);
        assertTrue(shelf.removeDecoration(decoration1));
        assertEquals(0, shelf.getNumberOfDecorations());
        assertEquals(0, shelf.getNumberOfObjects());
        assertFalse(shelf.containsDecoration(decoration1));
    }

    @Test
    void testRemoveDecorationNotFound() {
        String decoration1 = "A doll";
        String decoration2 = "A teddy bear";

        shelf.insertDecoration(decoration1);
        assertFalse(shelf.removeDecoration(decoration2));
        assertEquals(1, shelf.getNumberOfDecorations());
        assertEquals(1, shelf.getNumberOfObjects());
        assertTrue(shelf.containsDecoration(decoration1));
        assertFalse(shelf.containsDecoration(decoration2));
    }

    @Test
    void testRemoveDecorationMany() {
        String decoration1 = "A doll";
        String decoration2 = "A teddy bear";
        String decoration3 = "A photo frame";

        shelf.insertDecoration(decoration1);
        shelf.insertDecoration(decoration2);
        shelf.insertDecoration(decoration3);
        assertTrue(shelf.removeDecoration(decoration1));
        assertTrue(shelf.removeDecoration(decoration3));
        assertEquals(1, shelf.getNumberOfDecorations());
        assertEquals(1, shelf.getNumberOfObjects());
        assertFalse(shelf.containsDecoration(decoration1));
        assertFalse(shelf.containsDecoration(decoration3));
        assertTrue(shelf.containsDecoration(decoration2));
    }
}
