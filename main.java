/**
  @purpose An application that is intended to be a "snake game" that displays various animations through its 3 levels
  @author Kaan Kanadasi
  @since 13 Nov 2023
*/

ArrayList<PVector> snake = new ArrayList<PVector>();
PVector pos; // snake's current position
PVector food; // position of food
PVector dir = new PVector(0, 0); // direction of the snake's movement
int currentLevel = 1;
boolean nextLevelButtonClicked = false;

// types relating to the snake
int size = 40;
int w, h;
int speed = 20;
int len = 5;
int score = 0;
// to make the head of the snake red when it eats the food
boolean showRedHead = false;
int redHeadTimer = 60;

// time background before big bang - first level
int cx, cy;
float secondsRadius;
float minutesRadius;
float hoursRadius;
float clockDiameter;
// dot the background - first level
float max_distance;
// droplet of time -first level
int cols = 200;
int rows = 200;
// Initialize the 2D arrays with the correct dimensions
float[][] current = new float[cols][rows];
float[][] previous = new float[cols][rows];
float dampening = 0.9795;

// sun obstacle - second level
PVector lava;
int colorLava = 105;
int colorChangeSpeed = 1;
// background - second level
PImage explosion;
// an array to store balls makes it easier to modify them - second level
Ball[] balls;

// drops - third level
Drop[] drops;
ArrayList<Strike> s = new ArrayList<Strike>();


// SETUP: the block that runs when the program starts - for initializations
void setup() { 
  size(1080, 720); // width and height of the window
  w = width/size; // ekranın width'ı bölü size, snake'in x ekseni
  h = height/size; // ekranın height'ı bölü size, snake'in y ekseni
  pos = new PVector(w/2, h/2); // snake'in pozisyonu ekranın ortasından başlasın
  food = new PVector(int(random(w)), int(random(h))); // random bir x-y posisyonunda food yarat
  
  max_distance = dist(0, 0, width, height); // background for first level
  cols = width;
  rows = height;
  current = new float[cols][rows];
  previous = new float[cols][rows];

  int radius = min(width, height) / 2; // clock for the first level
  secondsRadius = radius * 0.72;
  minutesRadius = radius * 0.60;
  hoursRadius = radius * 0.50;
  clockDiameter = radius * 1.8;
  cx = width / 2;
  cy = height / 2;
  
  explosion = loadImage("BigBang.jpg"); // background for second level
  lava = new PVector(random(w), random(h)); // obstacle for the second level
  balls = new Ball[5];
  // x-y'de 50-400 eksenlerinde topların harekete başlamaları, 0-5 arası random hız belirlemek, 50-100 arasında size belirlemek
  for(int i=0; i<balls.length; i++) {
    balls[i] = new Ball((int)random(0,1080), (int)random(0,1080), (int)random(2,4), (int)random(2,4), (int)random(50,100));
  }
  
  drops = new Drop[300]; // for the third level
  for (int i=0; i<drops.length; i++) {
    drops[i] = new Drop(); 
    drops[i].init(); 
  }
}


