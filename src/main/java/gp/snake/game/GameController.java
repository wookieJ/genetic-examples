package gp.snake.game;

import java.util.ArrayList;


//Controls all the game logic .. most important class in this project.
public class GameController extends Thread {
    ArrayList<ArrayList<DataOfSquare>> squares = new ArrayList<ArrayList<DataOfSquare>>();
    Tuple headSnakePos;
    int sizeSnake = 3;
    long speed = 50;
    public static int directionSnake;
    boolean changeLayout;
    int windowSize;

    ArrayList<Tuple> positions = new ArrayList<Tuple>();
    Tuple foodPosition;

    GameController(Tuple positionDepart, int windowSize, boolean changeLayout) {
        this.changeLayout = changeLayout;
        this.windowSize = windowSize;
        squares = Snake.grid;

        headSnakePos = new Tuple(positionDepart.x, positionDepart.y);
        directionSnake = (int) (Math.random() * 3) + 1;

        Tuple headPos = new Tuple(headSnakePos.getX(), headSnakePos.getY());
        positions.add(headPos);

        foodPosition = new Tuple((int) (Math.random() * windowSize - 1), (int) (Math.random() * windowSize - 1));
//        foodPosition = new Tuple(1, 2);
        spawnFood(foodPosition);
    }

    public SnakeState runAuto() {
        moveInterne(directionSnake);
        if (collision()) {
            return null;
        }
        moveExterne();
        deleteTail();
        pauser();
        return getSnakeState();
    }

    public SnakeState nextStep() {
        moveInterne(directionSnake);
        if (collision()) {
            return null;
        }
        moveExterne();
        deleteTail();
        return getSnakeState();
    }

