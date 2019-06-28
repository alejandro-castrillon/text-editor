package interfaces.menu;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JCheckBox;

public class CheckBoxMenuItem extends JCheckBox {

    /* CONSTRUCTORS _________________________________________________________ */
    public CheckBoxMenuItem(String text, boolean selected, int mnemonic) {
        setText(text);
        setSelected(selected);
        setFont(new Font("Dialog", Font.PLAIN, 11));
        setForeground(Color.BLACK);
        setMnemonic(mnemonic);
    }
}
