package webscraper;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.IOException;
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
		System.out.println(websiteListString+"\n"+paginationString);
		StringTokenizer mangaList = new StringTokenizer(websiteListString,";");
		String mangaClassName = mangaList.nextToken();
		String mangaItemName = mangaList.nextToken();
		StringTokenizer paginationList = new StringTokenizer(paginationString,";");
		String paginationClassName = paginationList.nextToken();
		String paginationItemName = paginationList.nextToken();
		System.out.println(website+" "+mangaClassName+" "+
				mangaItemName+" "+paginationClassName+" "+paginationItemName);
		return WebScraper.getMangaList(website, mangaClassName, mangaItemName,
				paginationClassName,paginationItemName);
	}
	
	
	public static List<String> importWebsiteList(){
		List<String> list = new LinkedList<String>();
		StringTokenizer websiteList = new StringTokenizer(websiteDetails.getProperty("All-Websites"),";");
		while(websiteList.hasMoreTokens()) {
			list.add(websiteList.nextToken());
		}
		return list;
	}
	
}