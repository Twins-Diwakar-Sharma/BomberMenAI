package math;

public class Vector2f {
   public float x;
   public float y;
   
   public Vector2f() {
	   x = 0; y=0;
   }
   
   public Vector2f(float x, float y) {
	   this.x = x;
	   this.y = y;
   }
   
   public Vector2f(Vector2f vector) {
	   this(vector.x, vector.y);
   }
   
   public void translate(float dx, float dy) {
	   x += dx;
	   y += dy;
   }
   
   public void scale(float s) {
	   x = x*s;
	   y = y*s;
   }
   
   public void scale(float sx, float sy) {
	   x = x * sx;
	   y = y * sy;
   }
}
