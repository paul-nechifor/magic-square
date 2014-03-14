package net.nechifor.magicsquare;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.UIManager;

public class Applet extends java.applet.Applet
{
    private MainPanel panel;

    @Override
    public void init()
    {
        setLayout(new BorderLayout());
    }

    @Override
    public void start()
    {
    	EventQueue.invokeLater(new Runnable() { public void run() {
            try
            {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {}

            panel = new MainPanel(getWidth(), getHeight());
            add(panel, BorderLayout.CENTER);
        }});
	}

    @Override
    public void stop()
    {
    }
}