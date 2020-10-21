package gp.snake.game;

public class SnakeControl {
    //1:right 2:left 3:top 4:bottom 0:nothing
    public static void action(int action) {
        switch (action) {
            case 1:    // -> Right 
                //if it's not the opposite direction
                if (GameController.directionSnake != 2)
                    GameController.directionSnake = 1;
                break;
            case 2:    // -> Left 
                if (GameController.directionSnake != 1)
                    GameController.directionSnake = 2;
                break;
            case 3:    // -> Top
                if (GameController.directionSnake != 4)
                    GameController.directionSnake = 3;
                break;
            case 4:    // -> Bottom
                if (GameController.directionSnake != 3)
                    GameController.directionSnake = 4;
                break;
            default:
                break;
        }
    }

}
