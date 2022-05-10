package model;

import model.Book;
import model.StickyNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/IntegerSetLecLab
public class BookTest {
    private Book book;

    @BeforeEach
    void runBefore() {
        book = new Book(50, "Title", "Author", 1998);
    }

    @Test
    void testConstructor() {
        assertEquals(50, book.getPage());
        assertEquals("Title", book.getTitle());
        assertEquals("Author", book.getAuthor());
        assertEquals(1998, book.getYear());
        assertEquals(0, book.getMarkedPage());
        assertEquals(0, book.getNumberOfNotes());
    }

    @Test
    void testGetMarkedPage() {
        assertEquals(0, book.getMarkedPage());
    }

    @Test
    void testMarkValid() {
        assertTrue(book.mark(5));
        assertEquals(5, book.getMarkedPage());
    }

    @Test
    void testMarkLarge() {
        assertFalse(book.mark(51));
        assertEquals(0, book.getMarkedPage());
    }

    @Test
    void testMarkSmall() {
        assertFalse(book.mark(-1));
        assertEquals(0, book.getMarkedPage());
    }

    @Test
    void testAddNote() {
        StickyNote stickyNote1 = new StickyNote(0, "The content of note 1.");

        assertTrue(book.addNote(stickyNote1));
        assertEquals(1, book.getNumberOfNotes());
        assertTrue(book.containNote(stickyNote1));
        List<StickyNote> noteList = book.getNotes();
        assertEquals(stickyNote1, noteList.get(0));
    }

    @Test
    void testAddNoteOverlap() {
        StickyNote stickyNote1 = new StickyNote(0, "The content of note 1.");

        assertTrue(book.addNote(stickyNote1));
        assertFalse(book.addNote(stickyNote1));
        assertEquals(1, book.getNumberOfNotes());
        assertTrue(book.containNote(stickyNote1));
    }

    @Test
    void testAddNoteMany() {
        StickyNote stickyNote1 = new StickyNote(0, "The content of note 1.");
        StickyNote stickyNote2 = new StickyNote(10, "The content of note 2.");
        StickyNote stickyNote3 = new StickyNote(50, "The content of note 3.");

        assertTrue(book.addNote(stickyNote1));
        assertTrue(book.addNote(stickyNote2));
        assertTrue(book.addNote(stickyNote3));
        assertEquals(3, book.getNumberOfNotes());
        assertTrue(book.containNote(stickyNote1));
        assertTrue(book.containNote(stickyNote2));
        assertTrue(book.containNote(stickyNote3));
    }

    @Test
    void testRemoveNote() {
        StickyNote stickyNote1 = new StickyNote(0, "The content of note 1.");

        assertTrue(book.addNote(stickyNote1));
        assertTrue(book.removeNote(stickyNote1));
        assertEquals(0, book.getNumberOfNotes());
        assertFalse(book.containNote(stickyNote1));
    }

    @Test
    void testRemoveNoteNotFound() {
        StickyNote stickyNote1 = new StickyNote(0, "The content of note 1.");
        StickyNote stickyNote2 = new StickyNote(10, "The content of note 2.");

        assertTrue(book.addNote(stickyNote1));
        assertFalse(book.removeNote(stickyNote2));
        assertEquals(1, book.getNumberOfNotes());
        assertTrue(book.containNote(stickyNote1));
        assertFalse(book.containNote(stickyNote2));
    }

    @Test
    void testRemoveNoteMany() {
        StickyNote stickyNote1 = new StickyNote(0, "The content of note 1.");
        StickyNote stickyNote2 = new StickyNote(10, "The content of note 2.");
        StickyNote stickyNote3 = new StickyNote(50, "The content of note 3.");

        assertTrue(book.addNote(stickyNote1));
        assertTrue(book.addNote(stickyNote2));
        assertTrue(book.addNote(stickyNote3));
        assertTrue(book.removeNote(stickyNote1));
        assertTrue(book.removeNote(stickyNote3));
        assertEquals(1, book.getNumberOfNotes());
        assertFalse(book.containNote(stickyNote1));
        assertFalse(book.containNote(stickyNote3));
        assertTrue(book.containNote(stickyNote2));
    }

    @Test
    void testRemoveNoteMiddle() {
        StickyNote stickyNote1 = new StickyNote(0, "The content of note 1.");
        StickyNote stickyNote2 = new StickyNote(10, "The content of note 2.");
        StickyNote stickyNote3 = new StickyNote(50, "The content of note 3.");

        assertTrue(book.addNote(stickyNote1));
        assertTrue(book.addNote(stickyNote2));
        assertTrue(book.addNote(stickyNote3));
        assertTrue(book.removeNote(stickyNote2));
        assertEquals(2, book.getNumberOfNotes());
        assertTrue(book.containNote(stickyNote1));
        assertTrue(book.containNote(stickyNote3));
        assertFalse(book.containNote(stickyNote2));
    }
}
