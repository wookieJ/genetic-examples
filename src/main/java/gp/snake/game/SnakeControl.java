package gp.snake.game;

public class SnakeControl {
    //1:right 2:left 3:top 4:bottom 0:nothing
    public static void action(int action) {
//        GameController.lastDir = GameController.directionSnake;
//        if (Math.abs(GameController.lastDir - action) == 1) {
//            GameController.lastDirNumber = 0;
//        } else {
//            GameController.lastDirNumber++;
//        }
        switch (GameController.directionSnake) {
            case 1:    // -> Right 
                switch (action) {
                    case 1:
                        GameController.directionSnake = 4;
                        break;
                    case 2:
                        GameController.directionSnake = 3;
                        break;
                    default:
                        break;
                }
                break;
            case 2:    // -> Left 
                switch (action) {
                    case 1:
                        GameController.directionSnake = 3;
                        break;
                    case 2:
                        GameController.directionSnake = 4;
                        break;
                    default:
                        break;
                }
                break;
            case 3:    // -> Top
                switch (action) {
                    case 1:
                        GameController.directionSnake = 1;
                        break;
                    case 2:
                        GameController.directionSnake = 2;
                        break;
                    default:
                        break;
                }
                break;
            case 4:    // -> Bottom
                switch (action) {
                    case 1:
                        GameController.directionSnake = 2;
                        break;
                    case 2:
                        GameController.directionSnake = 1;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
