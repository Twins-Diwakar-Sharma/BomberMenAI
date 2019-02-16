package object;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import base.Engine;
import base.Window;

public class Bomb extends GameObject{

	private int tick = 0;
	private int tock = 0;
	public static int lifeTime = 2;
	private int blastDuration = 1;
	private boolean destroy = false;
	private boolean blastEnd = false;
	
	private int solidBrickNo = 4, player1no=1, player2no=2, breakbrickNo = 3;
	private int range;
	
	private ArrayList<Blast> blasts = new ArrayList<Blast>();
	private boolean once = true;
	
	private BufferedImage dhmaka;
	
	private int myRow, myCol;
	private boolean getOffMyBack = false;
	
	public Bomb(BufferedImage image, float x, float y, int range, BufferedImage boomImage) {
		super(image, x, y);
		tick = Engine.TIME+lifeTime;
		tock = tick+blastDuration;
		this.range = range;
	    dhmaka = boomImage;
	    myRow = (int)((y/Window.height)*StageMatrix.size);
	    myCol = (int)((x/Window.width)*StageMatrix.size);
	}
	
	public void update(Player p1, Player p2) {
	    if( !(StageMatrix.data[myRow][myCol]==1 || StageMatrix.data[myRow][myCol] == 2) && !getOffMyBack ) {
	    	getOffMyBack = true;
	    	StageMatrix.data[myRow][myCol] = 5;
	    }
		
		
		if(tock == Engine.TIME) {
			blastEnd = true;		
			StageMatrix.data[myRow][myCol] = 0; 
		}
		if(tick == Engine.TIME && once) {
						once = false;
			blasters();
			destroy = true;
			
		}
		if(!blasts.isEmpty()) {
			for(int i=0; i<blasts.size(); i++) {
				blasts.get(i).update(p1, p2);
			}
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setDestroy(boolean b) {
		destroy = b;
	}
	
	public boolean getDestroy() {
		return destroy;
	}
	
	public boolean getBlastEnd() {
		return blastEnd;
	}
	
	
	private void blasters() {
		
		int cellCol = (int)( ( position.x / Window.width) * StageMatrix.size);
		int cellRow = (int)( ( position.y / Window.height) *  StageMatrix.size);
		
		
				for(int i=0; i<range; i++) {		//right
					if(cellCol+i< StageMatrix.size) {
						int xxx = matrixAndProb(cellRow, cellCol+i);
							if(xxx < 0)
								break;
					}
				}
							
				for(int i=0; i<range; i++) {		//left
					if(cellCol-i > 0) {
						int xxx = matrixAndProb(cellRow, cellCol-i);
							if(xxx < 0)
								break;
					}
				}
							
		
				for(int i=0; i<range; i++) {		//down
					if(cellRow+i < StageMatrix.size) {
						int xxx = matrixAndProb(cellRow+i, cellCol);
							if(xxx < 0)
								break;
					}
				}
		
				
			
				for(int i=0; i<range; i++) {		//up
					if(cellRow-i > 0) {
						int xxx = matrixAndProb(cellRow-i, cellCol);
							if(xxx < 0)
								break;
					}
				}
		
			
			
			
	}

	
	
	private int matrixAndProb(int row, int col) {
		
		int no = StageMatrix.data[row][col];
		
		if(no != solidBrickNo) {
			boom(row,col);
			if(no == player1no) {
				Engine.player1Dead = true;
			}
			if(no == player2no) {
				Engine.player2Dead = true;
			}
			if(no == breakbrickNo) {
				int mat = 0;
		
				double rnd = Math.random();
				
				
				if(rnd < 0.3) { 		// chance of powers
					
					rnd = Math.random();
					
					if(rnd < 0.7) {			// conditional probabilty of range
						mat = -2;
					}else {
						
						rnd = Math.random();
						if(rnd < 0.5) {
							mat = -1;
						}
						else {
							mat = -3;
						}
					}
				
				
				}
				
				
				StageMatrix.data[row][col] = mat;
				
				
			}
			
			return 400;
		}else{
			return -100;
		}
		
	}
	

	
	private void boom(int row, int col) {
		  float posCellX = col * Window.width / StageMatrix.size;
		  float posCellY = row * Window.height / StageMatrix.size;
		  Blast blast = new Blast(dhmaka, posCellX, posCellY, row, col);
		  blasts.add(blast);
	}
	
	public ArrayList<Blast> BlastList(){
		return blasts;
	}
	
}
