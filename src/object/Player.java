package object;


import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import ai.AI;
import ai.AdvancedAI;
import base.Engine;
import base.Window;

public class Player extends GameObject{
	
	
	private AI intelligence ;
	
	public boolean ai = false;
	
	public static int sizeX = 28;
	public static int sizeY = 28;
	
	private float speed = 3;
	private float botSpeed = 4;
	
	
	private float velw, vela, vels, veld;
	
	private float preX, preY;

	private int playerNo  = -5, enemyNo = -5;
	
	//private float cellX,cellY;
	
	private int centerRow, centerCol;
	
	private boolean pressDown = false, pressUp = false, pressLeft = false, pressRight = false;
	
	// key bindings
	private int keyUp, keyDown, keyLeft, keyRight, keyAction;
	
	private int bombCounter = 1;
	private boolean invoke = false;
	private int tick;
	
	private int defaultRange = 2;
	private int rangePower = defaultRange;
	
	// animation stuff
	private BufferedImage boomImage;
	private BufferedImage bombImage;
	
	private BufferedImage[][] walkFrames;
	private BufferedImage deathFrame;
	
	private BufferedImage currentFrame;
	private int frameRow=0, frameCol=0, frameIndex=0;
	private int animTime=0, animTimeLimit=100;
	
	private boolean dead = true;
	
	public BufferedImage head;
	
