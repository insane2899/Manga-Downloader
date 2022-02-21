package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

public class OpenFile {

	private JFrame frame;
	private static JPanel coverPanel, menuPanel;
	private static JSplitPane splitPane;
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
	
	public void initialize() {
		this.frame = new JFrame();
		frame.setTitle("Manga Downloader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setSize(OUTER_PANEL_DIMENSION);
		coverPanel = new CoverPanel();
		menuPanel = new MenuPanel();
		splitPane = new JSplitPane(SwingConstants.VERTICAL,menuPanel,coverPanel);
		splitPane.resetToPreferredSizes();
		//this.splitPane.setEnabled(true);
		//this.splitPane.setResizeWeight(1.0);
		frame.getContentPane().add(splitPane,BorderLayout.CENTER);
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
	
	protected static void setImage(String url) {
		((CoverPanel)coverPanel).setImage(url);
	}

}
