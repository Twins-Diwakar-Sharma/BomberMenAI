package ai;

import base.Engine;
import base.Window;
import object.GameObject;
import object.Player;
import object.StageMatrix;

public class AdvancedAI{

	private int maxDepth = 4;
	private int traverseValue = 999999;
	private int bestPath = 0;
	
   private int moveTime= 50*2;
   private int currentTime = 52*2;
   
   private int moveToRow = 0;
   private int moveToCol = 0;
   private boolean iReached = true;
   
   private int powerDepth=1;
   private int powerCurrent = 1;
   private int prevDir = 0;
   private int currDir = 0;
   private int path = 0;
   
   private boolean localReached = true;
   private int trapTimeLimit = 200;
   private int timeElapsed = 0;
   
   private int runFromBombTimeLimit = 25*2;
   
   private boolean deadlock = false;
   
   private int bombRow, bombCol;
   
   private float speedRange = -1;
   private boolean diagonal = false;
   
   private boolean bombAround = false;
   
   private int[] vicinity = new int[9];
   private boolean runThink = false;
   private int runTimeLimit = 50;
   private int currentRunTime = 0;
   private int runDir=0;
   
   private int runFromBombTime;

   private boolean decidedToBreak = false;
   private boolean kill = false;
   
   public AdvancedAI() {
	   for(int i=0; i<maxDepth-1; i++) {
		   powerDepth*=10;
	   }
   }
   
	
	public void update(Player me , Player enemy) {
		if(iReached) {
			moveToRow = me.getCenterRow();
			moveToCol = me.getCenterCol();
		//	System.out.println("current position : ( "+moveToRow+" , "+moveToCol+" ) ");
			traverseValue = 9999999;
			bestPath = 0;
			findPath(enemy, 0,0,0, me.getCenterRow(), me.getCenterCol());
			path = bestPath; // use bestPath directly if everything fine
	//		System.out.println("path to traverse ::  "+path);
     		iReached = false;
     		powerCurrent = powerDepth;
     		timeElapsed = 0;
		}else {
			//if(!bombAround)
                traverse(me);
			//else
				//runAwayFromBomb(me);
		}
	}
	

	
	private void traverse(Player me) {
		if(path==0) {
			timeElapsed = 0;
	        me.stop();
			powerCurrent = powerDepth;
			iReached = true;
		//	System.out.println(" path " + bestPath + " is fully traversed ");
			return;
		}else if(localReached){ // not reached final but reached next local
			localReached = false;
			currDir = path/powerCurrent;
		//	System.out.println(":::: Direction chosen ::::: "+currDir);
			path = path%powerCurrent;
			powerCurrent = powerCurrent/10;
			switch(currDir) {
			 case 0:
	    		 break;
	    	 case 1: //up
	    		 moveToRow -= 1;
	    		 break;
	    	 case 2: // right
	    		 moveToCol += 1;
	    		 break;
	    	 case 3: // down
	    		 moveToRow += 1;
	    		 break;
	    	 case 4: // left
	    		 moveToCol -= 1;
	    		break;				
			}
		//	System.out.println("I have to reach : ( "+moveToRow+" , "+moveToCol+" ) ");
		}else if(!localReached ) {
			if( Math.abs(me.getPositionY() - moveToRow*GameObject.sizeY) <= me.getSpeed()  && Math.abs(me.getPositionX() - moveToCol*GameObject.sizeX) <= me.getSpeed()) {
			//	System.out.println("I reached at  : ( "+moveToRow+" , "+moveToCol+" ) ");
				me.setPosition(moveToCol*Window.width/StageMatrix.size + (GameObject.sizeX - Player.sizeX)/2, moveToRow*Window.height/StageMatrix.size + (GameObject.sizeY - Player.sizeY)/2);
				//Engine.updateStage = true;
				localReached = true;
				me.stop();
       		}else {
				timeElapsed++;
				if(timeElapsed > trapTimeLimit) {
                   timeElapsed = 0;
                   System.out.println("!!!!!!!!!!TRAPED!!!!!!!!!!!!");
                   localReached = true;
                   me.stop();
                  // me.setPosition(me.getCenterCol()*Window.width/StageMatrix.size, me.getCenterRow()*Window.height/StageMatrix.size);
                   path = 0;
                   iReached = true;
				}else {
				//	System.out.println("Going towards " + currDir);
					if(detectBomb(bombRow,bombCol)) {
						//runThink = true;
					    System.out.println(" Alert!! BOMB DETECTED ! .. restart rethinking ");
					    //bombAround = true;
					    path = 0;
					    iReached = true;
					    localReached = true;
					    me.stop();
					   
					    timeElapsed = 0;
					    currDir = 0;
					  //  return;
					}else if(kill) {
						if(runFromBombTime <= 0) {
							me.stop();
							me.plantBomb();
						}
						runFromBombTime++;
						moveAway(me);
						if(runFromBombTime >= runFromBombTimeLimit) {
							kill = false;
							path = 0;
							iReached = true;
							localReached = true;
							timeElapsed = 0;
							currDir = 0;
							runFromBombTime = 0;
							me.setPosition(me.getCenterCol()*Window.width/StageMatrix.size + (GameObject.sizeX - Player.sizeX)/2, me.getCenterRow()*Window.height/StageMatrix.size + (GameObject.sizeY - Player.sizeY)/2);
						   me.stop();
						}
					 }
					else if(decidedToBreak) {
						me.stop();
						me.plantBomb();
						 
						decidedToBreak = false;
					    path = 0;
					    iReached = true;
					    localReached = true;
					    timeElapsed = 0;
					    currDir = 0;
					}
					else {
		               moveToDir(me);
					}
				}
			}
		}
	}
	
