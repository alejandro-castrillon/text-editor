package interfaces;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import interfaces.menu.MenuBar;
import interfaces.panels.StatusBar;
import interfaces.panels.SearchSidebar;
import utilities.ColorChooser;
import utilities.DialogPane;
import utilities.FontChooser;
import utilities.Utilities;

public class TextEditorsManager extends JFrame {

    /* ATTRIBUTES ___________________________________________________________ */
    public static final String VERSION = "2.15.8";

    private ColorChooser colorChooser;
    private FontChooser fontChooser;

    private MenuBar menuBar;

    private JTabbedPane tabbedPane;
    private ArrayList<TextEditor> textEditors;

    private SearchSidebar findBarPanel;
    private StatusBar statusBarPanel;

    /* CONSTRUCTORS _________________________________________________________ */
    public TextEditorsManager() {
        Utilities.output("Text editor executed");

        textEditors = new ArrayList<>();

        initComponents();
        initEvents();
    }

    /* METHODS ______________________________________________________________ */
    private void initComponents() {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        // Set Up Frame --------------------------------------------------------
        getContentPane().setBackground(Color.white);
        setIconImage(Utilities.getImage("/images/icons/textEditorpng.png"));
        setLayout(new BorderLayout());
        setSize(465, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(281, 150));
        setTitle("Text Editor");

        // Set Up Menu ---------------------------------------------------------
        menuBar = new MenuBar();
        setJMenuBar(menuBar);

        // Set Up Tabbed Pane --------------------------------------------------
        tabbedPane = (JTabbedPane) setUpTabTraversalKeys();
        tabbedPane.setFocusable(false);
        add(tabbedPane, BorderLayout.CENTER);

        // Set Up Status Bar ---------------------------------------------------
        statusBarPanel = new StatusBar();

        // Set Up Find Bar -----------------------------------------------------
        findBarPanel = new SearchSidebar();

        // Set Up Color Chooser Dialog -----------------------------------------
        colorChooser = new ColorChooser();

        // Set Up Font Chooser Dialog ------------------------------------------
        fontChooser = new FontChooser();

        southPanel.add(findBarPanel);
        southPanel.add(statusBarPanel);
        add(southPanel, BorderLayout.SOUTH);
    }

