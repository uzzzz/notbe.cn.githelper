package git.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class WuxiaworldCrawler {

	private static Log log = LogFactory.getLog(WuxiaworldCrawler.class);

	private String template = "---\r\n" + //
			"layout: post\r\n" + //
			"title: \"%s\"\r\n" + //
			"date: \"%s\"\r\n" + //
			"categories: Novel\r\n" + //
			"tags: %s\r\n" + //
			"---\r\n" + //
			"<p> <strong><a href=\"/novel/%s/\">%s</a></strong></p>\r\n" + //
			"%s";

	public void crawl(String url, int i, //
			String name, String path, String prefilename, //
			int year, int month, int day, int hour, int min, int sec) {
		try {
			Calendar c = Calendar.getInstance();
			c.set(year, month, day, hour, min, sec);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			c.add(Calendar.MINUTE, 12 * i);
			c.add(Calendar.SECOND, 23 * i);
			String date = sdf.format(c.getTime());

			Document _doc = Jsoup.connect(url).get();
			String title = _doc.select("div.caption h4").text();
			String content = _doc.select("div.fr-view").outerHtml();
			String text = String.format(template, title, date, name, path, name, content);

			String filename = prefilename + "-chapter-" + i + ".html";
			String mulu = "C:\\uzblog\\novel\\" + path + "\\" + path + "\\";
			File muluDir = new File(mulu);
			if (!muluDir.exists()) {
				muluDir.mkdirs();
			}
			String filepath = mulu + filename;
			FileOutputStream fos = new FileOutputStream(filepath, false);
			IOUtils.write(text, fos, "UTF-8");
			fos.flush();
			fos.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private String index_template = "---\r\n" + //
			"layout: post\r\n" + //
			"title: \"%s\"\r\n" + //
			"date: \"%s\"\r\n" + //
			"categories: Novel\r\n" + //
			"tags: %s\r\n" + //
			"---\r\n" + //
			"%s";

	public void crawl_index(String url, //
			String name, String path, //
			int year, int month, int day, int hour, int min, int sec, //
			int i) {
		String mulu = "C:\\uzblog\\novel\\" + path + "\\";
		File muluDir = new File(mulu);
		if (!muluDir.exists()) {
			muluDir.mkdirs();
		}
		try { // 名字
			FileOutputStream fos = new FileOutputStream(mulu + "name.txt", false);
			IOUtils.write(name, fos, "UTF-8");
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Document _doc = Jsoup.connect(url).get();
			try { // 简介
				String intro = _doc.select("div.fr-view").text();
				FileOutputStream fos = new FileOutputStream(mulu + "intro.txt", false);
				IOUtils.write(intro, fos, "UTF-8");
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // cover
				String imgurl = _doc.select("img.media-object").attr("src");
				File file = new File(mulu + "cover.png");
				FileUtils.copyURLToFile(new URL(imgurl), file);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // index
				Calendar c = Calendar.getInstance();
				c.set(year, month, day, hour, min, sec);
				c.add(Calendar.MINUTE, 12 * i);
				c.add(Calendar.SECOND, 23 * i);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(c.getTime());

				String index = _doc.select("#accordion").outerHtml();
				index = index.replace("height: 0px;", "").replace("panel-collapse collapse", "panel-collapse");
				String content = String.format(index_template, name, date, name, index);

				FileOutputStream fos = new FileOutputStream(mulu + path + ".html", false);
				IOUtils.write(content, fos, "UTF-8");
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
