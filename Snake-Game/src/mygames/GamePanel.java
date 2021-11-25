package mygames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author  Alex Smarandache
 */
public class GamePanel extends JPanel implements ActionListener {

    private final int SCREEN_WIDTH = 1500;
    private final int SCREEN_HEIGHT = 750;
    private final int UNIT_SIZE = 50;
    private final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int[] x = new int[GAME_UNITS];
    private final int[] y;

    {
        y = new int[GAME_UNITS];
    }

    private int snakeLength = 5;
    private int score;
    private int appleX;
    private int appleY;
    private char direction = 'R';
    boolean isRunning = false;
    Timer timer;
    Random random;

    GamePanel() {
        score = 0;
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        generateApple();
        isRunning = true;
        int DELAY = 100;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (isRunning) {

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.green);
            g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

            for (int i = 1; i < snakeLength; i++) {
               //g.setColor(new Color(59, 155, 30));
            	g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Consoles", Font.BOLD, 45));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        } else {
            displayGameOverMessage(g);
        }

    }

    private boolean checkAvailable() {
        return IntStream.iterate(snakeLength, i -> i >= 0, i -> i - 1).noneMatch(i -> (appleX == x[i]) && (appleY == y[i]));
    }

    public void generateApple() {
        do {
            appleX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            appleY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        } while (!checkAvailable());
    }

    public void move() {

        IntStream.iterate(snakeLength, i -> i > 0, i -> i - 1).forEach(this::accept);

        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }

    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            snakeLength++;
            score++;
            generateApple();
        }
    }

    public void checkCollisions() {
        if (IntStream.iterate(snakeLength, i -> i > 0, i -> i - 1).anyMatch(i -> (x[0] == x[i]) && (y[0] == y[i]))) {
            isRunning = false;
        }

        if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            isRunning = false;
        }

        if (!isRunning) {
            timer.stop();
        }
    }

    public void displayGameOverMessage(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Console", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        g.setColor(Color.RED);
        g.setFont(new Font("Console", Font.BOLD, 100));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) >> 1, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (isRunning) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    private void accept(int i) {
        x[i] = x[i - 1];
        y[i] = y[i - 1];
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
