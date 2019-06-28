package interfaces.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class StatusBar extends JPanel {

    /* ATTRIBUTES ___________________________________________________________ */
    private JLabel wordsLabel;
    private JLabel lineCountLabel;
    private JLabel coordsLabel;

    /* CONSTRUCTORS _________________________________________________________ */
    public StatusBar() {
        initComponents();
    }

    /* METHODS ______________________________________________________________ */
    private void initComponents() {
        // ---------------------------------------------------------------------
        final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);
        JPanel westPanel = new JPanel();
        JPanel eastPanel = new JPanel();

        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.X_AXIS));
        westPanel.setBackground(Color.white);

        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.X_AXIS));
        eastPanel.setBackground(Color.white);

        // ---------------------------------------------------------------------
        setBackground(Color.white);
        setLayout(new BorderLayout());

        // ---------------------------------------------------------------------
        wordsLabel = new JLabel("0 words, 0 characters");
        lineCountLabel = new JLabel("1 row", JLabel.CENTER);
        coordsLabel = new JLabel("ln 0: col 0");

        wordsLabel.setFont(DEFAULT_FONT);
        lineCountLabel.setFont(DEFAULT_FONT);
        coordsLabel.setFont(DEFAULT_FONT);

        // ---------------------------------------------------------------------
        westPanel.add(wordsLabel);
        westPanel.add(new JLabel(" "));
        westPanel.add(new JSeparator(SwingConstants.VERTICAL));

        eastPanel.add(new JSeparator(SwingConstants.VERTICAL));
        eastPanel.add(coordsLabel);

        add(westPanel, BorderLayout.WEST);
        add(lineCountLabel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
    }

    /* SETTERS ______________________________________________________________ */
    public void setWords(String words) {
        wordsLabel.setText(words);
    }

    /* ______________________________________________________________________ */
    public void setLineCount(int lines) {
        lineCountLabel.setText(lines + " line" + (lines > 1 ? "s" : ""));
    }

    /* ______________________________________________________________________ */
    public void setCoords(String coords) {
        coordsLabel.setText(coords);
    }
}
