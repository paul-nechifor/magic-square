package magicsquare;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Fereastra extends JFrame
{
    public Fereastra(Panou panou)
    {
        setTitle("Pătrat magic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panou);
        pack();
        setVisible(true);
        setResizable(false);
        punePeMijloc();
    }

    private void punePeMijloc()
    {
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
    }
}
