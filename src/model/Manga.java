package model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class Manga {
	private Map<String,String> chapterLinks;
	private List<String> chapterList;
	private String name;
	/*
	 * TODO
	 *	private String imageUrl;
	*/
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Manga() {
		this.chapterLinks = new HashMap<>();
		this.chapterList = new LinkedList<>();
	}
	
	public void addChapter(String chapterName,String chapterLink) {
		chapterList.add(chapterName);
		chapterLinks.put(chapterName,chapterLink);
	}
	
	public List<String> getChapterList(){
		return this.chapterList;
	}
	
	public String getChapterURL(String chapterName) {
		return chapterLinks.getOrDefault(chapterName, null);
	}
	
	
}
