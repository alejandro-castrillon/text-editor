// 3.1.0
package utilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorChooser extends JColorChooser {

    /* ATTRIBUTES ___________________________________________________________ */
    private static final long serialVersionUID = 1L;

    private final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 11);
    private JDialog dialog;
    private JButton okButton;
    private JButton copyButton;
    private JButton cancelButton;

    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;
    public static final int ERROR_OPTION = -1;
    private int dialogResultValue;

    /* CONSTRUCTORS __________________________________________________________*/
    public ColorChooser(Color color) {
        this.dialogResultValue = ERROR_OPTION;
        setColor(color);
        initComponents();
    }

    public ColorChooser() {
        this.dialogResultValue = ERROR_OPTION;
        initComponents();
    }

    /* METHODS _______________________________________________________________*/
    private void initComponents() {

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonsPanel.setLayout(new GridLayout(3, 1));

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(buttonsPanel, BorderLayout.NORTH);

        // Set Up Buttons ------------------------------------------------------
        okButton = new JButton("Ok");
        okButton.setFont(DEFAULT_FONT);
        okButton.addActionListener(ae -> {
            dialogResultValue = OK_OPTION;
            dialog.dispose();
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(DEFAULT_FONT);
        cancelButton.addActionListener(ae -> {
            dialogResultValue = CANCEL_OPTION;
            dialog.dispose();
        });

        copyButton = new JButton("Copy");
        copyButton.setFont(DEFAULT_FONT);
        copyButton.addActionListener(ae -> {
            Color c = getColor();
            Utilities.setClipboard(c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
            System.out.println(dialog.getSize());
        });

        // Set Up Dialog -------------------------------------------------------
        dialog = new JDialog(new JFrame(), true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(630, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setTitle("Color Chooser");

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dialogResultValue = CANCEL_OPTION;
            }
        });

        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(copyButton);
        dialog.add(this, BorderLayout.CENTER);
        dialog.add(eastPanel, BorderLayout.EAST);

        // Set Up Color Chooser ------------------------------------------------
        for (int i = 0; i < 3; i++) {
            removeChooserPanel(getChooserPanels()[0]);
        }
        removeChooserPanel(getChooserPanels()[1]);
    }

    /* ______________________________________________________________________ */
    public int showDialog() {
        dialogResultValue = ERROR_OPTION;
        dialog.setVisible(true);
        return dialogResultValue;
    }
}
