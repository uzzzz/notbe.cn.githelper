package git.helper;

public interface SitemapParserCallback {

	default void sitemap(String sitemap) {
	}

	default boolean ignoreSitemap(String sitemap) {
		return false;
	}

	void url(String url);
}
