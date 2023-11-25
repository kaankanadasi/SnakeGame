public class Ball {
    private int x, y, dy, dx, size;
    // color( , , ) funciton - Hue (color type), Saturation (vibrancy), Brightness
    private color ballColor = color((int)random(100, 255), (int)random(0, 100), (int)random(0, 20));
    // we can make these public to change dx and dy, HOWEVER this may lead up to change variables that are not meant to change
    
    public Ball(int x, int y, int dx, int dy, int size) { 
      this.x = x; // x-axis center posiiton
      this.y = y; // y-axis center position
      this.dx = dx; // speed in x-axis
      this.dy = dy; // speed in y-axis
      this.size = size; // diameter
    }
    
    public void move() {
      y += dy; // y center of the ball changes in accordance to dy
      x += dx; // x center changes of the ball in accordance to dx
    }
    
    // To allow code outside the class to change the value of an instance variable, provide a setter (mutator method)
    // we need to change dx and dy, to make the code allow changing these values -> we have to define setters
    // we use "void" keyword for setters because their primary purpose is to modify the internal state of an object rather than returning a value
    public void setdy(int dy) {
      this.dy = dy;
    } 
    public void setdx(int dx) {
      this.dx = dx;
    }
    
    // To allow code outside the class to access the value of particular instance variables, provide a getter method (accessor method9
    // we need to be able to see where our ball is, to make the code access these values -> we have to define getters
    public int getdy() {
      return this.dy; 
      // you can say dy but it makes it clear that you are returning this.dy
    }
    public int getdx() {
      return this.dx; 
    }
    public int getY() {
      return this.y; 
    }
    public int getX() {
      return this.x; 
    }
    public int getSize() {
      return this.size;
    }
    
    // this is a method for the condition of the color change, if the mouse clicked is inside the ellipse the color will change
    public boolean pointInEllipse(int x, int y) {
      double distance = Math.sqrt(Math.pow((x-getX()), 2) + Math.pow((y-getY()),2));
      // distance between (x-y) and the center of the ball, √((x2 - x1)^2 + (y2 - y1)^2)).
      if (distance < getSize()/2) { // if distance s less than the radious
        return true; // point is inside the ellipse
      }
      return false; // point is outside the ellipse
    }
    
    public void changeColor() {
       this.ballColor = color((int)random(200, 255), (int)random(0, 100), (int)random(0, 0));
    }
    
    public boolean isCollidingVerticalWalls() {
      if(getX()-(getSize()/2) < 0 || getX()+(getSize()/2) > width) {
      // if [right of the circle (center x - radious) is right of the right wall (smaller than 0 in x-axis) OR left of the circle (center y + radious) is left of the left wall (bigger than width)]
      return true; // collision
    }
    return false; // no collision
  } 
    public boolean isCollidingHorizontalWalls() {
    if(getY()-(getSize()/2) < 0 || getY()+(getSize()/2) > height) { 
      // if [top of the circle (center y - radious) is above the top wall (smaller than 0 in y-axis) OR  bottom of the circle (center y + radious) is below bottom wall (bigger than height)]
      return true; // collision
    }
    return false; // no collision
  } 
    
    public void checkCollision() {
      if(isCollidingHorizontalWalls()) {
        // collision with top or bottom wall -> reverse the vertical direction (y-axis)
        setdy( getdy() * -1 );
        // dy *= -1;
      }   
      if(isCollidingVerticalWalls()) {
        // collision with right or left wall -> reverse the horizontal direction (x-axis)
        setdx( getdx() * -1 );
        // dx *= -1;
      }
    }
    
    public void update() {
      move();
      checkCollision();
    }
    
    public void display() {
      float c = random(10, 30);
      fill(red(ballColor), green(ballColor), blue(ballColor), c);
      ellipse(x, y, size * 0.5, size * 0.5); // x, y, width, height
      }
  } 