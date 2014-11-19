package test;

import static org.junit.Assert.assertEquals;
import models.Repository;

import org.junit.Test;

import fusion.InvalidRepositoryURLException;

public class RepositoryTest {
	
	private static final String GOOD_URL = "https://github.com/SebastianKG/410-yardwand";
	private static final String GOOD_CONTR_URL = "https://api.github.com/repos/SebastianKG/410-yardwand/stats/contributors";
	private static final String NOT_HTTPS = "http://github.com/SebastianKG/410-yardwand";
	private static final String NO_PROTOCOL = "github.com/SebastianKG/410-yardwand";
	private static final String TRAILING_SLASH = "https://github.com/SebastianKG/410-yardwand/";
	private static final String NULL = null;
	private static final String EMPTY = "";
	
	/**
	 * An example of a good URL. Show that the getters work as intended.
	 * @throws InvalidRepositoryURLException
	 */
	@Test
	public void testGoodURL() throws InvalidRepositoryURLException {
		Repository repo = new Repository(GOOD_URL);
		assertEquals(GOOD_URL, repo.getUrl());
		assertEquals(GOOD_CONTR_URL, repo.getContributorURL());
	}
	
	/**
	 * Repositories operate with the HTTPS protocol. Do not accept anything else.
	 * @throws InvalidRepositoryURLException
	 */
	@Test(expected = InvalidRepositoryURLException.class)
	public void testNotHttps() throws InvalidRepositoryURLException {
		@SuppressWarnings("unused")
		Repository repo = new Repository(NOT_HTTPS);
	}
	
	/**
	 * We want the protocol to be specified.
	 * @throws InvalidRepositoryURLException
	 */
	@Test(expected = InvalidRepositoryURLException.class)
	public void testNoProtocol() throws InvalidRepositoryURLException {
		@SuppressWarnings("unused")
		Repository repo = new Repository(NO_PROTOCOL);
	}
	
	/**
	 * Do not want a trailing slash at the end of the repository URL.
	 * @throws InvalidRepositoryURLException
	 */
	@Test(expected = InvalidRepositoryURLException.class)
	public void testTrailingSlash() throws InvalidRepositoryURLException {
		@SuppressWarnings("unused")
		Repository repo = new Repository(TRAILING_SLASH);
	}
	
	/**
	 * Null case. Expect <code>NullPointerException</code> rather than
	 * <code>InvalidRepositoryURLException</code>.
	 * @throws InvalidRepositoryURLException
	 */
	@Test(expected = NullPointerException.class)
	public void testNull() throws InvalidRepositoryURLException {
		@SuppressWarnings("unused")
		Repository repo = new Repository(NULL);
	}
	
	/**
	 * Empty case. Expect <code>InvalidRepositoryURLException</code> rather
	 * than <code>NullPointerException</code>.
	 * @throws InvalidRepositoryURLException
	 */
	@Test(expected = InvalidRepositoryURLException.class)
	public void testEmpty() throws InvalidRepositoryURLException {
		@SuppressWarnings("unused")
		Repository repo = new Repository(EMPTY);
	}
}
