package interfaces.panels;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public abstract class PopupMenuAdapter implements PopupMenuListener {

    /* METHODS ______________________________________________________________ */
    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
    }

    /* ______________________________________________________________________ */
    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
    }

    /* ______________________________________________________________________ */
    @Override
    public void popupMenuCanceled(PopupMenuEvent pme) {
    }
}
