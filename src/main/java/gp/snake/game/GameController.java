package gp.snake.game;

import java.util.ArrayList;


public class GameController extends Thread {
    ArrayList<ArrayList<DataOfSquare>> squares;
    Tuple headSnakePos;
    int sizeSnake = 3;
    public static int directionSnake;
    public static int lastDirNumber;
    public static int lastDir;
    boolean changeLayout;
    int windowSize;

    ArrayList<Tuple> positions = new ArrayList<>();
    Tuple foodPosition;

    GameController(Tuple positionDepart, int windowSize, boolean changeLayout) {
        this.changeLayout = changeLayout;
        this.windowSize = windowSize;
        squares = Snake.grid;

        headSnakePos = new Tuple(positionDepart.x, positionDepart.y);
        directionSnake = (int) (Math.random() * 3) + 1;

        Tuple headPos = new Tuple(headSnakePos.getX(), headSnakePos.getY());
        positions.add(headPos);

        foodPosition = getValAreaNotInSnake();
        while (nextRandom()) {
            foodPosition = getValAreaNotInSnake();
        }
        spawnFood(foodPosition);
    }

    private boolean nextRandom() {
        if (directionSnake == 3 || directionSnake == 4) {
            return foodPosition.y == headSnakePos.x;
        } else return foodPosition.x == headSnakePos.y;
    }

    public SnakeState nextStep() {
//        if (lastDirNumber > 40) {
//            lastDirNumber = 0;
//            return null;
//        }
        moveInterne(directionSnake);
        if (collision()) {
            return null;
        }
        moveExterne();
        deleteTail();
        return getSnakeState();
    }

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

    private void spawnFood(Tuple foodPositionIn) {
        try {
            squares.get(foodPositionIn.x).get(foodPositionIn.y).lightMeUp(1, this.changeLayout);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

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

    private void moveExterne() {
        for (Tuple t : positions) {
            int y = t.getX();
            int x = t.getY();
            squares.get(x).get(y).lightMeUp(0, this.changeLayout);
        }
    }

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
        return mapRange(0, 28.28, 0, 1, Math.sqrt((foodPosition.y - headSnakePos.x) * (foodPosition.y - headSnakePos.x) +
            (foodPosition.x - headSnakePos.y) * (foodPosition.x - headSnakePos.y)));
//        return Math.sqrt((foodPosition.y - headSnakePos.x) * (foodPosition.y - headSnakePos.x) +
//            (foodPosition.x - headSnakePos.y) * (foodPosition.x - headSnakePos.y));
    }

    private double getFoodAngle(Tuple foodPosition) {
        double angle;
        if (directionSnake == 3) { // top
            var x1 = foodPosition.y - headSnakePos.x;
            var x2 = headSnakePos.y - foodPosition.x;
            angle = Math.toDegrees(Math.atan2(x1, x2));
        } else if (directionSnake == 4) { // bottom
            var x1 = headSnakePos.x - foodPosition.y;
            var x2 = foodPosition.x - headSnakePos.y;
            angle = Math.toDegrees(Math.atan2(x1, x2));
        } else if (directionSnake == 1) { // right
            var x1 = foodPosition.x - headSnakePos.y;
            var x2 = foodPosition.y - headSnakePos.x;
            angle = Math.toDegrees(Math.atan2(x1, x2));
        } else { // left
            var x1 = headSnakePos.y - foodPosition.x;
            var x2 = headSnakePos.x - foodPosition.y;
            angle = Math.toDegrees(Math.atan2(x1, x2));
        }
        return mapRange(-180, 180, -1, 1, angle);
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
        return !(color == 0) ? 0.0 : 1.0;
    }
}
