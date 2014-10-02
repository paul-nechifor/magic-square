package net.nechifor.magic_square;

public class Console {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Wrong args.");
            System.exit(1);
        }

        String alg = args[0];
        int order = Integer.parseInt(args[1]);
        findSquare(alg, order);
    }

    public static void findSquare(String alg, int order) {
        Listener l = new Listener() {
            private int[][] square;

            @Override
            public void currentState(int[][] square) {
                this.square = square;
            }

            @Override
            public void restart() {
            }

            @Override
            public void finish() {
                for (int i = 0; i < square.length; i++) {
                    for (int j = 0; j < square.length; j++) {
                        System.out.printf(square[i][j] + " ");
                    }
                    System.out.println();
                }
            }
        };

        Solver s = new Solver(order, l, 1);

        if (alg.equals("FIHC")) {
            s.startFirstImprovementHillClimbing();
        } else if (alg.equals("BIHC")) {
            s.startBestImprovementHillClimbing();
        } else if (alg.equals("LAHC")) {
            s.startLateAcceptanceHillClimbing();
        } else {
            System.err.println("No such algorithm.");
            System.exit(1);
        }
    }
}