// DRAW: the block/method that runs over and over again - for any logic
void draw() {
  if (currentLevel == 1) {
    background(0);
        
    loadPixels();
    for(int i=1; i<cols-1; i++) {     // for every non-edge element
      for(int j=1; j<rows-1; j++) {   // for every non-edge element
        current[i][j] = (        // bir tane xox tahtası düşün current[i][j] bu tahtanın ortasında oluyor
        previous[i-1][j] +      // previous[i-1][j current[i][j]'nin sağında 
        previous[i+1][j] +       // previous[i+1][j] current[i][j]'nin solunda
        previous[i][j-1] +       // previous[i][j-1] current[i][j]'nin aşağısında
        previous[i][j+1]) / 2 -  // previous[i][j+1] current[i][j]'nin yukarısında
        current[i][j]; 
      current[i][j] = current[i][j] * (dampening + 0.0005*score) ;
      int index = i + j * cols; // pixels are stored in a 1-Dimensional array but we are looping through a 2-Dimensional array so find the right location in the 1-Dimensional array
      pixels[index] = color(current[i][j]*225); // then give me a color equal to the current i j value
      }
    } 
    updatePixels();
  
    float[][] temp = previous;
    previous = current;
    current = temp;
    
    // for the background with dots 
    for(int i = 0; i <= width; i += 20) {
      for(int j = 0; j <= height; j += 20) {
        float dotsize = dist(pos.x * size, pos.y * size, i, j); 
        // pos.x and pos.y are initial positions so to update the dotSize as the snake moves you should multiply them with size to indicate the current snake head position
        dotsize = dotsize/max_distance * 2.5;
        ellipse(i, j,dotsize, dotsize);
      }
    }
  
    fill(80);
    noStroke();
    ellipse(cx, cy, clockDiameter, clockDiameter);
    
    float s = map(second(), 0, 60, 0, TWO_PI) - HALF_PI;
    float m = map(minute() + norm(second(), 0, 60), 0, 60, 0, TWO_PI) - HALF_PI; 
    float h = map(hour() + norm(minute(), 0, 60), 0, 24, 0, TWO_PI * 2) - HALF_PI;
  
    stroke(255);
    strokeWeight(1);
    line(cx, cy, cx + cos(s) * secondsRadius, cy + sin(s) * secondsRadius);
    strokeWeight(2);
    line(cx, cy, cx + cos(m) * minutesRadius, cy + sin(m) * minutesRadius);
    strokeWeight(4);
    line(cx, cy, cx + cos(h) * hoursRadius, cy + sin(h) * hoursRadius);
  
    strokeWeight(2);
    beginShape(POINTS);
    for (int a = 0; a < 360; a+=6) {
      float angle = radians(a);
      float x = cx + cos(angle) * secondsRadius;
      float y = cy + sin(angle) * secondsRadius;
      vertex(x, y);
    }
    endShape();    
    
  } else if (currentLevel == 2) { 
    for(int i=0; i<200; i++) {
      float x = random(width);
      float y = random(height);
      color c = explosion.get(int(x),int(y));
      noStroke();
      fill(c, 100);
      ellipse(x,y,16,16);
    }
    for(int i=0; i<balls.length; i++) {
      balls[i].display();
      balls[i].update();
    }
    drawLava(); 
    
  } else {
    background(225);

    fill(0, 50);
    rect(0, 0, width, height);  
    
    float darkness = map(pos.y, 0, height, 0, 225); // as the snake moves to the bottom of the screen darkness increases
    fill(0, 30 + darkness * 3);
    rect(0, 400, width, 400);
    
    for (int i=0; i<drops.length; i++) {
      if(drops[i].y > drops[i].endPos) { 
        drops[i].end(); 
      } else {
        drops[i].show();
      }
    }
    
    for(int i = s.size() - 1; i >= 0; i--) {
      s.get(i).update(10); // 10 inside update method is actual parameter/argument
      s.get(i).show();
      if(s.get(i).dead) { s.remove(i); }
    }
  }
  
  // bunları sonra koyuyoruz ki background'un önünde belirsin aksi taktirde DRAW'un başına koyarsak snake gözükmez vb.
  strokeWeight(0.2);
  drawSnake();
  noStroke();
  drawFood();
  drawNextLevelButton();
  
  // Check if the button is clicked
  if (nextLevelButtonClicked) {
    goToNextLevel();
    nextLevelButtonClicked = false; // Reset the button click state
    reset();
  }

  fill(255); // beyaz olsun
  textSize(30);
  text("Score: " + score, 70, 30);
  
  updateAnimations();
  displayAnimations();
  
  rect(pos.x * size, pos.y * size, size, size); // head of the snake
  if (frameCount % speed == 0) { updateSnake(); }
}

void drawFood() {
  fill(255, 0, 0); // kırmızı food  
  rect(food.x * size, food.y * size, size, size); // food'un size'ı yarattığımız snake'in size'ı ile aynı oranda olsun
}

void newFood() {
  food = new PVector(int(random(w)), int(random(h))); // x-y ekseninde random belirlensin
}

void drawSnake() {
  rect(pos.x * size, pos.y * size, size, size);
  for(int i = 0; i < snake.size(); i ++) {
    fill(0, 50); // gri snake
    rect(snake.get(i).x * size, snake.get(i).y * size, size, size);
  }
}
  
