import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.util.Timer;
import java.util.TimerTask;

public class FinalProjectStart extends Applet implements MouseListener, MouseMotionListener, ActionListener, KeyListener 
{
	thing[] asteroid;	//declare the object
	thing[] outsideAsteroids;
	thing Player;
	thing Bullet[];
	thing superBullet[];
	
	Canvas c1;
	Canvas reloadCanvas;
	Canvas bulletCanvas;
	Graphics myG;
	Timer myTimer;
	Button startButton;
	//Timer reloadTimer;
	Timer spawnTimer;
	
	double newXSpeed;
	double newYSpeed;
	double oXSpeed;
	double oYSpeed;
	
	int asteroidCounter = 0;
	int bulletCounter = -1;
	//int masterBulletCounter = 0;
	
	int numOfAsteroids = 50;
	int numOutsideAsteroids = numOfAsteroids * 2;
	int numOfBullets = 10000;
	int numSuperBullets = 100;
	int theLocation;
	
	int ballSize = 5;
	int playerSize = 20;
	int bulletSize = 11;
	int superBulletSize = 50;
	
	boolean gameOver = false;
	boolean Start = false;
	boolean bulletShoot = false;
	boolean win = false;
	boolean testSuperBullet = false;
	
	Button scoreButton;
	Button superBulletButton;
	int score = 0;
	int superBullNum = 0;
	int superBulletCounter = -1;
	
	int killNum = 20;
	int difficultyNum = 0;
	double difficultyLevel = .1;
	
	public void init() 
	{	
		startButton = new Button("Start");
		add(startButton);
		startButton.addActionListener(this);
		
		scoreButton = new Button("Score: 0");
		add(scoreButton);
		scoreButton.addActionListener(this);
		
		superBulletButton = new Button("Kills Until Super Bullet: "+killNum);
		add(superBulletButton);
		superBulletButton.addActionListener(this);
		
		this.setSize(400, 500);
		this.setBackground(Color.black);
		c1 = new Canvas();
		c1.setSize(800, 600);
		add(c1);
		c1.addMouseListener(this);
		c1.addMouseMotionListener(this);
		myG = c1.getGraphics();
		
		//reloadCanvas = new Canvas();
		//reloadCanvas.setSize(100, 100);
		//add(reloadCanvas);
		
	
		asteroid = new thing[numOfAsteroids];	//instantiate the object and give it a size of 50, 50
		outsideAsteroids = new thing[numOutsideAsteroids];
		Player = new thing(playerSize, playerSize);
		Bullet = new thing[numOfBullets];
		superBullet = new thing[numSuperBullets];
		
		c1.addKeyListener(this);
		c1.requestFocus();
		
		
		int x = 0;
		int temp;
		while(x < numOfAsteroids)
		{
			temp = (int)(Math.random()*5)+2;
			asteroid[x] = new thing(ballSize*temp, ballSize*temp);
			x++;
		}
		
		int y = 0;
		while(y < numOfBullets)
		{
			Bullet[y] = new thing(bulletSize, bulletSize);
			Bullet[y].setColor(Color.black);
			y++;
		}
		
		int z = 0;
		while(z < numSuperBullets)
		{
			superBullet[z] = new thing(superBulletSize, superBulletSize);
			superBullet[z].setColor(Color.black);
			z++;
		}
	}
	
	public void paint(Graphics g) 
	{	
		startButton.setLocation(225, 25);
		scoreButton.setLocation(300, 25);
		scoreButton.setSize(100, 25);
		superBulletButton.setLocation(425, 25);
		superBulletButton.setSize(150, 25);
		c1.setLocation(50, 50);
		c1.setBackground(Color.white);
		Player.setColor(Color.red);
	}
	