	private void moveToDir(Player me) {
		if(currDir == 0) { // do nothing
			me.setPressDown(false); me.setPressLeft(false); me.setPressRight(false); me.setPressUp(false);		
		}
		if(currDir == 1) {// go up
			me.setPressUp(true);
		}
		if(currDir == 2) { // go right
			me.setPressRight(true);
		}
		if(currDir == 3) {// go down
			me.setPressDown(true);
		}
		if(currDir == 4) { // go left
			me.setPressLeft(true);
		}
		
	}
    
	private void moveAway(Player me) {
		int row = me.getCenterRow();
		int col = me.getCenterCol();
		
		
		if(StageMatrix.data[row-1][col-1] != 3 && StageMatrix.data[row-1][col-1] != 4 && StageMatrix.data[row-1][col-1] != 5) {
			me.setPressUp(true);
			me.setPressLeft(true);
		}
		else if(StageMatrix.data[row-1][col+1] != 3 && StageMatrix.data[row-1][col+1] !=4 && StageMatrix.data[row-1][col+1] !=5) {
			me.setPressUp(true);
			me.setPressRight(true);
		}
		else if(StageMatrix.data[row+1][col-1] !=3 && StageMatrix.data[row+1][col-1] !=4 && StageMatrix.data[row+1][col-1] !=5) {
			me.setPressDown(true);
			me.setPressLeft(true);
		}
		else if(StageMatrix.data[row+1][col+1] !=3 && StageMatrix.data[row+1][col+1] !=4 && StageMatrix.data[row+1][col+1] !=5) {
			me.setPressDown(true);
			me.setPressRight(true);
		}
		
	}

	
	
