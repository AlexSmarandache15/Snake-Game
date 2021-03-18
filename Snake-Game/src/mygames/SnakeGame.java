package mygames;

import javax.swing.*;
/**
 * @author Alex Smarandache
 */
public class SnakeGame extends JFrame {

    public SnakeGame() {
        this.add(new GamePanel());
        this.setIconImage((new ImageIcon("icon.png")).getImage());
        this.setTitle("Snake - by Alex Smarandache");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
