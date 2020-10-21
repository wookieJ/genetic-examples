package gp.snake.game;

import java.awt.*;
import java.util.ArrayList;

public class DataOfSquare {
    //ArrayList that'll contain the colors
    ArrayList<Color> C = new ArrayList<Color>();
    int color; //0: snake , 1: food, 2:empty 
    SquarePanel square;

    public DataOfSquare(int col, boolean changeLayout) {
        //Lets add the color to the arrayList
        C.add(Color.darkGray);//0
        C.add(Color.BLUE);    //1
        C.add(Color.white);   //2
        color = col;
        if (changeLayout) {
            square = new SquarePanel(C.get(color));
        }
    }

    public void lightMeUp(int c, boolean changeLayout) {
        if (changeLayout) {
            square.ChangeColor(C.get(c));
        }
        this.color = c;
    }
}
