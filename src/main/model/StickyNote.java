package model;

import org.json.JSONObject;
import persistence.Writable;

// This class represents a sticky note with the information of
// the page number where the sticky note is located and text on the sticky note.
public class StickyNote implements Writable {
    private int page;
    private String text;

    // constructs a StickyNote object
    // REQUIRES:    a pageNo is zero or a positive integer
    // EFFECTS: page of the sticky note is set to be the given pageNo
    //          text is set to be given note
    public StickyNote(int pageNo, String note) {
        page = pageNo;
        text = note;
    }

    // EFFECTS: returns the assigned page number of the sticky note
    public int getPage() {
        return page;
    }

    // EFFECTS: returns the content of the sticky note
    public String getText() {
        return text;
    }

    // REQUIRES:    a newPage is zero or a positive integer
    // MODIFIES:    this
    // EFFECTS: page of the sticky note is set to be the given newPage
    public void relocate(int newPage) {
        page = newPage;
    }

    // MODIFIES:    this
    // EFFECTS: text of the sticky note is overwritten to the given note
    public void rewrite(String note) {
        text = note;
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("text", text);
        return json;
    }
}
