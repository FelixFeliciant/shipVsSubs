package gameframework;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Ship {


	public BufferedImage shipImg;


	private final int healthInit = 100;
	//Ship Rockets
	private final int numberOfRocketsInit = 10;
	public int health;
	// Position of the Ship on the screen.
	public int xCoordinate;
	public int yCoordinate;
	public int numberOfRockets;
	//Moving speed
	private double movingXspeed;
	private double acceleratingXspeed;
	private double stoppingXspeed;


	/**
	 * Creates object of player.
	 *
	 * @param xCoordinate Starting x coordinate of Ship.
	 * @param yCoordinate Starting y coordinate of Ship.
	 */
	public Ship(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;

		LoadContent();
		Initialize();
	}


	/**
	 * Set variables and objects for this class.
	 */
	private void Initialize() {
		this.health = healthInit;

		this.numberOfRockets = numberOfRocketsInit;
		// this.numberOfAmmo = numberOfAmmoInit;

		this.movingXspeed = 0;
		this.acceleratingXspeed = 0.2;
		this.stoppingXspeed = 0.1;

	}


	/**
	 * Load files for this class.
	 */
	private void LoadContent() {

		try {
			URL shipImgUrl = this.getClass().getResource("/images/ship.gif");
			shipImg = ImageIO.read(Objects.requireNonNull(shipImgUrl));

		} catch (IOException ex) {
			Logger.getLogger(Submarine.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	/**
	 * Resets the player.
	 *
	 * @param xCoordinate Starting x coordinate of Ship.
	 * @param yCoordinate Starting y coordinate of Ship.
	 */
	public void Reset(int xCoordinate, int yCoordinate) {
		this.health = healthInit;

		this.numberOfRockets = numberOfRocketsInit;

		this.movingXspeed = 0;

	}


	/**
	 * Checks if player is fired a rocket. It also checks if player can
	 * fire a rocket (time between rockets, does a player have any rocket left).
	 *
	 * @param gameTime The current elapsed game time in nanoseconds.
	 * @return true if player is fired a rocket.
	 */
	public boolean isFiredRocket(long gameTime) {
		// Checks if right mouse button is down && if it is the time for new rocket && if he has any rocket left.
        return Canvas.keyboardKeyState(KeyEvent.VK_SPACE) &&
                ((gameTime - Rocket.timeOfLastCreatedRocket) >= Rocket.timeBetweenNewRockets) &&
                this.numberOfRockets > 0;
	}


	/**
	 * Checks if player moving Ship and sets its moving speed if player is moving.
	 */
	public void isMoving() {
		// Moving on the x coordinate.
		if (Canvas.keyboardKeyState(KeyEvent.VK_D) || Canvas.keyboardKeyState(KeyEvent.VK_RIGHT))
			movingXspeed += acceleratingXspeed;
		else if (Canvas.keyboardKeyState(KeyEvent.VK_A) || Canvas.keyboardKeyState(KeyEvent.VK_LEFT))
			movingXspeed -= acceleratingXspeed;
		else    // Stoping
			if (movingXspeed < 0)
				movingXspeed += stoppingXspeed;
			else if (movingXspeed > 0)
				movingXspeed -= stoppingXspeed;


	}

	public void Update() {


        xCoordinate = (int) (xCoordinate + movingXspeed);

	}

	public void draw(Graphics2D g2d) {
		//super.paintComponent (page);
		g2d.drawImage(shipImg, xCoordinate, yCoordinate, null);


	}


}
	
	
	
	
