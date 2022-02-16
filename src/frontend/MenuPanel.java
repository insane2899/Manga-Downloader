package frontend;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Manga;
import webscraper.WebScraper;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.LinkedList;

public class MenuPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension MENUPANEL_DIMENSION = new Dimension(400,1000);
	private final JLabel linkLabel,nameLabel,chapterLabel;
	private JTextField textField,textField2;
	private JButton search,download;
	private JScrollPane scrollPane;
	private JPanel chapterPanel;
	private JCheckBox selectAll,deselectAll;
	private List<String> downloadList = new LinkedList<>();
	private Manga manga;
	/**
	 * Create the application.
	 */
	public MenuPanel() {
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		this.setPreferredSize(MENUPANEL_DIMENSION);
		
		this.linkLabel = new JLabel("Enter the Link to the Manga:");
		linkLabel.setFont(new Font("Yrsa", Font.BOLD, 20));
		linkLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(linkLabel);
		
		this.textField = new JTextField();
		this.textField.setMaximumSize(new Dimension(350,40));
		textField.setAlignmentX(CENTER_ALIGNMENT);
		add(textField);
		
		this.nameLabel = new JLabel("Enter the Name of the Manga:");
		nameLabel.setFont(new Font("Yrsa", Font.BOLD, 20));
		nameLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(nameLabel);
		
		this.textField2 = new JTextField();
		this.textField2.setMaximumSize(new Dimension(350,40));
		textField2.setAlignmentX(CENTER_ALIGNMENT);
		add(textField2);
		
		this.search = new JButton("Search");
		search.setAlignmentX(CENTER_ALIGNMENT);
		add(search);
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchManga();
			}
		});
		
		this.selectAll = new JCheckBox("Select All Chapters");
		selectAll.setFont(new Font("Yrsa",Font.PLAIN,16));
		selectAll.setAlignmentX(CENTER_ALIGNMENT);
		add(selectAll);
		selectAll.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==1) {
					setChapterSelection(true);
				}
			}
		});
		
		this.deselectAll = new JCheckBox("Deselect All Chapters");
		deselectAll.setFont(new Font("Yrsa",Font.PLAIN,16));
		deselectAll.setAlignmentX(CENTER_ALIGNMENT);
		add(deselectAll);
		deselectAll.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==1) {
					setChapterSelection(false);
				}
			}
		});
		
		
		this.chapterLabel = new JLabel("Chapters:");
		chapterLabel.setFont(new Font("Yrsa", Font.BOLD, 20));
		chapterLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(chapterLabel);
		
		this.chapterPanel = new JPanel();
		chapterPanel.setLayout(new BoxLayout(chapterPanel,BoxLayout.PAGE_AXIS));
		chapterPanel.setSize(new Dimension(300,600));
		chapterPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		this.scrollPane = new JScrollPane(chapterPanel);
		scrollPane.setAlignmentX(CENTER_ALIGNMENT);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane);
		
		this.download = new JButton("Download");
		download.setAlignmentX(CENTER_ALIGNMENT);
		download.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				downloadChapters();
			}
		});
		add(download);
		
		this.setVisible(true);
	}
	
	private void searchManga() {
		chapterPanel.removeAll();
		//Perform Search using WebScraping
		this.manga = WebScraper.getManga(textField.getText(),textField2.getText());
		if(manga==null) {
			//Error display
			return;
		}
		
		
		//Add all checkboxes into the jpanel
		for(String s:manga.getChapterList()) {
			JCheckBox box = new JCheckBox(s);
			box.setFont(new Font("Yrsa",Font.ITALIC,20));
			box.setAlignmentX(LEFT_ALIGNMENT);
			box.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==1) {
						downloadList.add(s);
					}
					else if(e.getStateChange()==0) {
						if(downloadList.contains(s)) {
							downloadList.remove(s);
						}
					}
				}
			});
			chapterPanel.add(box);
		}
		SwingUtilities.invokeLater(()->{
			this.revalidate();
		});
	}
	
	private void downloadChapters() {
		//Perform Download using WebScraping
		
		WebScraper.downloadChapters(downloadList,manga);
		downloadList.clear();
		System.out.println("Done Downloading");
	}
	
	private void setChapterSelection(boolean selection) {
		Component[] components = chapterPanel.getComponents();
		for(int i=0;i<components.length;i++) {
			((JCheckBox)components[i]).setSelected(selection);
		}
	}
}
