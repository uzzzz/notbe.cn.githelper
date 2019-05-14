package git.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CsdnCrawler {

	private static Log log = LogFactory.getLog(CsdnCrawler.class);

	@Autowired
	private GitTask gitTask;

	public void crawl(String url) {
		try {
			Document _doc = Jsoup.connect(url).get();
			String time = _doc.select(".time").first().text().split("日")[0].replace("年", "-").replace("月", "-");
			String title = _doc.select(".title-article").text();
			Elements article = _doc.select("article");

			List<String> thumbnails = new ArrayList<>();

			article.select("img").stream().parallel().forEach(element -> {
				String src = imgUrl(element);
				thumbnails.add(src);
			});
			article.select("script, #btn-readmore, .article-copyright").remove();
			String c = article.html();

			String source = _doc.outerHtml();
			String username = Utils.substring(source, "var username = \"", "\";");
			String id = Utils.substring(source, "var articleId = \"", "\";");
			id = StringUtils.isEmpty(id) ? Utils.substring(source, "var fileName = \"", "\";") : id;
			String filename = username + "_" + id;

			gitTask.writeGit(filename, title, c, time);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private String imgUrl(Element element) {

		String src = element.absUrl("src");

		if (StringUtils.hasText(src)) {
			boolean b = false;
			if (src.startsWith("http://img-blog.csdn.net") //
					|| src.startsWith("https://img-blog.csdn.net")//
					|| src.startsWith("http://img-blog.csdnimg.cn") //
					|| src.startsWith("https://img-blog.csdnimg.cn")) {
				b = true;
			}
			if (b) {
				src = "https://uzshare.com/_p?" + src;
				element.attr("src", src);
			}
		}
		return src;
	}

}
