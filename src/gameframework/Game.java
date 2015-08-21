package gameframework;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JApplet;

/**
 * Actual game.
 * 
 * 
 */



public class Game {

	//Gameover sound
	private AudioClip gameOver;
	
	//Gamemusic sound
	public AudioClip gameMusic;
	
	//ship explosion sound
	private AudioClip shipExplosion;
	
	//sub explosion sound
	private AudioClip SubExplosion;
	
	 // Use this to generate a random number.
    private Random random;
    
    // We will use this for seting mouse position.
    private Robot robot;
    
    // Player - Ship that is managed by player.
    private Ship player;
    
    // Enemy Submarine.
    private ArrayList<Submarine> enemySubmarineList = new ArrayList<Submarine>();
    
    // Explosions
    private ArrayList<Animation> explosionsList;
    private BufferedImage explosionAnimImg;
    
    private BufferedImage  seaImg;
    
    // List of all the player rockets.
    private ArrayList<Rocket> rocketsList;
    
    private ArrayList<EnemyRocket> enemyrocketList;
    
    
    // List of all the rockets smoke.
    private ArrayList<RocketSmoke> rocketSmokeList;
    
    // Image for the sky color.
    private BufferedImage skyColorImg;
    
    
  
    
    // Font that we will use to write statistic to the screen.
    private Font font;
    
    // Statistics (destroyed enemies, run away enemies)
    private int runAwayEnemies;
    private int destroyedEnemies;
    
	
	public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        
        
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
            	 // Load game files (images, sounds, ...)
                LoadContent();
            	
            	// Sets variables and objects for the game.
                Initialize();
               
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
	
	
	
	
	
	
	
	
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
    	
    	
    	gameMusic.loop();
    	
    	
	random = new Random();
        
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        player = new Ship(Framework.frameWidth / 4, (Framework.frameHeight / 4)+100);
        
        enemySubmarineList = new ArrayList<Submarine>();
        
        explosionsList = new ArrayList<Animation>();
        
    
        
        rocketsList = new ArrayList<Rocket>();
        
        enemyrocketList = new ArrayList<EnemyRocket>();
        
        
        rocketSmokeList = new ArrayList<RocketSmoke>();
        
        
        font = new Font("monospaced", Font.BOLD, 30);
        
        runAwayEnemies = 0;
        destroyedEnemies = 0;
    
    	
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {

    	try
        {
    		
    		
    		
    		
    		//Game sound
    		URL gameSound= Game.class.getResource("/Sounds/Waterfall_Theme.wav");
            gameMusic = Applet.newAudioClip(gameSound);
    		//Ship explosion sound
            URL ShipeExplosionSound= Game.class.getResource("/Sounds/ship_explosion.wav");
            shipExplosion = Applet.newAudioClip(ShipeExplosionSound);
            //enemy explosion sound
            URL SubExplosionSound= Game.class.getResource("/Sounds/sub_explosion.wav");
            SubExplosion = Applet.newAudioClip(SubExplosionSound);
            //Game over sound
            URL gameOverSound= Game.class.getResource("/Sounds/Game_Over.wav");
            gameOver = Applet.newAudioClip(gameOverSound);
            
            
    		//background
            URL seaImgUrl = this.getClass().getResource("/Images/sea.gif");
            seaImg = ImageIO.read(seaImgUrl);
            
            
            //Images of enemy submarine
            URL submarineImgUrl = this.getClass().getResource("/Images/submarine3.gif");
            Submarine.submarineImg = ImageIO.read(submarineImgUrl);
           
            
            // Images of rocket and its smoke.
            
            URL enemyrocketImgUrl = this.getClass().getResource("/Images/rocket3.png");
            EnemyRocket.rocketImg = ImageIO.read(enemyrocketImgUrl);
            		
            		
            URL rocketImgUrl = this.getClass().getResource("/Images/rocket2.png");
            Rocket.rocketImg = ImageIO.read(rocketImgUrl);
          
            URL rocketSmokeImgUrl = this.getClass().getResource("/Images/rocket_smoke.png");
            RocketSmoke.smokeImg = ImageIO.read(rocketSmokeImgUrl);
           
            
            
            // Imege of explosion animation.
            URL explosionAnimImgUrl = this.getClass().getResource("/Images/explosion_anim.png");
            explosionAnimImg = ImageIO.read(explosionAnimImgUrl);
     
            
        }
        catch (IOException ex) {
            Logger.getLogger(Submarine.class.getName()).log(Level.SEVERE, null, ex);
        }
    	
    	
    }    
    
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
    	player.Reset(Framework.frameWidth / 4, Framework.frameHeight / 4);
        
