package base;

import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window {
	Engine engine;
	
	static boolean fullscreen = false;
    JFrame window = new JFrame("Bombermen");
    public static int width = 640;
    public static int height = 640;  // +30 for topbar
    static int bitDepth = 2;
    int refreshRate = 60;
    
    GraphicsEnvironment ge;
    GraphicsDevice gd;
    DisplayMode dm;

    KeyboardInput key;
    KeyboardInput key2;
    MouseInput mouse;
    
    public static double windowLocX, windowLocY;
    
    public void init(){

    	
    	engine  = new Engine();
    
    	
    	try {
       //     UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"); 
    		engine.init();
    	}catch(Exception ex) {}
    	
    
    	
    	window.setIconImage(engine.icon);
    	
    	key = new KeyboardInput(engine.getPlayer());
    	window.addKeyListener(key);
        
    	// if player2 = human
    	key2 = new KeyboardInput(engine.getPlayer2());
    	window.addKeyListener(key2);
    	

    	
    	
    	ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	gd = ge.getDefaultScreenDevice();
    	dm = new DisplayMode(640,640,32,dm.REFRESH_RATE_UNKNOWN);
    	
    	window.requestFocus();
    	window.setSize(width+5,height+30);  // +30 for top bar
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	GridBagLayout ly = new GridBagLayout();
    	
    	
    	GridBagConstraints gbc = new GridBagConstraints();
 
    	gbc.fill = GridBagConstraints.NONE;
    	gbc.gridx=0;
    	gbc.gridy=0;
    //	gbc.gridwidth=1;
    //	gbc.gridheight=1;
    	gbc.anchor = GridBagConstraints.CENTER;
    
 
    	JPanel p = new JPanel();
    	p.add(engine);
    	
    	//window.setLayout(ly);
        
    	
    	
        window.add(engine);
        
        
        window.setUndecorated(fullscreen);
    	int screenWidth  = gd.getDisplayMode().getWidth();
    	int screenHeight = gd.getDisplayMode().getHeight();
    	window.setLocation( (screenWidth - width  )/2, ( screenHeight - height )/2);
    	windowLocX = window.getLocation().getX();
    	windowLocY = window.getLocation().getY();
    	if(fullscreen)
    		gd.setFullScreenWindow(window);
    	window.setResizable(false);
    	if(dm!=null && gd.isDisplayChangeSupported()) {
    		try {
    			gd.setDisplayMode(dm);
    		}catch(Exception ex) {
    			ex.printStackTrace();
    		}
    	}
    	
    	// for mouse hover
   	 window.addComponentListener(new ComponentListener() 
	  {  
	    public void componentMoved(ComponentEvent evt) {
	      Component c = (Component)evt.getSource();
	    	windowLocX = window.getLocation().getX();
	    	windowLocY = window.getLocation().getY();
	    }

	    public void componentShown(ComponentEvent evt) {}

	    public void componentResized(ComponentEvent evt) {}

	    public void componentHidden(ComponentEvent evt) {}
	  }
	  );
    	
   	 
   	mouse = new MouseInput(engine.getFullMenu());
	window.addMouseListener(mouse);
   	
   	engine.addKeyListener(key);
   	engine.addKeyListener(key2);
   	engine.addMouseListener(mouse);  
   	
    	window.setVisible(true);
    	engine.start();
    	
    }
}
