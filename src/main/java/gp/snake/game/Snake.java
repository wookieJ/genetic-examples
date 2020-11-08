package gp.snake.game;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Snake extends JFrame {
    private static final long serialVersionUID = -2542001418764869760L;
    public static ArrayList<ArrayList<DataOfSquare>> grid;
    private int windowSize;
    private GameController gameController;
    private Random random = new Random();
    private boolean showLayout;
    private long frameNumber;
    private JLabel scoreLabel = new JLabel("Score: 0");

    public Snake(int windowSize, boolean showLayout) {
        this.windowSize = windowSize;
        this.showLayout = showLayout;
        grid = new ArrayList<>();
    }

    public void run() {
        createLayout();
        var initialX = random.nextInt(windowSize);
        var initialY = random.nextInt(windowSize);
        Tuple position = new Tuple(initialX, initialY);
        this.gameController = new GameController(position, this.windowSize, showLayout);
    }

    private void createLayout() {
        ArrayList<DataOfSquare> data;
        for (int i = 0; i < windowSize; i++) {
            data = new ArrayList<>();
            for (int j = 0; j < windowSize; j++) {
                DataOfSquare c = new DataOfSquare(2, this.showLayout);
                data.add(c);
            }
            grid.add(data);
        }
        if (grid.size() != windowSize) {
            System.out.println("ERROR! " + grid.size());
        }
        if (showLayout) {
//            scoreLabel.setMinimumSize(new Dimension(windowSize, 4));
//            scoreLabel.setPreferredSize(new Dimension(windowSize, 4));
//            scoreLabel.setMaximumSize(new Dimension(windowSize, 4));
//            scoreLabel.setSize(new Dimension(windowSize, 4));
//            scoreLabel.setBorder(new LineBorder(Color.BLACK));
//            scoreLabel.setBackground(Color.green);
            getContentPane().setLayout(new GridLayout(windowSize, windowSize, 0, 0));
//            getContentPane().add(scoreLabel, BorderLayout.PAGE_START);
            for (int i = 0; i < windowSize; i++) {
                for (int j = 0; j < windowSize; j++) {
                    getContentPane().add(grid.get(i).get(j).square, BorderLayout.CENTER);
                }
            }
        }
    }

    public void updateScore(long scoreValue) {
        scoreLabel.setText("Score: " + scoreValue);
    }

    public SnakeState nextStep(int action) {
        frameNumber++;
        SnakeControl.action(action);
        return gameController.nextStep();
    }

    public Score getScore() {
        return new Score(gameController.sizeSnake - 1, frameNumber);
    }
}
