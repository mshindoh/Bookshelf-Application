package model;

import model.StickyNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/IntegerSetLecLab
public class StickyNoteTest {
    private StickyNote note;

    @BeforeEach
    void runBefore() {
        note = new StickyNote(5, "The original content.");
    }

    @Test
    void testConstructor() {
        assertEquals(5, note.getPage());
        assertEquals("The original content.", note.getText());
    }

    @Test
    void testRelocate() {
        note.relocate(10);
        assertEquals(10, note.getPage());
    }

    @Test
    void testRewrite() {
        note.rewrite("The modified content.");
        assertEquals("The modified content.", note.getText());
    }
}
