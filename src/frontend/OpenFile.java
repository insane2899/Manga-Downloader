package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class OpenFile {

	private JFrame frame;
	private JPanel coverPanel, menuPanel;
	private JSplitPane splitPane;
	private static Dimension OUTER_PANEL_DIMENSION = new Dimension(1600,950);
	
	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public OpenFile() {
		initialize();
	}
	
	private void initialize() {
		this.frame = new JFrame();
		frame.setTitle("Manga Downloader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setSize(OUTER_PANEL_DIMENSION);
		this.coverPanel = new CoverPanel();
		this.menuPanel = new MenuPanel();
		this.splitPane = new JSplitPane(SwingConstants.VERTICAL,menuPanel,coverPanel);
		this.splitPane.resetToPreferredSizes();
		//this.splitPane.setEnabled(true);
		//this.splitPane.setResizeWeight(1.0);
		this.frame.getContentPane().add(splitPane,BorderLayout.CENTER);
		this.frame.pack();
	}
	
	public void openFile() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OpenFile window = new OpenFile();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