	 public void Looper(float hundredth) //this starts the animation "loop" running
     {
		 myTimer.schedule(new RemindTask(), 0, (int)(hundredth*10));
     }
     
     
     class RemindTask extends TimerTask 
     {
          public void run()	//Here's the animation "loop" (not really a loop, but it IS repeating).
          {					//The advantage of this approach is that the program will hear other events
        	  				//like mousePresses and mouseReleases while this is going on.
        	  asteroidCounter = 0;
      
        	  int counter = 0;
        	  while(asteroidCounter < numOfAsteroids && gameOver==false)
        	  {
        	   		asteroid[asteroidCounter].eraseMe(myG, Color.white);	//erase it
                    asteroid[asteroidCounter].moveMe();					//move it
                	asteroid[asteroidCounter].drawMe(myG);
                	asteroid[asteroidCounter].setColor(Color.getHSBColor((float)(Math.random()*1), (float)1.0, (float)1.0));
                	
                	Player.eraseMe(myG, Color.white);
                	Player.moveMe();
                	Player.drawMe(myG);
                	
                	asteroid[asteroidCounter].follow(Player);
                	asteroid[asteroidCounter].kick();
                	
                	if(difficultyNum == 50)
                	{
                		asteroid[asteroidCounter].setAgression(asteroid[asteroidCounter].getAgression() + difficultyLevel);
                		difficultyLevel++;
                		difficultyNum = 0;
                	}
                	
                	if(killNum >= 0)
                	{
                		superBulletButton.setLabel("Kills Until Super Bullet: "+killNum);
                	}
                	
                	if(Player.getXPosition() >= c1.getWidth() - Player.getWidth())
                	{
                		Player.eraseMe(myG, Color.white);
                		Player.setXPosition(1);
                	}
                	
                	if(Player.getXPosition() <= 0)
                	{
                		Player.eraseMe(myG, Color.white);
                		Player.setXPosition(c1.getWidth()- Player.getWidth());
                	}
                	
                	if(Player.getYPosition() >= c1.getHeight() - Player.getHeight())
                	{
                		Player.eraseMe(myG, Color.white);
                		Player.setYPosition(1);
                	}
                	
                	if(Player.getYPosition() <= 0)
                	{
                		Player.eraseMe(myG, Color.white);
                		Player.setYPosition(c1.getHeight() - Player.getHeight());
                	}
                	
                	int newCounter = 0;
                	
                	while(newCounter <= bulletCounter) 
                	{
                		//System.out.println("Shooting "+bulletCounter);
                    	//System.out.println(Bullet[bulletCounter].getXPosition());
                    	//System.out.println(Bullet[bulletCounter].getYPosition());
                    	if((Bullet[newCounter].getXPosition() < 850 && Bullet[newCounter].getXPosition() > -50) && (Bullet[newCounter].getYPosition() < 650 && Bullet[newCounter].getYPosition() > -50))
                    	{
                    		//System.out.println("Shooting "+bulletCounter);
                    		Bullet[newCounter].eraseMe(myG, Color.white);
                            Bullet[newCounter].moveMe();
                            Bullet[newCounter].drawMe(myG);
                            //Bullet[newCounter].setColor(Color.getHSBColor((float)(Math.random()*1), (float)1.0, (float)1.0));
                            //System.out.println(Bullet[newCounter].getXPosition()+" "+Bullet[newCounter].getYPosition());
                    	}
                    	
                    	if(Bullet[newCounter].hits(asteroid[asteroidCounter]))
                    	{
                    		Bullet[newCounter].eraseMe(myG, Color.white);
                    		asteroid[asteroidCounter].eraseMe(myG, Color.white);
                    		
                    		//asteroid[asteroidCounter].destroyMe();
                    		asteroid[asteroidCounter].resurrectMe();
                    		Bullet[newCounter].destroyMe();
                    		
                    		score++;
                    		scoreButton.setLabel("Score: "+String.valueOf(score));
                    		superBullNum++;
                    		difficultyNum++;
                    		killNum--;
                    		//System.out.println(superBullNum);
                    		
                    		//asteroid[asteroidCounter].breakUp();
                    		//Bullet[newCounter].breakUp();
                    	}
                        //Bullet[masterBulletCounter].highlightme(myG);
                        newCounter++;
                	}
                	
                	int counter100 = 0;
                	
                	while(counter100 <= superBulletCounter)
                	{
                		if((superBullet[counter100].getXPosition() < 850 && superBullet[counter100].getXPosition() > -50) && (superBullet[counter100].getYPosition() < 650 && superBullet[counter100].getYPosition() > -50))
                		{
                			superBullet[counter100].eraseMe(myG, Color.white);
                            superBullet[counter100].moveMe();
                            superBullet[counter100].drawMe(myG);
                            //superBullet[counter100].setColor(Color.getHSBColor((float)(Math.random()*1), (float)1.0, (float)1.0));
                            
                            //System.out.println(superBullet[counter100].getXPosition()+" "+superBullet[counter100].getYPosition());
                		}
                		
                		if(superBullet[counter100].hits(asteroid[asteroidCounter]))
                		{
                			superBullet[counter100].eraseMe(myG, Color.white);
                    		asteroid[asteroidCounter].eraseMe(myG, Color.white);
                    		asteroid[asteroidCounter].resurrectMe();
                    		
                    		score++;
                    		scoreButton.setLabel("Score: "+String.valueOf(score));
                		}
                		counter100++;
                	}
                	
                	counter = 0;
                	while(counter < numOfAsteroids)
                	{
                		if(asteroid[asteroidCounter].hits(asteroid[counter])&&counter!=asteroidCounter)
        				{
                			
                			newXSpeed = asteroid[asteroidCounter].getXSpeed();
                			newYSpeed = asteroid[asteroidCounter].getYSpeed();
                			oXSpeed = asteroid[counter].getXSpeed();
                			oYSpeed = asteroid[counter].getYSpeed();
                			
                			asteroid[asteroidCounter].setXSpeed(oXSpeed);
                			asteroid[asteroidCounter].setYSpeed(oYSpeed);
                			asteroid[counter].setXSpeed(newXSpeed);
                			asteroid[counter].setYSpeed(newYSpeed);
                			
                			
                			
                			//System.out.println("Yay");
        				}
                		
                		if(Player.hits(asteroid[counter])&&counter!=asteroidCounter)
                		{
                			gameOver = true;
                			System.out.println("Game Over");
                		}
                		//if(Bullet[bulletCounter].hits(asteroid[counter])&& counter!=bulletCounter)
                		//{
                		//	System.out.println("TRUE");
                		//}
                		counter++;
                	}
                	
                	if(asteroid[asteroidCounter].getXPosition()>=c1.getWidth()-ballSize + 100||asteroid[asteroidCounter].getXPosition()<=-100)
                	{	
                		asteroid[asteroidCounter].setXSpeed(asteroid[asteroidCounter].getXSpeed()*-1);
                	}
                	if(asteroid[asteroidCounter].getYPosition()>=c1.getHeight()-ballSize + 100||asteroid[asteroidCounter].getYPosition()<=-100)
                	{
                		asteroid[asteroidCounter].setYSpeed(asteroid[asteroidCounter].getYSpeed()*-1);
                	}
                	asteroidCounter++;
                	//bulletCounter++;
        	  }
          }	  
    }
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==startButton)
		{	
			asteroidCounter = 0;
			while(asteroidCounter < numOfAsteroids)
			{
				asteroid[asteroidCounter].setXSpeed(Math.random()*3-1.5);
				asteroid[asteroidCounter].setYSpeed(Math.random()*3-1.5);
				asteroidCounter++;
			}
					
			asteroidCounter = 0;
			int theXLocation = 5;
			int theYLocation = 5;
			while(asteroidCounter < numOfAsteroids && Start == false)	//overcomplicated method, could combine while's, copied them from another method
			{
				asteroid[asteroidCounter].setXPosition(theXLocation);
				asteroid[asteroidCounter].setYPosition(theYLocation);
				if(theXLocation > c1.getWidth())
				{
					theXLocation = 5;
					theYLocation = theYLocation + (int)asteroid[asteroidCounter].getWidth() + 15;
				}
				theXLocation = theXLocation + (int)asteroid[asteroidCounter].getWidth() + 15;
				asteroidCounter++;
			}
			
			if(Start == false)
			{
				Player.setXPosition(400);
				Player.setYPosition(500);
			}
			
			myTimer = new Timer(); //create the timer
			spawnTimer = new Timer();
			Looper(1); //begin the animation loop
			Start = true;
		}
		c1.requestFocus();
	}
	public void mousePressed(MouseEvent e)
	{
	
	}
	
	public void mouseReleased(MouseEvent e)
	{
	
	}		
	
	public void mouseClicked(MouseEvent e)
	{	
		
	}
	
	
	public void mouseMoved(MouseEvent e)
	{
		
	}
	
	public void mouseDragged(MouseEvent e)
	{

	}
	public void mouseEntered(MouseEvent e)
	{


	}
	
	public void mouseExited(MouseEvent e)
	{
		
		
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyChar()=='w')
		{
			//System.out.println("YAY");
			Player.setYSpeed(-0.1);
		}
		if(e.getKeyChar()=='a')
		{
			Player.setXSpeed(-0.1);
		}
		if(e.getKeyChar()=='s')
		{
			Player.setYSpeed(0.1);
		}
		if(e.getKeyChar()=='d')
		{
			Player.setXSpeed(0.1);
		}
		if(e.getKeyCode()==37)
		{
			if(!testSuperBullet)
			{
				bulletCounter++;
				Bullet[bulletCounter].setXPosition(Player.getXPosition() + Player.getWidth()/2 - Bullet[bulletCounter].getWidth()/2 + 1);
				Bullet[bulletCounter].setYPosition(Player.getYPosition() + Player.getHeight()/2 - Bullet[bulletCounter].getHeight()/2);
				//Player.fire(Bullet[bulletCounter]);
				Bullet[bulletCounter].setXSpeed(-0.3);
				Bullet[bulletCounter].setYSpeed(0);
				//masterBulletCounter++;
				//bulletCounter++; //move on
				
				//prepare the next one...
				//Bullet[bulletCounter] = new thing(bulletSize, bulletSize);
				//Bullet[bulletCounter].setColor(Color.black);
				bulletShoot = true;
			}
			
			if(testSuperBullet && superBullNum >= 20)
			{
				superBulletCounter++;
				superBullet[superBulletCounter].setXPosition(Player.getXPosition() + Player.getWidth()/2 - superBullet[superBulletCounter].getWidth()/2 + 1);
				superBullet[superBulletCounter].setYPosition(Player.getYPosition() + Player.getHeight()/2 - superBullet[superBulletCounter].getHeight()/2);
				superBullet[superBulletCounter].setXSpeed(-0.2);
				superBullet[superBulletCounter].setYSpeed(0);
				//System.out.println(superBullet[superBulletCounter].getXPosition()+" "+superBullet[superBulletCounter].getYPosition());
				superBullNum = 0;
				killNum = 20;
				testSuperBullet = false;
			}
		}
		if(e.getKeyCode()==38)
		{
			if(!testSuperBullet)
			{
				bulletCounter++;
				Bullet[bulletCounter].setXPosition(Player.getXPosition() + Player.getWidth()/2 - Bullet[bulletCounter].getWidth()/2 + 1);
				Bullet[bulletCounter].setYPosition(Player.getYPosition() + Player.getHeight()/2 - Bullet[bulletCounter].getHeight()/2);
				//Player.fire(Bullet[bulletCounter]); 
				Bullet[bulletCounter].setXSpeed(0);
				Bullet[bulletCounter].setYSpeed(-0.3);
				//masterBulletCounter++;
				//Bullet[bulletCounter] = new thing(bulletSize, bulletSize);
				//Bullet[bulletCounter].setColor(Color.black);
				bulletShoot = true;
			}
			
			if(testSuperBullet && superBullNum >= 20)
			{
				superBulletCounter++;
				superBullet[superBulletCounter].setXPosition(Player.getXPosition() + Player.getWidth()/2 - superBullet[superBulletCounter].getWidth()/2 + 1);
				superBullet[superBulletCounter].setYPosition(Player.getYPosition() + Player.getHeight()/2 - superBullet[superBulletCounter].getHeight()/2);
				superBullet[superBulletCounter].setXSpeed(0);
				superBullet[superBulletCounter].setYSpeed(-0.2);
				//System.out.println(superBullet[superBulletCounter].getXPosition()+" "+superBullet[superBulletCounter].getYPosition());
				superBullNum = 0;
				killNum = 20;
				testSuperBullet = false;
			}
		}
		if(e.getKeyCode()==39)
		{
			if(!testSuperBullet)
			{
				bulletCounter++;
				Bullet[bulletCounter].setXPosition(Player.getXPosition() + Player.getWidth()/2 - Bullet[bulletCounter].getWidth()/2 + 1);
				Bullet[bulletCounter].setYPosition(Player.getYPosition() + Player.getHeight()/2 - Bullet[bulletCounter].getHeight()/2);
				//Player.fire(Bullet[bulletCounter]);
				Bullet[bulletCounter].setXSpeed(0.3);
				Bullet[bulletCounter].setYSpeed(0);
				bulletShoot = true;
			}
			
			if(testSuperBullet && superBullNum >= 20)
			{
				superBulletCounter++;
				superBullet[superBulletCounter].setXPosition(Player.getXPosition() + Player.getWidth()/2 - superBullet[superBulletCounter].getWidth()/2 + 1);
				superBullet[superBulletCounter].setYPosition(Player.getYPosition() + Player.getHeight()/2 - superBullet[superBulletCounter].getHeight()/2);
				superBullet[superBulletCounter].setXSpeed(0.2);
				superBullet[superBulletCounter].setYSpeed(0);
				//System.out.println(superBullet[superBulletCounter].getXPosition()+" "+superBullet[superBulletCounter].getYPosition());
				superBullNum = 0;
				killNum = 20;
				testSuperBullet = false;
			}
		}
		if(e.getKeyCode()==40)
		{
			if(!testSuperBullet)
			{
				bulletCounter++;
				Bullet[bulletCounter].setXPosition(Player.getXPosition() + Player.getWidth()/2 - Bullet[bulletCounter].getWidth()/2 + 1);
				Bullet[bulletCounter].setYPosition(Player.getYPosition() + Player.getHeight()/2 - Bullet[bulletCounter].getHeight()/2);
				//Player.fire(Bullet[bulletCounter]);
				Bullet[bulletCounter].setXSpeed(0);
				Bullet[bulletCounter].setYSpeed(0.3);
				bulletShoot = true;
			}
			
			if(testSuperBullet && superBullNum >= 20)
			{
				superBulletCounter++;
				superBullet[superBulletCounter].setXPosition(Player.getXPosition() + Player.getWidth()/2 - superBullet[superBulletCounter].getWidth()/2 + 1);
				superBullet[superBulletCounter].setYPosition(Player.getYPosition() + Player.getHeight()/2 - superBullet[superBulletCounter].getHeight()/2);
				superBullet[superBulletCounter].setXSpeed(0);
				superBullet[superBulletCounter].setYSpeed(0.2);
				//System.out.println(superBullet[superBulletCounter].getXPosition()+" "+superBullet[superBulletCounter].getYPosition());
				superBullNum = 0;
				killNum = 20;
				testSuperBullet = false;
			}
		}
		
		if(e.getKeyChar() == 'v')
		{
			if(superBullNum >= 20)
			{
				testSuperBullet = true;
				//System.out.println("TRUE");
			}
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyChar()=='w')
		{
			//System.out.println("YAY");
			Player.setYSpeed(0);
		}
		if(e.getKeyChar()=='a')
		{
			Player.setXSpeed(0);
		}
		if(e.getKeyChar()=='s')
		{
			Player.setYSpeed(0);
		}
		if(e.getKeyChar()=='d')
		{
			Player.setXSpeed(0);
		}
		if(e.getKeyCode()==37)
		{
			//Bullet[bulletCounter].setXSpeed(0);
			//bulletShoot = false;
		}
		if(e.getKeyCode()==38)
		{
			//Bullet[bulletCounter].setYSpeed(0);
			//bulletShoot = false;
		}
		if(e.getKeyCode()==39)
		{
			//Bullet[bulletCounter].setXSpeed(0);
			//bulletShoot = false;
		}
		if(e.getKeyCode()==40)
		{
			//Bullet[bulletCounter].setYSpeed(0);
			//bulletShoot = false;
		}
		
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
}

