package interfaces.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.PopupMenuEvent;

import utilities.Utilities;

public class SearchSidebar extends JPanel {

    /* ATTRIBUTTES __________________________________________________________ */
    private JComboBox comboBox;
    private JButton previousButton;
    private JButton nextButton;
    private JToggleButton matchCaseButton;
    private JToggleButton highlightMatchesButton;
    private JLabel matchesCountLabel;
    private JButton closeButton;

    private Boolean matchCase;
    private Boolean highlightMatches;

    private SearchListener findListener;

    /* CONSTRUCTORS _________________________________________________________ */
    public SearchSidebar() {
        matchCase = false;
        highlightMatches = true;

        initComponents();
        initEvents();
    }

    /* METHODS ______________________________________________________________ */
    private void initComponents() {
        final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 10);
        final Dimension DEFAULT_DIMENSION = new Dimension(20, 20);

        // Set Up Panel --------------------------------------------------------
        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setVisible(false);

        // Set Up Components ---------------------------------------------------
        // Set Up Label --------------------------------------------------------
        JLabel findLabel = new JLabel("Find:");
        findLabel.setBackground(Color.white);
        findLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        findLabel.setPreferredSize(new Dimension(32, 20));
        findLabel.setOpaque(true);

        // Set Up ComboBox -----------------------------------------------------
        comboBox = new JComboBox();
        comboBox.setEditable(true);
        comboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
        comboBox.setPreferredSize(new Dimension(110, 20));

        // Set Up Buttons ------------------------------------------------------
        previousButton = new JButton("Previous", Utilities.getIcon("/images/back.png"));
        previousButton.setBackground(Color.white);
        previousButton.setEnabled(false);
        previousButton.setFont(DEFAULT_FONT);
        previousButton.setPreferredSize(new Dimension(94, 20));

        // ---------------------------------------------------------------------
        nextButton = new JButton("Next", Utilities.getIcon("/images/front.png"));
        nextButton.setBackground(Color.white);
        nextButton.setEnabled(false);
        nextButton.setFont(DEFAULT_FONT);
        nextButton.setPreferredSize(new Dimension(76, 20));

        // ---------------------------------------------------------------------
        matchCaseButton = new JToggleButton(Utilities.getIcon("/images/uppercase.png"));
        matchCaseButton.setBackground(Color.white);
        matchCaseButton.setPreferredSize(DEFAULT_DIMENSION);
        matchCaseButton.setToolTipText("Match case");

        // ---------------------------------------------------------------------
        highlightMatchesButton = new JToggleButton(Utilities.getIcon("/images/highlighter.png"));
        highlightMatchesButton.setBackground(Color.white);
        highlightMatchesButton.setPreferredSize(DEFAULT_DIMENSION);
        highlightMatchesButton.setSelected(true);
        highlightMatchesButton.setToolTipText("Highlight matches");

        // ---------------------------------------------------------------------
        matchesCountLabel = new JLabel("No matches ", JLabel.RIGHT);
        matchesCountLabel.setFont(new Font("Dialog", Font.PLAIN, 12));

        // ---------------------------------------------------------------------
        closeButton = new JButton(Utilities.getIcon("/images/closeblack.png"));
        closeButton.setBackground(Color.white);
        closeButton.setPreferredSize(DEFAULT_DIMENSION);

        add(findLabel);
        add(comboBox);

        add(previousButton);
        add(nextButton);
        add(matchCaseButton);
        add(highlightMatchesButton);
        add(matchesCountLabel);
        add(closeButton);
    }

    /* ______________________________________________________________________ */
    private void initEvents() {
        // JPanel --------------------------------------------------------------
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                matchesCountLabel.setPreferredSize(new Dimension(getWidth() - 372, 20));
                matchesCountLabel.updateUI();
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
                matchesCountLabel.setPreferredSize(new Dimension(getWidth() - 372, 20));
                matchesCountLabel.updateUI();
            }
        });

        // ComboBox Editor -----------------------------------------------------
        ComboBoxEditor editor = comboBox.getEditor();
        Component editorComponent = editor.getEditorComponent();

        editorComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                editorComponent.setForeground(Color.black);
                if (ke.getKeyChar() == '\n') {
                    findListener.findAction(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                String pattern = getPattern();
                previousButton.setEnabled(!pattern.isEmpty());
                nextButton.setEnabled(!pattern.isEmpty());
            }
        });

        comboBox.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                findListener.findAction(false);
            }
        });

        // Toggle Buttons ------------------------------------------------------
        matchCaseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                matchCase = !matchCase;
                findListener.findAction(false);
            }
        });

        highlightMatchesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                highlightMatches = !highlightMatches;
                findListener.findAction(false);
            }
        });

        // Close Button --------------------------------------------------------
        closeButton.addActionListener(ae -> {
            setVisible(false);
        });

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                closeButton.setIcon(Utilities.getIcon("/images/closered.png"));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                closeButton.setIcon(Utilities.getIcon("/images/closeblack.png"));
            }
        });
    }

    /* ______________________________________________________________________ */
    public void addPattern(String pattern) {
        String item;
        boolean exists = true;

        if (pattern != null) {
            if (!pattern.isEmpty()) {
                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    item = comboBox.getItemAt(i) + "";
                    if (item.equals(pattern)) {
                        exists = false;
                        break;
                    }
                }

                if (exists) {
                    comboBox.addItem(pattern);
                }
            }
        }
    }

    /* GETTERS ______________________________________________________________ */
    public Boolean isUpperCaseMatch() {
        return matchCase;
    }

    /* ______________________________________________________________________ */
    public Boolean isHighlightMatches() {
        return highlightMatches;
    }

    /* ______________________________________________________________________ */
    public JComboBox getComboBox() {
        return comboBox;
    }

    /* ______________________________________________________________________ */
    public JButton getPreviousButton() {
        return previousButton;
    }

    /* ______________________________________________________________________ */
    public JButton getNextButton() {
        return nextButton;
    }

    /* ______________________________________________________________________ */
    public JButton getCloseButton() {
        return closeButton;
    }

    /* ______________________________________________________________________ */
    public String getPattern() {
        String pattern = "";
        Object item = comboBox.getEditor().getItem();
        if (item != null) {
            pattern = item + "";
        }
        return pattern;
    }

    /* SETTERS ______________________________________________________________ */
    public void setFindListener(SearchListener findListener) {
        this.findListener = findListener;
    }

    /* ______________________________________________________________________ */
    public void setMatchesCount(int matchesCount) {
        matchesCountLabel.setText(matchesCount + " matches ");
    }
}
