package net.nechifor.magic_square;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements Listener
{
    static private int BLANA = 0;

    private int[][] patratul;
    private int[] scorColoane;
    private int[] scorLinii;
    private int[] scorDiagonale;
    private double[][] culoare;
    private int lungime;
    private int inaltime;
    private int n;
    private int M;
    private double gresealaMaxima;

    private int startX;
    private int startY;
    private int patrat;

    private int iteratia;

    private JComboBox ordineCb;
    private JComboBox algoritmCb;
    private JButton gasesteBtn;
    private JButton copiazaBtn;
    private JLabel iteratiaLb;

    static Color culoareLinii = new Color(170, 170, 170);
    static Color negru = new Color(80, 80, 80);


    public MainPanel(int lungime, int inaltime)
    {
        this.setPreferredSize(new Dimension(lungime, inaltime));
        this.lungime = lungime;
        this.inaltime = inaltime;

        adaugaComponente();
    }

    private void adaugaComponente()
    {
        int ordinulMaxim = 48;
        String[] ordine = new String[ordinulMaxim - 3 + 1];
        for (int i = 0; i < ordine.length; i++)
            ordine[i] = new Integer(i + 3).toString();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(Box.createRigidArea(new Dimension(0, inaltime - 26)));
        JPanel panouJos = new JPanel();
        panouJos.setBackground(Color.WHITE);

        panouJos.setLayout(new BoxLayout(panouJos, BoxLayout.LINE_AXIS));

        JLabel l = new JLabel("Algoritm: ");
        l.setForeground(negru);
        panouJos.add(l);
        algoritmCb = new JComboBox(new String[] {"FIHC", "BIHC", "LAHC"});
        algoritmCb.setSelectedIndex(1);
        algoritmCb.setMaximumSize(algoritmCb.getPreferredSize());
        panouJos.add(algoritmCb);

        panouJos.add(Box.createRigidArea(new Dimension(4, 0)));
        l = new JLabel("Ordin: ");
        l.setForeground(negru);
        panouJos.add(l);


        ordineCb = new JComboBox(ordine);
        ordineCb.setForeground(negru);
        ordineCb.setSelectedIndex(18 - 3);
        ordineCb.setMaximumSize(ordineCb.getPreferredSize());
        panouJos.add(ordineCb);
        panouJos.add(Box.createRigidArea(new Dimension(5, 0)));

        gasesteBtn = new JButton("Găsește");
        gasesteBtn.setForeground(negru);
        panouJos.add(gasesteBtn);
        panouJos.add(Box.createRigidArea(new Dimension(5, 0)));

        copiazaBtn = new JButton("Copiază");
        copiazaBtn.setForeground(negru);
        copiazaBtn.setEnabled(false);
        panouJos.add(copiazaBtn);
        panouJos.add(Box.createRigidArea(new Dimension(5, 0)));

        iteratiaLb = new JLabel("Iterația: 0000");
        iteratiaLb.setForeground(negru);
        panouJos.add(iteratiaLb);
        panouJos.add(Box.createRigidArea(new Dimension(4, 0)));

        this.add(panouJos);

        gasesteBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int n = ordineCb.getSelectedIndex() + 3;
                porneste(n);
            }
        });

        copiazaBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int pozitii = new Integer(n*n).toString().length() + 1;
                String format = "%" + pozitii + "d";

                StringBuilder tabel = new StringBuilder();
                for (int i = 0; i < n; i++)
                {
                    for (int j = 0; j < n; j++)
                        tabel.append(String.format(format, patratul[i][j]));
                    tabel.append("\n");
                }

                StringSelection stringSelection =
                        new StringSelection(tabel.toString());
                Clipboard clipboard =
                        Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });
    }

    private void porneste(int n)
    {
        this.n = n;
        int n2 = n * n;
        this.M = (n * (n2 + 1)) / 2;
        this.scorLinii = new int[n];
        this.scorColoane = new int[n];
        this.scorDiagonale = new int[2];
        this.culoare = new double[n][n];
        this.gresealaMaxima = 0;
        this.iteratia = 0;
        for (int i = 0; i < n; i++)
            this.gresealaMaxima += n2 - i;
        this.gresealaMaxima = Math.abs(this.gresealaMaxima - M);

        patrat = (lungime - 2 * BLANA) / n;
        startY = (lungime - (n * patrat)) / 2;
        startX = (lungime - (n * patrat)) / 2;

        algoritmCb.setEnabled(false);
        ordineCb.setEnabled(false);
        gasesteBtn.setEnabled(false);
        copiazaBtn.setEnabled(false);

        final Solver rezolvator = new Solver(n, this, 10);

        Runnable runnable = new Runnable()
        {
            public void run()
            {
                int algoritm = algoritmCb.getSelectedIndex();
                if (algoritm == 0)
                    rezolvator.startFirstImprovementHillClimbing();
                else if (algoritm == 1)
                    rezolvator.startBestImprovementHillClimbing();
                else if (algoritm == 2)
                    rezolvator.startLateAcceptanceHillClimbing();
            }
        };
        new Thread(runnable).start();
    }

    public void currentState(int[][] patratul)
    {
        this.patratul = patratul;
        repaint();
    }

    public void restart()
    {
        iteratia++;
        iteratiaLb.setText(String.format("Iterația: %04d", iteratia));
    }

    public void finish()
    {
        algoritmCb.setEnabled(true);
        ordineCb.setEnabled(true);
        gasesteBtn.setEnabled(true);
        copiazaBtn.setEnabled(true);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, lungime, inaltime);

        // Desenează un tabel gol.
        if (patratul == null)
        {
            g2d.setPaint(culoareLinii);
            g2d.drawRect(BLANA, BLANA, lungime-2*BLANA-1, lungime-2*BLANA-1);
            return;
        }

        g2d.setPaint(culoareLinii);
        g2d.setStroke(new BasicStroke((float) 1));

        g2d.setFont(new Font("Dialog", Font.PLAIN, (int) (patrat/2.3)));
        FontMetrics metrics = g2d.getFontMetrics();
        int fh = metrics.getHeight();
        fh -= fh / 4;

        scorDiagonale[0] = 0;
        scorDiagonale[1] = 0;
        for (int i = 0; i < n; i++)
        {
            scorDiagonale[0] += patratul[i][i];
            scorDiagonale[1] += patratul[i][n - i - 1];
            scorLinii[i] = 0;
            scorColoane[i] = 0;
            for (int j = 0; j < n; j++)
            {
                scorLinii[i] += patratul[i][j];
                scorColoane[i] += patratul[j][i];
            }
            scorLinii[i] = Math.abs(M - scorLinii[i]);
            scorColoane[i] = Math.abs(M - scorColoane[i]);
        }
        scorDiagonale[0] = Math.abs(M - scorDiagonale[0]);
        scorDiagonale[1] = Math.abs(M - scorDiagonale[1]);

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                culoare[i][j] = 4 * (scorLinii[i] / gresealaMaxima);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                culoare[j][i] += 4 * (scorColoane[i] / gresealaMaxima);
        for (int i = 0; i < n; i++)
            culoare[i][i] += 4 * (scorDiagonale[0] / gresealaMaxima);
        for (int i = 0; i < n; i++)
            culoare[i][n-i-1] += 4 * (scorDiagonale[1] / gresealaMaxima);
        

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            {
                int cul = (int) Math.ceil(255 * culoare[i][j]);
                if (cul > 0)
                    cul += 30;
                if (cul > 255)
                    cul = 255;
                g2d.setPaint(new Color(255, 255 - cul, 255 - cul));
                g2d.fillRect(startX + j * patrat, startY + i * patrat,
                        patrat, patrat);
                g2d.setPaint(culoareLinii);
                g2d.drawRect(startX + j * patrat, startY + i * patrat,
                        patrat, patrat);
                String numar = new Integer(patratul[i][j]).toString();
                int fw = metrics.stringWidth(numar);
                g2d.setPaint(negru);
                g2d.drawString(numar,
                        startX + j * patrat + (patrat-fw)/2,
                        startY + i * patrat + fh/2 + patrat/2);
            }
    }
}
