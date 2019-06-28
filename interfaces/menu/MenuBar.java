package interfaces.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {

    /* ATTRIBUTES ___________________________________________________________ */
    private final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);

    // Menu Items --------------------------------------------------------------
    // File --------------------------------------------------------------------
    private MenuItem newMenuItem;
    private MenuItem openMenuItem;
    private MenuItem saveMenuItem;
    private MenuItem saveAsMenuItem;
    private MenuItem closeTabMenuItem;
    private MenuItem closeAllTabsMenuItem;
    private MenuItem exitMenuItem;

    // Edit --------------------------------------------------------------------
    private MenuItem undoMenuItem;
    private MenuItem redoMenuItem;
    private MenuItem copyMenuItem;
    private MenuItem cutMenuItem;
    private MenuItem pasteMenuItem;
    private MenuItem selectAllMenuItem;
    private MenuItem findInFileMenuItem;

    // View --------------------------------------------------------------------
    private MenuItem backgroundMenuItem;
    private MenuItem foregroundMenuItem;
    private MenuItem highlightColorMenuItem;
    private MenuItem cursorColorMenuItem;
    private MenuItem fontMenuItem;
    private MenuItem marginSizeMenuItem;
    private CheckBoxMenuItem warpTextMenuItem;
    private CheckBoxMenuItem showStatusBarMenuItem;
    private MenuItem closeFindBarMenuItem;

    // Help --------------------------------------------------------------------
    private MenuItem aboutMenuItem;

    /* CONSTRUCTORS _________________________________________________________ */
    public MenuBar() {
        initComponents();
    }

    /* METHODS ______________________________________________________________ */
    private void initComponents() {

        // Menus ---------------------------------------------------------------
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");
        JMenu helpMenu = new JMenu("Help");

        // Menu Items ----------------------------------------------------------
        // File ----------------------------------------------------------------
        newMenuItem = new MenuItem("New", 'N', 'N', "/images/new.png");
        openMenuItem = new MenuItem("Open", 'O', 'O', "/images/open.png");
        saveMenuItem = new MenuItem("Save", 'S', 'S', "/images/save.png");
        saveAsMenuItem = new MenuItem("Save As", 'A');
        closeTabMenuItem = new MenuItem("Close Tab", 'C', 'W', "/images/closered.png");
        closeAllTabsMenuItem = new MenuItem("Close All", 'C', 'W', ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK);
        exitMenuItem = new MenuItem("Exit", 'E', java.awt.event.KeyEvent.VK_F4, ActionEvent.ALT_MASK);

        // Edit ----------------------------------------------------------------
        undoMenuItem = new MenuItem("Undo", 'U', 'Z', "/images/undo.png");
        redoMenuItem = new MenuItem("Redo", 'R', 'Y', "/images/redo.png");
        cutMenuItem = new MenuItem("Cut", 'C', 'X', "/images/cut.png");
        copyMenuItem = new MenuItem("Copy", 'C', 'C', "/images/copy.png");
        pasteMenuItem = new MenuItem("Paste", 'P', 'V', "/images/paste.png");
        selectAllMenuItem = new MenuItem("Select All", 'S', 'A', "/images/select.png");
        findInFileMenuItem = new MenuItem("Find", 'F', 'F', "/images/find.png");

        // View ----------------------------------------------------------------
        backgroundMenuItem = new MenuItem("Background", 'B', "/images/background.png");
        foregroundMenuItem = new MenuItem("Foreground", 'F', "/images/foreground.png");
        highlightColorMenuItem = new MenuItem("Highligh Color", 'H', "/images/highlighter.png");
        cursorColorMenuItem = new MenuItem("Cursor Color", 'C', "/images/cursor.png");
        fontMenuItem = new MenuItem("Font", 'F', "/images/font.png");
        marginSizeMenuItem = new MenuItem("Margin Size", 'B', "/images/marginSize.png");
        warpTextMenuItem = new CheckBoxMenuItem("WarpText", true, 'W');
        showStatusBarMenuItem = new CheckBoxMenuItem("StatusBar", true, 'T');
        closeFindBarMenuItem = new MenuItem("Close Find Bar", 'C', KeyEvent.VK_ESCAPE, 0);

        // Help ----------------------------------------------------------------
        aboutMenuItem = new MenuItem("About", 'A');

        // Set Up Components ---------------------------------------------------
        // File Menu -----------------------------------------------------------
        fileMenu.setFont(DEFAULT_FONT);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(closeTabMenuItem);
        fileMenu.add(closeAllTabsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        // Edit Menu -----------------------------------------------------------
        editMenu.setFont(DEFAULT_FONT);
        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);
        editMenu.addSeparator();
        editMenu.add(copyMenuItem);
        editMenu.add(cutMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.addSeparator();
        editMenu.add(selectAllMenuItem);
        editMenu.addSeparator();
        editMenu.add(findInFileMenuItem);

        // View Menu -----------------------------------------------------------
        viewMenu.setFont(DEFAULT_FONT);
        viewMenu.setMnemonic(KeyEvent.VK_V);
        viewMenu.add(backgroundMenuItem);
        viewMenu.add(foregroundMenuItem);
        viewMenu.addSeparator();
        viewMenu.add(highlightColorMenuItem);
        viewMenu.add(cursorColorMenuItem);
        viewMenu.addSeparator();
        viewMenu.add(fontMenuItem);
        viewMenu.add(marginSizeMenuItem);
        viewMenu.addSeparator();
        viewMenu.add(warpTextMenuItem);
        viewMenu.add(showStatusBarMenuItem);
        viewMenu.addSeparator();
        viewMenu.add(closeFindBarMenuItem);

        // Help Menu -----------------------------------------------------------
        helpMenu.setFont(DEFAULT_FONT);
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.add(aboutMenuItem);

        // Menu Bar
        setBackground(Color.white);
        setBorder(null);

        add(fileMenu);
        add(editMenu);
        add(viewMenu);
        add(helpMenu);
    }

    /* GETTERS ______________________________________________________________ */
    public MenuItem getNewMenuItem() {
        return newMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getOpenMenuItem() {
        return openMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getSaveMenuItem() {
        return saveMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getSaveAsMenuItem() {
        return saveAsMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getCloseTabMenuItem() {
        return closeTabMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getCloseAllTabsMenuItem() {
        return closeAllTabsMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getUndoMenuItem() {
        return undoMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getRedoMenuItem() {
        return redoMenuItem;
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

    /* ______________________________________________________________________ */
    public MenuItem getFindInFileMenuItem() {
        return findInFileMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getBackgroundMenuItem() {
        return backgroundMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getForegroundMenuItem() {
        return foregroundMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getHighlightColorMenuItem() {
        return highlightColorMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getCursorColorMenuItem() {
        return cursorColorMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getFontMenuItem() {
        return fontMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getMarginSizeMenuItem() {
        return marginSizeMenuItem;
    }

    /* ______________________________________________________________________ */
    public CheckBoxMenuItem getWarpTextCheckBoxMenuItem() {
        return warpTextMenuItem;
    }

    /* ______________________________________________________________________ */
    public CheckBoxMenuItem getShowStatusBarCheckBoxMenuItem() {
        return showStatusBarMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    /* ______________________________________________________________________ */
    public MenuItem getCloseFindBarMenuItem() {
        return closeFindBarMenuItem;
    }

    /* SETTERS ______________________________________________________________ */
    public void setSaveMenuItemEnabled(boolean bool) {
        saveMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setSaveAsMenuItemEnabled(boolean bool) {
        saveAsMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setCloseTabMenuItemEnabled(boolean bool) {
        closeTabMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setCloseAllTabsMenuItemEnabled(boolean bool) {
        closeAllTabsMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setUndoMenuItemEnabled(boolean bool) {
        undoMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setRedoMenuItemEnabled(boolean bool) {
        redoMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setCopyMenuItemEnabled(boolean bool) {
        copyMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setCutMenuItemEnabled(boolean bool) {
        cutMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setPasteMenuItemEnabled(boolean bool) {
        pasteMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setSelectAllMenuItemEnabled(boolean bool) {
        selectAllMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setFindInFileMenuItemEnabled(boolean bool) {
        findInFileMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setBackgroundMenuItemEnabled(boolean bool) {
        backgroundMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setForegroundMenuItemEnabled(boolean bool) {
        foregroundMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setHighlightColorMenuItemEnabled(boolean bool) {
        highlightColorMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setCursorColorMenuItemEnabled(boolean bool) {
        cursorColorMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setFontMenuItemEnabled(boolean bool) {
        fontMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setBorderSizMenuItemEnabled(boolean bool) {
        marginSizeMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setWarpTextMenuItemEnabled(boolean bool) {
        warpTextMenuItem.setEnabled(bool);
    }

    /* ______________________________________________________________________ */
    public void setShowStatusBarMenuItemEnabled(boolean bool) {
        showStatusBarMenuItem.setEnabled(bool);
    }
}
