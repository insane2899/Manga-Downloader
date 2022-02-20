package webscraper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Manga;

public final class WebScraper {
	
	private static final List<String> match = Arrays.asList(" Chapter"," Ch ", " Volume", " Vol ");
	
	private WebScraper() {}
	
	public static Map<String,String> getMangaList(String url,String mangaClassName,
			String mangaItemName,String paginationClassName,String paginationItemName){
		Map<String,String> mangaList = new HashMap<String,String>();
		try {
			Document page = Jsoup.connect(url).get();
			Elements classElement = page.getElementsByClass(paginationClassName);
			Elements mangaElements = classElement.select(paginationItemName);
			mangaList.putAll(getMangaListOnPage(url,mangaClassName,mangaItemName));
			for(Element e:mangaElements) {
				url = e.attr("abs:href");
				mangaList.putAll(getMangaListOnPage(url,mangaClassName,mangaItemName));
				//TODO only 3 pages will return as a single page has only 3 pagination links
				//Correct it.
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mangaList;
	}
	
	private static Map<String,String> getMangaListOnPage(String url,String mangaClassName,String mangaItemName){
		Map<String,String> mangaList = new HashMap<String,String>();
		try {
			Document page = Jsoup.connect(url).get();
			Elements classElement = page.getElementsByClass(mangaClassName);
			Elements mangaElements = classElement.select(mangaItemName);
			for(Element e:mangaElements) {
				mangaList.put(e.text(), e.attr("abs:href"));
			}
		}catch(org.jsoup.HttpStatusException e) {
			return mangaList;
		}catch(IOException e) {
			return mangaList;
		}
		return mangaList;
	}
	
	public static Manga getManga(String url,String titleClassName,String titleItemName,
			String chapterClassName,String chapterItemName,String coverClassName,String coverItemName) {
		Manga manga = new Manga();
		try {
			Document page = Jsoup.connect(url).get();
			Elements titleElements = page.getElementsByClass(titleClassName);
			Elements title = titleElements.select(titleItemName);
			for(Element e:title) {
				manga.setName(e.text());
			}
			Elements chapterElements = page.getElementsByClass(chapterClassName);
			Elements chapters = chapterElements.select(chapterItemName);
			for(Element e:chapters) {
				manga.addChapter(e.text(), e.attr("abs:href"));
			}
			Elements coverElements = page.getElementsByClass(coverClassName);
			Elements cover = coverElements.select(coverItemName);
			for(Element e:cover) {
				manga.setCoverUrl(e.attr("abs:href"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return manga;
	}
	
	public static Manga getManga(String url) {
		Manga manga = new Manga();
		try {
			Document page = Jsoup.connect(url).get();
			Elements pageElements = page.select("a[href]");
			for(Element e:pageElements) {
				boolean flag = false;
				for(String z:match) {
					if(e.text().contains(z) || e.text().contains(z.toLowerCase())||e.text().contains(z.toUpperCase())) {
						flag=true;
						break;
					}
				}
				if(flag) {
					manga.addChapter(e.text(), e.attr("abs:href"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return manga;
	}
	
	public static void downloadChapters(List<String> chapterList,Manga manga) {
		String destination = "Mangas/"+manga.getName();
		for(String s:chapterList) {
			downloadChapter(manga.getChapterURL(s),destination+"/"+s);
		}
	}
	
	private static void downloadChapter(String url,String destination) {
		try {
			int count = 1;
			Document page = Jsoup.connect(url).get();
			Elements pageElements = page.select("img");
			for(Element e:pageElements) {
				//System.out.println(e.attr("src"));
				if(!e.attr("src").contains("http")) {
					continue;
				}
				storeImageIntoFS(e.attr("src"),Integer.toString(count++)+".jpg",destination);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String storeImageIntoFS(String imageUrl,String fileName,String relativePath) {
		try {
			byte[] bytes = Jsoup.connect(imageUrl).ignoreContentType(true).execute().bodyAsBytes();
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			saveByteBufferImage(buffer,relativePath,fileName);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return imageUrl;
	}
	
	private static void saveByteBufferImage(ByteBuffer imageDataBytes,String rootTargetDirectory,String savedFileName) {
		String uploadInputFile = rootTargetDirectory+"/"+savedFileName;
		File rootTargetDir = new File(rootTargetDirectory);
		if(!rootTargetDir.exists()) {
			boolean created = rootTargetDir.mkdirs();
			if(!created) {
				System.out.println("Error while creating directory for location "+rootTargetDirectory);
			}
		}
		String[] fileNameParts = savedFileName.split("\\.");
		String format = fileNameParts[fileNameParts.length-1];
		File file = new File(uploadInputFile);
		BufferedImage bufferedImage;
		InputStream in = new ByteArrayInputStream(imageDataBytes.array());
		try {
			bufferedImage = ImageIO.read(in);
			ImageIO.write(bufferedImage, format, file);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
