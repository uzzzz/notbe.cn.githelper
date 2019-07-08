package git.helper;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;

public class Jar {

	public static void main(String[] args) throws Exception {

		try {
			String jarPath = "C:\\uzblog\\pdyw";
			String mulu = "C:\\uzblog\\";
			String jarTxtPath = mulu + "pdyw.txt";
			FileOutputStream fos = new FileOutputStream(jarTxtPath, true);

			File dir = new File(jarPath);
			for (String name : dir.list()) {
				IOUtils.write(name + "\r\n", fos);
			}
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
