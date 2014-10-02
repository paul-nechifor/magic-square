package net.nechifor.magic_square;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Solver {
    private int n;
    private Listener ascultator;
    private int interval;

    public Solver(int n, Listener ascultator, int peSecunda) {
        this.n = n;
        this.ascultator = ascultator;
        this.interval = 1000 / peSecunda;
    }

    public void startFirstImprovementHillClimbing() {
        int n2 = n * n;
        int M = (n * (n2 + 1)) / 2;

        int[][] mat = new int[n][n];

        int i, j;
        int a, b, ai, aj, bi, bj;
        int tmp;
        int scorNou;
        int scorCur;

        int[] sumaLin = new int[n];
        int[] sumaCol = new int[n];
        int sumaD1, sumaD2;

        long ultimaAnuntare = System.currentTimeMillis();

        while (true) {
            // Pune valori în ordine.
            for (i = 0; i < n; i++)
                for (j = 0; j < n; j++)
                    mat[i][j] = i * n + j + 1;

            // Amestecă valorile.
            for (a = 0; a < n2; a++) {
                b = (int) (Math.random() * n2);
                ai = a / n;
                aj = a % n;
                bi = b / n;
                bj = b % n;
                tmp = mat[ai][aj];
                mat[ai][aj] = mat[bi][bj];
                mat[bi][bj] = tmp;
            }

            if (System.currentTimeMillis() - ultimaAnuntare > interval) {
                ascultator.currentState(mat);
                ultimaAnuntare = System.currentTimeMillis();
            }
            ascultator.restart();

            // Calculez sumele și scorul curent;
            scorCur = 0;
            sumaD1 = 0;
            sumaD2 = 0;
            for (i = 0; i < n; i++) {
                sumaLin[i] = 0;
                sumaCol[i] = 0;
                for (j = 0; j < n; j++) {
                    sumaLin[i] += mat[i][j];
                    sumaCol[i] += mat[j][i];
                }
                sumaD1 += mat[i][i];
                sumaD2 += mat[i][n - i - 1];
                scorCur += Math.abs(M - sumaLin[i]) + Math.abs(M - sumaCol[i]);
            }
            scorCur += Math.abs(M - sumaD1) + Math.abs(M - sumaD2);

            for (int rep = 0; rep < 2000; rep++) {
                for (int x = 0; x < n2; x++) {
                    if (System.currentTimeMillis() - ultimaAnuntare
                            > interval) {
                        ascultator.currentState(mat);
                        ultimaAnuntare = System.currentTimeMillis();
                    }

                    a = (int) (Math.random() * n2);
                    do {
                        b = (int) (Math.random() * n2);
                    } while (a == b);

                    ai = a / n;
                    aj = a % n;
                    bi = b / n;
                    bj = b % n;

                    scorNou = scorCur;

                    // Dacă a și b sunt pe aceeași linie, coloană sau
                    // diagonale, nu trebuie să mai recalculez.
                    if (ai != bi) {
                        scorNou = scorNou - Math.abs(sumaLin[ai] - M)
                                + Math.abs(sumaLin[ai] - mat[ai][aj] +
                                mat[bi][bj] - M)
                                - Math.abs(sumaLin[bi] - M)
                                + Math.abs(sumaLin[bi] - mat[bi][bj] +
                                mat[ai][aj] - M);
                    }
                    if (aj != bj) {
                        scorNou = scorNou - Math.abs(sumaCol[aj] - M)
                                + Math.abs(sumaCol[aj] - mat[ai][aj] +
                                mat[bi][bj] - M)
                                - Math.abs(sumaCol[bj] - M)
                                + Math.abs(sumaCol[bj] - mat[bi][bj] +
                                mat[ai][aj] - M);
                    }
                    // Dacă a este pe D1 și b nu este.
                    if (ai == aj && bi != bj)
                        scorNou = scorNou - Math.abs(sumaD1 - M) +
                                Math.abs(sumaD1 - mat[ai][aj] +
                                        mat[bi][bj] - M);
                    // Dacă b este pe D1 și a nu este.
                    if (bi == bj && ai != aj)
                        scorNou = scorNou - Math.abs(sumaD1 - M) +
                                Math.abs(sumaD1 - mat[bi][bj] +
                                        mat[ai][aj] - M);
                    // Dacă a este pe D2 și b nu este.
                    if ((aj == n - ai - 1) && (bj != n - bi - 1))
                        scorNou = scorNou - Math.abs(sumaD2 - M) +
                                Math.abs(sumaD2 - mat[ai][aj] +
                                        mat[bi][bj] - M);
                    // Dacă b este pe D2 și a nu este.
                    if ((bj == n - bi - 1) && (aj != n - ai - 1))
                        scorNou = scorNou - Math.abs(sumaD2 - M) +
                                Math.abs(sumaD2 - mat[bi][bj] +
                                        mat[ai][aj] - M);

                    // Am găsit o mutare destul de bună.
                    if (scorNou > scorCur)
                        continue;

                    // Trebuie să modific sumele.
                    if (ai != bi) {
                        sumaLin[ai] = sumaLin[ai] - mat[ai][aj] + mat[bi][bj];
                        sumaLin[bi] = sumaLin[bi] - mat[bi][bj] + mat[ai][aj];
                    }
                    if (aj != bj) {
                        sumaCol[aj] = sumaCol[aj] - mat[ai][aj] + mat[bi][bj];
                        sumaCol[bj] = sumaCol[bj] - mat[bi][bj] + mat[ai][aj];
                    }
                    if (ai == aj && bi != bj)
                        sumaD1 = sumaD1 - mat[ai][aj] + mat[bi][bj];
                    if (bi == bj && ai != aj)
                        sumaD1 = sumaD1 - mat[bi][bj] + mat[ai][aj];
                    if ((aj == n - ai - 1) && (bj != n - bi - 1))
                        sumaD2 = sumaD2 - mat[ai][aj] + mat[bi][bj];
                    if ((bj == n - bi - 1) && (aj != n - ai - 1))
                        sumaD2 = sumaD2 - mat[bi][bj] + mat[ai][aj];

                    // Fac interschimbarea.
                    tmp = mat[ai][aj];
                    mat[ai][aj] = mat[bi][bj];
                    mat[bi][bj] = tmp;

                    // Și trebuie să modific și scorul curent.
                    scorCur = scorNou;

                    // Dacă scorul e zero, s-a ajuns la o soluție.
                    if (scorCur == 0) {
                        ascultator.currentState(mat);
                        ascultator.finish();
                        return;
                    }

                    break;
                }
            }
        }
    }

    public void startBestImprovementHillClimbing() {
        int n2 = n * n;
        int M = (n * (n2 + 1)) / 2;

        int[][] mat = new int[n][n];

        int i, j;
        int a, b, ai, aj, bi, bj;
        int aCmb, bCmb;
        int aPrec, bPrec;
        int tmp;

        int scorCmb;
        int scorNou;
        int scorCur;

        int[] sumaLin = new int[n];
        int[] sumaCol = new int[n];
        int sumaD1, sumaD2;

        long ultimaAnuntare = System.currentTimeMillis();

        while (true) {
            aPrec = n2; // Inițializat cu valoare imposibilă.
            bPrec = n2;

            // Pune valori în ordine.
            for (i = 0; i < n; i++)
                for (j = 0; j < n; j++)
                    mat[i][j] = i * n + j + 1;

            // Amestecă valorile.
            for (a = 0; a < n2; a++) {
                b = (int) (Math.random() * n2);
                ai = a / n;
                aj = a % n;
                bi = b / n;
                bj = b % n;
                tmp = mat[ai][aj];
                mat[ai][aj] = mat[bi][bj];
                mat[bi][bj] = tmp;
            }

            if (System.currentTimeMillis() - ultimaAnuntare > interval) {
                ascultator.currentState(mat);
                ultimaAnuntare = System.currentTimeMillis();
            }
            ascultator.restart();

            // Calculez sumele și scorul curent;
            scorCur = 0;
            sumaD1 = 0;
            sumaD2 = 0;
            for (i = 0; i < n; i++) {
                sumaLin[i] = 0;
                sumaCol[i] = 0;
                for (j = 0; j < n; j++) {
                    sumaLin[i] += mat[i][j];
                    sumaCol[i] += mat[j][i];
                }
                sumaD1 += mat[i][i];
                sumaD2 += mat[i][n - i - 1];
                scorCur += Math.abs(M - sumaLin[i]) + Math.abs(M - sumaCol[i]);
            }
            scorCur += Math.abs(M - sumaD1) + Math.abs(M - sumaD2);

            for (int rep = 0; rep < 2000; rep++) {
                scorCmb = Integer.MAX_VALUE;
                aCmb = n2 + 1;
                bCmb = n2 + 1;

                if (System.currentTimeMillis() - ultimaAnuntare > interval) {
                    ascultator.currentState(mat);
                    ultimaAnuntare = System.currentTimeMillis();
                }

                for (a = 1; a < n2; a++)
                    for (b = 0; b < a; b++) {
                        ai = a / n;
                        aj = a % n;
                        bi = b / n;
                        bj = b % n;

                        scorNou = scorCur;

                        // Dacă a și b sunt pe aceeași linie, coloană sau
                        // diagonale, nu trebuie să mai recalculez.
                        if (ai != bi) {
                            scorNou = scorNou - Math.abs(sumaLin[ai] - M)
                                    + Math.abs(sumaLin[ai] - mat[ai][aj] +
                                    mat[bi][bj] - M)
                                    - Math.abs(sumaLin[bi] - M)
                                    + Math.abs(sumaLin[bi] - mat[bi][bj] +
                                    mat[ai][aj] - M);
                        }
                        if (aj != bj) {
                            scorNou = scorNou - Math.abs(sumaCol[aj] - M)
                                    + Math.abs(sumaCol[aj] - mat[ai][aj] +
                                    mat[bi][bj] - M)
                                    - Math.abs(sumaCol[bj] - M)
                                    + Math.abs(sumaCol[bj] - mat[bi][bj] +
                                    mat[ai][aj] - M);
                        }
                        // Dacă a este pe D1 și b nu este.
                        if (ai == aj && bi != bj)
                            scorNou = scorNou - Math.abs(sumaD1 - M) +
                                    Math.abs(sumaD1 - mat[ai][aj] +
                                            mat[bi][bj] - M);
                        // Dacă b este pe D1 și a nu este.
                        if (bi == bj && ai != aj)
                            scorNou = scorNou - Math.abs(sumaD1 - M) +
                                    Math.abs(sumaD1 - mat[bi][bj] +
                                            mat[ai][aj] - M);
                        // Dacă a este pe D2 și b nu este.
                        if ((aj == n - ai - 1) && (bj != n - bi - 1))
                            scorNou = scorNou - Math.abs(sumaD2 - M) +
                                    Math.abs(sumaD2 - mat[ai][aj] +
                                            mat[bi][bj] - M);
                        // Dacă b este pe D2 și a nu este.
                        if ((bj == n - bi - 1) && (aj != n - ai - 1))
                            scorNou = scorNou - Math.abs(sumaD2 - M) +
                                    Math.abs(sumaD2 - mat[bi][bj] +
                                            mat[ai][aj] - M);

                        if (scorNou < scorCmb) {
                            scorCmb = scorNou;
                            aCmb = a;
                            bCmb = b;
                        }
                    }

                // Dacă se face aceași înlocuire ca data trecută am ajuns
                // într-un minim local.
                if (aPrec == aCmb && bPrec == bCmb)
                    break;

                aPrec = aCmb;
                bPrec = bCmb;

                // Am găsit mutarea cea mai bună și fac interschimbarea, dar mai
                // întâi sumele.
                ai = aCmb / n;
                aj = aCmb % n;
                bi = bCmb / n;
                bj = bCmb % n;

                // Trebuie să modific sumele.
                if (ai != bi) {
                    sumaLin[ai] = sumaLin[ai] - mat[ai][aj] + mat[bi][bj];
                    sumaLin[bi] = sumaLin[bi] - mat[bi][bj] + mat[ai][aj];
                }
                if (aj != bj) {
                    sumaCol[aj] = sumaCol[aj] - mat[ai][aj] + mat[bi][bj];
                    sumaCol[bj] = sumaCol[bj] - mat[bi][bj] + mat[ai][aj];
                }
                if (ai == aj && bi != bj)
                    sumaD1 = sumaD1 - mat[ai][aj] + mat[bi][bj];
                if (bi == bj && ai != aj)
                    sumaD1 = sumaD1 - mat[bi][bj] + mat[ai][aj];
                if ((aj == n - ai - 1) && (bj != n - bi - 1))
                    sumaD2 = sumaD2 - mat[ai][aj] + mat[bi][bj];
                if ((bj == n - bi - 1) && (aj != n - ai - 1))
                    sumaD2 = sumaD2 - mat[bi][bj] + mat[ai][aj];

                // Fac interschimbarea.
                tmp = mat[ai][aj];
                mat[ai][aj] = mat[bi][bj];
                mat[bi][bj] = tmp;

                // Și trebuie să modific și scorul curent.
                scorCur = scorCmb;

                // Dacă scorul e zero, s-a ajuns la o soluție.
                if (scorCur == 0) {
                    ascultator.currentState(mat);
                    ascultator.finish();
                    return;
                }
            }
        }
    }

    public void startLateAcceptanceHillClimbing() {
        Comparator comp = new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

        int n2 = n * n;
        int M = (n * (n2 + 1)) / 2;
        int L = 5000;
        int vechime;

        int[][] mat = new int[n][n];
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>(L, comp);

        int i, j;
        int a, b, ai, aj, bi, bj;
        int tmp;
        int scorNou;
        int scorCur;

        int[] sumaLin = new int[n];
        int[] sumaCol = new int[n];
        int sumaD1, sumaD2;

        long ultimaAnuntare = System.currentTimeMillis();

        while (true) {
            // Pune valori în ordine.
            for (i = 0; i < n; i++)
                for (j = 0; j < n; j++)
                    mat[i][j] = i * n + j + 1;

            // Amestecă valorile.
            for (a = 0; a < n2; a++) {
                b = (int) (Math.random() * n2);
                ai = a / n;
                aj = a % n;
                bi = b / n;
                bj = b % n;
                tmp = mat[ai][aj];
                mat[ai][aj] = mat[bi][bj];
                mat[bi][bj] = tmp;
            }

            if (System.currentTimeMillis() - ultimaAnuntare > interval) {
                ascultator.currentState(mat);
                ultimaAnuntare = System.currentTimeMillis();
            }
            ascultator.restart();

            // Calculez sumele și scorul curent;
            scorCur = 0;
            sumaD1 = 0;
            sumaD2 = 0;
            for (i = 0; i < n; i++) {
                sumaLin[i] = 0;
                sumaCol[i] = 0;
                for (j = 0; j < n; j++) {
                    sumaLin[i] += mat[i][j];
                    sumaCol[i] += mat[j][i];
                }
                sumaD1 += mat[i][i];
                sumaD2 += mat[i][n - i - 1];
                scorCur += Math.abs(M - sumaLin[i]) + Math.abs(M - sumaCol[i]);
            }
            scorCur += Math.abs(M - sumaD1) + Math.abs(M - sumaD2);

            // Golesc coada de prioritate și adaug de L ori scorul curent.
            pq.clear();
            for (i = 0; i < L; i++)
                pq.add(scorCur);

            // De câte ori s-a repetat valoarea curentă a scorului.
            vechime = 0;

            forDeVechime:
            for (int rep = 0; rep < 40000; rep++) {
                if (System.currentTimeMillis() - ultimaAnuntare > interval) {
                    ascultator.currentState(mat);
                    ultimaAnuntare = System.currentTimeMillis();
                }

                for (int x = 0; x < n2; x++) {
                    a = (int) (Math.random() * n2);
                    do {
                        b = (int) (Math.random() * n2);
                    } while (a == b);

                    ai = a / n;
                    aj = a % n;
                    bi = b / n;
                    bj = b % n;

                    scorNou = scorCur;

                    // Dacă a și b sunt pe aceeași linie, coloană sau
                    // diagonale, nu trebuie să mai recalculez.
                    if (ai != bi) {
                        scorNou = scorNou - Math.abs(sumaLin[ai] - M)
                                + Math.abs(sumaLin[ai] - mat[ai][aj] +
                                mat[bi][bj] - M)
                                - Math.abs(sumaLin[bi] - M)
                                + Math.abs(sumaLin[bi] - mat[bi][bj] +
                                mat[ai][aj] - M);
                    }
                    if (aj != bj) {
                        scorNou = scorNou - Math.abs(sumaCol[aj] - M)
                                + Math.abs(sumaCol[aj] - mat[ai][aj] +
                                mat[bi][bj] - M)
                                - Math.abs(sumaCol[bj] - M)
                                + Math.abs(sumaCol[bj] - mat[bi][bj] +
                                mat[ai][aj] - M);
                    }
                    // Dacă a este pe D1 și b nu este.
                    if (ai == aj && bi != bj)
                        scorNou = scorNou - Math.abs(sumaD1 - M) +
                                Math.abs(sumaD1 - mat[ai][aj] +
                                        mat[bi][bj] - M);
                    // Dacă b este pe D1 și a nu este.
                    if (bi == bj && ai != aj)
                        scorNou = scorNou - Math.abs(sumaD1 - M) +
                                Math.abs(sumaD1 - mat[bi][bj] +
                                        mat[ai][aj] - M);
                    // Dacă a este pe D2 și b nu este.
                    if ((aj == n - ai - 1) && (bj != n - bi - 1))
                        scorNou = scorNou - Math.abs(sumaD2 - M) +
                                Math.abs(sumaD2 - mat[ai][aj] +
                                        mat[bi][bj] - M);
                    // Dacă b este pe D2 și a nu este.
                    if ((bj == n - bi - 1) && (aj != n - ai - 1))
                        scorNou = scorNou - Math.abs(sumaD2 - M) +
                                Math.abs(sumaD2 - mat[bi][bj] +
                                        mat[ai][aj] - M);

                    // Am găsit o mutare destul de bună.
                    if (scorNou > pq.peek())
                        continue;

                    if (pq.peek() != scorNou) {
                        pq.poll(); // Șterge elementul maxim.
                        pq.add(scorNou); // Adaugă-l pe ăsta.
                    }

                    // Trebuie să modific sumele.
                    if (ai != bi) {
                        sumaLin[ai] = sumaLin[ai] - mat[ai][aj] + mat[bi][bj];
                        sumaLin[bi] = sumaLin[bi] - mat[bi][bj] + mat[ai][aj];
                    }
                    if (aj != bj) {
                        sumaCol[aj] = sumaCol[aj] - mat[ai][aj] + mat[bi][bj];
                        sumaCol[bj] = sumaCol[bj] - mat[bi][bj] + mat[ai][aj];
                    }
                    if (ai == aj && bi != bj)
                        sumaD1 = sumaD1 - mat[ai][aj] + mat[bi][bj];
                    if (bi == bj && ai != aj)
                        sumaD1 = sumaD1 - mat[bi][bj] + mat[ai][aj];
                    if ((aj == n - ai - 1) && (bj != n - bi - 1))
                        sumaD2 = sumaD2 - mat[ai][aj] + mat[bi][bj];
                    if ((bj == n - bi - 1) && (aj != n - ai - 1))
                        sumaD2 = sumaD2 - mat[bi][bj] + mat[ai][aj];

                    // Fac interschimbarea.
                    tmp = mat[ai][aj];
                    mat[ai][aj] = mat[bi][bj];
                    mat[bi][bj] = tmp;

                    // Vă dacă a mai fost scorul ăsta.
                    if (scorCur == scorNou)
                        vechime++;
                    else
                        vechime = 1;

                    if (vechime > 50)
                        break forDeVechime;

                    // Și trebuie să modific și scorul curent.
                    scorCur = scorNou;

                    // Dacă scorul e zero, s-a ajuns la o soluție.
                    if (scorCur == 0) {
                        ascultator.currentState(mat);
                        ascultator.finish();
                        return;
                    }

                    break;
                }
            }
        }
    }
}
