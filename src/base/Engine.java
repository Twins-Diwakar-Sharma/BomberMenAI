package base;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import object.Blast;
import object.Bomb;
import object.FullMenu;
import object.GameObject;
import object.MenuButtons;
import object.Player;
import object.StageMatrix;

public class Engine extends Canvas implements Runnable{
	
	public static boolean aiInGame = false;
	public static boolean displayResultNow = false;
	public static int player1Score = 0;
	public static int player2Score = 0;
	
	public static int gameCount = 0;
	
	public static boolean gameOver = false;
	public static boolean resumeIt = false;
	
	public static int TIME;
	public static int FINISHTIME = 200;
	
    private int deltaTime;
    
	private boolean once = true;
	public static boolean windowShouldClose = false;
	public static boolean reset = true;
	private Thread thread ;
	public static boolean menuOn = true;
	private Renderer renderer ;
	private float fps = 60;
	private float MS_PER_UPDATE = 1000/fps;    // 1000 is in milliseconds
	private Loader loader;
	
	private ArrayList<MenuButtons> menuButtons = new ArrayList<MenuButtons>();
	
	private ArrayList<GameObject> brickList = new ArrayList<GameObject>();
	public static ArrayList<Bomb> bombList = new ArrayList<Bomb>();
	
	public static ArrayList<ArrayList<Blast>> ListsOfBlasts = new ArrayList<ArrayList<Blast>>();
	
	private boolean brickChange = true; // for startup render bricks, then render only once;
	
	private BufferedImage steal, dusty;
	public static BufferedImage speedPower, rangePower, multiPower;
	
	Player player, player2;
	FullMenu fullMenu;
	public static boolean player1Dead=false, player2Dead=false;
	
	public static boolean updateStage = false;
	
	public MenuButtons resume, newstart, defeatBot, quit;
	
	private BufferedImage player1SpriteSheet, player2SpriteSheet,  botSpriteSheet;
	public BufferedImage icon;
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()	{
		System.exit(0);
		try {

			thread.join();
		}catch(Exception e) {
			
		}
		System.exit(0);
	}
	
	
	public void init() throws Exception{
	    
		fullMenu = new FullMenu();
		
		loader = new Loader();
		
         renderer  = new Renderer();
         
         steal = loader.loadImage("/safe2.jpg");
          dusty = loader.loadImage("/break1.jpg");
          
          speedPower = loader.loadImage("/speed3.jpg");
          rangePower = loader.loadImage("/range2.jpg");
          multiPower = loader.loadImage("/multi2.jpg");
          
         BufferedImage dhmaka = loader.loadImage("/blast3.png");
         BufferedImage bomb = loader.loadImage("/bomb.png");
         icon = bomb.getSubimage(0, 0, bomb.getWidth(), bomb.getHeight() );
         
         
         BufferedImage image = loader.loadImage("/warfareSpriteSheetNelu.png");
         player1SpriteSheet = image;
         player = new Player(image,240,240,bomb,dhmaka, 1);
         player.bindKeys(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
         //player.ai = true;
         
         BufferedImage image2 = loader.loadImage("/mullaSpriteSheetHra.png");
         player2SpriteSheet = image2;
         player2 = new Player(image2, 64, 64,bomb,dhmaka, 2);
         player2.bindKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SLASH);
        // player2.ai = true;
    
         BufferedImage image3 = loader.loadImage("/robotSpriteSheetWhite.png");
         botSpriteSheet = image3;
         
     
          
          
         reset();

         resume = new MenuButtons("RESUME",0,Window.width/2-100,Window.height/2 - 150);
         menuButtons.add(resume);
         resume.disabled = true;
         
         newstart = new MenuButtons("NEW GAME",1,Window.width/2-100, Window.height/2-50 );
         menuButtons.add(newstart);
         
         defeatBot = new MenuButtons("DEFEAT BOT",3,Window.width/2-100,Window.height/2+50);
         menuButtons.add(defeatBot);
         
         quit = new MenuButtons("QUIT",2,Window.width/2-100,Window.height/2 + 150);
         menuButtons.add(quit);
         
	}
	
	private void loadStage(){
		
		for(int i=0; i<StageMatrix.size; i++) {
			for(int j=0; j<StageMatrix.size; j++) {
				switch(StageMatrix.data[i][j]) {
					case 4:
						GameObject brick = new GameObject(steal);
						brick.setPositionX(j*GameObject.sizeX); // increase in col = increase in x
						brick.setPositionY(i*GameObject.sizeY); // increase in row = increase in y
						brickList.add(brick);
						break;
					case 3:
						GameObject destructable = new GameObject(dusty);
						destructable.setPositionX(j*GameObject.sizeX);
						destructable.setPositionY(i*GameObject.sizeY);
						brickList.add(destructable);
						break;
					case -1:
						GameObject speed = new GameObject(speedPower);
						speed.setPosition(j*GameObject.sizeX, i*GameObject.sizeY);
						brickList.add(speed);
						break;
					case -2:	
						GameObject range = new GameObject(rangePower);
						range.setPosition(j*GameObject.sizeX, i*GameObject.sizeY);
						brickList.add(range);
						break;
					case -3:
						GameObject mult = new GameObject(multiPower);
						mult.setPosition(j*GameObject.sizeX, i*GameObject.sizeY);
						brickList.add(mult);
						break;
					default:
						break;
				}
			}
		}
		
		
	}
	
