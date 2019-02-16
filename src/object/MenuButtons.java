package object;

import java.awt.Color;
import java.awt.Font;

public class MenuButtons {
	public String name;
	public int x, y;
	
	public boolean disabled = false;
	
	Font f = new Font("Arial",Font.BOLD,20);
	Color fontColor;
	Color bgColor = Color.BLACK;	
	public static Color highLightColor = Color.YELLOW;
	public static Color notHighLightColor = Color.WHITE;
	int type;  // 0 = resume ; 1 = new Game; 2 = quit;
	
	public static int buttonWidth =  GameObject.sizeX*5;
	public static int buttonHeight = GameObject.sizeY;
	
    public MenuButtons(String name, int type, int x, int y) {
    	this.name = name;
    	this.type = type;
    	this.x = x;
    	this.y = y;
    	fontColor = notHighLightColor;
    }
    
    public Color getFontColor() {
    	return fontColor;
    }
    public Color getBgColor() {
    	return bgColor;
    }
    
    public int getType() {
    	return type;
    }
    public Font getFont() {
    	return f;
    }
    
}
