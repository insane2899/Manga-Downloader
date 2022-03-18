package webscraper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Manga;

public final class WebScraper {
	
	private WebScraper() {}
	
	public static Map<String,String> getMangaListBFS(String url,String mangaClassName,
			String mangaItemName,String paginationClassName,String paginationItemName){
		Map<String,String> mangaList = new HashMap<String,String>();
		Set<String> done = new HashSet<>();
		LinkedList<String> queue = new LinkedList<String>();
		queue.add(url);
		done.add(url);
		while(queue.size()!=0) {
			try {
				url = queue.poll();
				System.out.println(url);
				mangaList.putAll(getMangaListOnPage(url,mangaClassName,mangaItemName));
				Document page = Jsoup.connect(url).get();
				Elements classElement = page.select(paginationClassName);
				Elements mangaElements = classElement.select(paginationItemName);
				for(Element e:mangaElements) {
					url = e.attr("abs:href");
					if(!done.contains(url)) {
						done.add(url);
						queue.add(url);
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return mangaList;
	}
	
	public static Map<String,String> getMangaListOnePage(String url,String mangaClassName,
			String mangaItemName,String paginationClassName,String paginationItemName){
		return getMangaListOnPage(url,mangaClassName,mangaItemName);
	}

	
	public static Map<String,String> getMangaList(String url,String mangaClassName,
			String mangaItemName,String paginationClassName,String paginationItemName){
		Map<String,String> mangaList = new HashMap<String,String>();
		Set<Integer> done = new HashSet<>();
		try {
			Document page = Jsoup.connect(url).get();
			Elements classElement = page.select(paginationClassName);
			Elements mangaElements = classElement.select(paginationItemName);
			mangaList.putAll(getMangaListOnPage(url,mangaClassName,mangaItemName));
			done.add(1);
			for(Element e:mangaElements) {
				url = e.attr("abs:href");
				String str = e.text().replaceAll("[^0-9]","");
				if(str.equals("")) {
					continue;
				}
				int pageNo = Integer.parseInt(str);
				
				if(!done.contains(pageNo)) {
					System.out.println(pageNo);
					done.add(pageNo);
					mangaList.putAll(getMangaList(url,mangaClassName,mangaItemName,paginationClassName,paginationItemName,done));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mangaList;
	}
	
	private static Map<String,String> getMangaList(String url,String mangaClassName,
			String mangaItemName,String paginationClassName,String paginationItemName,Set<Integer> done){
		Map<String,String> mangaList = new HashMap<String,String>();
		try {
			Document page = Jsoup.connect(url).get();
			Elements classElement = page.select(paginationClassName);
			Elements mangaElements = classElement.select(paginationItemName);
			mangaList.putAll(getMangaListOnPage(url,mangaClassName,mangaItemName));
			for(Element e:mangaElements) {
				url = e.attr("abs:href");
				String str = e.text().replaceAll("[^0-9]","");
				if(str.equals("")) {
					continue;
				}
				int pageNo = Integer.parseInt(str);
				if(!done.contains(pageNo)) {
					System.out.println(pageNo);
					done.add(pageNo);
					mangaList.putAll(getMangaList(url,mangaClassName,mangaItemName,paginationClassName,paginationItemName,done));
				}
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
			Elements classElement = page.select(mangaClassName);
			Elements mangaElements = classElement.select(mangaItemName);
			for(Element e:mangaElements) {
				if(e.text().equals("")) {
					mangaList.put(e.attr("title"), e.attr("abs:href"));
				}
				else {
					mangaList.put(e.text(), e.attr("abs:href"));
				}
				//System.out.println(e.text()+" "+e.attr("abs:href"));
			}
		}catch(org.jsoup.HttpStatusException e) {
			e.printStackTrace();
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
			Elements titleElements = page.select(titleClassName);
			Elements title = titleElements.select(titleItemName);
			for(Element e:title) {
				manga.setName(e.text());
			}
			Elements chapterElements = page.select(chapterClassName);
			Elements chapters = chapterElements.select(chapterItemName);
			for(Element e:chapters) {
				//System.out.println(e.text()+" "+e.attr("abs:href"));
				manga.addChapter(e.text(), e.attr("abs:href"));
			}
			Elements coverElements = page.select(coverClassName);
			Elements cover = coverElements.select(coverItemName);
			for(Element e:cover) {
				manga.setCoverUrl(e.attr("abs:src"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return manga;
	}
	
	public static void downloadChapters(List<String> chapterList,Manga manga,String imageClass,String imageItem,String userAgent,String referrer) {
		String destination = "Mangas/"+manga.getName();
		for(String s:chapterList) {
			downloadChapter(manga.getChapterURL(s),destination+"/"+s,imageClass,imageItem,userAgent,referrer);
		}
	}
	
	public static void downloadChapters(List<String> chapterList,Manga manga,String imageClass,String imageItem,String userAgent,String referrer,String downloadPath) {
		String destination = downloadPath+"/"+manga.getName();
		for(String s:chapterList) {
			downloadChapter(manga.getChapterURL(s),destination+"/"+s,imageClass,imageItem,userAgent,referrer);
		}
	}
	
	private static void downloadChapter(String url,String destination,String imageClass,String imageItem,String userAgent,String referrer) {
		try {
			int count = 1;
			Document page = Jsoup.connect(url).get();
			Elements imageElements = page.select(imageClass);
			Elements pageElements = imageElements.select(imageItem);
			for(Element e:pageElements) {
				//System.out.println(e.attr("abs:src"));
				storeImageIntoFS(e.attr("abs:src"),Integer.toString(count++)+".jpg",destination,userAgent,referrer);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String storeImageIntoFS(String imageUrl,String fileName,String relativePath,String userAgent,String referrer) {
		try {
			byte[] bytes = Jsoup.connect(imageUrl)
					.userAgent(userAgent)
					.referrer(referrer)
					.ignoreContentType(true)
					.execute()
					.bodyAsBytes();
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
