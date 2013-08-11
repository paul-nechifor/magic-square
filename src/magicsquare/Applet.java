package magicsquare;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.UIManager;

public class Applet extends java.applet.Applet
{
    private Panou panou;

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

            panou = new Panou(getWidth(), getHeight());
            add(panou, BorderLayout.CENTER);
        }});
	}

    @Override
    public void stop()
    {
    }
}