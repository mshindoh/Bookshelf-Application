package ui;


import model.Book;
import model.Bookshelf;
import model.Event;
import model.EventLog;
import model.StickyNote;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;

//realizes a GUI application imitating a bookshelf
public class BookshelfAppGUI extends JPanel implements ActionListener {
    private JTextField titleField;
    private JTextField authorField;
    private JSpinner yearSpinner;
    private JSpinner pageSpinner;
    private JList list;
    private DefaultListModel listModel;

    private boolean afterRemoving = false;

    private Bookshelf bookshelf;

    private static final String JSON_STORE = "./data/bookshelf.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //MODIFIES  this
    //EFFECTS: Construct a BookshelfAppGUI (the application)
    public BookshelfAppGUI() {
        initializeBookshelf();
        setUpGUI();
    }

    //MODIFIES: this
    //EFFECTS:  constructs the GUI with a list of books in the bookshelf and a field to add a book
    //This method references code from this website
    //Link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
    private void setUpGUI() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel leftHalf = new JPanel();
        leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.PAGE_AXIS));
        leftHalf.add(createShelf());
        add(leftHalf);

        //Don't allow us to stretch vertically.
        JPanel rightHalf = new JPanel() {
            //Don't allow us to stretch vertically.
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height);
            }
        };
        rightHalf.setLayout(new BoxLayout(rightHalf, BoxLayout.PAGE_AXIS));
        rightHalf.add(createEntryFields());
        rightHalf.add(createButtons());
        add(rightHalf);
    }

    //MODIFIES: this
    //EFFECTS:  Creates and returns a GUI panel that contains a list
    //          of information (title and author) of the books in the bookshelf
    //This method references code from this website
    //Link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
    private JComponent createShelf() {
        JPanel panel = new JPanel();
        listModel = new DefaultListModel();
        getTitleAndAuthor();

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(new SelectListener());
        list.setVisibleRowCount(10);
        JScrollPane listScrollPane = new JScrollPane(list);

        add(listScrollPane, BorderLayout.CENTER);

        return panel;
    }

    //REQUIRES: bookshelf is not null
    //MODIFIES: this
    //EFFECTS:  get titles and authors of the books in the bookshelf and add them to listModel
    private void getTitleAndAuthor() {
        String backCover;
        for (Book b: bookshelf.getBooks()) {
            backCover = b.getTitle() + " - " + b.getAuthor();
            listModel.addElement(backCover);
        }
    }

    //MODIFIES: this
    //EFFECTS:  Creates and returns a GUI panel to put a book's information
    //This method references code from this website
    //Link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
    private JComponent createEntryFields() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String[] labelStrings = {"Title", "Author", "Year", "Pages"};

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = setUpFields(labelStrings.length);

        //Associate label/field pairs, add everything, and lay it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i], JLabel.TRAILING);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf;
            if (fields[i] instanceof JSpinner) {
                tf = getTextField((JSpinner) fields[i]);
            } else {
                tf = (JTextField) fields[i];
            }
            tf.addActionListener(this);
        }
        return panel;
    }

    //MODIFIES: this
    //EFFECTS:  Creates and returns an array of JComponents (JTextFields and JSpinners)
    //          that will be used in a panel to add a book
    public JComponent[] setUpFields(int numOfFields) {
        JComponent[] fields = new JComponent[numOfFields];
        int fieldNum = 0;

        //Create the fields and set it up.
        titleField = new JTextField();
        titleField.setColumns(20);
        fields[fieldNum++] = titleField;

        authorField = new JTextField();
        authorField.setColumns(20);
        fields[fieldNum++] = authorField;

        yearSpinner = new JSpinner(new SpinnerNumberModel(2000, 1000, 2022, 1));
        fields[fieldNum++] = yearSpinner;

        pageSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 2000, 1));
        fields[fieldNum] = pageSpinner;
        return fields;
    }

    //EFFECTS:  obtains information from a spinner
    //This method references code from this website
    //Link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor) editor).getTextField();
        } else {
            System.err.println("Unexpected editor type:");
            return null;
        }
    }

    //EFFECTS:  Creates and returns a panel with three buttons
    //          to add a book, clear fields and remove a book
    private JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Add");
        button.addActionListener(this);
        button.setActionCommand("add");
        panel.add(button);

        button = new JButton("Clear");
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);

        button = new JButton("Remove");
        button.addActionListener(this);
        button.setActionCommand("remove");
        panel.add(button);

        return panel;
    }

    //MODIFIES: this
    //EFFECTS:  listens to Add, Clear and Remove buttons
    //          and performs actions according to the source button
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("clear".equals(e.getActionCommand())) {
            //empty the fields
            titleField.setText("");
            authorField.setText("");
            return;
        } else if ("remove".equals(e.getActionCommand())) {
            //remove the selected book
            removeBook();
            return;
        }
        //if none of two buttons above, it must be the Add button
        addOneBookToShelf();
    }

    //REQUIRES: a book is selected in list and the size of listModel is not zero
    //MODIFIES: this
    //EFFECTS:  removes a book from the GUI list and bookshelf
    //This method references code from this website
    //Link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
    protected void removeBook() {
        //identify the selected index and remove it from the list and the bookshelf
        int index = list.getSelectedIndex();
        listModel.remove(index);
        bookshelf.removeBook(bookshelf.getBooks().get(index));
        afterRemoving = true;

        //select another item if the list is not empty
        if (listModel.getSize() != 0) {
            if (index == listModel.getSize()) {
                index--;
            }
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
    }

    //MODIFIES: this
    //EFFECTS:  reads the user input from text fields and spinners and creates
    //          and adds a book with the information to the list and bookshelf
    protected void addOneBookToShelf() {
        //add the book to bookshelf
        Book book = new Book((int) pageSpinner.getValue(), titleField.getText(),
                authorField.getText(), (int) yearSpinner.getValue());
        bookshelf.insertBook(book);

        //add to the list
        String backCover = book.getTitle() + " - " + book.getAuthor();
        int index = list.getSelectedIndex();
        listModel.addElement(backCover);
        list.ensureIndexIsVisible(index);

    }

    //a listener for selecting a book in the list
    private class SelectListener implements ListSelectionListener {

        //REQUIRES: ListSelectionEvent e is not null
        //MODIFIES: this
        //EFFECTS:  performs actions according to the element the user selects on the list
        @Override
        public void valueChanged(ListSelectionEvent e) {
            //ignore extra messages
            if (e.getValueIsAdjusting()) {
                return;
            }

            //if the event occurs due to removing the item, do nothing
            if (afterRemoving) {
                afterRemoving = false;
                return;
            }

            //get which item is selected
            int selected = list.getSelectedIndex();

            //display the information of the book
            if (selected >= 0) {
                bookDetailsDialog(bookshelf.getBooks().get(selected));
            }
        }

        //REQUIRES: book is not null
        //EFFECTS:  displays a dialog window with book's information
        //This method references code from this website
        //Link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
        private void bookDetailsDialog(Book book) {
            Object[] options = {"Mark a page", "Sticky notes", "Close"};
            //create book details string
            String details = "Title: " + book.getTitle() + "\nAuthor: " + book.getAuthor()
                    + "\nYear: " + book.getYear() + "\nPages: " + book.getPage();
            JFrame frame = new JFrame();
            int n = JOptionPane.showOptionDialog(frame, details, book.getTitle(),
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);
            if (n == JOptionPane.YES_OPTION) {
                System.out.println("Mark a page");
            } else if (n == JOptionPane.NO_OPTION) {
                System.out.println("Sticky note menu");
            }

        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the bookshelf, jsonReader and jsonWriter
    private void initializeBookshelf() {
        bookshelf = new Bookshelf();
        Book book1 = new Book(100, "A flower for algernon", "Keyes", 1959);
        Book book2 = new Book(50, "A tiny princess", "Burnett", 1888);
//        Book book3 = new Book(140, "The Rabbit Revenge Plan", "Spinnen", 2001);
        book1.mark(72);
        book2.addNote(new StickyNote(3, "My mom's favorite!"));
        book2.addNote(new StickyNote(7, "This is an interesting story."));
//        book3.mark(137);
//        book3.addNote(new StickyNote(90, "My childhood favourite!"));
        bookshelf.insertBook(book1);
        bookshelf.insertBook(book2);
//        bookshelf.insertBook(book3);
        bookshelf.insertDecoration("A teddy bear");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    //EFFECTS:  Creates and returns a menu bar
    //This method references code from this website
    //Link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Save/Load");
        menuBar.add(menu);

        //save menu
        menuItem = new JMenuItem("Save the bookshelf");
        menuItem.addActionListener(new SaveLoadListener());
        menuItem.setActionCommand("save");
        menu.add(menuItem);

        //load menu
        menuItem = new JMenuItem("Load a bookshelf");
        menuItem.addActionListener(new SaveLoadListener());
        menuItem.setActionCommand("load");
        menu.add(menuItem);

        return menuBar;
    }


    //a listener for the menu bar
    private class SaveLoadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if ("save".equals(e.getActionCommand())) {
                //save the bookshelf and show a dialog
                saveBookshelf();
                displaySavedDialog();
            } else if ("load".equals(e.getActionCommand())) {
                //load a bookshelf
                loadBookshelf();
            }
        }

        //MODIFIES: the file JSON_STORE
        //EFFECTS:  saves the current state of the bookshelf to the json file
        private void saveBookshelf() {
            try {
                jsonWriter.open();
                jsonWriter.write(bookshelf);
                jsonWriter.close();
                System.out.println("Saved the bookshelf to " + JSON_STORE);
            } catch (FileNotFoundException exception) {
                System.out.println("Unable to write to file: " + JSON_STORE);
            }
        }

        //EFFECTS:  displays a dialog to tell the user that the bookshelf is saved
        private void displaySavedDialog() {
            JFrame frame = new JFrame("Saved");
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            frame.setSize(500, 410);
            ImageIcon picture = new ImageIcon("tinyroom.jpg");
            JLabel label = new JLabel(picture);
            panel.add(label);
            JLabel savedMessage = new JLabel("Your bookshelf is saved. Enjoy reading!");
            panel.add(savedMessage);
            frame.add(panel);
            frame.setVisible(true);
        }

        //REQUIRES: JSON_STORE is not null
        //MODIFIES: this
        //EFFECTS:  loads a bookshelf from a json file and put information to the GUI list
        private void loadBookshelf() {
            try {
                bookshelf = jsonReader.read();
                //update the bookshelf (the list)
                listModel.clear();
                getTitleAndAuthor();
                System.out.println("Loaded a bookshelf from " + JSON_STORE);
            } catch (IOException exception) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS:  Creates the GUI and shows it as a window
    //This method references code from this website
    //Link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
    private static void createAndShowGUI() {
        //Create and set up the window
        JFrame frame = new JFrame("Your Bookshelf");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        //show the log after closing the window
        frame.addWindowListener(new WindowActionListener());

        BookshelfAppGUI bookShelfApp = new BookshelfAppGUI();
        //Add contents to the window.
        frame.add(bookShelfApp);

        //Create the menu bar
        frame.setJMenuBar(bookShelfApp.createMenuBar());

        //display the window
        frame.setVisible(true);
    }

    //EFFECTS:  runs the program
    public static void main(String[] args) {
        // This method references code from this website
        // Link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
        // EFFECTS: crates and shows this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            //EFFECTS:  creates and shows the GUI
            public void run() {
                createAndShowGUI();
            }
        });
    }

    //a listener for closing the main window
    private static class WindowActionListener implements WindowListener {

        //EFFECTS:  do nothing
        @Override
        public void windowOpened(WindowEvent e) {
        }

        //EFFECTS:  display the EventLog in the console when closing the window
        @Override
        public void windowClosing(WindowEvent e) {
            for (Event next : EventLog.getInstance()) {
                System.out.println(next.toString());
            }
        }

        //EFFECTS:  do nothing
        @Override
        public void windowClosed(WindowEvent e) {
        }

        //EFFECTS:  do nothing
        @Override
        public void windowIconified(WindowEvent e) {
        }

        //EFFECTS:  do nothing
        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        //EFFECTS:  do noting
        @Override
        public void windowActivated(WindowEvent e) {
        }

        //EFFECTS:  do nothing
        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }
}
