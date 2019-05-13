package git.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitHelperApplication implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(GitHelperApplication.class);

	@Autowired
	private CsdnCrawler csdnCrawler;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(GitHelperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args == null || args.length == 0) {
			log.error("no args");
		} else {
			for (String arg : args) {
				log.warn("githelper crawl start : " + arg);
				new SitemapParser(csdnCrawler::crawl).parseXml(arg);
				log.warn("githelper crawl end : " + arg);
			}
		}
	}
}
