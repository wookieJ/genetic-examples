package gp.snake.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

 public class KeyboardListener extends KeyAdapter{
 	
 		public void keyPressed(KeyEvent e){
 		    switch(e.getKeyCode()){
		    	case 39:	// -> Right 
		    				//if it's not the opposite direction
		    				if(GameController.directionSnake!=2) 
		    					GameController.directionSnake=1;
		    			  	break;
		    	case 38:	// -> Top
							if(GameController.directionSnake!=4) 
								GameController.directionSnake=3;
		    				break;
		    				
		    	case 37: 	// -> Left 
							if(GameController.directionSnake!=1)
								GameController.directionSnake=2;
		    				break;
		    				
		    	case 40:	// -> Bottom
							if(GameController.directionSnake!=3)
								GameController.directionSnake=4;
		    				break;
		    	
		    	default: 	break;
 		    }
 		}
 	
 }
