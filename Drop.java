// source: https://www.youtube.com/watch?v=KkyIDI6rQJI
// source: https://www.youtube.com/watch?v=Yg3HWVqskTQ

public class Drop { // the code works even if I don't write "public"
  float x,y, z, len, yspeed; 
  float ellipseX,ellipseY,endPos;
  color c;
  
  void drops() { init(); }
  
  // initialize drop parameters
  void init() {
    x = random(width); // random x position within the width of the window
    y = random(-300, 0); // random start between the top of the window and off-screen
    z = random(0, 20); // depth for 3D effect
    yspeed = map(z, 0, 20, 4, 10); // closer drops are faster for 3D effect
    c = color(random(5,20),random(10,50),random(150,225)); 
    ellipseX = 0;
    ellipseY = 0;
    endPos = height - random(300); 
  }
  
  void update() {
    y += yspeed; // move the drops downward
    if (y>height) { // If drop reaches the bottom of the window
      y = random(-200, -100); // Reset the drop above the window
      yspeed = map(z, 0, 20, 4, 10); // Adjust speed based on depth for 3D effect
    }
  }
  
  void show() {
    fill(c);
    noStroke();
    rect(x,y,2, 15); // the drops 
    update();
  }
  
  void end() {
    stroke(c);
    noFill();
    ellipse(x, y, ellipseX, ellipseY); // drop striking effect
    //  the x axis is bigger than y axis for the wanted drop striking ellipse effect
    ellipseX += yspeed * 0.5; // x axis of the striking effect is proportional to yspeed * 0.5
    ellipseY += yspeed * 0.2; // y axis of the striking effect is proportional to yspeed * 0.2
    if (ellipseX>50) { init(); }
  }
  
}