class thing
{
	private double xPosition;
	private double yPosition;
	
	private double xSpeed;
	private double ySpeed;
	
	private double width;
	private double height;
	
	private Color thingColor = Color.black;
	
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	
	Timer spawnTimer;
	private double agression = (Math.random()*.5) + 0.2;

	
	//constructor
	public thing(double newWidth, double newHeight)
	{
		width = newWidth;
		height = newHeight;
	}
	
	public void destroyMe()
	{
		this.setXPosition(900);
		this.setXSpeed(0);
		this.setYSpeed(0);
	}
	
	
	public void resurrectMe()
	{
		int xDec  = (int)(Math.random()*4) + 1;
		
		if(xDec == 1)
		{
			this.setXPosition((int)(Math.random()*700) + 0);
			this.setYPosition(0);
		}
		
		if(xDec == 2)
		{
			this.setXPosition((int)(Math.random()*700) + 0);
			this.setYPosition(600);
		}
		
		if(xDec == 3)
		{
			this.setXPosition(0);
			this.setYPosition((int)(Math.random()*500) + 0);
		}
		
		if(xDec == 4)
		{
			this.setXPosition(800);
			this.setYPosition((int)(Math.random()*500) + 0);
		}
		
	}
	public void breakUp()
	{
		this.setWidth(this.width * .5);
		this.setHeight(this.height * .5);
	}
	public void drawMe(Graphics thingG)
	{
		thingG.setColor(thingColor);
		thingG.fillOval((int)xPosition, (int)yPosition, (int)width, (int)height);
	}
	public void highlightme(Graphics thingG)
	{
		thingG.setColor(Color.red);
		thingG.fillOval((int)xPosition, (int)yPosition, (int)width, (int)height);
	}
	public void eraseMe(Graphics thingG, Color canvasColor)
	{
		thingG.setColor(canvasColor);
		thingG.fillOval((int)(xPosition), (int)(yPosition), (int)width, (int)height);
		thingG.setColor(thingColor);
	}

