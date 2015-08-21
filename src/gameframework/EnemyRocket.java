package gameframework;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class EnemyRocket {

	
	// Time that must pass before another rocket can be fired.
		public final static long timeBetweenNewRockets = Framework.secInNanosec / 4;
		public static long timeOfLastCreatedRocket = 0;

		// Damage that is made to an player ship when it is hit with a rocket.
		public static int damagePower = 100;

		// Rocket position
		public int xCoordinate;
		public int yCoordinate;

		// Moving speed and also direction. Rocket goes always straight, so we move
		// it only on x coordinate.
		private double movingYspeed;

		// Life time of current piece of rocket smoke.
		public long currentSmokeLifeTime;

		// Image of rocket. Image is loaded and set in Game class in LoadContent()
		// method.
		public static BufferedImage rocketImg;

		  /**
	     * Checks if the rocket is left the screen.
	     * 
	     * @return true if the rocket is left the screen, false otherwise.
	     */
	    public boolean isItOnScreen()
	    {
	        if(yCoordinate > 0 && yCoordinate < Framework.frameHeight) //Rocket moves only on y coordinate so we don't need to check x coordinate.
	            return false;
	        else
	            return true;
	    }

		public int getX() {return this.xCoordinate;}

		public int getY() {return this.yCoordinate;}

		

		 /**
	     * Set variables and objects for this class.
	     */
	    public void Initialize(int xCoordinate, int yCoordinate)
	    {
	        this.xCoordinate = xCoordinate;
	        this.yCoordinate = yCoordinate;
	        
	        this.movingYspeed = 15;
	        
	        this.currentSmokeLifeTime = Framework.secInNanosec / 2;
	    }
	    
		public void Update() {

			yCoordinate -= movingYspeed;

		}

		public void draw(Graphics2D g2d) {
			
			g2d.drawImage(rocketImg, xCoordinate, yCoordinate, null);
		}
	
	
	
	
	
	
	
	
	
	
	
	
}
