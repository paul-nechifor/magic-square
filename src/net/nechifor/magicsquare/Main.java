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

            Panou panou = new Panou(498, 524);
            Fereastra fereastra = new Fereastra(panou);
        }});
    }
}