    //delay between each move of the snake
    private void pauser() {
        try {
            sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Checking if the snake bites itself or is eating
    private boolean collision() {
        Tuple posCritique = positions.get(positions.size() - 1);
        for (int i = 0; i <= positions.size() - 2; i++) {
            boolean biteItself = posCritique.getX() == positions.get(i).getX() && posCritique.getY() == positions.get(i).getY();
            if (biteItself) {
                return true;
            }
        }

        boolean eatingFood = posCritique.getX() == foodPosition.y && posCritique.getY() == foodPosition.x;
        if (eatingFood) {
            sizeSnake = sizeSnake + 1;
            foodPosition = getValAreaNotInSnake();
            spawnFood(foodPosition);
        }
        return false;
    }

    //Put food in a position and displays it
    private void spawnFood(Tuple foodPositionIn) {
        try {
            squares.get(foodPositionIn.x).get(foodPositionIn.y).lightMeUp(1, this.changeLayout);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //return a position not occupied by the snake
    private Tuple getValAreaNotInSnake() {
        Tuple p;
        int ranX = (int) (Math.random() * windowSize - 1);
        int ranY = (int) (Math.random() * windowSize - 1);
        p = new Tuple(ranX, ranY);
        for (int i = 0; i <= positions.size() - 1; i++) {
            if (p.getY() == positions.get(i).getX() && p.getX() == positions.get(i).getY()) {
                ranX = (int) (Math.random() * windowSize - 1);
                ranY = (int) (Math.random() * windowSize - 1);
                p = new Tuple(ranX, ranY);
                i = 0;
            }
        }
        return p;
    }

    //Moves the head of the snake and refreshes the positions in the arraylist
    //1:right 2:left 3:top 4:bottom 0:nothing
    private void moveInterne(int dir) {
        switch (dir) {
            case 4:
                headSnakePos.ChangeData(headSnakePos.x, (headSnakePos.y + 1) % windowSize);
                positions.add(new Tuple(headSnakePos.x, headSnakePos.y));
                break;
            case 3:
                if (headSnakePos.y - 1 < 0) {
                    headSnakePos.ChangeData(headSnakePos.x, windowSize - 1);
                } else {
                    headSnakePos.ChangeData(headSnakePos.x, Math.abs(headSnakePos.y - 1) % windowSize);
                }
                positions.add(new Tuple(headSnakePos.x, headSnakePos.y));
                break;
            case 2:
                if (headSnakePos.x - 1 < 0) {
                    headSnakePos.ChangeData(windowSize - 1, headSnakePos.y);
                } else {
                    headSnakePos.ChangeData(Math.abs(headSnakePos.x - 1) % windowSize, headSnakePos.y);
                }
                positions.add(new Tuple(headSnakePos.x, headSnakePos.y));
                break;
            case 1:
                headSnakePos.ChangeData(Math.abs(headSnakePos.x + 1) % windowSize, headSnakePos.y);
                positions.add(new Tuple(headSnakePos.x, headSnakePos.y));
                break;
        }
    }

    //Refresh the squares that needs to be 
    private void moveExterne() {
        for (Tuple t : positions) {
            int y = t.getX();
            int x = t.getY();
            squares.get(x).get(y).lightMeUp(0, this.changeLayout);
        }
    }

    //Refreshes the tail of the snake, by removing the superfluous data in positions arraylist
    //and refreshing the display of the things that is removed
    private void deleteTail() {
        int cmpt = sizeSnake;
        for (int i = positions.size() - 1; i >= 0; i--) {
            if (cmpt == 0) {
                Tuple t = positions.get(i);
                squares.get(t.y).get(t.x).lightMeUp(2, this.changeLayout);
            } else {
                cmpt--;
            }
        }
        cmpt = sizeSnake;
        for (int i = positions.size() - 1; i >= 0; i--) {
            if (cmpt == 0) {
                positions.remove(i);
            } else {
                cmpt--;
            }
        }
    }

    public SnakeState getSnakeState() {
        switch (directionSnake) {
            case 4: // botton
                return new SnakeState(
                    isEmptyCell(headSnakePos.x + 1, headSnakePos.y),
                    isEmptyCell(headSnakePos.x - 1, headSnakePos.y),
                    isEmptyCell(headSnakePos.x, headSnakePos.y + 1),
                    getFoodAngle(foodPosition),
                    getDistance(headSnakePos, foodPosition)
                );
            case 3: // top
                return new SnakeState(
                    isEmptyCell(headSnakePos.x - 1, headSnakePos.y),
                    isEmptyCell(headSnakePos.x + 1, headSnakePos.y),
                    isEmptyCell(headSnakePos.x, headSnakePos.y - 1),
                    getFoodAngle(foodPosition),
                    getDistance(headSnakePos, foodPosition)
                );
            case 2: // left
                return new SnakeState(
                    isEmptyCell(headSnakePos.x, headSnakePos.y + 1),
                    isEmptyCell(headSnakePos.x, headSnakePos.y - 1),
                    isEmptyCell(headSnakePos.x - 1, headSnakePos.y),
                    getFoodAngle(foodPosition),
                    getDistance(headSnakePos, foodPosition)
                );
            case 1: // right
                return new SnakeState(
                    isEmptyCell(headSnakePos.x, headSnakePos.y - 1),
                    isEmptyCell(headSnakePos.x, headSnakePos.y + 1),
                    isEmptyCell(headSnakePos.x + 1, headSnakePos.y),
                    getFoodAngle(foodPosition),
                    getDistance(headSnakePos, foodPosition)
                );
        }
        return null;
    }

    private double getDistance(Tuple headSnakePos, Tuple foodPosition) {
        var distance = Math.sqrt((foodPosition.y - headSnakePos.x) * (foodPosition.y - headSnakePos.x) +
            (foodPosition.x - headSnakePos.y) * (foodPosition.x - headSnakePos.y));
        return mapRange(0, 28.28, 0, 1, distance);
    }

    private double getFoodAngle(Tuple foodPosition) {
        var line1Y1 = positions.get(positions.size() - 2).y;
        var line1Y2 = positions.get(positions.size() - 1).y;
        var line1X1 = positions.get(positions.size() - 2).x;
        var line1X2 = positions.get(positions.size() - 1).x;
        var line2Y1 = line1Y1;
        var line2Y2 = foodPosition.x;
        var line2X1 = line1X1;
        var line2X2 = foodPosition.y;
        double angle1 = Math.atan2(line1Y1 - line1Y2, line1X1 - line1X2);
        double angle2 = Math.atan2(line2Y1 - line2Y2, line2X1 - line2X2);
        var angle = Math.toDegrees(angle1 - angle2);
        if (angle < 0) {
            angle *= (-1);
        }
        return mapRange(0, 360, 0, 1, angle);
    }

    public static double mapRange(double a1, double a2, double b1, double b2, double s) {
        return b1 + ((s - a1) * (b2 - b1)) / (a2 - a1);
    }

    private double isEmptyCell(int x, int y) {
        if (x < 0) {
            x = windowSize - 1;
        } else if (x > windowSize - 1) {
            x = 0;
        }
        if (y < 0) {
            y = windowSize - 1;
        } else if (y > windowSize - 1) {
            y = 0;
        }
        var color = squares.get(y).get(x).color;
        return !(color == 0) ? 1.0 : 0.0;
    }
}
