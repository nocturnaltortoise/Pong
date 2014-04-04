package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	// game states
	private final int GAME_PLAYING = 1;
	private final int GAME_PAUSED = 0;
	private final int GAME_ENDED = 2;
	private int gameState = GAME_PAUSED;

	// window height and width
	private final int WIDTH = 640;
	private final int HEIGHT = 480;

	// paddle constants
	private final int PADDLE_WIDTH = 16;
	private final int PADDLE_HEIGHT = 72;
	private final int PADDLE_START_Y = (HEIGHT - PADDLE_HEIGHT) / 2;
	private final int PADDLE1_START_X = PADDLE_WIDTH;
	private final int PADDLE2_START_X = WIDTH - (PADDLE_WIDTH * 2);
	private final int PADDLE_SPEED = 8;

	// ball constants
	private final int BALL_DIAMETER = 8;
	private final int BALL_START_X = (WIDTH - BALL_DIAMETER) / 2;
	private final int BALL_START_Y = (HEIGHT - BALL_DIAMETER) / 2;

	// ball variables - higher xvelocity than yvelocity means that the ball
	// travels at an angle
	private double ballXVelocity = 3;
	private double ballYVelocity = 2.5;

	// score variables
	private int p1score = 0;
	private int p2score = 0;

	// rectangle objects for the paddles, the game background and the "ball"
	Rectangle ball = new Rectangle(BALL_START_X, BALL_START_Y, BALL_DIAMETER,
			BALL_DIAMETER);

	Rectangle paddle1 = new Rectangle(PADDLE1_START_X, PADDLE_START_Y, 

	        PADDLE_WIDTH, PADDLE_HEIGHT);
	
	Rectangle paddle2 = new Rectangle(PADDLE2_START_X, PADDLE_START_Y, 
	        PADDLE_WIDTH, PADDLE_HEIGHT);

	PADDLE_WIDTH, PADDLE_HEIGHT);
	Rectangle paddle2 = new Rectangle(PADDLE2_START_X, PADDLE_START_Y, 
	PADDLE_WIDTH, PADDLE_HEIGHT);


	Rectangle screen = new Rectangle(0, 0, WIDTH, HEIGHT);

	// swing timer with a delay of 20ms and the Game object's actionListener
	Timer timer = new Timer(20, this);

	// array to store key presses for paddle controls
	boolean[] keys = new boolean[256];

	/*
	 * This is the constructor for the Game class. As game extends JPanel, this
	 * method can use the this keyword to set the background colour of the game,
	 * and set the size based on the constant class variables defined earlier
	 * (WIDTH and HEIGHT). This constructor also makes a new Frame for the game,
	 * to replace the menu frame, and configures it correctly. This method also
	 * adds a keylistener to the frame, so the program listens for keypresses
	 * and adds them to the keys array. Finally the method starts the game timer
	 * so that the actionlistener will allow for key input to be added to the
	 * keys array.
	 */

	public Game() {

		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.black);

		JFrame frame = new JFrame("Pong");
		frame.setResizable(false);
		frame.setContentPane(this);
		frame.addKeyListener(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.timer.start();


	}

	/*
	 * This method controls the audio for Pong - it's separated into a different
	 * method to allow for easy setup of the audio, and for easy use of the
	 * audio file where it's needed, in the update method. Method has a lot of
	 * error protection - it can throw an exception if anything goes wrong, and
	 * has a try catch block to catch an error with the audio file (most likely
	 * caused by the file not existing). The audio file is a beep sound stored
	 * in a .wav file, in the bin folder (hence the use of this.getClass()). The
	 * method returns the audio clip (which is the beep sound) if no errors are
	 * thrown, so that the method can be called elsewhere to allow for properly
	 * encapsulated use of the audio file.
	 */

	private Clip audio() throws Exception {

		Clip clip = AudioSystem.getClip();
		try {

			AudioInputStream ais = AudioSystem.getAudioInputStream(this.

			        getClass().getResource("beep-07.wav"));

			getClass().getResource("beep-07.wav"));

			
			clip.open(ais);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return clip;

	}

	/*
	 * This method performs a simple job - it resets the position of the ball
	 * and the paddles on both the x and y axes, and then picks a random
	 * direction for the ball to travel in, by modifying the velocity of the
	 * ball.
	 */

	private void reset() {

		ball.x = BALL_START_X;
		ball.y = BALL_START_Y;
		paddle1.x = PADDLE1_START_X;
		paddle2.x = PADDLE2_START_X;
		paddle1.y = PADDLE_START_Y;
		paddle2.y = PADDLE_START_Y;

		int randNum = (int) (Math.random() * 2);

		if (randNum == 0) {
			ballXVelocity = 3;
			ballYVelocity = 2.5;
		} else {
			ballXVelocity = -3;
			ballYVelocity = 2.5;
		}

	}

	/*
	 * The update method is the longest, and also the most important method in
	 * the code. It has to also be able to throw an exception, as it uses the
	 * audio method from earlier. Firstly, it checks whether the game is paused
	 * - if so, it returns control to where the update method is called (in the
	 * actionPerformed method). If the game is playing and not paused, the keys
	 * array is checked for up and down keys, or the w and s keys. The paddles
	 * are then moved accordingly. The method then adds the ball's velocity to
	 * the ball's position to keep it moving. The method then checks for
	 * collision between ball and paddle, or paddle and wall, using the
	 * collision rectangles defined earlier. If the ball collides with a paddle,
	 * then it's velocity is not simply reversed but multiplied by a random
	 * number, so as to make the ball deflect at a different angle and velocity
	 * to the way it collided with the paddle. The audio method is called,
	 * playing the clip once using the loop method with a parameter of 0. If the
	 * ball hits the edge, it inverts the direction of the velocity of the ball,
	 * so it appears to bounce off. If the ball goes off the left or right edge
	 * without hitting a paddle, the score of the opposite player is
	 * incremented, and the reset method is called to replace the paddles and
	 * ball in their original positions. The game state is changed to game ended
	 * so the game pauses and displays the score.
	 */

	private void update() throws Exception {

		if (gameState != GAME_PLAYING) {
			return;
		}

		if (keys[KeyEvent.VK_UP]) {
			if (paddle2.y - PADDLE_SPEED >= 0) {
				paddle2.y -= PADDLE_SPEED;
			}
		}

		if (keys[KeyEvent.VK_DOWN]) {
			if (paddle2.y + PADDLE_HEIGHT + PADDLE_SPEED >= 0) {
				paddle2.y += PADDLE_SPEED;
			}
		}

		if (keys[KeyEvent.VK_W]) {
			if (paddle1.y - PADDLE_SPEED >= 0) {
				paddle1.y -= PADDLE_SPEED;
			}
		}

		if (keys[KeyEvent.VK_S]) {
			if (paddle1.y + PADDLE_HEIGHT + PADDLE_SPEED >= 0) {
				paddle1.y += PADDLE_SPEED;
			}
		}

		ball.x += ballXVelocity;
		ball.y += ballYVelocity;

		if (ball.intersects(paddle1)) {

			ball.x = paddle1.x + PADDLE_WIDTH;

			if (ballXVelocity <= 4 && ballYVelocity <= 3) {
				ballXVelocity = -(ballXVelocity * (Math.random() + 0.75));
				ballYVelocity = ballYVelocity * (Math.random() + 0.75);
			} else {
				ballXVelocity = -ballXVelocity;
			}

			audio().loop(0);

		} else if (ball.intersects(paddle2)) {

			ball.x = paddle2.x - PADDLE_WIDTH;

			if (ballXVelocity <= 4 && ballYVelocity <= 3) {
				ballXVelocity = -(ballXVelocity * (Math.random() + 0.75));
				ballYVelocity = ballYVelocity * (Math.random() + 0.75);
			} else {
				ballXVelocity = -ballXVelocity;
			}

			audio().loop(0);

		} else if (ball.x < 0) {

			reset();
			p2score++;

			gameState = GAME_ENDED;

		} else if (ball.x > WIDTH - BALL_DIAMETER) {

			reset();
			p1score++;

			gameState = GAME_ENDED;

		} else if (ball.y < 0) {

			ball.y = 0;
			ballYVelocity = -ballYVelocity;

			audio().loop(0);

		} else if (ball.y > HEIGHT - BALL_DIAMETER) {

			ball.y = HEIGHT - BALL_DIAMETER;
			ballYVelocity = -ballYVelocity;

			audio().loop(0);

		} else if (paddle1.y > HEIGHT - PADDLE_HEIGHT) {

			paddle1.y = paddle1.y - 8;

		} else if (paddle2.y > HEIGHT - PADDLE_HEIGHT) {

			paddle2.y = paddle2.y - 8;
		}

	}

	/*
	 * This method casts an existing Graphics object into a new instance of the
	 * Graphics2D class. It then calls the method paintComponent as the JPanel,
	 * using the super keyword to do this. The method then calls a series of
	 * methods from the Graphics2D class, and makes some new Objects, using the
	 * methods of the Graphics2D class to change key details, like making the
	 * line dashed with setStroke. If the game is paused, the method displays a
	 * message, depending on whether the game has ended or has been paused mid
	 * game with the space key.
	 */
	
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);

		g2d.setColor(Color.white);
		g2d.fill(paddle1);
		g2d.fill(paddle2);
		g2d.fill(ball);

		Stroke drawingStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, 

		        BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);

		BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
		

		Line2D line = new Line2D.Double(WIDTH / 2, 0, (WIDTH / 2) + 3, HEIGHT);

		g2d.setStroke(drawingStroke);
		g2d.draw(line);

		if (gameState != GAME_PLAYING) {
			String message;

			switch (gameState) {
			case GAME_PAUSED:
				message = "PAUSED. Press Space to continue.";
				break;
			case GAME_ENDED:
				message = p1score + ":" + p2score + " Press space to continue.";
				break;
			default:
				message = p1score + ":" + p2score + " Press space to continue.";
				break;
			}

			FontMetrics fm = g2d.getFontMetrics();

			g2d.setColor(Color.white);

			g2d.drawString(message, (WIDTH - fm.stringWidth(message)) / 2, 
			        HEIGHT + fm.getAscent() / (2 - fm.getDescent()));

			g2d.drawString(message, (WIDTH - fm.stringWidth(message)) / 2,
			HEIGHT + fm.getAscent() / (2 - fm.getDescent()));

		}

	}

	/*
	 * Method from the ActionListener interface. When the timer updates, the if
	 * statement evaluates to true and runs the update method, and then calls
	 * repaint, updating the graphics. In practice this runs on the delay set by
	 * the timer, and ensures smooth graphics without screen flashing.
	 */

	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == timer) {
			try {
				update();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			repaint();
		}

	}


	// checks for keypresses, adds keypresses to array of keys. Pauses on space
	// key press.

	/* checks for keypresses, adds keypresses to array of keys. Pauses on space
	 key press. */
	

	public void keyPressed(KeyEvent e) {

		keys[e.getKeyCode()] = true;

		if (gameState == GAME_PLAYING) {
			if (keys[KeyEvent.VK_SPACE]) {
				gameState = GAME_PAUSED;
			}
		} else {
			if (keys[KeyEvent.VK_SPACE]) {
				gameState = GAME_PLAYING;
			}
		}

	}


	// stops adding keys to array when key is released, preventing paddle from
	// keeping moving when key is not held down.

	/* stops adding keys to array when key is released, preventing paddle from
	 keeping moving when key is not held down. */
	

	public void keyReleased(KeyEvent e) {

		keys[e.getKeyCode()] = false;

	}


	// keytyped is just here because KeyListener is an interface, and so all
	// it's methods need to be implemented.

	/* keytyped is just here because KeyListener is an interface, and so all
	 it's methods need to be implemented. */

	public void keyTyped(KeyEvent e) {
	}

}


