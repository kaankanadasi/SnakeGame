// source: https://www.youtube.com/watch?v=bYgEyWckOI0
// Code in other classes can only interact with the public methods you provide and cannot directly access the private instance variables
public class Strike {
  
    // instance variables
    private PVector start, end;
    private boolean split = false;
    private boolean both = false;
    private boolean dead = false;
    private float a, r = 0;
    private float h, d;
    private int n, max;
    private Strike s1, s2;
    
    // constructor
    public Strike(PVector start, float h, int n, int max) {
      // Within a non-static method or a constructor, this.variable is a way to indicate that we are referring to the instance variables of this object instead of a local variable.
      this.start = start;
      this.h = h;
      this.d = random(h/2) + h;
      this.n = n;
      this.max = max;
      
      a = random(PI/4, 3 * PI/4);
      end = new PVector(start.x + cos(a) * this.d, start.y + sin(a) * this.d);
    }
  
    public void update(float speed) { // float speed is the formal parameter
      if(r < d) {
        r += speed;
      } else {
        r = d;
        if(!split && n < max) {
          split = true;
          if(random(1 ) > 0.5)  { 
            both = true;
            s1 = new Strike(end, h, n+1, max);
            s2 = new Strike(end, h, n+1, max);
          } else {
            both = false;
            s1 = new Strike(end, h, n+1, max);
          }
        } if(n >= max) {
          dead = true; 
          } else {
            if(both) { dead = s1.dead && s2.dead; }
            else { dead = s1.dead; }
          }
        }
        if(split) {
          s1.update(speed);
          if(both) {
            s2.update(speed);
          }     
        }
      
    }
    
    public void show() {
      stroke(255, 255, 150);
      strokeWeight(r/d * 5);
      
      line(start.x, start.y, start.x + cos(a) * r, start.y + sin(a) * r);
      if(split) {
        s1.show();
        if(both) {
          s2.show( );
        }
      }
    }
  }