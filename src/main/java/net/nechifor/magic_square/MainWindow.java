package net.nechifor.magic_square;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow(MainPanel panou) {
        setTitle("Magic square");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panou);
        pack();
        setVisible(true);
        setResizable(false);
        centerWindow();
    }

    private void centerWindow() {
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width / 2 - getWidth() / 2, size.height / 2 - getHeight() / 2);
    }
}
