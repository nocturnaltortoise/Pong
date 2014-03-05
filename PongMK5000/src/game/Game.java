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
	
	//game state variables for pausing the game and displaying score
	private final int GAME_PLAYING = 1;
	private final int GAME_PAUSED = 0;
	private final int GAME_ENDED = 2;
	private int gameState = GAME_PAUSED;

	//width and height of the game window
	private final int WIDTH = 640;
	private final int HEIGHT = 480;

	//paddle constants - starting positions, dimensions and movement speed
	private final int PADDLE_WIDTH = 16;
	private final int PADDLE_HEIGHT = 72;
	private final int PADDLE_START_Y = (HEIGHT - PADDLE_HEIGHT) / 2;
	private final int PADDLE1_START_X = PADDLE_WIDTH;
	private final int PADDLE2_START_X = WIDTH - (PADDLE_WIDTH * 2);
	private final int PADDLE_SPEED = 8;

	//ball constants
	private final int BALL_DIAMETER = 8;
	private final int BALL_START_X = (WIDTH - BALL_DIAMETER) / 2;
	private final int BALL_START_Y = (HEIGHT - BALL_DIAMETER) / 2;
	
	//ball variables - higher xvelocity than yvelocity means that the ball travels at an angle
	private double ballXVelocity = 3;
	private double ballYVelocity= 2.5;

	//score variables
	private int p1score = 0;
	private int p2score = 0;

	//rectangle objects for the paddles, the game background and the "ball"
	Rectangle ball = new Rectangle(BALL_START_X, BALL_START_Y, BALL_DIAMETER, BALL_DIAMETER);

	Rectangle paddle1 = new Rectangle(PADDLE1_START_X, PADDLE_START_Y, PADDLE_WIDTH, PADDLE_HEIGHT);
	Rectangle paddle2 = new Rectangle(PADDLE2_START_X, PADDLE_START_Y, PADDLE_WIDTH, PADDLE_HEIGHT);

	Rectangle screen = new Rectangle(0, 0, WIDTH, HEIGHT);

	//swing timer with a delay of 20ms and the Game object's actionListener
	Timer timer = new Timer(20, this);

	//array to store key presses for paddle controls 
	boolean[] keys = new boolean[256];

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

	public Clip audio() throws Exception {

		Clip clip = AudioSystem.getClip();
		try {

			AudioInputStream ais = AudioSystem.getAudioInputStream(this
					.getClass().getResource("beep-07.wav"));
			clip.open(ais);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return clip;

	}

	public void reset() {

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

	public void update() throws Exception {

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

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);

		g2d.setColor(Color.white);
		g2d.fill(paddle1);
		g2d.fill(paddle2);
		g2d.fill(ball);

		Stroke drawingStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
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
			g2d.drawString(message, (WIDTH - fm.stringWidth(message)) / 2, HEIGHT + fm.getAscent() / (2 - fm.getDescent()));
		}

	}

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

	public void keyReleased(KeyEvent e) {

		keys[e.getKeyCode()] = false;

	}

	public void keyTyped(KeyEvent e) {
	}

}