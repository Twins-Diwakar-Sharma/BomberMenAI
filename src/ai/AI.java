package ai;

import base.Engine;
import base.Window;
import object.Player;

public class AI {
    
	private int thinkTime=75;
	
	private int nextInvoke=0;
	
	private boolean letsPlantBomb = false;
	
	public  void update(Player me,Player enemy) {
		  
		 nextInvoke = (++nextInvoke)%thinkTime;
		 
          if(nextInvoke == 1) {
        	  stop(me);
        	  chooseDirection(me);
          }
	
	
	}
	
	private void stop(Player me) {
		me.setPressDown(false);
		me.setPressLeft(false);
		me.setPressRight(false);
		me.setPressUp(false);
		double shouldIPlant = Math.random();
		if(shouldIPlant > 0.7 && !Engine.gameOver) {
			letsPlantBomb = true;
			me.plantBomb();
		}else {
			letsPlantBomb = false;
		}
	}
	
	private void chooseDirection(Player me) {

		
		int direction = (int)(Math.random()*4) ;
        switch(direction) {
        case 0: // up
        	me.setPressUp(true);
        	System.out.println("bot decided to go up");
        	if(letsPlantBomb) {
        		me.setPressRight(true);
        		System.out.println(".... as well as right");
        	}
        	break;
        case 1: // down
        	me.setPressDown(true);
        	System.out.println("bot decided to go down");
        	if(letsPlantBomb) {
        		me.setPressLeft(true);
        		System.out.println(".... as well as left");
        	}
        	break;
        case 2: // left
        	me.setPressLeft(true);
        	System.out.println("bot decided to go left");
        	if(letsPlantBomb) {
        		me.setPressUp(true);
        		System.out.println(".... as well as up");
        	}
        	break;
        case 3: // right
        	me.setPressRight(true);
        	System.out.println("bot decided to go right");
        	if(letsPlantBomb) {
        		me.setPressDown(true);
        		System.out.println(".... as well as down");
        	}
        	break;
        }
	}
	 
	public void resetInvoke() {
		nextInvoke = 0;
	}

}
