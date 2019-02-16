package base;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import object.Bomb;
import object.GameObject;
import object.MenuButtons;
import object.Player;

public class Renderer {
     
	Color gameBgColor = new Color(52,32,0);
	
	Color menuBgColor = new Color(191,86,1);
	Color menuFontColor = new Color(52,52,52);
	
	Color guiFontColor = Color.white;
	Color guiBgColor = Color.BLACK;
	
	Color guiResult = new Color(6,6,6);
	
	String esc = "Next round in : ";
	
	public void renderStage(Graphics2D g, ArrayList<GameObject> objectList) {
		g.setColor(gameBgColor);
		g.fillRect(0, 0, Window.width, Window.height);
		for(GameObject go : objectList) {
			g.drawImage(go.getImage(), (int)go.getPositionX(), (int)go.getPositionY(), null);
		}

	}
	
	public void renderGui(Graphics2D g, Player player1, Player player2) {
		Font f = new Font("Arial",Font.BOLD,20);
		g.setColor(guiBgColor);
		g.fillRect(0, 0, Window.width, GameObject.sizeY);
		g.setColor(guiFontColor);
		g.setFont(f);
		
		
		if(Engine.displayResultNow) {
		if(Engine.player1Dead && !Engine.player2Dead) {
			g.setColor(guiResult);
			g.fillRect(0, 0, Window.width, Window.height);
			g.setColor(guiFontColor);
			Font fB = new Font("Arial",Font.BOLD,60);
			g.setFont(fB);
			String winner = "Winner : ";
			g.drawString(winner, Window.width/2 - 80, Window.height/2 - 80  + 30);
			g.drawImage(player2.head, Window.width/2, Window.height/2 - 40+10, Player.sizeX*2, Player.sizeY*2, null);
			g.setFont(new Font("Arial",Font.BOLD,30));
			g.drawString(esc + " : "+(Engine.FINISHTIME - Engine.TIME), Window.width/2-120 + 10, 3*Window.height/4 );
			return;
		}
		else if(Engine.player2Dead && !Engine.player1Dead) {
			g.setColor(guiResult);
			g.fillRect(0, 0, Window.width, Window.height);
			g.setColor(guiFontColor);
			Font fB = new Font("Arial",Font.BOLD,60);
			g.setFont(fB);
			String winner = "Winner : ";
			g.drawString(winner, Window.width/2 - 80, Window.height/2 - 80+ 30);
			g.drawImage(player1.head, Window.width/2, Window.height/2 - 40+10, Player.sizeX*2, Player.sizeY*2, null);
			g.setFont(new Font("Arial",Font.BOLD,30));
			g.drawString(esc + " : "+(Engine.FINISHTIME - Engine.TIME), Window.width/2-120 + 10, 3*Window.height/4 );
			return;
		}else if( (Engine.player1Dead && Engine.player2Dead  ) || Engine.gameOver) {
			g.setColor(guiResult);
			g.fillRect(0, 0, Window.width, Window.height);
			g.setColor(guiFontColor);
			Font fB = new Font("Arial",Font.BOLD,60);
			g.setFont(fB);
			String draw = "DRAW!!";
			g.drawString(draw, Window.width/2 - 80, Window.height/2 - 80+30);
			g.setFont(new Font("Arial",Font.BOLD,30));
			g.drawString(esc + " : "+(Engine.FINISHTIME - Engine.TIME), Window.width/2-120 + 10, 3*Window.height/4 );
			return;
		}
		
		}
		String time = "Time : " + (Engine.FINISHTIME - Engine.TIME+1);
		if(!Engine.gameOver)
		     g.drawString(time, Window.width/2 - 80, 30);
		g.drawImage(player1.head, 10, 10, null);
		g.drawString(Engine.player1Score + "", Player.sizeX + 20 ,  25);
		g.drawImage(player2.head, Window.width - Player.sizeX - 10 , 10, null);
		g.drawString(Engine.player2Score + "", Window.width - 2*Player.sizeX - 10 , 25);
		
	}
	
	public void renderPlayer(Graphics2D g, Player player) {
		g.setColor(gameBgColor);
	
		g.fillRect((int)player.getPreX(), (int)player.getPreY(), Player.sizeX, Player.sizeY);
		
		g.drawImage(player.getImage(), (int)player.getPositionX(), (int)player.getPositionY(), null);
		
		
	//	System.out.println(player.getPositionX()+" "+player.getPositionY());
	}
	
	public void renderBombs(Graphics2D g, ArrayList<Bomb> bombs) {
		
		for(int i =0; i<bombs.size(); i++) {	
			if(!bombs.get(i).getDestroy()) {
			 g.drawImage(bombs.get(i).getImage(), (int)bombs.get(i).getPositionX(), (int)bombs.get(i).getPositionY(), null);
			
			} else {		
	     			for(int j=0; j<bombs.get(i).BlastList().size(); j++) {
						g.drawImage(bombs.get(i).BlastList().get(j).getImage(), (int)bombs.get(i).BlastList().get(j).getPositionX(), (int)bombs.get(i).BlastList().get(j).getPositionY(), null);
					}
	     		//	bombs.get(i).setDestroy(false);
				}

			
			if(bombs.get(i).getBlastEnd()) {
				Engine.bombList.remove(i);
				Engine.updateStage = true;
			}
			
		}	
		
	}

	
	public void renderMenu(Graphics2D g, ArrayList<MenuButtons> menuButtons) {
          g.setColor(menuBgColor);
          g.fillRect(0, 0, Window.width, Window.height);
          for(int i=0; i<menuButtons.size(); i++) {
        	  if(menuButtons.get(i).disabled) {
        		  g.setColor(menuBgColor);
        		  g.fillRect(menuButtons.get(i).x, menuButtons.get(i).y, MenuButtons.buttonWidth, MenuButtons.buttonHeight);
        		  continue;
        	  }
        	  
        	  g.setColor(menuButtons.get(i).getBgColor());
        	  g.fillRect(menuButtons.get(i).x, menuButtons.get(i).y, MenuButtons.buttonWidth, MenuButtons.buttonHeight);
        	  g.setColor(menuButtons.get(i).getFontColor());
        	  g.setFont(menuButtons.get(i).getFont());
        	  g.drawString(menuButtons.get(i).name, menuButtons.get(i).x+( MenuButtons.buttonWidth/2-menuButtons.get(i).name.length()*6 - 5), menuButtons.get(i).y+25);
          }
	}
	
}