	public void moveMe()
	{
		xPosition+=xSpeed;
		yPosition+=ySpeed;
	}
	
	public void fire(thing other)
	{
		other.setXPosition(xPosition + this.getWidth()/2 - other.getWidth()/2 + 1);
		other.setYPosition(yPosition + this.getHeight()/2 - other.getHeight()/2);
		
		//double y = yPos-(yPosition + height/2);		Finish later
		//double x = xPos-(xPosition + width/2);
		
		//double V = y/x;
		//double newXSpeed;
		//double newYSpeed;
		
		//newXSpeed = Math.sqrt(Math.pow(V, 2)/(Math.pow(0.1, 2)+1));
		//System.out.println(newXSpeed);
		
		//other.setYSpeed();
		//other.setXSpeed(newXSpeed);
	}
	public void setXSpeed(double newXSpeed)
	{
		xSpeed = newXSpeed;
	}

	public void setYSpeed(double newYSpeed)
	{
		ySpeed = newYSpeed;
	}

	public void setPosition(double newXPosition, double newYPosition)
	{
		xPosition = newXPosition;
		yPosition = newYPosition;
	}
	public void setXPosition(double newXPosition)
	{
		xPosition = newXPosition;
	}
	
	public void setYPosition(double newYPosition)
	{
		yPosition = newYPosition;
	}
	
