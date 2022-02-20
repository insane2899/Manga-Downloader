package frontend;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import model.Manga;
import webscraper.WebScraper;
import webscraper.Website;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

public class MenuPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension MENUPANEL_DIMENSION = new Dimension(1000,950);
	private final JLabel linkLabel;
	private JComboBox websiteDropdown;
	private JButton download;
	private JScrollPane chapterScrollPane,mangaScrollPane;
	private JSplitPane splitPane;
	private JPanel mangaPanel,chapterPanel;
	private JCheckBox selectAll,deselectAll;
	private List<String> downloadList = new LinkedList<>();
	private Manga manga;
	private List<String> websiteList;
	private TitledBorder chapterBorder,mangaBorder;
	/**
	 * Create the application.
	 */
	public MenuPanel() {
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		this.setPreferredSize(MENUPANEL_DIMENSION);
		
		this.linkLabel = new JLabel("Select a website:");
		linkLabel.setFont(new Font("Yrsa", Font.BOLD, 20));
		linkLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(linkLabel);
		
		websiteList = Website.importWebsiteList();
		this.websiteDropdown = new JComboBox(websiteList.toArray());
		websiteDropdown.setAlignmentX(CENTER_ALIGNMENT);
		websiteDropdown.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					importMangaList();
				}
			}
		});
		
		add(websiteDropdown);
		
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
		
		
		this.chapterBorder = new TitledBorder("Chapter List:");
		chapterBorder.setTitleJustification(TitledBorder.CENTER);
		chapterBorder.setTitlePosition(TitledBorder.TOP);
		this.chapterPanel = new JPanel();
		chapterPanel.setLayout(new BoxLayout(chapterPanel,BoxLayout.PAGE_AXIS));
		chapterPanel.setSize(new Dimension(300,600));
		chapterPanel.setBorder(chapterBorder);
		
		this.chapterScrollPane = new JScrollPane(chapterPanel);
		chapterScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		this.mangaBorder = new TitledBorder("Manga List:");
		mangaBorder.setTitleJustification(TitledBorder.CENTER);
		mangaBorder.setTitlePosition(TitledBorder.TOP);
		this.mangaPanel = new JPanel();
		mangaPanel.setLayout(new BoxLayout(mangaPanel,BoxLayout.PAGE_AXIS));
		mangaPanel.setSize(new Dimension(300,600));
		mangaPanel.setBorder(mangaBorder);
		
		this.mangaScrollPane = new JScrollPane(mangaPanel);
		mangaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);;
		
		this.splitPane = new JSplitPane(SwingConstants.VERTICAL,mangaScrollPane,chapterScrollPane);
		this.splitPane.resetToPreferredSizes();
		this.splitPane.setEnabled(false);
		this.splitPane.setResizeWeight(0.5);
		this.splitPane.setAlignmentX(CENTER_ALIGNMENT);
		add(splitPane);
		
		
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
	
	private void searchManga(String mangaUrl,String websiteName) {
		manga = Website.getManga(mangaUrl,websiteName);
		for(String s:manga.getChapterList()) {
			JCheckBox box = new JCheckBox(s);
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
	
	private void importMangaList() {
		String websiteName = websiteDropdown.getSelectedItem().toString();
		Map<String,String> mangaList = 
				Website.getMangaList(websiteName);
		for(Map.Entry<String,String> mapElements:mangaList.entrySet()) {
			JCheckBox box = new JCheckBox(mapElements.getKey());
			box.setAlignmentX(LEFT_ALIGNMENT);
			box.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==1) {
						chapterPanel.removeAll();
						manga = null;
						searchManga(mangaList.get(mapElements.getKey()),websiteName);
					}
					else if(e.getStateChange()==0) {
						chapterPanel.removeAll();
						manga=null;
					}
				}
			});
			mangaPanel.add(box);
		}
		SwingUtilities.invokeLater(()->{
			this.revalidate();
		});
	}
}