        Submarine.restartEnemy();
        
        
        Rocket.timeOfLastCreatedRocket = 0;
        
        EnemyRocket.timeOfLastCreatedRocket = 0;
        
        // Empty all the lists.
        enemySubmarineList.clear();
        
        
        enemyrocketList.clear();
        rocketsList.clear();
        rocketSmokeList.clear();
        explosionsList.clear();
        
        // Statistics
        runAwayEnemies = 0;
        destroyedEnemies = 0;
      }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
    	
    	
    	
    	
    	if( !isPlayerAlive() && explosionsList.isEmpty() ){
    		
    		
    		gameMusic.stop();
    		gameOver.play();
            Framework.gameState = Framework.GameState.GAMEOVER;
            return; // If player is destroyed, we don't need to do thing below.
        }
    	
    	if(isPlayerAlive()){
            didPlayerFiredRocket(gameTime);
            didEnemyFiredRocket(gameTime);
            player.isMoving();
            player.Update();
        }
        
    	updateRockets(gameTime);
    	
    	updateRocketSmoke(gameTime);
        
        /* Enemies */
        createEnemysubmarine(gameTime);
        updateEnemyRockets(gameTime);
        updateEnemies(gameTime);
        
        /* Explosions */
        updateExplosions();
    	
        
    	
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    
    
    
    public void Draw(Graphics2D g2d, Point mousePosition, long gameTime)
    {
    	g2d.drawImage(seaImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
    	
    	if(isPlayerAlive())
            player.draw(g2d);
    	
     
    	// Draws all the enemies.
        for(int i = 0; i < enemySubmarineList.size(); i++)
        {
            enemySubmarineList.get(i).draw(g2d);
        }
        
        // Draws all the enemy rockets. 
        for(int i = 0; i < enemyrocketList.size(); i++)
        {
            enemyrocketList.get(i).draw(g2d);
        }
        
        // Draws all the rockets. 
        for(int i = 0; i < rocketsList.size(); i++)
        {
            rocketsList.get(i).draw(g2d);
        }
        // Draws smoke of all the rockets.
        for(int i = 0; i < rocketSmokeList.size(); i++)
        {
            rocketSmokeList.get(i).Draw(g2d);
        }
        
        // Draw all explosions.
        for(int i = 0; i < explosionsList.size(); i++)
        {
            explosionsList.get(i).Draw(g2d);
        }
    	
    	
        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        
        g2d.drawString(formatTime(gameTime), Framework.frameWidth/2 - 45, 21);
        g2d.drawString("DESTROYED: " + destroyedEnemies, 10, 21);
        g2d.drawString("RUNAWAY: "   + runAwayEnemies,   10, 51);
        g2d.drawString("ROCKETS: "   + player.numberOfRockets, 10, 81);
        g2d.drawString("ENEMY SUBMARINES: "   + enemySubmarineList.size(), 10, 151);
      
        
        
    }






    /**
     * Draws some game statistics when game is over.
     * 
     * @param g2d Graphics2D
     * @param gameTime Elapsed game time.
     */
    public void DrawStatistic(Graphics2D g2d, long gameTime){
        g2d.drawString("Time: " + formatTime(gameTime),                Framework.frameWidth/2 - 80, Framework.frameHeight/2 + 140);
        g2d.drawString("Rockets left: "      + player.numberOfRockets, Framework.frameWidth/2 - 80, Framework.frameHeight/2 + 160);
        g2d.drawString("Destroyed enemies: " + destroyedEnemies,       Framework.frameWidth/2 - 80, Framework.frameHeight/2 + 180);
        g2d.drawString("Runaway enemies: "   + runAwayEnemies,         Framework.frameWidth/2 - 80, Framework.frameHeight/2 + 200);
        g2d.setFont(font);
        g2d.drawString("Statistics: ",                                 Framework.frameWidth/2 - 85, Framework.frameHeight/2 + 120);
    }
    
    

    /**
     * Format given time into 00:00 format.
     * 
     * @param time Time that is in nanoseconds.
     * @return Time in 00:00 format.
     */
    private static String formatTime(long time){
            // Given time in seconds.
            int sec = (int)(time / Framework.milisecInNanosec / 1000);

            // Given time in minutes and seconds.
            int min = sec / 60;
            sec = sec - (min * 60);

            String minString, secString;

            if(min <= 9)
                minString = "0" + Integer.toString(min);
            else
                minString = "" + Integer.toString(min);

            if(sec <= 9)
                secString = "0" + Integer.toString(sec);
            else
                secString = "" + Integer.toString(sec);

            return minString + ":" + secString;
    }

/*
    
    * Methods for updating the game. 
    * 
    */
   
    
   /**
    * Check if player is alive. If not, set game over status.
    * 
    * @return True if player is alive, false otherwise.
    */
   private boolean isPlayerAlive()
   {
       if(player.health <= 0)
           return false;
       
       return true;
   }
   
   
   
   
   /**
    * Checks if the player is fired the rocket and creates it if he did.
    * It also checks if player can fire the rocket.
    * 
    * @param gameTime Game time.
    */
   private void didPlayerFiredRocket(long gameTime)
   {
       if(player.isFiredRocket(gameTime))
       {
           Rocket.timeOfLastCreatedRocket = gameTime;
           player.numberOfRockets--;
           
           Rocket r = new Rocket();
           r.Initialize(player.xCoordinate, player.yCoordinate);
           rocketsList.add(r);
       }
   }
   
  /**
   * Check if enemy fired a rocket and create it if he did.
   */
   
   private void didEnemyFiredRocket(long gameTime)
   {
	   for(int j = 0; j < enemySubmarineList.size(); j++){
		   Submarine eh = enemySubmarineList.get(j);
	   
       if(eh.isFiredRocket(gameTime)){
       
           EnemyRocket.timeOfLastCreatedRocket = gameTime;
         
           
          EnemyRocket r = new EnemyRocket();
           r.Initialize(eh.xCoordinate, eh.yCoordinate);
           enemyrocketList.add(r);
       }
	   
	   }   
   }
   
   
   
   
   
   /**
    * Creates a new enemy if it's time.
    * 
    * @param gameTime Game time.
    */
   private void createEnemysubmarine(long gameTime)
   {
       if(gameTime - Submarine.timeOfLastCreatedEnemy >= Submarine.timeBetweenNewEnemies)
       {
           Submarine eh = new Submarine();
           int xCoordinate = Framework.frameWidth;
           int yCoordinate = random.nextInt((Framework.frameHeight - Submarine.submarineImg.getHeight()-480))+480;
          
           eh.Initialize(xCoordinate, yCoordinate);
           // Add created enemy to the list of enemies.
           enemySubmarineList.add(eh);
           player.numberOfRockets +=1;
           // Speed up enemy speed and aperence.
           Submarine.speedUp();
           
           // Sets new time for last created enemy.
           Submarine.timeOfLastCreatedEnemy = gameTime;
       }
   }
   
   /**
    * Updates all enemies.
    * Move the subs and checks if he left the screen.
    * Updates subs animations.
    * Checks if enemy was destroyed.
    * Checks if any enemy collision with player.
    */
   private void updateEnemies(long gameTime)
   {
	   
	   
       for(int i = 0 ; i < enemySubmarineList.size() ; i++)
       {
           Submarine eh = enemySubmarineList.get(i);
       
           eh.Update();
       
           
         
           
           
           
           
           // Is rocket enemy hit the player?
           Rectangle playerRectangel = new Rectangle(player.xCoordinate, player.yCoordinate, player.shipImg.getWidth(), player.shipImg.getHeight());
           Rectangle enemyRectangel = new Rectangle(eh.xCoordinate, eh.yCoordinate, Submarine.submarineImg.getWidth(), Submarine.submarineImg.getHeight());
           if(playerRectangel.intersects(enemyRectangel)){
               player.health = 0;
               
               // Remove Sub from the list.
               enemySubmarineList.remove(i);
               
               // Add explosion of player ship.
               for(int exNum = 0; exNum < 3; exNum++){
                   Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, player.xCoordinate + exNum*60, player.yCoordinate - random.nextInt(100), exNum * 200 +random.nextInt(100));
                   explosionsList.add(expAnim);
               }
               // Add explosion of enemy sub.
               for(int exNum = 0; exNum < 3; exNum++){
                   Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, eh.xCoordinate + exNum*60, eh.yCoordinate - random.nextInt(100), exNum * 200 +random.nextInt(100));
                   explosionsList.add(expAnim);
               }
               
               // Because player crashed with enemy the game will be over so we don't need to check other enemies.
               break;
           }
           
           // Check health.
           if(eh.health <= 0){
               // Add explosion of sub.
        	   
        	   SubExplosion.play();
               Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, eh.xCoordinate, eh.yCoordinate - explosionAnimImg.getHeight()/3, 0); 
               explosionsList.add(expAnim);

               // Increase the destroyed enemies counter.
               destroyedEnemies++;
               
               // Remove sub from the list.
               enemySubmarineList.remove(i);
               
               // sub was destroyed so we can move to next sub.
               continue;
           }
           
           // If the current enemy is left the screen we remove him from the list and update the runAwayEnemies variable.
           if(eh.isLeftScreen())
           {
               enemySubmarineList.remove(i);
               runAwayEnemies++;
           }
       }
   }
   
  
  

   /**
    * Update rockets. 
    * It moves rocket and add smoke behind it.
    * Checks if the rocket is left the screen.
    * Checks if any rocket is hit any enemy.
    * 
    * @param gameTime Game time.
    */
   private void updateRockets(long gameTime)
   {
       for(int i = 0; i < rocketsList.size(); i++)
       {
           Rocket rocket = rocketsList.get(i);
           
           // Moves the rocket.
           rocket.Update();
           
           // Checks if it is left the screen.
           if(rocket.isItOnScreen())
           {
               rocketsList.remove(i);
               // Rocket left the screen so we removed it from the list and now we can continue to the next rocket.
               continue;
           }
           
           // Creates a rocket smoke.
           RocketSmoke rs = new RocketSmoke();
           int xCoordinate = rocket.xCoordinate - RocketSmoke.smokeImg.getWidth(); // Subtract the size of the rocket smoke image (rocketSmokeImg.getWidth()) so that smoke isn't drawn under/behind the image of rocket.
           int yCoordinte = rocket.yCoordinate - 5 + random.nextInt(6); // Subtract 5 so that smok will be at the middle of the rocket on y coordinate. We rendomly add a number between 0 and 6 so that the smoke line isn't straight line.
           rs.Initialize(xCoordinate, yCoordinte, gameTime, rocket.currentSmokeLifeTime);
           rocketSmokeList.add(rs);
           
           // Because the rocket is fast we get empty space between smokes so we need to add more smoke. 
           // The higher is the speed of rockets, the bigger are empty spaces.
           int smokePositionX = 5 + random.nextInt(8); // We will draw this smoke a little bit ahead of the one we draw before.
           rs = new RocketSmoke();
           xCoordinate = rocket.xCoordinate - RocketSmoke.smokeImg.getWidth() + smokePositionX; // Here we need to add so that the smoke will not be on the same x coordinate as previous smoke. First we need to add 5 because we add random number from 0 to 8 and if the random number is 0 it would be on the same coordinate as smoke before.
           yCoordinte = rocket.yCoordinate - 5 + random.nextInt(6); // Subtract 5 so that smok will be at the middle of the rocket on y coordinate. We rendomly add a number between 0 and 6 so that the smoke line isn't straight line.
           rs.Initialize(xCoordinate, yCoordinte, gameTime, rocket.currentSmokeLifeTime);
           rocketSmokeList.add(rs);
           
           // Increase the life time for the next piece of rocket smoke.
           rocket.currentSmokeLifeTime *= 1.02;
           
           // Checks if current rocket hit any enemy.
           if( checkIfRocketHitEnemy(rocket) )
               // Rocket was also destroyed so we remove it.
               rocketsList.remove(i);
       }
   }
   
   
   
   private void updateEnemyRockets (long gameTime){
	   
	   for(int i = 0; i < enemyrocketList.size(); i++)
       {
           EnemyRocket Erocket = enemyrocketList.get(i);
           
           // Moves the enemy rocket.
           Erocket.Update();
           
           // Checks if it is left the screen.
           if(Erocket.isItOnScreen())
           {
               enemyrocketList.remove(i);
               // Rocket left the screen so we removed it from the list and now we can continue to the next rocket.
               continue;
           }
           
           // Creates a rocket smoke.
           RocketSmoke rs = new RocketSmoke();
           int xCoordinate = Erocket.xCoordinate - RocketSmoke.smokeImg.getWidth(); // Subtract the size of the rocket smoke image (rocketSmokeImg.getWidth()) so that smoke isn't drawn under/behind the image of rocket.
           int yCoordinte = Erocket.yCoordinate - 5 + random.nextInt(6); // Subtract 5 so that smok will be at the middle of the rocket on y coordinate. We rendomly add a number between 0 and 6 so that the smoke line isn't straight line.
           rs.Initialize(xCoordinate, yCoordinte+50, gameTime, Erocket.currentSmokeLifeTime);
           rocketSmokeList.add(rs);
           
           // Because the rocket is fast we get empty space between smokes so we need to add more smoke. 
           // The higher is the speed of rockets, the bigger are empty spaces.
           int smokePositionX = 5 + random.nextInt(8); // We will draw this smoke a little bit ahead of the one we draw before.
           rs = new RocketSmoke();
           xCoordinate = Erocket.xCoordinate - RocketSmoke.smokeImg.getWidth() + smokePositionX; // Here we need to add so that the smoke will not be on the same x coordinate as previous smoke. First we need to add 5 because we add random number from 0 to 8 and if the random number is 0 it would be on the same coordinate as smoke before.
           yCoordinte = Erocket.yCoordinate - 5 + random.nextInt(6); // Subtract 5 so that smok will be at the middle of the rocket on y coordinate. We rendomly add a number between 0 and 6 so that the smoke line isn't straight line.
           rs.Initialize(xCoordinate, yCoordinte+50, gameTime, Erocket.currentSmokeLifeTime);
           rocketSmokeList.add(rs);
           
           // Increase the life time for the next piece of rocket smoke.
           Erocket.currentSmokeLifeTime *= 1.02;
           
           // Checks if current rocket hit any enemy.
           if( checkIfRocketHitPlayer(Erocket) ){
               // Rocket was also destroyed so we remove it.
        	   
        	   shipExplosion.play();
        	   
               enemyrocketList.remove(i);
               player.health = 0;
        	   
        	  
        	   for(int exNum = 0; exNum < 3; exNum++){
                   Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, player.xCoordinate + exNum*60, player.yCoordinate - random.nextInt(100), exNum * 200 +random.nextInt(100));
                   explosionsList.add(expAnim);
               }
               
             
               
               
               break;
        	   
           }
           
       }
   }
	   
	   
   
   
   
   /**
    * Checks if the given rocket is hit any of enemy subs.
    * 
    * @param rocket Rocket to check.
    * @return True if it hit any of enemy subs, false otherwise.
    */
   private boolean checkIfRocketHitEnemy(Rocket rocket)
   {
       boolean didItHitEnemy = false;
       
     
       Rectangle rocketRectangle = new Rectangle(rocket.xCoordinate, rocket.yCoordinate, 2, Rocket.rocketImg.getHeight());
       
       // Go trough all enemis.
       for(int j = 0; j < enemySubmarineList.size(); j++)
       {
           Submarine eh = enemySubmarineList.get(j);

           // Current enemy rectangle.
           Rectangle enemyRectangel = new Rectangle(eh.xCoordinate, eh.yCoordinate, Submarine.submarineImg.getWidth(), Submarine.submarineImg.getHeight());

           // Is current rocket over currnet enemy?
           if(rocketRectangle.intersects(enemyRectangel))
           {
               didItHitEnemy = true;
               
               // Rocket hit the enemy so we reduce his health.
               eh.health -= Rocket.damagePower;
               
               // Rocket hit enemy so we don't need to check other enemies.
               break;
           }
       }
       
       return didItHitEnemy;
   }
   
   /**
    * Check if enemy rocket hit the player.
    * @param Erocket
    * @return
    */
   
   private boolean checkIfRocketHitPlayer(EnemyRocket Erocket)
   {
       boolean didItHitPlayer = false;
       
       
       

           // Current enemy rectangle.
           Rectangle playerRectangel = new Rectangle(player.xCoordinate, player.yCoordinate, player.shipImg.getWidth(), player.shipImg.getHeight());
           
           for (int i = 0; i<enemyrocketList.size();i++){
        	   
        	 Rectangle ErocketRectangle = new Rectangle(Erocket.xCoordinate, Erocket.yCoordinate, 2, EnemyRocket.rocketImg.getHeight()); 	   
           // Is current rocket over currnet enemy?
           if(ErocketRectangle.intersects(playerRectangel))
           {
               didItHitPlayer = true;
               
               // Rocket hit the enemy so we reduce his health.
               player.health -= EnemyRocket.damagePower;
               
               // Rocket hit enemy so we don't need to check other enemies.
               break;
           }
       }
       
       return didItHitPlayer;
   }
   
    
   
   
   
   
   /**

    * Updates smoke of all the rockets.
    * If the life time of the smoke is over then we delete it from list.
    * It also changes a transparency of a smoke image, so that smoke slowly disappear.
    * 
    * @param gameTime Game time.
    */
   
     
   private void updateRocketSmoke(long gameTime)
   {
       for(int i = 0; i < rocketSmokeList.size(); i++)
       {
           RocketSmoke rs = rocketSmokeList.get(i);
           
           // Is it time to remove the smoke.
           if(rs.didSmokeDisapper(gameTime))
               rocketSmokeList.remove(i);
           
           // Set new transparency of rocket smoke image.
           rs.updateTransparency(gameTime);
       }
   }
   
   /**
    * Updates all the animations of an explosion and remove the animation when is over.
    */
   private void updateExplosions()
   {
       for(int i = 0; i < explosionsList.size(); i++)
       {
           // If the animation is over we remove it from the list.
           if(!explosionsList.get(i).active)
               explosionsList.remove(i);
       }
   }
   
  























}
