package object;

import java.awt.image.BufferedImage;

import base.Loader;
import math.Vector2f;

public class GameObject {
	  public static int sizeX  = 32;
	  public static int sizeY = 32;
	
      protected Vector2f position ;
      protected BufferedImage image;
      
      protected Loader loader = new Loader();
      
      public GameObject(BufferedImage image) {
    	  position = new Vector2f();
    	  this.image = image;
      }
      
      public GameObject(BufferedImage image, float x, float y) {
    	  position = new Vector2f(x,y);
    	  this.image = image;
      }
      
      
      public void translate(float dx, float dy) {
    	  position.translate(dx, dy);
      }
      
      public float getPositionX() {
    	  return position.x;
      }
      
      public float getPositionY() {
    	  return position.y;
      }
      
      public Vector2f getPosition() {
    	  return position;
      }
      
      public void setPosition(float x, float y) {
    	  this.position.x = x;
    	  this.position.y = y;
      }
      
      public void setPosition(Vector2f position) {
    	  this.setPosition(position.x, position.y);
      }
   
      public void setPositionY(float y) {
    	  position.y = y;
      }
      
      public void setPositionX(float x) {
    	  position.x = x;
      }
     
      public BufferedImage getImage() {
    	  return image;
      }
}
