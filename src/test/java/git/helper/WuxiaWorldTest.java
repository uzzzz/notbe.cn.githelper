package git.helper;

public class WuxiaWorldTest {

	public static void main(String[] args) throws Exception {

		String name = "Dragon King With Seven Stars";
		String path = "dragon-king-with-seven-stars";
		String prefilename = "dkss";
		int year = 2019;
		int month = 4;
		int day = 20;
		int hour = 13;
		int min = 22;
		int sec = 23;

		int start = 1;
		int end = 25;

		WuxiaworldCrawler wc = new WuxiaworldCrawler();

		String index_url = "https://www.wuxiaworld.com/novel/" + path;
		wc.crawl_index(index_url, name, path, year, month, day, hour, min, sec, end + 1);
		System.out.println("index finish");

		for (int i = start; i <= end; i++) {
			String url = "https://www.wuxiaworld.com/novel/" + path + "/" + prefilename + "-chapter-" + i;
			wc.crawl(url, i, name, path, prefilename, year, month, day, hour, min, sec);
			System.out.println(i + " finish");
		}
	}
}
