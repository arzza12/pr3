package tavern;

import tavern.ui.TavernGUI;

import javax.swing.*;

public class TavernApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TavernGUI gui = new TavernGUI();
            gui.setVisible(true);
        });
    }
}