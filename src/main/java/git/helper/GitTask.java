package git.helper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GitTask {

	private static Logger log = LoggerFactory.getLogger(GitTask.class);

	public void writeGit(long id, String title, String c, String time) {
//		try {
//			c = URLEncoder.encode(c, "UTF-8");
//			c = "{{ \"" + c + "\" | url_decode}}";
//		} catch (UnsupportedEncodingException ee) {
//			c = "{% raw %} \n" + c + "\n{% endraw %}";
//		}

		c = "{% raw %} \n" + c + "\n{% endraw %}";
		String content = "---\n" //
				+ "layout: post\n" //
				+ "title: " + title + "\n" //
				+ "---\n\n" //
				+ c;
		writeGitForNotBeCN(id, title, content, time);
	}

	private void writeGitForNotBeCN(long id, String title, String content, String time) {
		try {
			String path = "/web/github.io/notbe.cn/_posts/";
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(path + time + "-" + id + ".html", false), "UTF-8"));
			writer.write(content);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}
}
