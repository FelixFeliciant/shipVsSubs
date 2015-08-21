package gameframework;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;



import sun.awt.RepaintArea;




public class Submarine {


	// For creating new enemies.
    private static final long timeBetweenNewEnemiesInit = Framework.secInNanosec * 3;
    public static long timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
    public static long timeOfLastCreatedEnemy = 0;
    
    
    // Health of the Submarine.
    public int health;
    
    // Position of the Submarine on the screen.
    public int xCoordinate;
    public int yCoordinate;
    
    // Moving speed and direction.
    private static final double movingXspeedInit = -4;
    private static double movingXspeed = movingXspeedInit;
         
    // Images of enemy Submarine. Images are loaded and set in Game class in LoadContent() method.
    public static BufferedImage submarineImg;
	
    
    //submarine rockets
    private final int numberOfRocketsInit = 5;
    public int numberOfRockets;
    
    
    
    /**
     * Checks if Submarine fired a rocket. It also checks if submarine can 
     * fire a rocket (time between rockets, does a player have any rocket left).
     * 
     * @param gameTime The current elapsed game time in nanoseconds.
     * @return true if player is fired a rocket.
     */
    public boolean isFiredRocket(long gameTime)
    {
     
    	
    	Random r = new Random();
    	int Low = 10;
    	int High = 200;
    	int R = r.nextInt(High-Low) + Low;
    	
   
    	if(10<R && R<13)
        {
            return true;
        } else
            return false;
    }
    
    
    
    
    
    

    /**
     * Initialize enemy Submarine.
     * 
     * @param xCoordinate Starting x coordinate of Submarine.
     * @param yCoordinate Starting y coordinate of Submarine.
     */
    public void Initialize(int xCoordinate, int yCoordinate)
    {
        health = 100;
        
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
       
        // Moving speed and direction of enemy.
        Submarine.movingXspeed = -4;
    }
    
	

    /**
     * It sets speed and time between enemies to the initial properties.
     */
    public static void restartEnemy(){
        Submarine.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        Submarine.timeOfLastCreatedEnemy = 0;
        Submarine.movingXspeed = movingXspeedInit;
    }
	
	
    /**
     * It increase enemy speed and decrease time between new enemies.
     */
    public static void speedUp(){
        if(Submarine.timeBetweenNewEnemies > Framework.secInNanosec)
            Submarine.timeBetweenNewEnemies -= Framework.secInNanosec / 100;
        
        Submarine.movingXspeed -= 0.25;
    }
	
	
	
    /**
     * Checks if the enemy is left the screen.
     * 
     * @return true if the enemy is left the screen, false otherwise.
     */
    
    public boolean isLeftScreen()
    {
        if(xCoordinate < 0 - submarineImg.getWidth()) // When the entire Submarine is out of the screen.
            return true;
        else
            return false;
    }
	
	
    
    /**
     * Updates position of Submarine, animations.
     */
    public void Update()
    {
        // Move enemy on x coordinate.
        xCoordinate += movingXspeed;
        
    }
  
	
    
    public void draw (Graphics2D g2d) {
    	
    	g2d.drawImage(submarineImg, xCoordinate, yCoordinate, null);
    	
    }
	
	

}