    /* ______________________________________________________________________ */
    private void initEvents() {
        // Frame Events --------------------------------------------------------
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int closeOperation = DO_NOTHING_ON_CLOSE;
                int option = saveFilesAtClose();

                if (option == DialogPane.OK_OPTION || option == DialogPane.NO_OPTION) {
                    closeOperation = EXIT_ON_CLOSE;
                    destructor();
                }
                setDefaultCloseOperation(closeOperation);
            }
        });

        // Menu Events ---------------------------------------------------------
        initFileMenuEvents();
        initEditMenuEvents();
        initViewMenuEvents();

        // About Menu ----------------------------------------------------------
        menuBar.getAboutMenuItem().addActionListener(ae -> {
            DialogPane.show("About", "Text Editor v." + VERSION + "\n"
                    + "Created by: Laplace's Demon", DialogPane.PLAIN);
        });

        // Tabbed Pane Events --------------------------------------------------
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent me) {
                updateMenuBar();
                updateStatusBar();
            }
        });

        // Find Bar Panel Events -----------------------------------------------
        findBarPanel.setFindListener(bool -> {
            findWord(bool);
        });

        findBarPanel.getPreviousButton().addActionListener(ae -> {
            previousMatchAction();
        });

        findBarPanel.getNextButton().addActionListener(ae -> {
            nextMatchAction();
        });

        findBarPanel.getCloseButton().addActionListener(ae -> {
            TextEditor textEditor = getSelectedTextEditor();
            if (textEditor != null) {
                textEditor.removeHighlights();
                textEditor.requestFocus();
            }
        });
    }

    /* ______________________________________________________________________ */
    private void initFileMenuEvents() {
        menuBar.getNewMenuItem().addActionListener(ae -> {
            addNewTab();
        });

        menuBar.getOpenMenuItem().addActionListener(ae -> {
            openFile(null, false);
        });

        menuBar.getSaveMenuItem().addActionListener(ae -> {
            boolean bool = getSelectedTextEditor().save();
            getSelectedTitleTab().setSaved(bool);
            updateMenuBar();
            updateStatusBar();
        });

        menuBar.getSaveAsMenuItem().addActionListener(ae -> {
            boolean bool = getSelectedTextEditor().saveAs();
            getSelectedTitleTab().setSaved(bool);
            updateMenuBar();
            updateStatusBar();
        });

        menuBar.getCloseTabMenuItem().addActionListener(ae -> {
            closeSelectedTab();
        });

        menuBar.getCloseAllTabsMenuItem().addActionListener(ae -> {
            int option = saveFilesAtClose();
            if (option == DialogPane.OK_OPTION || option == DialogPane.NO_OPTION) {
                closeAllTabs();
            }
        });

        menuBar.getExitMenuItem().addActionListener(ae -> {
            int option = saveFilesAtClose();
            if (option == DialogPane.OK_OPTION || option == DialogPane.NO_OPTION) {
                destructor();
                System.exit(0);
            }
        });
    }

    /* ______________________________________________________________________ */
    private void initEditMenuEvents() {
        menuBar.getUndoMenuItem().addActionListener(ae -> {
            getSelectedTextEditor().undo();
            updateMenuBar();
            updateStatusBar();
        });

        menuBar.getRedoMenuItem().addActionListener(ae -> {
            getSelectedTextEditor().redo();
            updateMenuBar();
            updateStatusBar();
        });

        menuBar.getCopyMenuItem().addActionListener(ae -> {
            getSelectedTextEditor().copy();
        });

        menuBar.getCutMenuItem().addActionListener(ae -> {
            getSelectedTextEditor().cut();
            updateMenuBar();
            updateStatusBar();
        });

        menuBar.getPasteMenuItem().addActionListener(ae -> {
            getSelectedTextEditor().paste();
            updateMenuBar();
            updateStatusBar();
        });

        menuBar.getSelectAllMenuItem().addActionListener(ae -> {
            getSelectedTextEditor().selectAll();
        });

        menuBar.getFindInFileMenuItem().addActionListener(ae -> {
            findBarPanel.setVisible(true);
            findBarPanel.getComboBox().requestFocus();
        });
    }

    /* ______________________________________________________________________ */
    private void initViewMenuEvents() {
        menuBar.getBackgroundMenuItem().addActionListener(ae -> {
            TextEditor textEditor = getSelectedTextEditor();
            colorChooser.setColor(textEditor.getBackground());

            if (colorChooser.showDialog() == ColorChooser.OK_OPTION) {
                textEditor.setBackground(colorChooser.getColor());
            }
        });

        menuBar.getForegroundMenuItem().addActionListener(ae -> {
            TextEditor textEditor = getSelectedTextEditor();
            colorChooser.setColor(textEditor.getForeground());

            if (colorChooser.showDialog() == ColorChooser.OK_OPTION) {
                textEditor.setForeground(colorChooser.getColor());
            }
        });

        menuBar.getHighlightColorMenuItem().addActionListener(ae -> {
            highlightColorAction();
        });

        menuBar.getCursorColorMenuItem().addActionListener(ae -> {
            TextEditor textEditor = getSelectedTextEditor();
            colorChooser.setColor(textEditor.getCaretColor());

            if (colorChooser.showDialog() == ColorChooser.OK_OPTION) {
                textEditor.setCaretColor(colorChooser.getColor());
            }
        });

        menuBar.getFontMenuItem().addActionListener(ae -> {
            TextEditor textEditor = getSelectedTextEditor();
            fontChooser.setSelectedFont(textEditor.getFont());

            if (fontChooser.showDialog(null) == FontChooser.OK_OPTION) {
                textEditor.setFont(fontChooser.getSelectedFont());
            }
        });

        menuBar.getMarginSizeMenuItem().addActionListener(ae -> {
            TextEditor textEditor = getSelectedTextEditor();
            int i = textEditor.getMargin().top;
            int j;
            String input = DialogPane.selectedInput("Change Margin Size",
                    "Enter new margin size", i);

            try {
                i = Integer.parseInt(input);
                j = i == 0 ? 0 : i / 2;
                textEditor.setMargin(new Insets(i, i, j, j));
                
                textEditor.updateUI();
            } catch (NumberFormatException e) {
            }
        });

        menuBar.getWarpTextCheckBoxMenuItem().addActionListener(ae -> {
            boolean bool = menuBar.getWarpTextCheckBoxMenuItem().isSelected();
            TextEditor textEditor = getSelectedTextEditor();
            textEditor.setLineWrap(bool);
            textEditor.setWrapStyleWord(bool);
        });

        menuBar.getShowStatusBarCheckBoxMenuItem().addActionListener(ae -> {
            boolean bool = menuBar.getShowStatusBarCheckBoxMenuItem().isSelected();
            statusBarPanel.setVisible(bool);
        });

        menuBar.getCloseFindBarMenuItem().addActionListener(ae -> {
            findBarPanel.getCloseButton().doClick();
            updateMenuBar();
            updateStatusBar();
        });
    }

    /* ______________________________________________________________________ */
    public int saveFilesAtClose() {
        TextEditor opened = null;
        int option = DialogPane.NO_OPTION;

        for (TextEditor textEditor : textEditors) {
            if (!textEditor.isClosable()) {
                opened = textEditor;
            }
        }
        if (opened != null) {
            option = DialogPane.yesNoCancelOption("Save files before close", "Save all files?");

            if (option == DialogPane.OK_OPTION) {
                saveFiles();
                Utilities.output("Files saved succesfully");
            } else {
                Utilities.output("Files didn't be saved");
            }
        }

        return option;
    }

    /* ______________________________________________________________________ */
    public void saveFiles() {
        textEditors.forEach(textEditor -> {
            if (!textEditor.isSaved()) {
                textEditor.save();
            }
        });
    }

    /* ______________________________________________________________________ */
    public TextEditor addNewTab() {

        Integer index;
        TitleTab titleTab;
        TextEditor textEditor = null;
        JScrollPane scrollPane;
        String name = "untitled" + generateTabNumber() + ".txt";

        titleTab = new TitleTab(name);
        scrollPane = new JScrollPane();
        textEditor = initTextEditorEvents(name, scrollPane, titleTab);
        scrollPane.setViewportView(textEditor);

        tabbedPane.addTab(name, scrollPane);
        index = tabbedPane.indexOfComponent(scrollPane);
        tabbedPane.setTabComponentAt(index, titleTab);
        tabbedPane.setSelectedIndex(index);

        textEditors.add(textEditor);

        updateMenuBar();
        updateStatusBar();
        updatePopupMenus();

        index = null;
        titleTab = null;
        scrollPane = null;
        name = null;

        return textEditor;
    }

    /* ______________________________________________________________________ */
    public TextEditor initTextEditorEvents(String n, JScrollPane s, TitleTab t) {
        TextEditor textEditor = new TextEditor(n);

        textEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                TextEditor textEditor = getSelectedTextEditor();
                TitleTab titleTab = getSelectedTitleTab();
                boolean saved;
                if (!ke.isControlDown() && !ke.isShiftDown() && !ke.isAltDown()
                        && ke.getKeyChar() != KeyEvent.VK_ESCAPE) {
                    saved = textEditor.isSaved();
                    if (saved) {
                        textEditor.setSaved(!saved);
                        titleTab.setSaved(!saved);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                updateMenuBar();
                updateStatusBar();
                updatePopupMenus();
            }
        });
        textEditor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                updateMenuBar();
                updateStatusBar();
                updatePopupMenus();
            }
        });
        textEditor.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent me) {
                updateMenuBar();
                updateStatusBar();
                updatePopupMenus();
            }

            @Override
            public void mouseMoved(MouseEvent me) {
                updatePopupMenus();
            }
        });
        textEditor.addMouseWheelListener(mwe -> {
            Font font = textEditor.getFont();
            String familyName = font.getName();
            int style = font.getStyle();
            int textSize = font.getSize();

            int scrollUnits = mwe.getUnitsToScroll();
            int horizontalValue;
            int verticalValue;

            if (mwe.isControlDown()) {
                if (scrollUnits > 0 && textSize - 2 >= 8) {
                    textEditor.setFont(new Font(familyName, style, textSize - 1));
                } else if (textSize + 2 <= 72) {
                    textEditor.setFont(new Font(familyName, style, textSize + 1));
                }
            } else if (mwe.isShiftDown()) {
                horizontalValue = s.getHorizontalScrollBar().getValue();
                s.getHorizontalScrollBar().setValue(horizontalValue + scrollUnits * 10);
            } else {
                verticalValue = s.getVerticalScrollBar().getValue();
                s.getVerticalScrollBar().setValue(verticalValue + scrollUnits * 10);
            }
        });

        t.addCloseAction(ae -> {
            closeTab(s);
        });

        t.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                tabbedPane.setSelectedComponent(s);
            }
        });

        t.getShiftLeftMenuItem().addActionListener(ae -> {
            try {
                int leftIndex = tabbedPane.getSelectedIndex() - 1;
                tabbedPane.setSelectedIndex(leftIndex);
            } catch (Exception e) {
            }
        });

        t.getShiftRightMenuItem().addActionListener(ae -> {
            try {
                int rightIndex = tabbedPane.getSelectedIndex() + 1;
                tabbedPane.setSelectedIndex(rightIndex);
            } catch (Exception e) {
                tabbedPane.setSelectedIndex(0);
            }
        });

        t.getCloseTabMenuItem().addActionListener(ae -> {
            closeSelectedTab();
        });

        t.getCloseAllTabsMenuItem().addActionListener(ae -> {
            closeAllTabs();
        });

        t.getCloseOtherTabsMenuItem().addActionListener(ae -> {
            closeOtherTabs();
        });

        t.getCloneTabMenuItem().addActionListener(ae -> {
            openFile(textEditor.getFile().getAbsolutePath(), true);
        });

        return textEditor;
    }

    /* ______________________________________________________________________ */
    public int generateTabNumber() {
        int newTabNumber = 1;
        Integer tabCount = tabbedPane.getTabCount();
        String tabTitle;
        Boolean exists;

        do {
            exists = false;
            for (int i = 0; i < tabCount; i++) {
                tabTitle = tabbedPane.getTitleAt(i);
                if (tabTitle.equals("untitled" + newTabNumber + ".txt")) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                newTabNumber++;
            }
        } while (exists);

        tabCount = null;
        tabTitle = null;
        exists = null;

        return newTabNumber;
    }

    /* ______________________________________________________________________ */
    public TextEditor getSelectedTextEditor() {
        TextEditor textEditor = null;
        Integer index = null;

        try {
            index = tabbedPane.getSelectedIndex();
            textEditor = textEditors.get(index);
        } catch (Exception e) {
        }

        return textEditor;
    }

    /* ______________________________________________________________________ */
    public TitleTab getSelectedTitleTab() {
        TitleTab titleTab = null;
        Integer index = null;

        try {
            index = tabbedPane.getSelectedIndex();
            titleTab = (TitleTab) tabbedPane.getTabComponentAt(index);
        } catch (Exception e) {
        }
        index = null;

        return titleTab;
    }

    /* ______________________________________________________________________ */
    public void closeSelectedTab() {
        JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
        if (scrollPane != null) {
            closeTab(scrollPane);
            scrollPane = null;
        } else {
            Utilities.output("No tab selected");
        }
    }

    /* ______________________________________________________________________ */
    public void closeTab(JScrollPane scrollPane) {
        Component viewPort = (Component) scrollPane.getViewport().getView();
        TextEditor textEditor = (TextEditor) viewPort;

        String fileName;
        int option = DialogPane.NO_OPTION;

        if (!textEditor.isClosable()) {
            fileName = textEditor.getName();

            option = DialogPane.yesNoCancelOption("Save file before close",
                    "Save " + fileName + "?");

            if (option == DialogPane.OK_OPTION) {
                textEditor.save();

                Utilities.output(fileName + " saved succesfully");
                close(scrollPane);
            } else if (option == DialogPane.NO_OPTION) {

                Utilities.output(fileName + " file didn't be saved");
                close(scrollPane);
            }
        } else {
            close(scrollPane);
        }

        updateMenuBar();
        updateStatusBar();
    }

    /* ______________________________________________________________________ */
    public void close(JScrollPane scrollPane) {
        Component viewPort = (Component) scrollPane.getViewport().getView();
        TextEditor textEditor = (TextEditor) viewPort;

        tabbedPane.remove(scrollPane);
        textEditors.remove(textEditor);
        Utilities.output(textEditor + " closed");
        textEditor.destructor();
    }

    /* ______________________________________________________________________ */
    public void closeAllTabs() {
        tabbedPane.removeAll();
        textEditors.clear();
        updateMenuBar();
        updateStatusBar();
    }

    /* ______________________________________________________________________ */
    public void closeOtherTabs() {
        TextEditor selectedTextEditor = getSelectedTextEditor();
        for (int i = textEditors.size() - 1; i >= 0; i--) {
            if (textEditors.get(i) != selectedTextEditor) {
                closeTab((JScrollPane) tabbedPane.getComponentAt(i));
            }
        }
        updateMenuBar();
        updateStatusBar();
    }

    /* ______________________________________________________________________ */
    public void openFile(String pathName, boolean openSame) {
        TextEditor textEditor = getSelectedTextEditor();
        TitleTab titleTab;
        boolean saved;

        if (textEditor != null) {
            if (!textEditor.getName().contains("untitled") || !textEditor.getText().isEmpty()) {
                addNewTab();
            }
        } else {
            addNewTab();
        }

        textEditor = getSelectedTextEditor();

        if (pathName != null) {
            textEditor.setFile(new File(pathName));
            saved = textEditor.open();
        } else {
            saved = textEditor.openAs();
        }

        textEditors.remove(textEditor);

        if (!openSame) {
            if (!textEditorIsOpened(textEditor)) {
                textEditors.add(textEditor);

                titleTab = getSelectedTitleTab();
                titleTab.setText(textEditor.getName());
                titleTab.setSaved(saved);
            } else {
                closeSelectedTab();
                DialogPane.show("File is already opened", DialogPane.ERROR);
            }
        } else {
            textEditors.add(textEditor);

            titleTab = getSelectedTitleTab();
            titleTab.setText(textEditor.getName());
            titleTab.setSaved(saved);
        }

        updateMenuBar();
        updateStatusBar();

        textEditor = null;
        titleTab = null;
    }

    /* ______________________________________________________________________ */
    public boolean textEditorIsOpened(TextEditor textEditor) {
        String absolutePath1 = textEditor.getFile().getAbsolutePath();
        String absolutePath2;
        File file;
        boolean exists = false;

        for (int i = 0; i < textEditors.size(); i++) {

            file = textEditors.get(i).getFile();

            if (file != null) {
                absolutePath2 = file.getAbsolutePath();
                if (absolutePath1.equals(absolutePath2)) {
                    exists = true;
                    break;
                }
            }
        }

        return exists;
    }

    /* ______________________________________________________________________ */
    public void updateMenuBar() {
        TextEditor textEditor = getSelectedTextEditor();
        if (textEditor != null) {
            menuBar.getSaveMenuItem().setEnabled(!textEditor.isSaved());
            menuBar.getSaveAsMenuItem().setEnabled(true);
            menuBar.getCloseTabMenuItem().setEnabled(true);
            menuBar.getCloseAllTabsMenuItem().setEnabled(true);

            menuBar.getUndoMenuItem().setEnabled(textEditor.canUndo());
            menuBar.getRedoMenuItem().setEnabled(textEditor.canRedo());
            menuBar.getCopyMenuItem().setEnabled(textEditor.isSelectedText());
            menuBar.getCutMenuItem().setEnabled(textEditor.isSelectedText());
            menuBar.getPasteMenuItem().setEnabled(true);
            menuBar.getSelectAllMenuItem().setEnabled(!textEditor.getText().isEmpty());

            menuBar.getBackgroundMenuItem().setEnabled(true);
            menuBar.getForegroundMenuItem().setEnabled(true);
            menuBar.getHighlightColorMenuItem().setEnabled(true);
            menuBar.getCursorColorMenuItem().setEnabled(true);
            menuBar.getFontMenuItem().setEnabled(true);
            menuBar.getMarginSizeMenuItem().setEnabled(true);
            menuBar.getWarpTextCheckBoxMenuItem().setEnabled(true);
            menuBar.getCloseFindBarMenuItem().setEnabled(findBarPanel.isVisible());
        } else {
            menuBar.getSaveMenuItem().setEnabled(false);
            menuBar.getSaveAsMenuItem().setEnabled(false);
            menuBar.getCloseTabMenuItem().setEnabled(false);
            menuBar.getCloseAllTabsMenuItem().setEnabled(false);

            menuBar.getUndoMenuItem().setEnabled(false);
            menuBar.getRedoMenuItem().setEnabled(false);
            menuBar.getCopyMenuItem().setEnabled(false);
            menuBar.getCutMenuItem().setEnabled(false);
            menuBar.getPasteMenuItem().setEnabled(false);
            menuBar.getSelectAllMenuItem().setEnabled(false);
            menuBar.getSelectAllMenuItem().setEnabled(false);

            menuBar.getBackgroundMenuItem().setEnabled(false);
            menuBar.getForegroundMenuItem().setEnabled(false);
            menuBar.getHighlightColorMenuItem().setEnabled(false);
            menuBar.getCursorColorMenuItem().setEnabled(false);
            menuBar.getFontMenuItem().setEnabled(false);
            menuBar.getMarginSizeMenuItem().setEnabled(false);
            menuBar.getWarpTextCheckBoxMenuItem().setEnabled(false);
        }
    }

    /* ______________________________________________________________________ */
    public void updateStatusBar() {
        TextEditor textEditor = getSelectedTextEditor();
        try {
            statusBarPanel.setWords(textEditor.getWords());
            statusBarPanel.setLineCount(textEditor.getLineCount());
            statusBarPanel.setCoords(textEditor.getCoords());
        } catch (Exception e) {
            statusBarPanel.setWords("0 words, 0 characters");
            statusBarPanel.setLineCount(0);
            statusBarPanel.setCoords("ln 0: col 0");
        }
    }

    /* ______________________________________________________________________ */
    public void updatePopupMenus() {
        try {
            TextEditor textEditor = getSelectedTextEditor();
            TitleTab titleTab = getSelectedTitleTab();

            textEditor.getCopyMenuItem().setEnabled(textEditor.isSelectedText());
            textEditor.getCutMenuItem().setEnabled(textEditor.isSelectedText());
            textEditor.getSelectAllMenuItem().setEnabled(!textEditor.getText().isEmpty());

            titleTab.getShiftLeftMenuItem().setEnabled(tabbedPane.getTabCount() > 1);
            titleTab.getShiftRightMenuItem().setEnabled(tabbedPane.getTabCount() > 1);
            titleTab.getCloseAllTabsMenuItem().setEnabled(tabbedPane.getTabCount() > 1);
            titleTab.getCloseOtherTabsMenuItem().setEnabled(tabbedPane.getTabCount() > 1);
        } catch (Exception e) {
        }
    }

    /* ______________________________________________________________________ */
    public void previousMatchAction() {
        TextEditor textEditor;
        JComboBox comboBox = findBarPanel.getComboBox();
        ComboBoxEditor editor = comboBox.getEditor();
        String pattern = findBarPanel.getPattern();

        boolean uppercase;
        boolean highlight;
        boolean bool = false;

        if (!pattern.isEmpty()) {
            textEditor = getSelectedTextEditor();

            if (textEditor != null) {

                uppercase = findBarPanel.isUpperCaseMatch();
                highlight = findBarPanel.isHighlightMatches();

                textEditor.removeHighlights();
                if (highlight) {
                    highlight(textEditor, pattern, uppercase);
                }
                bool = findPrevious(textEditor, pattern, uppercase);
            }
        } else {
            comboBox.requestFocus();
        }

        editor.getEditorComponent().setForeground(!bool ? Color.red : Color.black);
    }

    /* ______________________________________________________________________ */
    public void nextMatchAction() {
        TextEditor textEditor;
        JComboBox comboBox = findBarPanel.getComboBox();
        ComboBoxEditor editor = comboBox.getEditor();
        String pattern = findBarPanel.getPattern();

        boolean uppercase;
        boolean highlight;
        boolean bool = false;

        if (!pattern.isEmpty()) {

            textEditor = getSelectedTextEditor();
            if (textEditor != null) {

                uppercase = findBarPanel.isUpperCaseMatch();
                highlight = findBarPanel.isHighlightMatches();

                textEditor.removeHighlights();
                if (highlight) {
                    highlight(textEditor, pattern, uppercase);
                }
                bool = findNext(textEditor, pattern, uppercase);
            }
        } else {
            comboBox.requestFocus();
        }

        editor.getEditorComponent().setForeground(!bool ? Color.red : Color.black);
    }

    /* ______________________________________________________________________ */
    public void findWord(boolean jumpNext) {
        TextEditor textEditor;
        JComboBox comboBox = findBarPanel.getComboBox();
        ComboBoxEditor editor = comboBox.getEditor();
        String pattern = findBarPanel.getPattern();

        boolean bool = false, uppercase, highlight;

        int index;
        int start;
        int caretPosition;
        int length;

        if (!pattern.isEmpty()) {

            textEditor = getSelectedTextEditor();
            if (textEditor != null) {

                uppercase = findBarPanel.isUpperCaseMatch();
                highlight = findBarPanel.isHighlightMatches();
                int count = getMatchesCount(textEditor, pattern, uppercase);
                findBarPanel.setMatchesCount(count);

                textEditor.removeHighlights();
                if (highlight) {
                    highlight(textEditor, pattern, uppercase);
                }

                caretPosition = textEditor.getCaretPosition();
                length = pattern.length();
                start = caretPosition - length;
                index = textEditor.getText().indexOf(pattern, start);

                if (index != start || jumpNext) {
                    bool = findNext(textEditor, pattern, uppercase);
                } else {
                    bool = true;

                    textEditor.setSelectionStart(start);
                    textEditor.setSelectionEnd(caretPosition);
                    textEditor.requestFocus();
                }

                findBarPanel.addPattern(pattern);
            }
        } else {
            comboBox.requestFocus();
        }

        editor.getEditorComponent().setForeground(!bool ? Color.red : Color.black);

        textEditor = null;
        comboBox = null;
        pattern = null;
    }

    /* ______________________________________________________________________ */
    public boolean findPrevious(TextEditor textEditor, String pattern, boolean uppercase) {
        int index;
        int caretPosition;
        int length;
        String text;

        try {
            caretPosition = textEditor.getCaretPosition();
            length = pattern.length();

            text = textEditor.getText(0, caretPosition - length);

            if (!uppercase) {
                text = text.toLowerCase();
            }

            index = text.lastIndexOf(pattern, caretPosition);

            if (index > -1) {
                textEditor.setSelectionStart(index);
                textEditor.setSelectionEnd(index + length);
                textEditor.requestFocus();
            }
            return index > -1;

        } catch (BadLocationException ex) {
        }

        return false;
    }

    /* ______________________________________________________________________ */
    public boolean findNext(TextEditor textEditor, String pattern, boolean uppercase) {
        int index;
        int caretPosition;
        int length;
        String text;

        caretPosition = textEditor.getCaretPosition();
        length = pattern.length();

        text = textEditor.getText();

        if (!uppercase) {
            text = text.toLowerCase();
        }

        index = text.indexOf(pattern, caretPosition);

        if (index > -1) {
            textEditor.setSelectionStart(index);
            textEditor.setSelectionEnd(index + length);
            textEditor.requestFocus();
        }
        return index > -1;
    }

    /* ______________________________________________________________________ */
    public int getMatchesCount(TextEditor textEditor, String pattern, boolean uppercase) {
        int count = 0;
        int index = 0;
        int length = pattern.length();
        String text = textEditor.getText();

        if (!uppercase) {
            text = text.toLowerCase();
        }

        while (true) {
            index = text.indexOf(pattern, index);

            if (index < 0) {
                break;
            } else {
                count++;
                index += length;
            }
        }

        return count;
    }

    /* ______________________________________________________________________ */
    public void highlightColorAction() {
        TextEditor textEditor = getSelectedTextEditor();
        Color color = textEditor.getHighlightColor();
        String pattern;
        boolean uppercase;
        boolean highlight;

        colorChooser.setColor(color);
        if (colorChooser.showDialog() == ColorChooser.OK_OPTION) {
            color = colorChooser.getColor();
            textEditor.setHighlightColor(color);
            pattern = findBarPanel.getPattern();

            if (!pattern.isEmpty()) {
                uppercase = findBarPanel.isUpperCaseMatch();
                highlight = findBarPanel.isHighlightMatches();
                textEditor.removeHighlights();

                if (highlight) {
                    highlight(textEditor, pattern, uppercase);
                }
            }
        }
    }

    /* ______________________________________________________________________ */
    public boolean highlight(TextEditor textEditor, String pattern, boolean uppercase) {
        int index = 0;
        int length;
        Highlighter highlighter;
        HighlightPainter highlightPainter = textEditor.getHighlightPainter();
        String text;

        try {
            length = pattern.length();
            highlighter = textEditor.getHighlighter();

            text = textEditor.getText();

            if (!uppercase) {
                text = text.toLowerCase();
            }

            while (true) {
                index = text.indexOf(pattern, index);

                if (index < 0) {
                    break;
                } else {
                    highlighter.addHighlight(index, index += length, highlightPainter);
                }
            }

            highlighter = null;
            highlightPainter = null;
            pattern = text = null;
        } catch (BadLocationException e) {
        }

        return index > -1;
    }

    /* ______________________________________________________________________ */
    private JTabbedPane setUpTabTraversalKeys() {
        JTabbedPane component;
        KeyStroke ctrlTab;
        KeyStroke ctrlShiftTab;
        Integer traversalKeys;
        Integer condition;
        Set<AWTKeyStroke> focusTraversalKeys;
        Set<AWTKeyStroke> forwardKeys;
        Set<AWTKeyStroke> backwardKeys;
        InputMap inputMap;

        component = new JTabbedPane();
        ctrlTab = KeyStroke.getKeyStroke("ctrl TAB");
        ctrlShiftTab = KeyStroke.getKeyStroke("ctrl shift TAB");

        // Remove ctrl-tab from normal focus traversal
        traversalKeys = KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS;
        focusTraversalKeys = component.getFocusTraversalKeys(traversalKeys);
        forwardKeys = new HashSet<>(focusTraversalKeys);
        forwardKeys.remove(ctrlTab);

        component.setFocusTraversalKeys(traversalKeys, forwardKeys);

        // Remove ctrl-shift-tab from normal focus traversal
        traversalKeys = KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS;
        focusTraversalKeys = component.getFocusTraversalKeys(traversalKeys);
        backwardKeys = new HashSet<>(focusTraversalKeys);
        backwardKeys.remove(ctrlShiftTab);

        component.setFocusTraversalKeys(traversalKeys, backwardKeys);

        // Add keys to the tab's input map
        condition = JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
        inputMap = component.getInputMap(condition);
        inputMap.put(ctrlTab, "navigateNext");
        inputMap.put(ctrlShiftTab, "navigatePrevious");

        ctrlTab = ctrlShiftTab = null;
        traversalKeys = condition = null;
        focusTraversalKeys = forwardKeys = backwardKeys = null;
        inputMap = null;

        return component;
    }

    /* ______________________________________________________________________ */
    private void destructor() {
        for (int i = 0; i < textEditors.size(); i++) {
            textEditors.get(i).destructor();
        }

        menuBar = null;
        colorChooser = null;
        fontChooser = null;

        textEditors = null;
        tabbedPane = null;

        findBarPanel = null;
        statusBarPanel = null;
    }

    /* MAIN _________________________________________________________________ */
    public static void main(String[] args) {
        TextEditorsManager textEditorFrame = new TextEditorsManager();
        textEditorFrame.addNewTab();

        if (args.length > 0) {
            for (String arg : args) {
                textEditorFrame.openFile(arg, true);
            }
        }/* else {
            textEditorFrame.openFile(System.getProperty("user.dir") + "/Files/Lorem_ipsum.txt", false);
            textEditorFrame.openFile(System.getProperty("user.dir") + "/Files/FutureFeatures.txt", false);
        }*/
        textEditorFrame.setVisible(true);
        textEditorFrame.getSelectedTextEditor().requestFocus();
    }
}
