package net.nechifor.magicsquare;

import java.awt.EventQueue;
import javax.swing.UIManager;

public class Main
{
    public static void main(String[] args)
    {
    	EventQueue.invokeLater(new Runnable() { public void run() {
            try
            {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {}

            MainPanel panel = new MainPanel(498, 524);
            MainWindow window = new MainWindow(panel);
        }});
    }
}