package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* extends JFrame to allow for less static referencing - no point creating 
 a new named instance of jFrame for the sake of static referencing when 
jFrame could just be inherited. */

public class Window extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	JButton playButton = new JButton("Play");
	JButton exitButton = new JButton("Exit");

	/* 640 x 480 dimensions picked because they're large enough to play a
	 reasonable game, small enough to look retro.
 	Plus, large dimensions don't benefit primitive graphics. */

	private int windowWidth = 640;
	private int windowHeight = 480;

	//main method for the whole program - makes a new instance of the Window class. 

	
	private int windowWidth = 640;
	private int windowHeight = 480;

	//main method for the whole program - makes a new instance of the 
	//Window class. 

	public static void main(String[] args) {

		new Window();

	}

	//constructor for the window class, ran when a new instance of Window is 
	//created.

	public Window() {

		// creating basic window of the right size, setting title etc
		this.setResizable(false);
		this.setSize(windowWidth, windowHeight);
		this.setLocationRelativeTo(null);
		this.setTitle("This version of Pong is still better than yours.");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//make and configure the menu panel.
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);
		panel.setSize(windowWidth, windowHeight);
		getContentPane().add(panel);

		// adding background image for the menu
		ImageIcon icon;
		JLabel picture;
		icon = new ImageIcon(this.getClass().getResource("Pongmenu.jpg"));
		picture = new JLabel(new ImageIcon(icon.getImage()));
		this.add(picture);

		// making the menu buttons

		playButton.setBounds((windowWidth / 2) - 50, 
		        (windowHeight / 2) - 30, 100, 30);

		playButton.setBounds((windowWidth / 2) - 50, (windowHeight / 2) - 30,
		100, 30);
		

		playButton.setText("Play");
		playButton.setFocusable(false);
		panel.add(playButton);


		exitButton.setBounds((windowWidth / 2) - 35, 
		        (windowHeight / 2) + 10, 70, 30);

		exitButton.setBounds((windowWidth / 2) - 35, (windowHeight / 2) + 10, 
		70, 30);
		

		exitButton.setText("Exit");
		exitButton.setFocusable(false);
		panel.add(exitButton);
		
		this.setVisible(true);

		
		//adding listeners for button clicks

		playButton.addActionListener(this);
		exitButton.addActionListener(this);

	}

	/* Method from the ActionListener interface, checks for user clicking on the play or quit buttons, gets rid of the menu frame
	 * on play button click and then makes a new Game instance. */

	public void actionPerformed(ActionEvent e) {

		// if the play button is clicked, start a new game, get rid of the menu
		if (e.getSource() == playButton) {

			this.dispose();
			new Game();
			// if the exit button's clicked, call System.exit to terminate the program.
		} else if (e.getSource() == exitButton) {
			System.exit(0);
		}

	}

}