void updateSnake() { 
  if(dir.x != 0 || dir.y != 0) { snake.add(new PVector(pos.x, pos.y)); }
  while(snake.size() > len) { snake.remove(0); }
  pos.add(dir);
 
  if(pos.x == food.x && pos.y == food.y) {
    newFood();
    len += 1;
    score += 1;
    showRedHead = true; 
    // food yediğinde snake belli bir limite kadar (3) hızlansın 
    if(speed > 3) { speed = constrain(speed-1, 0, 20); }
    if(currentLevel == 1) { current[int(random(width))][int(random(height))] = 255; } // advanced damla efekti belirsin 
  }
  // resets the position of the snake if the snake crashes to itself
  for(int i = 0; i < snake.size(); i++) {
    if (pos.x == snake.get(i).x && pos.y == snake.get(i).y) { reset(); }
  }
  
  if (currentLevel == 1) {
    if(pos.x < 0) { pos.x = w - 1; }
    if(pos.x > w-1) { pos.x = 0; }
    if(pos.y < 0) { pos.y = h - 1; }
    if(pos.y > h-1) { pos.y = 0; }
  } else {
    if(pos.x < 0 || pos.x > w-1 || pos.y < 0 || pos.y > h-1) { reset(); } // diğer levellerde duvara çarpınca ölüyor
    if(pos.x == lava.x && pos.y == lava.y) { reset(); } // reset the position of the sanake if it touches lava
  }
}

void updateAnimations() {
    // Update red head timer
    if (showRedHead) {
        redHeadTimer--;
        if (redHeadTimer <= 0) {
            showRedHead = false;
            redHeadTimer = 60; // Reset the timer for the next time the snake eats food
        }
    }
}

void displayAnimations() {
  if (showRedHead) {
    fill(255, 0, 0); // red color for the head of the snake
    rect(pos.x * size, pos.y * size, size, size);
  }
}

void drawNextLevelButton() {
  fill(100, 200, 100); // yeşil buton
  rect(900, 20, 150, 50); // position and size of the button
  fill(255);  // beyaz yazı 
  textSize(20);
  textAlign(CENTER, CENTER);
  text("Next Level", 975, 45); 
}

// reset attığımızda herşey default hale dönsün
void reset() {
  speed = 20;
  len = 5;
  pos = new PVector(w/2, h/2);
  dir = new PVector(0, 0); 
  score = 0;
  showRedHead = false;
  snake.clear(); // Clear the existing contents of the snake ArrayList -chatGPT
}

void goToNextLevel() {
  // Increment the current level
  currentLevel++;
  // Reset and adjust the game for the new level
    if (currentLevel == 2) {
    reset();
    drawLava();
  } else if (currentLevel == 3) {
    reset();
    // bunu demezsek lava level 3'te gözükmüyor ama bulunmaya devam ediyor
    lava.x = -100;
    lava.y = -100;
  }
}

void colorLavaDensity() {
  colorLava += colorChangeSpeed; 
  if (colorLava <= 85 || colorLava >= 205) { // colorLava 205 olduğunda colorLava -1 loopuna düşüyor ve azalmaya başlıyor, 85 e düştüğünde ise +1 loopunda artıyor
    colorChangeSpeed *= -1; // Reverse the color change 
  }
}

void newLava() {  
  lava.x = int(random(w));
  lava.y = int(random(h)); 
}

void drawLava() {
  colorLavaDensity();
  fill(225, colorLava, 0); // lava turuncu olsun
  noStroke();
  ellipse(lava.x * size + size / 2, lava.y * size + size / 2, size, size); // Lava'nın size'ı yarattığımız snake'in size'ı ile aynı oranda olsun 
}

// snake'i kontrol etmek
void keyPressed() {
  if(key == CODED) {
    if(keyCode == UP && dir.y != 1) { dir = new PVector(0, -1); } // if the snake is not moving down and we press UP the snake moves up
    if(keyCode == DOWN && dir.y != -1) { dir = new PVector(0, 1); }
    if(keyCode == LEFT && dir.x != 1) { dir = new PVector(-1, 0); }
    if(keyCode == RIGHT && dir.x != -1) { dir = new PVector(1, 0); }
  }
}

void mousePressed() {
  // tıkladığın x-y ekseninde olan top renk değiştirsin
  for(int i=0; i<balls.length; i++) { // ball array'i içinde iterate et  
    if(balls[i].pointInEllipse(mouseX, mouseY)) { balls[i].changeColor(); } // pointInEllipse returns "True" if point P is inside the ellipse 
  }
  if (mouseX > 900 && mouseX < 1050 && mouseY > 20 && mouseY < 70) { nextLevelButtonClicked = true; }
  if(currentLevel == 3) { s.add(new Strike(new PVector(mouseX, mouseY), 50, 0, 5)); }

}