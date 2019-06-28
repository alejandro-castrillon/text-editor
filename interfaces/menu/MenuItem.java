package interfaces.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import utilities.Utilities;

public final class MenuItem extends JMenuItem/* implements Cloneable*/ {

    /* ATTRIBUTES ___________________________________________________________ */
    private static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 11);

    /* CONSTRUCTORS _________________________________________________________ */
    public MenuItem(MenuItem menuItem) {

        setText(menuItem.getText());
        setIcon(menuItem.getIcon());
        setFont(DEFAULT_FONT);
        setForeground(Color.BLACK);
        setMnemonic(menuItem.getMnemonic());
        setAccelerator(menuItem.getAccelerator());

        ActionListener[] actionListeners = menuItem.getActionListeners();
        for (ActionListener actionListener1 : actionListeners) {
            addActionListener(actionListener1);
        }
    }

    /* ______________________________________________________________________ */
    public MenuItem(String text, int mnemonic1, int mnemonic2, int mnemonic3, String iconPath) {

        setText(text);
        setIcon(Utilities.getIcon(iconPath));
        setFont(DEFAULT_FONT);
        setForeground(Color.BLACK);
        setMnemonic(mnemonic1);

        if (mnemonic2 != 0) {
            setAccelerator(KeyStroke.getKeyStroke(mnemonic2, mnemonic3));
        }
    }

    /* ______________________________________________________________________ */
    public MenuItem(String name, int mnemonic1, int mnemonic2, String iconPath) {
        this(name, mnemonic1, mnemonic2, ActionEvent.CTRL_MASK, iconPath);
    }

    /* ______________________________________________________________________ */
    public MenuItem(String text, int mnemonic, String iconPath) {
        this(text, mnemonic, 0, 0, iconPath);
    }

    /* ______________________________________________________________________ */
    public MenuItem(String name, int mnemonic1, int mnemonic2, int mnemonic3) {
        this(name, mnemonic1, mnemonic2, mnemonic3, null);
    }

    /* ______________________________________________________________________ */
    public MenuItem(String name, int mnemonic1, int mnemonic2) {
        this(name, mnemonic1, mnemonic2, null);
    }

    /* ______________________________________________________________________ */
    public MenuItem(String name, int mnemonic) {
        this(name, mnemonic, null);
    }

    /* METHODS ______________________________________________________________ */
//    @Override
//    public MenuItem clone() {
//        try {
//            return (MenuItem) super.clone();
//        } catch (CloneNotSupportedException e) {
//            return null;
//        }
//    }
}
