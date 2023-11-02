package gameframework;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * 
 */

public class Framework extends Canvas {
    
	
	
	
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    
    
    /**
     * Menu background.
     */
    private BufferedImage MenuImg;
    
    /**
     * Gameover background.
     */
    private BufferedImage GameoverImg;
    
    
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 60;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    public Framework() {
        super();

        gameState = GameState.VISUALIZING;

        //We start game in new thread.
        Thread gameThread = new Thread(this::GameLoop);
        gameThread.start();
    }
    /**
     * Current state of the game
     */
    public static GameState gameState;
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;
    
    // The actual game
    private Game game;
    
    private Font font;
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent()
    {


        try{
    		//Menu
            URL menuImgUrl = this.getClass().getResource("/images/Menu.gif");
            MenuImg = ImageIO.read(Objects.requireNonNull(menuImgUrl));


            URL GameoverImgUrl = this.getClass().getResource("/images/GameOver_Menu.gif");
            GameoverImg = ImageIO.read(Objects.requireNonNull(GameoverImgUrl));


        }
    	catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void Initialize() {
        font = new Font("monospaced", Font.BOLD, 28);
    }

    /**
     * Possible states of the game
     */
    public enum GameState {STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}


    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop()
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    
                    game.UpdateGame(gameTime, mousePosition());
                    
                    lastTime = System.nanoTime();
                break;
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                    //...
                break;
                case OPTIONS:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    // Sets variables and objects.
                    Initialize();
                    // Load files - images, sounds, ...
                    LoadContent();

                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                   
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            // Repaint the screen.
            repaint();
            
            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition(),gameTime);
                
                
            break;
            case GAMEOVER:
            	
            	
            	drawGameoverBackground(g2d);
            	g2d.setColor(Color.white);
            	
    			g2d.drawString("Press ENTER to restart or ESC to exit.", frameWidth / 2 - 113, frameHeight / 2 + 80);
    			game.DrawStatistic(g2d, gameTime);
    			g2d.setFont(font);
    			g2d.drawString("GAME OVER", frameWidth / 2 - 90, frameHeight / 2 + 60);
            	
            break;
            case MAIN_MENU:
                //...
            	 drawMenuBackground(g2d);
            	 g2d.setFont(font);
            	 g2d.setColor(Color.white);
            	 g2d.drawString("MAIN MENU", frameWidth / 2 - 100 , frameHeight / 4);
                 g2d.drawString("Press any key to start the Game", frameWidth / 2 - 250 , frameHeight / 4 + 40);
                 g2d.drawString("Press ESC to exit the Game", frameWidth / 2 - 250, frameHeight / 4 + 70);
                 
                 g2d.drawString("INSTRUCTIONS", frameWidth / 2 - 120 , frameHeight / 4 + 150);
                 g2d.drawString("Welcome to survival of the fittest", frameWidth / 2 - 250 , frameHeight / 4 + 180);
                 g2d.drawString("Use your Rockets Wisely", frameWidth / 2 - 250, frameHeight / 4 + 210);
                 g2d.drawString("Each new enemy will increase your rocket arsenal", frameWidth / 2 - 250 , frameHeight / 4 + 240);
                 g2d.drawString("The goal is to stay alive as long as possible", frameWidth / 2 - 250 , frameHeight / 4 + 270);
                 g2d.drawString("GOOD LUCK SAILOR!", frameWidth / 2 - 250 , frameHeight / 4 + 300);
                 
                 g2d.drawString("CONTROLS", frameWidth / 2 - 150 , frameHeight / 4 + 400);
                 g2d.drawString("USE A or Left arrow keys to move left", frameWidth / 2 - 250, frameHeight / 4 + 450);
                 g2d.drawString("USE D or Right arrow keys to move right", frameWidth / 2 - 250 , frameHeight / 4 + 480);
                 g2d.drawString("USE Spacebar to fire a rocket", frameWidth / 2 - 250 , frameHeight / 4 + 510);
            break;
            case OPTIONS:
                //...
            break;
            case GAME_CONTENT_LOADING:
                //...
            break;
        }
    }
    
    
    /**
     * Starts new game.
     */
    private void newGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
    	
    	game.gameMusic.loop();
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }
    
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
    	if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
    	
    	 switch (gameState)
         {
             case MAIN_MENU:
                 newGame();
             break;
             case GAMEOVER:
            	
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                     restartGame();
             break;
         }
        
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }
    
    private void drawMenuBackground(Graphics2D g2d) {
		g2d.drawImage(MenuImg, 0, 0, Framework.frameWidth,Framework.frameHeight, null);
		
		
	}
    
    private void drawGameoverBackground(Graphics2D g2d){
    	
    	g2d.drawImage(GameoverImg, 0, 0, Framework.frameWidth,Framework.frameHeight, null);
    	
    	
    }
    
    
    
    
}