	public double getXPosition()
	{
		return xPosition;
	}

	public double getYPosition()
	{
		return yPosition;
	}

	public double getXSpeed()
	{
		return xSpeed;
	}

	public double getYSpeed()
	{
		return ySpeed;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public void setWidth(double newWidth)
	{
		width = newWidth;
	}
	
	public void setHeight(double newHeight)
	{
		height = newHeight;
	}
	
	public Color getColor()
	{
		return thingColor;
	}
	
	public void setColor(Color newThingColor)
	{
		thingColor = newThingColor;
	}
	
	public void setAgression(double newAgression)
	{
		agression = newAgression;
	}
	
	public double getAgression()
	{
		return agression;
	}
	
	public boolean hits(thing other)
	{
		double centerX = xPosition + this.getWidth()/2;
		double centerY = yPosition + this.getWidth()/2;
		
		double oCenterX = other.getXPosition() + other.getWidth()/2;
		double oCenterY = other.getYPosition() + other.getWidth()/2;
		
		double theDistance = Math.sqrt(Math.pow(centerX-oCenterX, 2)+Math.pow(centerY-oCenterY, 2));
		
		if(Math.abs(theDistance)<=Math.abs((this.getWidth()+other.getWidth())/2))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void follow(thing other)
	{	
		double playerXPos = other.getXPosition() + other.getWidth()/2;
		double playerYPos = other.getYPosition() + other.getWidth()/2;
		
		if(xPosition - playerXPos < 0)
		{
			this.setXSpeed(agression);
		}
		
		if(xPosition - playerXPos > 0)
		{
			this.setXSpeed(-agression);
		}
		
		if(yPosition - playerYPos < 0)
		{
			this.setYSpeed(agression);
		}
		
		if(yPosition - playerYPos > 0)
		{
			this.setYSpeed(-agression);
		}
	}
	
	public void kick()
	{
		int kickNum = (int)(Math.random()*10);
		
		if(kickNum == 3 || kickNum == 4)
		{
			this.setXSpeed(agression * -1);
		}
		
		//if(kickNum == 4)
		//{
		//	this.setXSpeed(agression);
		//}
		
		if(kickNum == 2 || kickNum == 6)
		{
			this.setYSpeed(agression * -1);
		}
		
		//if(kickNum == 9)
		//{
		//	this.setYSpeed(agression);
		//}
	}
}