	public void reload() {
		brickList.clear();
		loadStage();

	}
	
	
	public void update() {

		
		if(resumeIt)
			resume.disabled = false;
		
		if(menuOn) {

			fullMenu.update(menuButtons);
		}else {
			
			
			if(reset) {
				reset = false;
				reset();
			}
			
		    player.setDead(player1Dead);
		    player2.setDead(player2Dead);
		
		    if( (player1Dead || player2Dead || (Engine.FINISHTIME - Engine.TIME) <= 0 ) && !gameOver ) {

		    	Engine.TIME = Engine.FINISHTIME - 11;
		    	gameOver = true;
		    	resume.disabled = true;
		    	resumeIt = false;
		    	if(player1Dead)player2Score++;
		    	if(player2Dead)player1Score++;
		    	gameCount++;
		    }
		    	
		    if(gameOver && Engine.TIME > Engine.FINISHTIME) {
		    	Engine.menuOn = false;
				Engine.reset = true;
			    Engine.resumeIt = true;
			    Engine.displayResultNow = false;
		    }
		    
		    if(gameOver && Engine.FINISHTIME - Engine.TIME < 10) {
		    	displayResultNow = true;
		    }
		    
		    deltaTime++;
		    if(deltaTime > 25) {
		    	deltaTime = 0;
		    	TIME++;
		    }
		
		    if(updateStage) {
		    	updateStage = false;
		    	reload();
		    	once = true;
		    }
		
		    player.update(player2);
		    player2.update(player);
		
		
		    for(int i=0; i<bombList.size(); i++) {
		    	bombList.get(i).update(player, player2); 
		    }
		}
	}
	
	public void render(double dt) {
		BufferStrategy strategy = this.getBufferStrategy();
		if(strategy == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics gr = strategy.getDrawGraphics();
		Graphics2D g = (Graphics2D)gr;
	
		if(menuOn) {
			renderer.renderMenu(g,menuButtons);
		}else{	
			renderer.renderStage(g, brickList);
			renderer.renderPlayer(g, player);
			renderer.renderPlayer(g, player2);						
			renderer.renderBombs(g, bombList);
			renderer.renderGui(g,player,player2);
			
			if(once) {
				renderer.renderStage(g,brickList);
				once = false;
			}
		}
		
		
		g.dispose();
		strategy.show();
	}
	
	public void input() {
		
	}
	
	
	public void run() {
		try {
			
			double previous = System.currentTimeMillis();
			double lag = 0;
			while(!windowShouldClose) {
				double current = System.currentTimeMillis();
				double elapsed = current - previous;
				previous = current;
				lag += elapsed;
				
				input();
				
				while(lag >= MS_PER_UPDATE) {
					update();
					lag -= MS_PER_UPDATE;
				}
				
				render(lag/MS_PER_UPDATE);
			}
			
			stop();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Player getPlayer2() {
		return player2;
	}
	
	public FullMenu getFullMenu() {
		return fullMenu;
	}

	public void reset() {
		Engine.displayResultNow = false;
		Engine.gameOver = false;
		Engine.TIME = 0;

		StageMatrix.reset();
		if(gameCount == 1) {
			//StageMatrix.loadEvenMoreHardCoded();
			StageMatrix.loadProbabilisticLevel();
		}else {
			StageMatrix.loadProbabilisticLevel();
			//StageMatrix.loadEvenMoreHardCoded();
		}
		brickList.clear();
		player.setDead(false);
		player2.setDead(false);
	    player1Dead = false;
	    player2Dead = false;
	    player.respawn();
	    player2.respawn();
	
	    if(Engine.aiInGame) {
	    	player2.resetSpriteSheet(botSpriteSheet);
	    	player2.ai = true;
	    }else {
	    	player2.resetSpriteSheet(player2SpriteSheet);
	    	player2.ai = false;
	    }
	    
	    
	    for(int b = 0; b<bombList.size(); b++) {
		     bombList.get(b).BlastList().clear();
	    }
	    bombList.clear();
		for(int i=0; i<StageMatrix.size; i++) {
			for(int j=0; j<StageMatrix.size; j++) {
				switch(StageMatrix.data[i][j]) {
					case 4:
						GameObject brick = new GameObject(steal);
						brick.setPositionX(j*GameObject.sizeX); // increase in col = increase in x
						brick.setPositionY(i*GameObject.sizeY); // increase in row = increase in y
						brickList.add(brick);
						break;
					case 3:
						GameObject destructable = new GameObject(dusty);
						destructable.setPositionX(j*GameObject.sizeX);
						destructable.setPositionY(i*GameObject.sizeY);
						brickList.add(destructable);
						break;
					case 1:
						player.setPosition(j*GameObject.sizeX  , i*GameObject.sizeY  );
						break;
					case 2:
						player2.setPosition(j*GameObject.sizeX , i*GameObject.sizeY );
						break;	
					default:
						break;
				}
			}
		}
		player.respawn();
		player2.respawn();
		
		
		Engine.updateStage = true;
	   
	}
	

	
}
