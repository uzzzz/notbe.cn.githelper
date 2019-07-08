package git.helper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WuxiaWorldTest2 {

	public static void main(String[] args) throws Exception {

		String keywords = "Desolate Era English version,莽荒纪英文版,莽荒纪";
		String name = "Desolate Era";
		String path = "desolate-era";
		int year = 2019;
		int month = 6;
		int day = 21;
		int hour = 15;
		int min = 19;
		int sec = 56;

		// --------------------------------------------

		WuxiaworldCrawler wc = new WuxiaworldCrawler();

		Document _doc = Jsoup.parse(new File("C:\\uzblog\\novel\\" + path + "\\" + path + ".html"), "UTF-8");
		Elements es = _doc.select("a");
		int i = 0;
		for (Element e : es) {
			String href = e.attr("href");
			if (href.startsWith("/novel")) {
				i++;
				Calendar c = Calendar.getInstance();
				c.set(year, month, day, hour, min, sec);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				c.add(Calendar.MINUTE, 12 * i);
				c.add(Calendar.SECOND, 23 * i);
				String date = sdf.format(c.getTime());

				int s = href.lastIndexOf("/");
				String filename = href.substring(s + 1);

				String url = "https://www.wuxiaworld.com" + href;
				if ("de-book-26-chapter-19".equals(filename) //
						|| "de-book-44-chapter-27".equals(filename)//
						|| "de-book-45-chapter-9".equals(filename)//
						|| "de-book-45-chapter-15".equals(filename)) {
					wc.crawl(url, name, path, filename, date, keywords, 3);
					System.out.println(href + " finish");
				}
			}
		}
	}
}
