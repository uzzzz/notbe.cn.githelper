package git.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
			"keywords: %s\r\n" + //
			"---\r\n" + //
			"<p> <strong><a href=\"/novel/%s/\">%s</a></strong></p>\r\n" + //
			"%s";

	public void crawl(String url, //
			String name, //
			String path, //
			String filename, //
			String date, //
			String keywords, //
			int repeat) {
		String mulu = "C:\\uzblog\\novel\\" + path + "\\" + path + "\\";

		try {
			Document _doc = Jsoup.connect(url).get();
			String title = _doc.select("div.caption h4").text();
			String content = _doc.select("div.fr-view").outerHtml();
			String text = String.format(template, title, date, name, keywords, path, name, content);

			File muluDir = new File(mulu);
			if (!muluDir.exists()) {
				muluDir.mkdirs();
			}
			String filepath = mulu + filename + ".html";
			FileOutputStream fos = new FileOutputStream(filepath, false);
			IOUtils.write(text, fos, "UTF-8");
			fos.flush();
			fos.close();
		} catch (Exception e) {
			if (repeat > 1) {
				crawl(url, name, path, filename, date, keywords, repeat - 1);
			} else {
				log.error(e.getMessage() + " - " + url);
				String errorPath = "C:\\uzblog\\novel\\" + path + "\\" + "error.txt";
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(errorPath, true);
					IOUtils.write(url + "\r\n", fos);
					fos.flush();
					fos.close();
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		}
	}

	private String index_template = "---\r\n" + //
			"layout: post\r\n" + //
			"title: \"%s\"\r\n" + //
			"date: \"%s\"\r\n" + //
			"categories: Novel\r\n" + //
			"tags: %s\r\n" + //
			"keywords: %s\r\n" + //
			"---\r\n" + //
			"%s";

	public void crawl_index(String url, //
			String name, //
			String path, //
			String date, //
			String keywords) {
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
				log.error(e.getMessage() + " - " + url);
			}

			try { // cover
				String imgurl = _doc.select("img.media-object").attr("src");
				int ind = imgurl.indexOf("?");
				if (ind > 0) {
					imgurl = imgurl.substring(0, ind);
				}
				File file = new File(mulu + "cover.png");

				URL u = new URL(imgurl);
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setRequestProperty("User-agent", "Mozilla/4.0");
				InputStream in = conn.getInputStream();
				OutputStream out = new FileOutputStream(file);
				IOUtils.copy(in, out);
				out.flush();
				out.close();
				in.close();

			} catch (Exception e) {
				log.error(e.getMessage() + " - " + url);
			}

			try { // index

				String index = _doc.select("#accordion").outerHtml();
				index = index.replace("height: 0px;", "").replace("panel-collapse collapse", "panel-collapse");
				String content = String.format(index_template, name, date, name, keywords, index);

				FileOutputStream fos = new FileOutputStream(mulu + path + ".html", false);
				IOUtils.write(content, fos, "UTF-8");
				fos.flush();
				fos.close();
			} catch (Exception e) {
				log.error(e.getMessage() + " - " + url);
			}
		} catch (IOException e) {
			log.error(e.getMessage() + " - " + url);
		}
	}
}
