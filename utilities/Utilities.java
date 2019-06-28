package utilities;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.ImageIcon;

public class Utilities {

    /* ______________________________________________________________________ */
    public static void output(String string) {
        Calendar calendar = Calendar.getInstance();
        System.out.println("[" + calendar.get(Calendar.HOUR_OF_DAY)
                + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND)
                + ":" + calendar.get(Calendar.MILLISECOND)
                + "] " + string);
    }

    /* ______________________________________________________________________ */
    public static ImageIcon getIcon(String iconPath) { // /Package/Image.ext
        return iconPath != null ? new ImageIcon(Utilities.class.getResource(iconPath)) : null;
    }

    /* ______________________________________________________________________ */
    public static Image getImage(String imagePath) { // /Package/Image.ext
        return getIcon(imagePath).getImage();
    }

    /* ______________________________________________________________________ */
//    public static ImageIcon get(String iconPath) {
//        return getIcon(iconPath);
//    }

    /* ______________________________________________________________________ */
    public static void setClipboard(String string) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new java.awt.datatransfer.StringSelection(string),
                (java.awt.datatransfer.Clipboard clpbrd, Transferable t) -> {
                });
    }

    /* ______________________________________________________________________ */
    public static String getClipboard() {
        String result = "";
        Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                System.out.println(ex);
            }
        }
        return result;
    }
}
