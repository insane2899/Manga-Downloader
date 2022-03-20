package frontend;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sun.tools.javac.Main;

public class MangaDownloader {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MangaDownloader window = new MangaDownloader();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MangaDownloader() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(new Dimension(1200,800));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Manga Downloader");
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.PAGE_AXIS ));
		
		JLabel lblNewLabel = new JLabel("Manga Downloader");
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setMaximumSize(new Dimension(240, 30));
		frame.getContentPane().add(lblNewLabel);
		lblNewLabel.setFont(new Font("Z003", Font.BOLD | Font.ITALIC, 30));
		
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(1200,1000));
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		frame.getContentPane().add(panel);
		
		JLabel picLabel;
		
		try {
			BufferedImage img = ImageIO.read(MangaDownloader.class.getResourceAsStream("/static/intro.png"));
			picLabel = new JLabel(new ImageIcon(img));
			panel.add(picLabel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JButton btnNewButton = new JButton("Enter");
		btnNewButton.setMaximumSize(new Dimension(100,40));
		frame.getContentPane().add(btnNewButton);
		btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enterPressed();
			}
		});
	}
	
	private void enterPressed() {
		OpenFile of = new OpenFile();
		of.openFile();
		frame.dispose();
	}
	
}