	private void findPath(Player enemy, int depth, int direction, int path, int row, int col) {

		
		if(depth > maxDepth) {
			int distanceCol = enemy.getCenterCol() - col;
			int distanceRow = enemy.getCenterRow() - row;
			int distance = distanceCol*distanceCol + distanceRow*distanceRow;
			
			if( distance < traverseValue  ) {
				traverseValue = distance;
				bestPath = path;
			}
			
			return;
		}
		
		if(direction == 0 || depth == 0) {     
												// initial four iterations
			findPath(enemy,depth+1, 1, path, row, col);
			findPath(enemy,depth+1, 2, path, row, col);
			findPath(enemy,depth+1, 3, path, row, col);
			findPath(enemy,depth+1, 4, path, row, col);
			return;
		}
		
	
		switch(direction) {
		case 1:				// up
			row -= 1 ;
			break;
		case 2:			   // right
			col += 1;
			break;
		case 3:				// down
			row += 1;
			break;
		case 4:				// left
			col -= 1;
			break;
		}
		
		if(StageMatrix.data[row][col] == enemy.getNumber()) {		// Player
			if(depth <= 1) {
				kill = true;
				System.out.println("wanna kill");
			}
	
		}
		else if(StageMatrix.data[row][col] == 4) {    // Solid Bricks
			return;
		}
		else if(StageMatrix.data[row][col] == 3) {    // Breakable Bricks
			
			if(depth <= 1) {
				double rand = Math.random();
				if(rand > 0.9) {
					decidedToBreak = true;
				}
			}
			return;
		}
		else if(StageMatrix.data[row][col] == 5) {
			bombRow = row; bombCol = col;
			System.out.println("Bomb!!");
			return;		// detect bomb here xD
		}
		
		path = path*10 + direction;
		
		if(direction!=3)
			findPath(enemy,depth+1, 1, path, row, col);
		if(direction!=4)
			findPath(enemy,depth+1, 2, path, row, col);
		if(direction!=1)
			findPath(enemy,depth+1, 3, path, row, col);
		if(direction!=2)
			findPath(enemy,depth+1, 4, path, row, col);
		
	}
	
	
	private boolean detectBomb(int row, int col) {
		/*int row = me.getCenterRow();
		int col = me.getCenterCol();
		*/
		if(row-1 < 0 || row+1 > 20 || col-1<0 || col+1>20 || row < 0 || row > 20 || col <0 || col >20)
			return false;
		
		int ul=StageMatrix.data[row-1][col-1];	int u=StageMatrix.data[row-1][col];		int ur=StageMatrix.data[row-1][col+1];
		int l=StageMatrix.data[row][col-1];		int c=StageMatrix.data[row][col];		int r=StageMatrix.data[row][col+1];
		int dl=StageMatrix.data[row+1][col];	int d=StageMatrix.data[row+1][col];		int dr=StageMatrix.data[row+1][col+1];
		
		vicinity[0] = ul;  	vicinity[1] = u; 	vicinity[2] = ur;
		vicinity[3] = l;  	vicinity[4] = c; 	vicinity[5] = r;
		vicinity[6] = dl;  	vicinity[7] = d; 	vicinity[8] = dr;
		
		
		
		for(int i=0; i<9; i++) {
			if(vicinity[i] == 5) {
				return true;
			}
		}
		
		return false;
	}
	
	
	private void runAwayFromBomb(Player me) {
		 // start thinking
	//		System.out.println("Deciding where to run ......");
		if(vicinity[0] == 5 || vicinity[2] == 5 ||vicinity[6] == 5 || vicinity[8] == 5 ) {
			// stop
			runDir = 0;
		}
		else if(vicinity[3] == 5 || vicinity[5] == 5) {
			if(vicinity[1] == 4 || vicinity[1] == 3) {
				//move down
				runDir = 3;
			}
			else {
				//move up
				runDir = 1;
			}				
		}
		else if(vicinity[1] == 5 || vicinity[7] == 5) {
			if(vicinity[3] == 4 || vicinity[3] == 3) {
				//move right
				runDir = 2;
			}
			else {
				//move left
				runDir = 4;
			}				
		}
		else if(vicinity[4] == 5) {
			if(vicinity[0] == 4 || vicinity[0] == 3) {
				// move diagonal right up ie go to vicinity[2]
			    runDir = 6;
			}
			if(vicinity[2] == 4 || vicinity[2] == 3) {
				// move diagonal right down ie go to vicinity[8]
			    runDir = 7;
			}
			if(vicinity[8] == 4 || vicinity[8] == 3) {
				// move diagonal left down ie go to vicinity[6]
			   runDir = 8;
			}
			if(vicinity[6] == 4 || vicinity[6] == 3) {
				// move diagonal left up ie go to vicinity[0]
			   runDir = 5;
			}
		}
		//  System.out.println(" I chose " + runDir+ " to run away from bomb");
		
			
			// Now is the time to act !!!
			  //runto RunDIr
			//  System.out.println("###########=========== RUNNING To : "+runDir+" ============################");
			  
			  runTo(runDir,me);
		  
			
		
		// if far away detectBomb = false;
	}
	
	
	public void runTo(int i,Player me) {
		switch(i) {
		case 0:
			me.stop();
			break;
		case 1:
			me.setPressUp(true);
			break;
		case 2:
			me.setPressRight(true);
			break;
		case 3:
			me.setPressDown(true);
			break;
		case 4:
			me.setPressLeft(true);
			break;
		case 5:
			me.setPressUp(true);
			me.setPressLeft(true);
			break;
		case 6:
			me.setPressUp(true);
			me.setPressRight(true);
			break;
		case 7:
			me.setPressDown(true);
			me.setPressRight(true);
			break;
		case 8:
			me.setPressDown(true);
			me.setPressLeft(true);
			break;
		default:
		//	System.out.println("wtf value of run :: "+i);
			break;
	
			
		}
	}
	
	
	public void resetAI(Player me) {
		currDir = 0;
		moveToRow = me.getCenterRow();
		moveToCol = me.getCenterCol();
		me.stop();
		iReached = true;
		localReached = true;
		bombAround = false;
		kill =false;
		decidedToBreak = false;
		currentTime  = moveTime + 2;
		timeElapsed = 0;
		currDir = 0;
		runDir = 0;
		bestPath = 0;
		path = 0;
		traverseValue = 9999999;
		powerCurrent = powerDepth;
		
	}

}
