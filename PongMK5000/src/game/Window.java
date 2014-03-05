package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	JButton playButton = new JButton("Play");
	JButton exitButton = new JButton("Exit");

	private int windowWidth = 640;
	private int windowHeight = 480;

	public static void main(String[] args) {

		new Window();

	}
	
	public Window() {

		setResizable(false);
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setTitle("This version of Pong is still better than yours.");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);
		panel.setSize(windowWidth, windowHeight);
		getContentPane().add(panel);
		
		ImageIcon icon;
		JLabel picture;
		icon = new ImageIcon(this.getClass().getResource("Pongmenu.jpg"));
		picture = new JLabel(new ImageIcon(icon.getImage()));
		add(picture);

		playButton.setBounds((windowWidth / 2) - 50, (windowHeight / 2) - 30, 100, 30);
		playButton.setText("Play");
		playButton.setFocusable(false);
		panel.add(playButton);

		exitButton.setBounds((windowWidth / 2) - 35, (windowHeight / 2) + 10, 70, 30);
		exitButton.setText("Exit");
		exitButton.setFocusable(false);
		panel.add(exitButton);

		setVisible(true);

		playButton.addActionListener(this);
		exitButton.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playButton) {

			this.dispose();
			new Game();

		} else if (e.getSource() == exitButton) {
			System.exit(0);
		}

	}

	

}