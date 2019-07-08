package git.helper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WuxiaWorldTest {

	public static void main(String[] args) throws Exception {

		String originalName = "我欲封天";
		String englishName = "I Shall Seal the Heavens";
		String path = "i-shall-seal-the-heavens";
		int year = 2018;
		int month = 9; // 1 ~ 12
		int totalChapter = 1620;

		// --------------------------------------------

		String keywords = String.format( //
				"%s English version,%s,novel", englishName, originalName);

		WuxiaworldCrawler wc = new WuxiaworldCrawler();

		String index_url = "https://www.wuxiaworld.com/novel/" + path;
		String date_index = getDate(year, month, totalChapter + 100);
		wc.crawl_index(index_url, englishName, path, date_index, keywords);
		System.out.println("index finish");

		Document _doc = Jsoup.parse(new File("C:\\uzblog\\novel\\" + path + "\\" + path + ".html"), "UTF-8");
		Elements es = _doc.select("a");
		int i = 0;
		for (Element e : es) {
			String href = e.attr("href");
			if (href.startsWith("/novel")) {
				i++;
				String date = getDate(year, month, i);
				int s = href.lastIndexOf("/");
				String filename = href.substring(s + 1);

				String url = "https://www.wuxiaworld.com" + href;
				wc.crawl(url, englishName, path, filename, date, keywords, 3);
				System.out.println(href + " finish");
			}
		}
	}

	private static String getDate(int year, int month, int delay) {
		Calendar c_index = Calendar.getInstance();
		c_index.set(year, month - 1, 2, 8, 20, 10);
		c_index.add(Calendar.MINUTE, 12 * delay);
		c_index.add(Calendar.SECOND, 23 * delay);
		SimpleDateFormat sdf_index = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf_index.format(c_index.getTime());
	}

}
