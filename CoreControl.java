import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class CoreControl {

    public static class Grid extends JPanel {

        private List<Point> fillCells;

        public Grid() {
            fillCells = new ArrayList<>(25);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Point fillCell : fillCells) {
                int cellX = 10 + (fillCell.x * 10);
                int cellY = 10 + (fillCell.y * 10);
                g.setColor(Color.BLACK);
                g.fillRect(cellX, cellY, 10, 10);
            }
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 800, 500);

            for (int i = 10; i <= 800; i += 10) {
                g.drawLine(i, 10, i, 510);
            }

            for (int i = 10; i <= 500; i += 10) {
                g.drawLine(10, i, 810, i);
            }
        }

        public void fillCell(int x, int y) {
            fillCells.add(new Point(x, y));
            repaint();
        }

    }

    public void makeScore() {
        int[][] score = new int[50][15];
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                Grid grid = new Grid();
                JFrame window = new JFrame();
                window.setSize(840, 560);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.add(grid);
                window.setVisible(true);
                int[] ca = new int[80];
                int[] caNew = new int[80];
                for (int i = 0; i < 80; i++) {
                    ca[i] = 0;
                    caNew[i] = 0;
                }
                ca[39] = 1;
                score[0][6] = 1;
                grid.fillCell(39, 0);
                for (int i = 1; i < 50; i++) {
                    for (int j = 1; j < 79; j++) {
                        if (rules(ca[j - 1], ca[j], ca[j + 1])) {
                            if (j >= 33 && j <=44) {
                                //System.out.println("i: " + i + " j: " + j + " -> ");
                                score[i][j - 33] = 1;
                            }
                            caNew[j] = 1;
                            grid.fillCell(j, i);
                        }
                    }
                    ca = caNew;
                    caNew = new int[80];
                }
                boolean foundBlack;
                for (int i = 0; i < score.length; i++) {
                    foundBlack = false;
                    for (int j = 0; j < score[0].length; j++) {
                        if (foundBlack && score[i][j] == 1) {
                            score[i][j] = 0;
                        } else if (!foundBlack && score[i][j] == 1) {
                            foundBlack = true;
                        } else if (foundBlack && score[i][j] == 0) {
                            foundBlack = false;
                        }
                    }
                }
            }
        });
    }

    private boolean rules(int a, int b, int c) {
        if (a == 1 && b == 1 && c == 1) return false;
        else if (a == 1 && b == 1 && c == 0) return false;
        else if (a == 1 && b == 0 && c == 1) return false;
        else if (a == 1 && b == 0 && c == 0) return true;
        else if (a == 0 && b == 1 && c == 1) return true;
        else if (a == 0 && b == 1 && c == 0) return true;
        else if (a == 0 && b == 0 && c == 1) return true;
        else if (a == 0 && b == 0 && c == 0) return false;
        else return true;
    }

    public int[][] getScore() {
        int[][] score = new int[100][12];
        int[] ca = new int[1000];
        int[] caNew = new int[1000];
        for (int i = 0; i < 1000; i++) {
            ca[i] = 0;
            caNew[i] = 0;
        }
        ca[499] = 1;
        score[0][6] = 1;
        for (int i = 1; i < 100; i++) {
            for (int j = 1; j < 999; j++) {
                if (rules(ca[j - 1], ca[j], ca[j + 1])) {
                    if (j >= 493 && j <= 504) {
                        //System.out.println("i: " + i + " j: " + j + " -> ");
                        score[i][j - 493] = 1;
                    }
                    caNew[j] = 1;
                }
            }
            ca = caNew;
            caNew = new int[1000];
        }
        boolean foundBlack;
        for (int i = 0; i < score.length; i++) {
            foundBlack = false;
            for (int j = 0; j < score[0].length; j++) {
                if (foundBlack && score[i][j] == 1) {
                    score[i][j] = 0;
                } else if (!foundBlack && score[i][j] == 1) {
                    foundBlack = true;
                } else if (foundBlack && score[i][j] == 0) {
                    foundBlack = false;
                }
            }
        }
        return score;
    }
}