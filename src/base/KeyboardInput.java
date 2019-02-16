package base;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import object.Player;

public class KeyboardInput extends KeyAdapter{
	
	private Player player;
	
	public KeyboardInput(Player player) {
		this.player = player;
	}
	
	public void keyPressed(KeyEvent e){
  	  player.keyPressed(e);
    }
	
	public void keyReleased(KeyEvent e) {
		player.keyReleased(e);
	}
	
	
	
}