	private AdvancedAI advancedIntelligence;
	
	
	public Player(BufferedImage image, float x, float y,BufferedImage bombImage, BufferedImage boomImage, int no) {
		
		super(image,x,y);
		centerRow = (int)(((this.getPositionY() + Player.sizeY/2 )/Window.height)*StageMatrix.size);
		centerCol = (int)(((this.getPositionX()+ Player.sizeX/2)/Window.width)*StageMatrix.size);
		
		dead = false;
		this.boomImage = boomImage;
		this.bombImage = bombImage;
		walkFrames = new BufferedImage[2][4];
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				walkFrames[i][j] = image.getSubimage(j*Player.sizeX, i*Player.sizeY, Player.sizeX, Player.sizeY);
			}
		}
		deathFrame = image.getSubimage(0*Player.sizeX, 2*Player.sizeY, Player.sizeX, Player.sizeY);
		currentFrame = walkFrames[0][0];
		head = image.getSubimage(0, 0, 28, 18);
		playerNo = no;
	//	intelligence = new AI();
	   advancedIntelligence = new AdvancedAI();
		
		enemyNo = no%2==0?1:2;
	
	}
	
	public void bindKeys(int up, int down, int left, int right, int action) {
		keyUp = up;
		keyDown = down;
		keyLeft = left;
		keyRight = right;
		keyAction = action;
	}
	
	public void update(Player enemy) {
		//System.out.println("Player : "+playerNo+ " ::->  ( "+ centerRow+" , "+centerCol+" ) " + "data is "+StageMatrix.data[centerRow][centerCol]);
		
		preX = position.x;
		preY = position.y;
		
		if(ai) {
			speed = botSpeed;
			this.advancedIntelligence.update(this, enemy);
		//	this.intelligence.update(this,enemy);
		}

		
		if(dead) {
			currentFrame = deathFrame;
			return;
		}
		
		if( Engine.gameOver)
			return;
		

		
		
		updateFrame();
		
		
		// Here limiting no. of bombs one can place at a time. Player can only put new bomb if lifeTime of previous bomb expires ie timer is increased by bomblife time 
		// If time comes, we increase bombCounter. This allows us to put bomb again as bomb can only be put if bombCuonter > 0		
		if(invoke) {
			if(tick == Engine.TIME) {
				invoke = false;
				bombCounter++;
			}
		}
		
		StageMatrix.data[centerRow][centerCol] = 0;
		if(pressDown || pressUp || pressLeft || pressRight) {
		
			
			if(pressDown){
				detectCollisionDown(StageMatrix.data,position.y + speed, enemy );
			}
			if(pressUp) {
				detectCollisionUp(StageMatrix.data,position.y - speed, enemy );
			}
			if(pressLeft) {
				detectCollisionLeft(StageMatrix.data,position.x - speed, enemy);
			}
			if(pressRight) {
				detectCollisionRight(StageMatrix.data,position.x + speed, enemy);
			}
			

		
    		this.translate(vela + veld, velw + vels);
		
		}
        StageMatrix.data[centerRow][centerCol] = playerNo;
		
	
	}
	
	private void detectPower(int[][] data, int centerInMatrixY, int centerInMatrixX) {
		   if(data[centerInMatrixY][centerInMatrixX] == -1) { // speed power
			   data[centerInMatrixY][centerInMatrixX] = 0;
			   Engine.updateStage = true;
			   this.speed = 5;
			   if(ai)
				   this.speed = 6;
		   }else if(data[centerInMatrixY][centerInMatrixX] == -2) { // range power
			   data[centerInMatrixY][centerInMatrixX] = 0;
			   Engine.updateStage = true;
			   this.rangePower = 4;
		   }else if(data[centerInMatrixY][centerInMatrixX] == -3) { // multi Bomb
			   data[centerInMatrixY][centerInMatrixX] = 0;
			   Engine.updateStage = true;
			   this.bombCounter = 10;
		   }
	}
	
	private void detectCollisionDown(int[][] data, float newPosY, Player enemy) {
		int newMatrixY = (int)( ((newPosY + Player.sizeY) / Window.height  )*StageMatrix.size ) ;
		int oldMatrixXLeft = (int)( ((preX) / Window.width  )*StageMatrix.size ) ;
		int oldMatrixXRight = (int)( ((preX + Player.sizeX) / Window.width  )*StageMatrix.size ) ;
		if(data[newMatrixY][oldMatrixXLeft]> 2 ||  data[newMatrixY][oldMatrixXRight]>2 ) { //walls
			vels = 0;
			this.position.y = newMatrixY * GameObject.sizeY - Player.sizeY - 1;
		}else { // powers
			vels = speed;
			detectPower(data,newMatrixY,oldMatrixXLeft);
			detectPower(data,newMatrixY,oldMatrixXRight);
		}
		
		
		centerRow = (int)( ((this.position.y + Player.sizeY/2  ) / Window.height)*StageMatrix.size);
      
	}
	
	private void detectCollisionUp(int[][] data, float newPosY, Player enemy) {
		int newMatrixY = (int)( ((newPosY) / Window.height  )*StageMatrix.size ) ;
		int oldMatrixXLeft = (int)( ((preX) / Window.width  )*StageMatrix.size ) ;
		int oldMatrixXRight = (int)( ((preX + Player.sizeX) / Window.width  )*StageMatrix.size ) ;
		if(data[newMatrixY][oldMatrixXLeft]>2 ||  data[newMatrixY][oldMatrixXRight]>2 ) {
				velw = 0;
				this.position.y = newMatrixY * GameObject.sizeY + GameObject.sizeY + 1;
			}
		else { // powers
			velw = -speed;
			detectPower(data,newMatrixY,oldMatrixXLeft);
			detectPower(data,newMatrixY,oldMatrixXRight);
		}
		centerRow = (int)( ((this.position.y + Player.sizeY/2  ) / Window.height)*StageMatrix.size);
	}
	
	private void detectCollisionLeft(int[][] data, float newPosX, Player enemy) {
		int newMatrixX = (int)( ((newPosX) / Window.width  )*StageMatrix.size ) ;
		int oldMatrixYUp = (int)( ((preY) / Window.height )*StageMatrix.size ) ;
		int oldMatrixYDown = (int)( ((preY + Player.sizeY) / Window.height  )*StageMatrix.size ) ;
		if(data[oldMatrixYUp][newMatrixX]>2 ||  data[oldMatrixYDown][newMatrixX]>2 ) {
			vela = 0;
			this.position.x  = newMatrixX * GameObject.sizeX + GameObject.sizeX + 1;
		}else { // powers
			vela = -speed;
			detectPower(data,oldMatrixYUp,newMatrixX);
			detectPower(data,oldMatrixYDown,newMatrixX);
		}
		centerCol = (int)( ((this.position.x + Player.sizeX/2  ) / Window.width)*StageMatrix.size);
	}
	
	private void detectCollisionRight(int[][] data, float newPosX, Player enemy) {
		int newMatrixX = (int)( ((newPosX+Player.sizeX) / Window.width  )*StageMatrix.size ) ;
		int oldMatrixYUp = (int)( ((preY) / Window.height )*StageMatrix.size ) ;
		int oldMatrixYDown = (int)( ((preY + Player.sizeY) / Window.height  )*StageMatrix.size ) ;
		if(data[oldMatrixYUp][newMatrixX]>2 ||  data[oldMatrixYDown][newMatrixX]>2) {
			veld = 0;
			this.position.x = newMatrixX * GameObject.sizeX - Player.sizeX - 1;
		}else { // powers
			veld = speed;
			detectPower(data,oldMatrixYUp,newMatrixX);
			detectPower(data,oldMatrixYDown,newMatrixX);
		}
		centerCol = (int)( ((this.position.x + Player.sizeX/2  ) / Window.width)*StageMatrix.size);
	}
	
	
    
	
	public void keyPressed(KeyEvent e) {
        if(ai)
        	return;
		
		int k = e.getKeyCode();
		if(k == KeyEvent.VK_ESCAPE) {
			Engine.menuOn = true;
		}
		
		if(k == keyUp) {
			pressUp = true;
		}else if(k == keyDown) {
			pressDown = true;
		}else if(k == keyRight) {
			pressRight = true;
		}else if(k == keyLeft) {
			pressLeft = true;
	    }else if(k == keyAction && !Engine.gameOver) {
            plantBomb();
	    }
	}
	

		
		public void keyReleased(KeyEvent e) {
			if(ai)
				return;
				
			int k = e.getKeyCode();
			if(k == keyUp) {
				velw = 0;
				pressUp = false;
			}else if(k == keyDown) {
				vels = 0;
				pressDown = false;
			}else if(k == keyLeft) {
				vela = 0;
				pressLeft = false;
			}else if(k == keyRight) {
				veld = 0;
				pressRight = false;
			}
			
		
		}
		
		
		public void plantBomb() {
	    	if(bombCounter > 0) {									// bomb cab only be placed if bombCounter > 0    (this will help in case of powers we increase bombCount)
	    		bombCounter--;										// decrease bombCounter so it becomes 0 and no bomb can be placed until its life time expires
	    		invoke = true;
	    		tick = Engine.TIME + Bomb.lifeTime;
	    		addBomb(bombImage,boomImage);
	    	}
		}
		
		
		public void addBomb(BufferedImage selfImage, BufferedImage boomImage){
			
			     Bomb b = new Bomb(selfImage,centerCol*Window.width/StageMatrix.size,centerRow*Window.height/StageMatrix.size,rangePower,boomImage);
				Engine.bombList.add(b);
		}

	public float getPreX() {
		return preX;
	}
	
	public float getPreY() {
		return preY;
	}
	
	
	
	private void updateFrame() {
		animTime+= speed*2;
		if(animTime >= animTimeLimit) {
			animTime = 0;
			if(pressUp | pressDown | pressLeft | pressRight) {
				frameIndex = (frameIndex+1)%2;
			}
            nextFrame();
		}
	}
	
	private void nextFrame() {
		
		if(pressLeft) {
		    frameRow = 1;
		    frameCol = 0;
		}
		if(pressRight) {
			frameRow = 1;
			frameCol = 2;
		}
		if(pressUp) {
			frameRow = 0;
			frameCol = 2;
		}
		if(pressDown) {
			frameRow = 0;
			frameCol = 0;
		}
		currentFrame = walkFrames[frameRow][frameCol+frameIndex];
	}

	public BufferedImage getImage() {
		return currentFrame;
	}
	
	public void setDead(boolean death) {
		this.dead = death;
	}
	
	public boolean isDead() {
		return dead;
	}
	public void respawn() {
		centerRow = (int)(((this.getPositionY() + Player.sizeY/2 )/Window.height)*StageMatrix.size);
		centerCol = (int)(((this.getPositionX()+ Player.sizeX/2)/Window.width)*StageMatrix.size);
		dead = false;
		speed = 3;
		if(ai)
			speed = botSpeed;
		rangePower = defaultRange;
		bombCounter = 1;
		this.setPressDown(false); this.setPressLeft(false); this.setPressRight(false); this.setPressUp(false);
		//this.intelligence.resetInvoke();
		this.advancedIntelligence.resetAI(this);
	}

	public boolean isPressDown() {
		return pressDown;
	}

	public void setPressDown(boolean pressDown) {
		this.pressDown = pressDown;
		if(pressDown == false)
			vels = 0;
	}

	public boolean isPressUp() {
		return pressUp;
	}

	public void setPressUp(boolean pressUp) {
		this.pressUp = pressUp;
		if(pressUp == false)
			velw = 0;
	}

	public boolean isPressLeft() {
		return pressLeft;
	}

	public void setPressLeft(boolean pressLeft) {
		this.pressLeft = pressLeft;
		if(pressLeft == false)
			vela = 0;
	}

	public boolean isPressRight() {
		return pressRight;
	}

	public void setPressRight(boolean pressRight) {
		this.pressRight = pressRight;
		if(pressRight == false)
			veld = 0;
	}
	
	
	public float getVela() {
		return vela;
	}
	
	public float getVeld() {
		return veld;
	}
	
	public float getVelw() {
		return velw;
	}
	
	public float getVels() {
		return vels;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	
	public void resetSpriteSheet(BufferedImage image) {
		this.boomImage = boomImage;
		this.bombImage = bombImage;
		walkFrames = new BufferedImage[2][4];
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				walkFrames[i][j] = image.getSubimage(j*Player.sizeX, i*Player.sizeY, Player.sizeX, Player.sizeY);
			}
		}
		deathFrame = image.getSubimage(0*Player.sizeX, 2*Player.sizeY, Player.sizeX, Player.sizeY);
		currentFrame = walkFrames[0][0];
		head = image.getSubimage(0, 0, 28, 18);
		
	}
	
	
	public int getCenterRow() {
		return this.centerRow;
	}
	
	public int getCenterCol() {
		return this.centerCol;
	}
	
	public void setCenterRow(int row) {
		centerRow = row;
	}
	public void setCenterCol(int col) {
		centerCol = col;
	}

	public boolean centerCollidesWith(int row, int col) {
		if(row == centerRow && col == centerCol)
			return true;
		else
			return false;
	}
	
	public void stop() {
		setPressDown(false); setPressUp(false); setPressLeft(false); setPressRight(false);
	}
	
	public int getNumber() {
		return playerNo;
	}
	
	public int getEnemyNumber() {
		return enemyNo;
	}
}
