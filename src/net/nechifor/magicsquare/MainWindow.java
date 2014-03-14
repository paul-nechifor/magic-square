package net.nechifor.magicsquare;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class MainWindow extends JFrame
{
    public MainWindow(MainPanel panou)
    {
        setTitle("Magic square");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panou);
        pack();
        setVisible(true);
        setResizable(false);
        centerWindow();
    }

    private void centerWindow()
    {
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
    }
}
