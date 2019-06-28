package interfaces;

import java.awt.Color;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import interfaces.menu.MenuItem;
import utilities.DialogPane;
import utilities.Utilities;

public class TextEditor extends JTextArea {

    /* ATTRIBUTES ___________________________________________________________ */
    private String name;
    private Boolean saved;
    private File file;

    private UndoManager undoManager;
    private JPopupMenu popupMenu;
    private DefaultHighlightPainter highlightPainter;

    private MenuItem copyMenuItem;
    private MenuItem cutMenuItem;
    private MenuItem pasteMenuItem;
    private MenuItem selectAllMenuItem;

    /* CONSTRUCTORS _________________________________________________________ */
    public TextEditor(String name) {
        this.name = name;
        this.saved = false;
        highlightPainter = new DefaultHighlightPainter(Color.orange);

        initComponents();
        initEvents();
    }

    /* ______________________________________________________________________ */
    public TextEditor(File file) {
        this.file = file;
        this.name = file.getName();
        this.saved = false;

        initComponents();
        initEvents();
    }

    /* METHODS ______________________________________________________________ */
    private void initComponents() {

        // Set Up TextArea
        setForeground(Color.black);
        setLineWrap(true);
        setMargin(new Insets(14, 14, 7, 7));
        setTabSize(4);
        setWrapStyleWord(true);

        // Set PopUp Menu
        popupMenu = new JPopupMenu();
        setComponentPopupMenu(popupMenu);

        // Set Up Menu Items
        copyMenuItem = new MenuItem("Copy", 'Y', 'C', "/images/copy.png");
        cutMenuItem = new MenuItem("Cut", 'T', 'X', "/images/cut.png");
        pasteMenuItem = new MenuItem("Paste", 'P', 'V', "/images/paste.png");
        selectAllMenuItem = new MenuItem("Select All", 'S', 'A', "/images/select.png");

        popupMenu.add(copyMenuItem);
        popupMenu.add(cutMenuItem);
        popupMenu.add(pasteMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(selectAllMenuItem);
    }

    /* ______________________________________________________________________ */
    private void initEvents() {
        undoManager = new UndoManager();
        getDocument().addUndoableEditListener(undoManager);

        copyMenuItem.addActionListener(ae -> {
            copy();
        });
        cutMenuItem.addActionListener(ae -> {
            cut();
        });
        pasteMenuItem.addActionListener(ae -> {
            paste();
        });
        selectAllMenuItem.addActionListener(ae -> {
            selectAll();
        });
    }

    /* ______________________________________________________________________ */
    public void updatePopupMenu() {
        copyMenuItem.setEnabled(isSelectedText());
        cutMenuItem.setEnabled(isSelectedText());
        selectAllMenuItem.setEnabled(!getText().isEmpty());
    }

    /* ______________________________________________________________________ */
    public boolean save() {
        String fileName;

        if (file != null) {

            if (file.exists()) {
                fileName = file.getName();

                if (!saved) {
                    if (file.canWrite()) {
                        writeText();
                        saved = true;

                        Utilities.output(fileName + " saved successfully");
                         fileName = null;

                        return true;
                    } else {
                        Utilities.output(fileName + " couldn't be write");
                        DialogPane.show(fileName + " couldn't be write", DialogPane.ERROR);
                    }
                } else {
                    Utilities.output(fileName + " is already saved");
                }
            }
        } else {
             fileName = null;
            return saveAs();
        }
         fileName = null;

        return false;
    }

    /* ______________________________________________________________________ */
    public boolean saveAs() {
        JFileChooser fileChooser;
        Integer state;

        fileChooser = new JFileChooser(System.getProperty("user.dir") + "/Files");
        fileChooser.setSelectedFile(new File(name));
        state = fileChooser.showSaveDialog(null);

        if (state == JFileChooser.APPROVE_OPTION) {

            file = fileChooser.getSelectedFile();
            if (file != null) {
                if (!file.exists()) {
                    createFile();
                     fileChooser = null;
                     state = null;

                    return save();
                } else {
                    Utilities.output(file.getName() + " is already created");
                    DialogPane.show(file.getName() + " is already created", DialogPane.ERROR);
                }
            } else {
                Utilities.output("No one file selected");
            }
        }
         fileChooser = null;
         state = null;

        return false;
    }

    /* ______________________________________________________________________ */
    public boolean openAs() {
        JFileChooser fileChooser;
        Integer state;

        fileChooser = new JFileChooser(file != null ? file.getAbsolutePath() : System.getProperty("user.dir") + "/Files");
        state = fileChooser.showOpenDialog(null);

        if (state == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            open();
             fileChooser = null;
             state = null;

            return true;
        } else if (state == JFileChooser.CANCEL_OPTION) {
            Utilities.output("No file is selected");
        }

         fileChooser = null;
         state = null;

        return false;
    }

    /* ______________________________________________________________________ */
    public boolean open() {
        if (file != null) {

            if (file.exists()) {
                if (file.canRead()) {

                    readText();
                    setName(file.getName());
                    saved = true;
                    setCaretPosition(0);
                    Utilities.output(file.getName() + " opened");
                    return true;
                } else {
                    Utilities.output(file.getName() + " couldn't be read");
                    DialogPane.show(file.getName() + " couldn't be read", DialogPane.ERROR);
                }
            } else {
                Utilities.output(file.getName() + " does't exist");
                DialogPane.show(file.getName() + " does't exist", DialogPane.ERROR);
            }
        }

        return false;
    }

    /* ______________________________________________________________________ */
    public void createFile() {
        try {
            if (file.createNewFile()) {
                Utilities.output(file.getName() + " created successfully");
            } else {
                Utilities.output(file.getName() + " could not be created");
                DialogPane.show(file.getName() + " could not be created", DialogPane.ERROR);
            }
        } catch (IOException ex) {
            Utilities.output("" + ex);
        }
    }

    /* ______________________________________________________________________ */
    public void writeText() {
        try ( FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(getText());
            fileWriter.close();
        } catch (IOException ex) {
            Utilities.output(file.getName() + " couldn't be writed");
        }
    }

    /* ______________________________________________________________________ */
    public String readText() {
        String text = "";
        try ( BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            text = bufferedReader.readLine();
            setText("");
            while (text != null) {
                append(text);
                text = bufferedReader.readLine();
                if (text != null) {
                    append("\n");
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Utilities.output(file.getName() + " don't exist");
            DialogPane.show(file.getName() + " don't exist", DialogPane.ERROR);
        } catch (IOException | NullPointerException ex) {
            Utilities.output("" + ex);
        }
        return text;
    }

    /* ______________________________________________________________________ */
    public void undo() {
        try {
            undoManager.undo();
        } catch (CannotUndoException e) {
        }
    }

    /* ______________________________________________________________________ */
    public void redo() {
        try {
            undoManager.redo();
        } catch (CannotRedoException e) {
        }
    }

    /* ______________________________________________________________________ */
    public void removeHighlights() {
        Highlighter highlighter = getHighlighter();
        highlighter.removeAllHighlights();
         highlighter = null;
    }

    /* GETTERS ______________________________________________________________ */
    public Boolean isSaved() {
        return saved;
    }

    /* ______________________________________________________________________ */
    @Override
    public String getName() {
        return name;
    }

    /* ______________________________________________________________________ */
    public Color getHighlightColor() {
        return getHighlightPainter().getColor();
    }

    /* ______________________________________________________________________ */
    public DefaultHighlightPainter getHighlightPainter() {
        return highlightPainter;
    }

    /* ______________________________________________________________________ */
    public boolean isClosable() {
//        return !saved && (!name.contains("untitled") || !getText().isEmpty());
        return saved || (name.contains("untitled") && getText().isEmpty());
    }

    /* ______________________________________________________________________ */
    public String getCoords() {
        String coords = null;
        Integer caretPosition;
        Integer row;
        Integer column;
        Integer lineStartOffset;

        try {
            caretPosition = getCaretPosition();
            row = getLineOfOffset(caretPosition);

            lineStartOffset = getLineStartOffset(row);
            column = caretPosition - lineStartOffset;

            coords = "ln " + (row + 1) + ": col " + (column + 1);
        } catch (BadLocationException ex) {
        }

        return coords;
    }

    /* ______________________________________________________________________ */
    public String getWords() {
        String text = isSelectedText() ? getSelectedText() : getText();
        Integer words = new StringTokenizer(text).countTokens();
        Integer characters = text.replaceAll(" ", "").replace("\n", "").length();

        text = words + " words, " + characters + " characters";
        return text;
    }

    /* ______________________________________________________________________ */
    public boolean isSelectedText() {
        return getSelectedText() != null;
    }

    /* ______________________________________________________________________ */
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    /* ______________________________________________________________________ */
    @Override
    public String toString() {
        return "TextEditor{" + "name=" + name + ", saved=" + saved + '}';
    }

    /* ______________________________________________________________________ */
    public boolean canUndo() {
        return undoManager.canUndo();
    }

    /* ______________________________________________________________________ */
    public boolean canRedo() {
        return undoManager.canRedo();
    }

    /* ______________________________________________________________________ */
    public File getFile() {
        return file;
    }

    /* ______________________________________________________________________ */
    public MenuItem getCopyMenuItem() {
        return copyMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getCutMenuItem() {
        return cutMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getPasteMenuItem() {
        return pasteMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getSelectAllMenuItem() {
        return selectAllMenuItem;
    }

    /* SETTERS ______________________________________________________________ */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /* ______________________________________________________________________ */
    public void setSaved(Boolean saved) {
        this.saved = saved;
        Utilities.output(name + (saved ? " Saved" : " Unsaved"));
    }

    /* ______________________________________________________________________ */
    public void setHighlightColor(Color color) {
        highlightPainter = new DefaultHighlightPainter(color);
    }

    /* ______________________________________________________________________ */
    public void setFile(File file) {
        this.file = file;
    }

    /* ______________________________________________________________________ */
    public void destructor() {
        name = null;
        saved = null;
        file = null;

        undoManager = null;
        popupMenu = null;
        highlightPainter = null;

        copyMenuItem = cutMenuItem = pasteMenuItem = selectAllMenuItem = null;
    }
}
