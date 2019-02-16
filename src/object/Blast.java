package object;

import java.awt.image.BufferedImage;

import base.Engine;

public class Blast extends GameObject{

	int row, col;
	
	public Blast(BufferedImage image, float x, float y, int row, int col) {
		super(image, x, y);
		this.row = row;
		this.col = col;
	}
	
	public void update(Player p1, Player p2) {
		if(p1.centerCollidesWith(row, col))
			Engine.player1Dead = true;
		if(p2.centerCollidesWith(row, col))
			Engine.player2Dead = true;
			
	}
	
	
	
}
