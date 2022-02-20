package frontend;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CoverPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension COVER_PANEL_DIMENSION = new Dimension(800,950);
	private Image image;
	private Image scaledImage;
	
	public CoverPanel() {
		this.setPreferredSize(COVER_PANEL_DIMENSION);
		loadImage("static/page1.jpg");
		this.setVisible(true);
	}
	
	private void loadImage(String fileName) {
		try {
			image = ImageIO.read(new File(fileName));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		int width = this.getWidth();
		int height = this.getHeight();
		if(width>0 || height>0) {
			scaledImage = image.getScaledInstance(width-20, height-20, Image.SCALE_AREA_AVERAGING);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return image==null?new Dimension(200,200):COVER_PANEL_DIMENSION;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(scaledImage, 10,10,null);
	}
	

}
