package webscraper;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import model.Manga;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Website {
	
	private static FileReader fileReader;
	private static Properties websiteDetails;
	static {
		try {
			fileReader = new FileReader("static/website.properties");
			websiteDetails = new Properties();
			websiteDetails.load(fileReader);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private Website() {}
	
	public static Map<String,String> getMangaList(String websiteName){
		String website = websiteDetails.getProperty(websiteName+"-Website");
		String websiteListString = websiteDetails.getProperty(websiteName+"-Manga-List");
		String paginationString = websiteDetails.getProperty(websiteName+"-Manga-List-Pagination");
		//System.out.println(websiteListString+"\n"+paginationString);
		StringTokenizer mangaList = new StringTokenizer(websiteListString,";");
		String mangaClassName = mangaList.nextToken();
		String mangaItemName = mangaList.nextToken();
		StringTokenizer paginationList = new StringTokenizer(paginationString,";");
		String paginationClassName = paginationList.nextToken();
		String paginationItemName = paginationList.nextToken();
		//System.out.println(website+" "+mangaClassName+" "+ mangaItemName+" "+paginationClassName+" "+paginationItemName);
		return WebScraper.getMangaListBFS(website, mangaClassName, mangaItemName,
				paginationClassName,paginationItemName);
	}
	
	public static Manga getManga(String url,String websiteName){
		String mangaTitle = websiteDetails.getProperty(websiteName+"-Manga-Title");
		String mangaChapters = websiteDetails.getProperty(websiteName+"-Manga-Chapters");
		String coverImage = websiteDetails.getProperty(websiteName+"-Manga-CoverImage");
		StringTokenizer title = new StringTokenizer(mangaTitle,";");
		StringTokenizer chapters = new StringTokenizer(mangaChapters,";");
		StringTokenizer cover = new StringTokenizer(coverImage,";");
		//System.out.println(coverImage+" "+mangaTitle+" "+mangaChapters);
		Manga manga = WebScraper.getManga(url,title.nextToken(),title.nextToken(),
				chapters.nextToken(),chapters.nextToken(),cover.nextToken(),cover.nextToken());
		return manga;
	}
	
	
	public static List<String> importWebsiteList(){
		List<String> list = new LinkedList<String>();
		StringTokenizer websiteList = new StringTokenizer(websiteDetails.getProperty("All-Websites"),";");
		while(websiteList.hasMoreTokens()) {
			list.add(websiteList.nextToken());
		}
		return list;
	}
	
	public static void downloadChapters(String websiteName,List<String> downloadList,Manga manga) {
		String image = websiteDetails.getProperty(websiteName+"-Manga-Chapter-Images");
		StringTokenizer st = new StringTokenizer(image,";");
		WebScraper.downloadChapters(downloadList, manga,st.nextToken(),st.nextToken());
	}
	
	public static void downloadChapters(String websiteName,List<String> downloadList,Manga manga,String downloadPath) {
		String image = websiteDetails.getProperty(websiteName+"-Manga-Chapter-Images");
		StringTokenizer st = new StringTokenizer(image,";");
		WebScraper.downloadChapters(downloadList, manga,st.nextToken(),st.nextToken(),downloadPath);
	}
	
}
