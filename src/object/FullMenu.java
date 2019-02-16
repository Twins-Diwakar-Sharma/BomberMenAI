package object;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import base.Engine;
import base.Window;

public class FullMenu{
	
	private Point curLock;
	
	private float adjustY = 20;
	
	
	private int highLightedButton = -1;
	private boolean nowhere = true;
	
	public void update(ArrayList<MenuButtons> buttons) {
		
		nowhere = true;
		PointerInfo pi = MouseInfo.getPointerInfo();
		curLock = pi.getLocation();
	    curLock.setLocation(curLock.getX() - Window.windowLocX , curLock.getY() - Window.windowLocY);
		//System.out.println(highLightedButton);
		for(int i=0; i<buttons.size(); i++) {
			if(curLock.getX() >= buttons.get(i).x  && curLock.getX() < buttons.get(i).x + MenuButtons.buttonWidth
			   && curLock.getY() >= buttons.get(i).y + adjustY && curLock.getY() < buttons.get(i).y + MenuButtons.buttonHeight + adjustY
			    && !buttons.get(i).disabled) {
				nowhere = false;
				buttons.get(i).fontColor = MenuButtons.highLightColor;
				highLightedButton = buttons.get(i).getType();
			}else {
				buttons.get(i).fontColor = MenuButtons.notHighLightColor;
			}
		}
		if(nowhere) {
			highLightedButton = -1;
		}
		
	}
	


	public void mouseClicked(MouseEvent arg0) {
	  if(Engine.menuOn) {	
	 	
		if(arg0.getClickCount() == MouseEvent.BUTTON1) {

			if(highLightedButton == 0) { // resume
				Engine.menuOn = false;
				Engine.updateStage = true;
			}
			else if(highLightedButton == 1) {  // new game
				Engine.menuOn = false;
				Engine.reset = true;
			    Engine.resumeIt = true;
			    Engine.player1Score = 0;
			    Engine.player2Score = 0;
			    Engine.gameCount++;
			    Engine.aiInGame = false;
			}
			else if(highLightedButton == 2) { // quit
				Engine.windowShouldClose = true;
			}
			else if(highLightedButton == 3) { // defeat bot
				Engine.menuOn = false;
				Engine.reset = true;
			    Engine.resumeIt = true;
			    Engine.player1Score = 0;
			    Engine.player2Score = 0;
			    Engine.gameCount++;
			    Engine.aiInGame = true;
			
			}
		}
		
	}
	}


	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

   public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
   
}
